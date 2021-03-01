package me.soo.helloworld.model.language;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.soo.helloworld.enumeration.LanguageStatus;

import java.util.List;

@Getter
@AllArgsConstructor
public class LanguageUpsertRequest {

    private final List<LanguageData> languagesRequest;

    private final LanguageStatus status;
}
