package me.soo.helloworld.model.language;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.soo.helloworld.enumeration.LanguageLevel;
import me.soo.helloworld.enumeration.Language;

@Getter
@AllArgsConstructor
public class LanguageData {

    private final Language name;

    private final LanguageLevel level;

}
