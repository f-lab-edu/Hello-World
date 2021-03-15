package me.soo.helloworld.mapper;

import me.soo.helloworld.model.alarm.Alarm;
import me.soo.helloworld.model.alarm.AlarmData;
import me.soo.helloworld.model.alarm.AlarmListRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface AlarmMapper {

    public void insertAlarm(AlarmData alarm);

    List<Alarm> getAlarmList(AlarmListRequest request);

    Optional<Alarm> getAlarm(@Param("alarmId") int alarmId, @Param("userId") String userId);

    public void updateToRead(@Param("alarmId") int alarmId, @Param("userId") String userId);
}
