package me.soo.helloworld.model.language;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.soo.helloworld.enumeration.LanguageLevel;

@Getter
@AllArgsConstructor
public class LanguageData {

    private final int id;

    private final LanguageLevel level;

}
