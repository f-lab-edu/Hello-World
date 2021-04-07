package me.soo.helloworld.model.alarm;

import lombok.Value;
import me.soo.helloworld.enumeration.AlarmTypes;

import java.time.LocalDate;

@Value
public class Alarm {

    int id;

    String to;

    String from;

    AlarmTypes type;

    String hasRead;

    LocalDate createdAt;
}
