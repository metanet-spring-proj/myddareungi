package com.metanet.myddareungi.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@ConfigurationProperties(prefix = "app.seed.user")
public class SeedUserProperties {

	private String loginId;
	private String password;
	private String userName;
	private String email;
	private String role = "USER";
	private String question;
	private String answer;

	public boolean isReady() {
		return StringUtils.hasText(loginId)
			&& StringUtils.hasText(password)
			&& StringUtils.hasText(userName)
			&& StringUtils.hasText(email)
			&& StringUtils.hasText(question)
			&& StringUtils.hasText(answer);
	}

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}
}
