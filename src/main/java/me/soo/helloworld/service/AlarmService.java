package me.soo.helloworld.service;

import lombok.RequiredArgsConstructor;
import me.soo.helloworld.enumeration.AlarmTypes;
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

}
