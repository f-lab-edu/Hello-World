package me.soo.helloworld.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BlockUserMapper {

    public void insertBlockUser(String userId, String targetId);

    public boolean isUserBlocked(String userId, String targetId);

    public void deleteBlockUser(String userId, String targetId);
}
