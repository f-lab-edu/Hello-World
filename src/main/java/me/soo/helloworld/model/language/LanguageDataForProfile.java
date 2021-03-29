package me.soo.helloworld.model.language;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.soo.helloworld.enumeration.LanguageLevel;
import me.soo.helloworld.enumeration.LanguageStatus;

@Getter
@RequiredArgsConstructor
public class LanguageDataForProfile {

    private final int id;

    private final LanguageLevel level;

    private final LanguageStatus status;
}
