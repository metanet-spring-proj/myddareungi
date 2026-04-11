package com.metanet.myddareungi.common.aspect;

import com.metanet.myddareungi.config.CustomUserDetails;
import com.metanet.myddareungi.common.log.writer.SystemLogWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class LoggingAspect {

    private final SystemLogWriter systemLogWriter;

    @Around("execution(* com.metanet.myddareungi.domain..*Controller.*(..))")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {

        long start = System.currentTimeMillis();
        Long userId = resolveCurrentUserId();

        Exception caughtException = null;

        try {
            return joinPoint.proceed();

        } catch (Exception e) {
            caughtException = e; // 에러 잡아두고
            throw e; // 원래 흐름 유지

        } finally {
            // 정상이든 에러든 항상 저장
            long executionTime = System.currentTimeMillis() - start;
            systemLogWriter.saveAsync(joinPoint, executionTime, userId, caughtException);
        }
    }

    private Long resolveCurrentUserId() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.getPrincipal() instanceof CustomUserDetails userDetails) {
                return userDetails.getUserId();
            }
        } catch (Exception e) {
            log.warn("[LoggingAspect] userId 추출 실패: {}", e.getMessage());
        }
        return null;
    }
}