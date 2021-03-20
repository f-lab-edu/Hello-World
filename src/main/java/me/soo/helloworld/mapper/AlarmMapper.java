package me.soo.helloworld.mapper;

import me.soo.helloworld.model.alarm.Alarm;
import me.soo.helloworld.model.alarm.AlarmData;
import me.soo.helloworld.model.alarm.AlarmListRequest;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AlarmMapper {

    public void insertAlarm(AlarmData alarm);

    List<Alarm> getAlarmList(AlarmListRequest request);
}
