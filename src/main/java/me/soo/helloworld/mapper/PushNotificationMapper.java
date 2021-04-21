package me.soo.helloworld.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PushNotificationMapper {

    public void upsertToken(String userId, String token);

    public String getToken(String userId);
}
