package me.soo.helloworld.controller

import me.soo.helloworld.annotation.CurrentUser
import me.soo.helloworld.annotation.LoginRequired
import me.soo.helloworld.model.language.UpsertLanguageRequest
import me.soo.helloworld.service.LanguageService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/my-infos")
class LanguageController(

    val languageService: LanguageService
) {

    @LoginRequired
    @PostMapping("/languages")
    fun addLanguages(@CurrentUser userId: String, @RequestBody addRequest: UpsertLanguageRequest) =
        languageService.addLanguages(userId, addRequest.languages, addRequest.status)

    @LoginRequired
    @PutMapping("/languages")
    fun modifyLevels(@CurrentUser userId: String, @RequestBody modifyRequest: UpsertLanguageRequest) =
        languageService.modifyLevels(userId, modifyRequest.languages, modifyRequest.status)

    @LoginRequired
    @DeleteMapping("languages")
    fun deleteLanguages(@CurrentUser userId: String, @RequestBody deleteRequest: List<Int>) =
        languageService.deleteLanguages(userId, deleteRequest)
}
