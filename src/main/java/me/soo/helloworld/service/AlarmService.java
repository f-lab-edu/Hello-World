package me.soo.helloworld.service;

import lombok.RequiredArgsConstructor;
import me.soo.helloworld.enumeration.AlarmTypes;
import me.soo.helloworld.exception.NoSuchAlarmException;
import me.soo.helloworld.mapper.AlarmMapper;
import me.soo.helloworld.model.alarm.Alarm;
import me.soo.helloworld.model.alarm.AlarmData;
import me.soo.helloworld.model.alarm.AlarmListRequest;
import me.soo.helloworld.util.Pagination;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AlarmService {

    private final AlarmMapper alarmMapper;

    private final Pagination pagination;

    public void dispatchAlarm(String to, String from, AlarmTypes type) {
        AlarmData alarm = AlarmData.create(to, from, type);
        alarmMapper.insertAlarm(alarm);
    }

    public List<Alarm> getAlarmList(String userId, int pageNumber) {
        AlarmListRequest request = AlarmListRequest.create(userId, pageNumber, pagination);
        return alarmMapper.getAlarmList(request);
    }

    @Transactional
    public Alarm getAlarm(int alarmId, String userId) {
        markUnreadAsRead(alarmId, userId);
        return alarmMapper.getAlarm(alarmId, userId);
    }

    private void markUnreadAsRead(int alarmId, String userId) {
        String hasRead = alarmMapper.getHasReadStatus(alarmId, userId)
                .orElseThrow(() -> new NoSuchAlarmException("존재하지 않는 알람에 대한 정보는 읽어올 수 없습니다."));

        if ("N".equals(hasRead)) {
            alarmMapper.updateToRead(alarmId, userId);
        }
    }

    public void removeAlarm(int alarmId, String userId) {
        if (!alarmMapper.isAlarmExist(alarmId, userId)) {
            throw new NoSuchAlarmException("존재하지 않는 알림을 삭제할 수 없습니다. 알림 정보를 다시 한 번 확인해주세요");
        }

        alarmMapper.deleteAlarm(alarmId, userId);
    }

    public void removeDispatchedAlarm(String to, String from, AlarmTypes type) {
        AlarmData alarm = AlarmData.create(to, from, type);
        alarmMapper.deleteDispatchedAlarm(alarm);
    }
}
