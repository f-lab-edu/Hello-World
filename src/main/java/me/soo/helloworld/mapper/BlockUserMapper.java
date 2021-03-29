package me.soo.helloworld.mapper;

import me.soo.helloworld.model.blockuser.BlockUserList;
import me.soo.helloworld.model.blockuser.BlockUserListRequest;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BlockUserMapper {

    public void insertBlockUser(String userId, String targetId);

    public boolean isUserBlocked(String userId, String targetId);

    public void deleteBlockUser(String userId, String targetId);

    public List<BlockUserList> getBlockUserList(BlockUserListRequest request);
}
