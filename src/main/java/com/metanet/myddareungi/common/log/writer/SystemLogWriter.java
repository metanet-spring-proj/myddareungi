package com.metanet.myddareungi.common.log.writer;  // service → writer

import com.metanet.myddareungi.common.log.model.SystemLog;
import com.metanet.myddareungi.common.log.repository.SystemLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Component
@RequiredArgsConstructor
public class SystemLogWriter {

    private final SystemLogRepository systemLogRepository;

    @Async("logTaskExecutor")
    public void saveAsync(ProceedingJoinPoint joinPoint, long executionTime,
                          Long userId, Exception exception) {
        try {
            String methodName = joinPoint.getSignature().getDeclaringTypeName()
                    + "." + joinPoint.getSignature().getName();

            String arguments = Arrays.toString(joinPoint.getArgs());
            if (arguments.length() > 2000) {
                arguments = arguments.substring(0, 1997) + "...";
            }

            String errorMessage = null;
            if (exception != null) {
                errorMessage = exception.getClass().getSimpleName()
                        + ": " + exception.getMessage();
                if (errorMessage.length() > 4000) {
                    errorMessage = errorMessage.substring(0, 3997) + "...";
                }
            }

            systemLogRepository.insertLog(SystemLog.builder()
                    .userId(userId)
                    .methodName(methodName)
                    .arguments(arguments)
                    .errorMessage(errorMessage)
                    .executionTime(executionTime)
                    .build());

        } catch (Exception e) {
            log.warn("[SystemLogWriter] 로그 저장 실패: {}", e.getMessage());
        }
    }
}