package me.soo.helloworld.mapper;

import me.soo.helloworld.model.recommendation.Recommendation;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RecommendationMapper {

    public boolean isRecommendationExist(String from, String to);

    public void insertRecommendation(Recommendation recommendation);
}
