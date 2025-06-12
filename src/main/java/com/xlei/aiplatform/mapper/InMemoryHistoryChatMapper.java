package com.xlei.aiplatform.mapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author https://github.com/xuzhixing181
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class InMemoryHistoryChatMapper implements HistoryChatMapper {

    private Map<String,List<String>> historyChat = new HashMap<>();

    @Override
    public void save(String type,String chatId) {
        // Todo 根据真实的业务场景获取
        String userId = String.valueOf(101);
//        if (!chatHistory.containsKey(type)) {
//            chatHistory.put(type, new ArrayList<>());
//        }
//        List<String> chatIds = chatHistory.get(type);
        List<String> chatIdList = historyChat.computeIfAbsent(userId, uid -> new ArrayList<>());
        if (chatIdList.contains(chatId)){
            return;
        }
        chatIdList.add(chatId);
    }

    @Override
    public List<String> getChatIds(String userId) {
        return historyChat.getOrDefault(userId,List.of());
    }
}