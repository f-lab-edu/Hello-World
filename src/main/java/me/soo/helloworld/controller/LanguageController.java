package me.soo.helloworld.controller;

import lombok.RequiredArgsConstructor;
import me.soo.helloworld.annotation.CurrentUser;
import me.soo.helloworld.model.language.LanguageDataWrapper;
import me.soo.helloworld.service.LanguageService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/languages")
@RequiredArgsConstructor
public class LanguageController {

    private final LanguageService languageService;

    @PostMapping
    public void addLanguages(@CurrentUser String userId, @RequestBody LanguageDataWrapper languageDataWrapper) {

        languageService.addLanguages(userId, languageDataWrapper.getDataList(), languageDataWrapper.getStatus());
    }
}
