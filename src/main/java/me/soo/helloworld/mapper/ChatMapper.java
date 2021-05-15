package me.soo.helloworld.mapper;

import me.soo.helloworld.model.chat.ChatBox;
import me.soo.helloworld.model.chat.ChatWrite;
import me.soo.helloworld.util.Pagination;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ChatMapper {

    public void insertChats(List<ChatWrite> chats);

    public boolean isChatBoxExist(String sender, String recipient);

    public void insertChatBox(String sender, String recipient);

    public int getChatBoxId(String sender, String recipient);

    public List<ChatBox> getChatBoxes(@Param("userId") String userId,
                                      @Param("pagination") Pagination pagination);
}
