package me.soo.helloworld.controller;

import lombok.RequiredArgsConstructor;
import me.soo.helloworld.annotation.CurrentUser;
import me.soo.helloworld.annotation.LoginRequired;
import me.soo.helloworld.model.alarm.Alarm;
import me.soo.helloworld.service.AlarmService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/my-alarms")
public class AlarmController {

    private final AlarmService alarmService;

    @LoginRequired
    @GetMapping
    public List<Alarm> getAlarmList(@CurrentUser String userId,
                                    @RequestParam(defaultValue = "1") Integer pageNumber) {
        return alarmService.getAlarmList(userId, pageNumber);
    }

    @LoginRequired
    @GetMapping("/{alarm-id}")
    public Alarm getAlarm(@CurrentUser String userId, @PathVariable("alarm-id") Integer alarmId) {
        return alarmService.getAlarm(alarmId, userId);
    }

    @LoginRequired
    @DeleteMapping("/{alarm-id}")
    public void removeAlarm(@CurrentUser String userId, @PathVariable("alarm-id") Integer alarmId) {
        alarmService.removeAlarm(alarmId, userId);
    }
}