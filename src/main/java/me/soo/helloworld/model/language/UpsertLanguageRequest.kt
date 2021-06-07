package me.soo.helloworld.model.language

import me.soo.helloworld.enumeration.LanguageStatus

data class UpsertLanguageRequest(

    val languages: List<LanguageRequest>,

    val status: LanguageStatus
)
