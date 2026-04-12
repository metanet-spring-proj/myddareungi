package com.metanet.myddareungi.common.aspect;

import com.metanet.myddareungi.common.log.writer.SystemLogWriter;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoggingAspectTest {

    @Mock
    SystemLogWriter systemLogWriter;

    @Mock
    ProceedingJoinPoint joinPoint;

    @Mock
    Signature signature;

    LoggingAspect loggingAspect;

    @BeforeEach
    void setUp() {
        loggingAspect = new LoggingAspect(systemLogWriter);
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("정상 실행 시 saveAsync가 exception=null로 호출된다")
    void logAround_정상실행_saveAsync호출() throws Throwable {
        // given
        when(joinPoint.proceed()).thenReturn("ok");
        // "joinPoint.proceed()가 호출되면 'ok'를 반환해"
        // = Controller가 정상 실행됐다고 가정

        // when
        loggingAspect.logAround(joinPoint); // AOP 실행

        // then
        verify(systemLogWriter).saveAsync(
                eq(joinPoint),  // joinPoint 그대로 전달됐는지
                anyLong(),      // executionTime은 아무 숫자나 (측정값이라 정확히 모름)
                isNull(),       // userId = null (비로그인)
                isNull()        // exception = null (정상 실행이라 예외 없음)
        );
    }
    @Test
    @DisplayName("예외 발생 시 saveAsync가 exception 담아서 호출되고, 예외는 그대로 전파된다")
    void logAround_예외발생_saveAsync에exception전달() throws Throwable {
        // given
        RuntimeException fakeException = new RuntimeException("테스트 오류");
        when(joinPoint.proceed()).thenThrow(fakeException);
        // "proceed() 호출하면 예외를 던져" = Controller에서 오류 발생

        // when & then
        assertThatThrownBy(() -> loggingAspect.logAround(joinPoint))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("테스트 오류");
        // "logAround 실행하면 예외가 그대로 밖으로 나오는지 확인"

        verify(systemLogWriter).saveAsync(
                eq(joinPoint),
                anyLong(),
                isNull(),
                eq(fakeException)  // 예외가 담겨서 saveAsync에 전달됐는지 확인
        );
    }

    @Test
    @DisplayName("비로그인 상태에서 userId=null로 saveAsync가 호출된다")
    void logAround_비로그인_userId_null() throws Throwable {
        // given - setUp()에서 SecurityContextHolder.clearContext() 했으므로
        //         로그인 정보가 없는 상태
        when(joinPoint.proceed()).thenReturn(null);

        // when
        loggingAspect.logAround(joinPoint);

        // then
        verify(systemLogWriter).saveAsync(
                eq(joinPoint),
                anyLong(),
                isNull(),   // userId = null 인지 확인 (비로그인이라 userId 없음)
                isNull()
        );
    }


    @Test
    @DisplayName("executionTime은 0 이상의 값으로 saveAsync가 호출된다")
    void logAround_executionTime_0이상() throws Throwable {
        // given
        when(joinPoint.proceed()).thenReturn(null);

        // when
        loggingAspect.logAround(joinPoint);

        // then
        ArgumentCaptor<Long> timeCaptor = ArgumentCaptor.forClass(Long.class);
        verify(systemLogWriter).saveAsync(eq(joinPoint), timeCaptor.capture(), isNull(), isNull());
        assertThat(timeCaptor.getValue()).isGreaterThanOrEqualTo(0L);
    }
}