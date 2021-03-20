package me.soo.helloworld.model.alarm;

import lombok.Value;
import me.soo.helloworld.enumeration.AlarmTypes;

import java.util.Date;

@Value
public class Alarm {

    int id;

    String to;

    String from;

    AlarmTypes type;

    String hasRead;

    Date createdAt;
  
    public static Alarm create(String to, String from, AlarmTypes type) {

        return Alarm.builder()
                    .to(to)
                    .from(from)
                    .type(type)
                    .build();
    }
}
