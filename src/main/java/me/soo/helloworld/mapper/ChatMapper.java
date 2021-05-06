package me.soo.helloworld.mapper;

import me.soo.helloworld.model.chat.ChatWrite;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ChatMapper {

    public void insertChats(List<ChatWrite> chats);

    public boolean isChatBoxExist(String sender, String recipient);

    public void insertChatBox(String sender, String recipient);

    public int getChatBoxId(String sender, String recipient);
}
