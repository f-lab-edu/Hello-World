package me.soo.helloworld.controller;

import lombok.RequiredArgsConstructor;
import me.soo.helloworld.annotation.CurrentUser;
import me.soo.helloworld.annotation.LoginRequired;
import me.soo.helloworld.model.alarm.Alarm;
import me.soo.helloworld.service.AlarmService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}