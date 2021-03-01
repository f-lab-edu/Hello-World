package me.soo.helloworld.controller;

import lombok.RequiredArgsConstructor;
import me.soo.helloworld.annotation.CurrentUser;
import me.soo.helloworld.model.language.LanguageUpsertRequest;
import me.soo.helloworld.service.LanguageService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/languages")
@RequiredArgsConstructor
public class LanguageController {

    private final LanguageService languageService;

    @PostMapping
    public void addLanguages(@CurrentUser String userId, @RequestBody LanguageUpsertRequest languageAddRequest) {

        languageService.addLanguages(userId, languageAddRequest.getLanguagesRequest(), languageAddRequest.getStatus());
    }

    @PutMapping
    public void modifyLanguageLevel(@CurrentUser String userId, @RequestBody LanguageUpsertRequest languageModifyRequest) {

        languageService.modifyLevel(userId, languageModifyRequest.getLanguagesRequest(), languageModifyRequest.getStatus());
    }

    @DeleteMapping
    public void deleteLanguages(@CurrentUser String userId,
                                @RequestBody List<Integer> languageDeleteRequest) {

        languageService.deleteLanguages(userId, languageDeleteRequest);
    }
}
