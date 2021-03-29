package me.soo.helloworld.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface FetchNameMapper {

    public List<Map<String, Object>> fetchAllCountryIdsAndNames();

    public List<Map<String, Object>> fetchAllTownIdsAndNames();

    public List<Map<String, Object>> fetchAllLanguageIdsAndNames();
}
