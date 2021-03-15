package me.soo.helloworld.service;

import lombok.RequiredArgsConstructor;
import me.soo.helloworld.enumeration.AlarmTypes;
import me.soo.helloworld.exception.InvalidRequestException;
import me.soo.helloworld.exception.NoSuchAlarmException;
import me.soo.helloworld.mapper.AlarmMapper;
import me.soo.helloworld.model.alarm.Alarm;
import me.soo.helloworld.model.alarm.AlarmData;
import me.soo.helloworld.model.alarm.AlarmListRequest;
import me.soo.helloworld.util.Pagination;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AlarmService {

    private final AlarmMapper alarmMapper;

    private final Pagination pagination;

    public void addAlarm(String to, String from, AlarmTypes type) {
        AlarmData alarm = AlarmData.create(to, from, type);
        alarmMapper.insertAlarm(alarm);
    }

    public List<Alarm> getAlarmList(String userId, int pageNumber) {
        AlarmListRequest request = AlarmListRequest.create(userId, pageNumber, pagination);
        return alarmMapper.getAlarmList(request);
    }

    public Alarm readAlarm(int alarmId, String userId) {
        Alarm alarm = alarmMapper.getAlarm(alarmId, userId)
                                    .orElseThrow(() -> new NoSuchAlarmException("존재하지 않는 알람에 대한 정보는 읽어올 수 없습니다. 다시 한 번 확인해주세요."));

        if (alarm.getHasRead().equals("N")) {
            alarmMapper.updateToRead(alarmId, userId);
        }

        return alarm;
    }

}
