package me.soo.helloworld.mapper;

import me.soo.helloworld.model.recommendation.RecommendationForProfile;
import me.soo.helloworld.model.recommendation.Recommendation;
import me.soo.helloworld.model.recommendation.Recommendations;
import me.soo.helloworld.util.Pagination;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface RecommendationMapper {

    public boolean isRecommendationExist(String from, String to);

    public void insertRecommendation(Recommendation recommendation);

    public Optional<Integer> getHowLongSinceWrittenAt(@Param("to") String to, @Param("from") String from);

    public void updateRecommendation(@Param("to") String to, @Param("from") String from,
                                     @Param("modifiedContent") String modifiedContent);

    public List<RecommendationForProfile> getRecommendationsForProfile(String userId);

    public List<Recommendations> getRecommendationsAboutTarget(@Param("targetId") String targetId,
                                                               @Param("pagination") Pagination pagination,
                                                               @Param("userId") String userId);
}
