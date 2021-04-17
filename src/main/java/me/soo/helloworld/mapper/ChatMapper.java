package me.soo.helloworld.mapper;

import me.soo.helloworld.model.chat.ChatSendRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ChatMapper {

    public void insertChat(@Param("sender") String sender,
                           @Param("chat") ChatSendRequest chat);

}
