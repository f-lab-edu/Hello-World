package me.soo.helloworld.service;

import lombok.RequiredArgsConstructor;
import me.soo.helloworld.enumeration.AlarmTypes;
import me.soo.helloworld.mapper.AlarmMapper;
import me.soo.helloworld.model.alarm.Alarm;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AlarmService {

    private final AlarmMapper alarmMapper;

    public void addAlarm(String to, String from, AlarmTypes type) {
        Alarm alarm = Alarm.create(to, from, type);
        alarmMapper.insertAlarm(alarm);
    }
}
