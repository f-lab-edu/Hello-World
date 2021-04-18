package me.soo.helloworld.mapper;

import me.soo.helloworld.model.chat.ChatData;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ChatMapper {

    public void insertChat(ChatData chatData);

    public boolean isChatBoxExist(String sender, String recipient);

    public void insertChatBox(String sender, String recipient);

    public int getChatBoxId(String sender, String recipient);
}
