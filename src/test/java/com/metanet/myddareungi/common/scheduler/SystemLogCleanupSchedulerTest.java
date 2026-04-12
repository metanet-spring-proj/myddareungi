package com.metanet.myddareungi.common.scheduler;

import com.metanet.myddareungi.common.log.repository.SystemLogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SystemLogCleanupSchedulerTest {

    @Mock
    SystemLogRepository systemLogRepository;

    SystemLogCleanupScheduler scheduler;

    @BeforeEach
    void setUp() {
        scheduler = new SystemLogCleanupScheduler(systemLogRepository);
        // @Value로 주입되는 값을 테스트 환경에서 직접 세팅
        ReflectionTestUtils.setField(scheduler, "intervalDays", 30);
        ReflectionTestUtils.setField(scheduler, "intervalMinutes", 0);
    }

    @Test
    @DisplayName("deleteOldLogs() - repository.deleteOldLogs(30, 0)이 정상 호출된다")
    void deleteOldLogs_정상호출() {
        // given
        when(systemLogRepository.deleteOldLogs(30, 0)).thenReturn(5);

        // when
        scheduler.deleteOldLogs();

        // then
        verify(systemLogRepository, times(1)).deleteOldLogs(30, 0);
    }

    @Test
    @DisplayName("deleteOldLogs() - repository에서 예외 발생해도 외부로 전파되지 않는다")
    void deleteOldLogs_예외발생시_전파안됨() {
        // given
        when(systemLogRepository.deleteOldLogs(30, 0))
                .thenThrow(new RuntimeException("DB 오류"));

        // when & then
        // 예외가 밖으로 나오지 않아야 함 (스케줄러는 자동으로 반복 실행되는 작업이라서, 한 번 실패했다고 전체가 멈추면 안되기때문에 실제 코드에서 try-catch로 내부 처리되도록 했기 때문)
        assertThatCode(() -> scheduler.deleteOldLogs())
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("deleteOldLogs() - @Value intervalDays, intervalMinutes 값이 정확히 전달된다")
    void deleteOldLogs_Value값_정확히전달() {
        // given - intervalDays=7, intervalMinutes=10 으로 변경해서 확인
        ReflectionTestUtils.setField(scheduler, "intervalDays", 7);
        ReflectionTestUtils.setField(scheduler, "intervalMinutes", 10);
        when(systemLogRepository.deleteOldLogs(7, 10)).thenReturn(3);

        // when
        scheduler.deleteOldLogs();

        // then - 변경된 값(7, 10)으로 호출됐는지 검증
        verify(systemLogRepository).deleteOldLogs(7, 10);
        // 잘못된 값으로는 호출되지 않았는지 확인
        verify(systemLogRepository, never()).deleteOldLogs(30, 0);
    }
}