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
}
