package me.soo.helloworld.model.language;

import lombok.*;
import me.soo.helloworld.enumeration.LanguageLevel;
import me.soo.helloworld.enumeration.LanguageStatus;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class Language {

    private String name;

    private LanguageLevel level;

    private LanguageStatus status;
}
