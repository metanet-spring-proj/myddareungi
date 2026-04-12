package com.metanet.myddareungi.domain.member.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.metanet.myddareungi.domain.files.model.UploadFile;
import com.metanet.myddareungi.domain.files.service.IUploadFileService;
import com.metanet.myddareungi.domain.member.model.Member;
import com.metanet.myddareungi.domain.member.service.MemberAuthService;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;

@Hidden
@Controller
@RequiredArgsConstructor
public class MemberViewController {
	private final MemberAuthService memberAuthService;
	 private final IUploadFileService uploadFileService; 

	@GetMapping("/login")
	public String loginPage(Authentication authentication) {
		if (isAuthenticated(authentication)) {
			return "redirect:/dashboard";
		}
		return "member/login";
	}

	@GetMapping("/signup")
	public String signupPage(Authentication authentication) {
		if (isAuthenticated(authentication)) {
			return "redirect:/dashboard";
		}
		return "member/signup";
	}


	@GetMapping("/mypage/edit")
	public String updatePage(Authentication authentication, Model model) {
		if (!isAuthenticated(authentication)) {
			return "redirect:/login";
		}
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		Member member = memberAuthService.getMember(userDetails.getUsername());
		model.addAttribute("member", member);

		// JWT authorities에서 role 추출
		String role = authentication.getAuthorities().stream()
				.findFirst()
				.map(a -> a.getAuthority().replace("ROLE_", ""))
				.orElse("USER");
		model.addAttribute("userRole", role);

		return "member/update";
	}

	@GetMapping("/password/forgot")
	public String forgotPasswordPage(Authentication authentication) {
		if (isAuthenticated(authentication)) {
			return "redirect:/dashboard";
		}
		return "member/forgot-password";
	}


	@GetMapping("/mypage")
	public String myPage(Authentication authentication, Model model,
						 @RequestParam(defaultValue = "0") int page,
						 @RequestParam(defaultValue = "5") int size) {
		if (!isAuthenticated(authentication)) {
			return "redirect:/login";
		}

		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		Member member = memberAuthService.getMember(userDetails.getUsername());

		int totalItems = uploadFileService.countFilesByUploaderId(member.getUserId());
		int totalPages = (totalItems == 0) ? 1 : (int) Math.ceil((double) totalItems / size);
		page = Math.max(0, Math.min(page, totalPages - 1));

		List<UploadFile> fileList = uploadFileService.getFilesByUploaderIdPaged(
				member.getUserId(), page * size, size);

		// 페이지 윈도우 계산 : 10 블록씩
		int blockSize = 10;
		int currentBlock = page / blockSize;
		int blockStart = currentBlock * blockSize;
		int blockEnd = Math.min(blockStart + blockSize - 1, totalPages - 1);
		List<Integer> pageNumbers = new ArrayList<>();
		for (int i = blockStart; i <= blockEnd; i++) pageNumbers.add(i);

		model.addAttribute("userName", member.getUserName());
		model.addAttribute("userEmail", member.getEmail());
		model.addAttribute("userRoleInfo", member.getRole());
		model.addAttribute("fileList", fileList);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", totalPages);
		model.addAttribute("pageNumbers", pageNumbers);
		model.addAttribute("activeMenu", "mypage");

		return "mypage/user-mypage";
	}

	private boolean isAuthenticated(Authentication authentication) {
		return authentication != null
			&& authentication.isAuthenticated()
			&& !(authentication instanceof AnonymousAuthenticationToken);
	}
}
