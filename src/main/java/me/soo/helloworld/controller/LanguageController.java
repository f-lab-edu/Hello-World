package me.soo.helloworld.controller;

import lombok.RequiredArgsConstructor;
import me.soo.helloworld.annotation.CurrentUser;
import me.soo.helloworld.enumeration.LanguageStatus;
import me.soo.helloworld.model.language.LanguageData;
import me.soo.helloworld.service.LanguageService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

import static me.soo.helloworld.util.http.HttpResponses.HTTP_RESPONSE_OK;

@Controller
@RequestMapping("/languages")
@RequiredArgsConstructor
public class LanguageController {

    private final LanguageService languageService;

    @PostMapping
    public ResponseEntity<Void> addLanguages(@CurrentUser String userId,
                                            @RequestBody List<LanguageData> newLangData,
                                            @RequestParam LanguageStatus status) {

        languageService.addLanguages(userId, newLangData, status);

        return HTTP_RESPONSE_OK;
    }
}
