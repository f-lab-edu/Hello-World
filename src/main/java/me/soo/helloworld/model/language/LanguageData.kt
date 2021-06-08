package me.soo.helloworld.model.language

import me.soo.helloworld.enumeration.LanguageLevel
import me.soo.helloworld.enumeration.LanguageStatus

data class LanguageData(val id: Int, val level: LanguageLevel, val status: LanguageStatus)
