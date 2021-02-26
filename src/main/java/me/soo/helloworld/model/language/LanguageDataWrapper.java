package me.soo.helloworld.model.language;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.soo.helloworld.enumeration.LanguageStatus;

import java.util.List;

@Getter
@AllArgsConstructor
public class LanguageDataWrapper {

    private final List<LanguageData> dataList;

    private final LanguageStatus status;
}
