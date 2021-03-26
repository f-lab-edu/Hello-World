package me.soo.helloworld.mapper;

import me.soo.helloworld.model.recommendation.RecommendationDataForProfile;
import me.soo.helloworld.model.recommendation.Recommendation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface RecommendationMapper {

    public boolean isRecommendationExist(String from, String to);

    public void insertRecommendation(Recommendation recommendation);

    public Optional<Integer> getHowLongSinceWrittenAt(@Param("id") int id, @Param("from") String from);

    public void updateRecommendation(@Param("id") int id, @Param("from") String from,
                                     @Param("modifiedContent") String modifiedContent);

    public List<RecommendationDataForProfile> getRecommendationsByUserId(String userId);
}
