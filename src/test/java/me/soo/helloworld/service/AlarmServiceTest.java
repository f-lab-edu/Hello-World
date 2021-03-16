package me.soo.helloworld.service;

import me.soo.helloworld.exception.NoSuchAlarmException;
import me.soo.helloworld.mapper.AlarmMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class AlarmServiceTest {

    private final String to = "Soo";

    private final int alarmId = 1;

    @InjectMocks
    AlarmService alarmService;

    @Mock
    AlarmMapper alarmMapper;

    @Test
    @DisplayName("해당 알람이 존재하지 않는 경우 hasRead 상태를 읽어오는데 실패하며 예외가 발생합니다.")
    public void readAlarmFailOnNotExistingAlarm() {
        when(alarmMapper.getHasReadStatus(alarmId, to)).thenReturn(Optional.empty());

        assertThrows(NoSuchAlarmException.class, () -> {
            alarmService.getAlarm(alarmId, to);
        });

        verify(alarmMapper, times(1)).getHasReadStatus(alarmId, to);
        verify(alarmMapper, never()).updateToRead(alarmId, to);
        verify(alarmMapper, never()).getAlarm(alarmId, to);

    }

    @Test
    @DisplayName("해당 알람이 존재하여 기존에 읽지 않은 상태인 'N'을 리턴하는 경우 읽었다는 'Y'로 업데이트하고 알람을 읽어오는데 성공합니다.")
    public void readAlarmSuccessOnExistingUnreadAlarm() {
        String unRead = "N";
        when(alarmMapper.getHasReadStatus(alarmId, to)).thenReturn(Optional.of(unRead));
        doNothing().when(alarmMapper).updateToRead(alarmId, to);

        alarmService.getAlarm(alarmId, to);

        verify(alarmMapper, times(1)).getHasReadStatus(alarmId, to);
        verify(alarmMapper, times(1)).updateToRead(alarmId, to);
        verify(alarmMapper, times(1)).getAlarm(alarmId, to);
    }

    @Test
    @DisplayName("해당 알람이 존재하여 기존에 읽은 상태인 'Y'를 리턴하는 경우 읽은 상태로 업데이트 하는 과정 없이 바로 알람을 읽어오는데 성공합니다.")
    public void readAlarmSuccessOnExistingReadAlarm() {
        String read = "Y";
        when(alarmMapper.getHasReadStatus(alarmId, to)).thenReturn(Optional.of(read));

        alarmService.getAlarm(alarmId, to);

        verify(alarmMapper, times(1)).getHasReadStatus(alarmId, to);
        verify(alarmMapper, never()).updateToRead(alarmId, to);
        verify(alarmMapper, times(1)).getAlarm(alarmId, to);
    }

    @Test
    @DisplayName("해당 알람이 존재하지 않는 경우, 알람을 삭제하는데 실패하며 NoSuchAlarmException 이 발생합니다.")
    public void removeAlarmFailOnNoExistingAlarm() {
        when(alarmMapper.isAlarmExist(alarmId, to)).thenReturn(false);

        assertThrows(NoSuchAlarmException.class, () -> {
            alarmService.removeAlarm(alarmId, to);
        });

        verify(alarmMapper, times(1)).isAlarmExist(alarmId, to);
        verify(alarmMapper, never()).deleteAlarm(alarmId, to);
    }

    @Test
    @DisplayName("해당 알람이 존재하는 경우, 알람을 삭제하는데 성공합니다.")
    public void removeAlarmSuccessOnExistingAlarm() {
        when(alarmMapper.isAlarmExist(alarmId, to)).thenReturn(true);

        alarmService.removeAlarm(alarmId, to);

        verify(alarmMapper, times(1)).isAlarmExist(alarmId, to);
        verify(alarmMapper, times(1)).deleteAlarm(alarmId, to);
    }

}
