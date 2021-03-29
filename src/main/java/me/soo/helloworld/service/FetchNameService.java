package me.soo.helloworld.service;

import lombok.RequiredArgsConstructor;
import me.soo.helloworld.mapper.FetchNameMapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static me.soo.helloworld.util.CacheNames.*;

@Service
@RequiredArgsConstructor
public class FetchNameService {

    private final FetchNameMapper fetchNameMapper;

    @Cacheable(COUNTRIES_MAP)
    public Map<Integer, String> loadAllCountriesMap() {
        return convertIntoMap(fetchNameMapper.fetchAllCountryIdsAndNames());
    }

    @Cacheable(TOWNS_MAP)
    public Map<Integer, String> loadAllTownsMap() {
        return convertIntoMap(fetchNameMapper.fetchAllTownIdsAndNames());
    }

    @Cacheable(LANGUAGES_MAP)
    public Map<Integer, String> loadAllLanguagesMap() {
        return convertIntoMap(fetchNameMapper.fetchAllLanguageIdsAndNames());
    }

    private Map<Integer, String> convertIntoMap(List<Map<String, Object>> idsAndNames) {

        return idsAndNames.stream().collect(
                Collectors.toMap(
                    id -> (Integer) id.get(ID),
                    name -> (String) name.get(NAME)
                ));
    }
}
