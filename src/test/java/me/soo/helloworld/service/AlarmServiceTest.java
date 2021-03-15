package me.soo.helloworld.service;

import me.soo.helloworld.enumeration.AlarmTypes;
import me.soo.helloworld.exception.InvalidRequestException;
import me.soo.helloworld.exception.NoSuchAlarmException;
import me.soo.helloworld.mapper.AlarmMapper;
import me.soo.helloworld.model.alarm.Alarm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class AlarmServiceTest {

    private final String to = "Soo";

    private final String from = "Not Soo";

    private final int alarmId = 1;

    private final Date createdDate = Date.valueOf("2021-03-15");

    Alarm readAlarm;

    Alarm unreadAlarm;

    @InjectMocks
    AlarmService alarmService;

    @Mock
    AlarmMapper alarmMapper;

    @BeforeEach
    public void createUnreadAlarm() {
        readAlarm = new Alarm(alarmId, to, from, AlarmTypes.FRIEND_REQUEST_RECEIVED, "Y", createdDate);
    }

    @BeforeEach
    public void createReadAlarm() {
        unreadAlarm = new Alarm(alarmId, to, from, AlarmTypes.FRIEND_REQUEST_RECEIVED, "N", createdDate);
    }

    @Test
    @DisplayName("해당 알람이 존재하고 기존에 읽지 않았을 경우 읽은 상태를 'Y'로 업데이트하고 읽어오는데 성공합니다.")
    public void readAlarmSuccessOnUnreadExistingAlarm() {
        when(alarmMapper.getAlarm(alarmId, to)).thenReturn(Optional.ofNullable(unreadAlarm));
        doNothing().when(alarmMapper).updateToRead(alarmId, to);

        alarmService.readAlarm(alarmId, to);

        verify(alarmMapper, times(1)).getAlarm(alarmId, to);
        verify(alarmMapper, times(1)).updateToRead(alarmId, to);
    }

    @Test
    @DisplayName("해당 알람이 존재하고 기존에 읽었던 알람의 경우는 hasRead 의 상태를 변경하는 부가 작업 없이 알람을 바로 읽어오는데 성공합니다.")
    public void readAlarmSuccessOnReadExistingAlarm() {
        when(alarmMapper.getAlarm(alarmId, to)).thenReturn(Optional.ofNullable(readAlarm));

        alarmService.readAlarm(alarmId, to);

        verify(alarmMapper, times(1)).getAlarm(alarmId, to);
        verify(alarmMapper, never()).updateToRead(alarmId, to);
    }

    @Test
    @DisplayName("해당 알람이 더이상 존재하지 않는 경우 알림을 읽어오는데 실패하며 예외가 발생합니다.")
    public void readAlarmFailOnNotExistingAlarm() {
        when(alarmMapper.getAlarm(alarmId, to)).thenReturn(Optional.empty());

        assertThrows(NoSuchAlarmException.class, () -> {
           alarmService.readAlarm(alarmId, to);
        });

        verify(alarmMapper, times(1)).getAlarm(alarmId, to);
        verify(alarmMapper, never()).updateToRead(alarmId, to);
    }
}
