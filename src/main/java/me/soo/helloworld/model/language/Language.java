package me.soo.helloworld.model.language;

import lombok.*;
import me.soo.helloworld.enumeration.LanguageLevel;
import me.soo.helloworld.enumeration.LanguageStatus;

@Getter
@Builder
public class Language {

    private final String name;

    private final LanguageLevel level;

    private final LanguageStatus status;
}
