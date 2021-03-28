package me.soo.helloworld.service;

import lombok.RequiredArgsConstructor;
import me.soo.helloworld.mapper.FetchNameMapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static me.soo.helloworld.util.CacheNames.*;

@Service
@RequiredArgsConstructor
public class FetchNameService {

    private final FetchNameMapper fetchNameMapper;

    @Cacheable(value = COUNTRIES_MAP, cacheManager = "caffeineCacheManager")
    public Map<Integer, String> loadAllCountriesMap() {
        return convertIntoMap(fetchNameMapper.fetchAllCountryIdsAndNames());
    }

    @Cacheable(value = TOWNS_MAP, cacheManager = "caffeineCacheManager")
    public Map<Integer, String> loadAllTownsMap() {
        return convertIntoMap(fetchNameMapper.fetchAllTownIdsAndNames());
    }

    @Cacheable(value = LANGUAGES_MAP, cacheManager = "caffeineCacheManager")
    public Map<Integer, String> loadAllLanguagesMap() {
        return convertIntoMap(fetchNameMapper.fetchAllLanguageIdsAndNames());
    }

    private Map<Integer, String> convertIntoMap(List<Map<String, Object>> idsAndNames) {
        Map<Integer, String> convertedMap = new HashMap<>();
        idsAndNames.forEach(idAndName -> convertedMap.put((Integer) idAndName.get(ID), (String) idAndName.get(NAME)));

        return convertedMap;
    }
}
