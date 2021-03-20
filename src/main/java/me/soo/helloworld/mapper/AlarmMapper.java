package me.soo.helloworld.mapper;

import me.soo.helloworld.model.alarm.Alarm;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AlarmMapper {

    public void insertAlarm(Alarm alarm);
}
