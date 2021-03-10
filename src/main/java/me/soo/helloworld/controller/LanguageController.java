package me.soo.helloworld.controller;

import lombok.RequiredArgsConstructor;
import me.soo.helloworld.annotation.CurrentUser;
import me.soo.helloworld.annotation.LoginRequired;
import me.soo.helloworld.model.language.LanguageUpsertRequest;
import me.soo.helloworld.service.LanguageService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/my-infos")
@RequiredArgsConstructor
public class LanguageController {

    private final LanguageService languageService;

    @LoginRequired
    @PostMapping("/languages")
    public void addLanguages(@CurrentUser String userId,
                             @RequestBody LanguageUpsertRequest languageAddRequest) {

        languageService.addLanguages(userId, languageAddRequest.getLanguagesRequest(), languageAddRequest.getStatus());
    }

    @LoginRequired
    @PutMapping("/languages")
    public void modifyLanguageLevels(@CurrentUser String userId,
                                     @RequestBody LanguageUpsertRequest languageModifyRequest) {

        languageService.modifyLanguageLevels(userId, languageModifyRequest.getLanguagesRequest(), languageModifyRequest.getStatus());
    }

    @LoginRequired
    @DeleteMapping("/languages")
    public void deleteLanguages(@CurrentUser String userId,
                                @RequestBody List<Integer> languageDeleteRequest) {

        languageService.deleteLanguages(userId, languageDeleteRequest);
    }
}
