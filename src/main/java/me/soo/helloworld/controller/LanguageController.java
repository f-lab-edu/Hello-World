package me.soo.helloworld.controller;

import lombok.RequiredArgsConstructor;
import me.soo.helloworld.annotation.CurrentUser;
import me.soo.helloworld.model.language.LanguageDataWrapper;
import me.soo.helloworld.service.LanguageService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/languages")
@RequiredArgsConstructor
public class LanguageController {

    private final LanguageService languageService;

    @PostMapping
    public void addLanguages(@CurrentUser String userId, @RequestBody LanguageDataWrapper languageDataWrapper) {

        languageService.addLanguages(userId, languageDataWrapper.getDataList(), languageDataWrapper.getStatus());
    }

    @PutMapping
    public void modifyLanguageLevel(@CurrentUser String userId, @RequestBody LanguageDataWrapper languageDataWrapper) {

        languageService.modifyLevel(userId, languageDataWrapper.getDataList(), languageDataWrapper.getStatus());
    }

    @DeleteMapping
    public void deleteLanguages(@CurrentUser String userId,
                                @RequestBody List<Integer> languageDeleteRequest) {

        languageService.deleteLanguages(userId, languageDeleteRequest);
    }
}
