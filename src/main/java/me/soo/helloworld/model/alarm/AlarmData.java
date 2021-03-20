package me.soo.helloworld.model.alarm;

import lombok.Builder;
import me.soo.helloworld.enumeration.AlarmTypes;

@Builder
public class AlarmData {

    private final String to;

    private final String from;

    private final AlarmTypes type;

    public static AlarmData create(String to, String from, AlarmTypes type) {

        return AlarmData.builder()
                        .to(to)
                        .from(from)
                        .type(type)
                        .build();
    }
}
