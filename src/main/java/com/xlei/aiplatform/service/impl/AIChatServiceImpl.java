package com.xlei.aiplatform.service.impl;

import com.xlei.aiplatform.mapper.HistoryChatMapper;
import com.xlei.aiplatform.model.enums.ServiceType;
import com.xlei.aiplatform.service.AIChatService;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;
import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY;

/**
 * @author https://github.com/xuzhixing181
 */
@Service
@RequiredArgsConstructor
public class AIChatServiceImpl implements AIChatService {

    private final ChatClient chatClient;

    private final HistoryChatMapper historyChatMapper;




    @Override
    public String simpleChat(String prompt, String chatId) {
        ChatResponse chatResponse = chatClient.prompt().user(prompt).
                advisors(a -> a.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        // CHAT_MEMORY_CONVERSATION_ID_KEY: 聊天会话的唯一标识符
                        // CHAT_MEMORY_RETRIEVE_SIZE_KEY: 指定从记忆中检索的历史消息数量,即上下文的长度
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 5)).call().chatResponse();
        String content = chatResponse.getResult().getOutput().getText();
        return content;
    }

    @Override
    public Flux<String> chatByStream(String prompt, String chatId) {
        // Todo 根据真实的业务场景获取userId
        String userId = String.valueOf(101);
        // 1.保存会话id
        historyChatMapper.save(String.valueOf(ServiceType.CHAT),chatId);
        // 2.调用大模型
        Flux<String> content = chatClient.prompt().user(prompt).
                advisors(a -> a.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        // CHAT_MEMORY_CONVERSATION_ID_KEY: 聊天会话的唯一标识符
                        // CHAT_MEMORY_RETRIEVE_SIZE_KEY: 指定从记忆中检索的历史消息数量,即上下文的长度
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 5)).stream().content();
        return content;
    }
}