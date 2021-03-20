package me.soo.helloworld.model.alarm;

import lombok.Builder;
import lombok.Getter;
import me.soo.helloworld.enumeration.AlarmTypes;

@Getter
@Builder
public class Alarm {

    private final String to;

    private final String from;

    private final AlarmTypes type;

    public static Alarm create(String to, String from, AlarmTypes type) {

        return Alarm.builder()
                    .to(to)
                    .from(from)
                    .type(type)
                    .build();
    }
}
