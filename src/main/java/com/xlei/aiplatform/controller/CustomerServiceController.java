package com.xlei.aiplatform.controller;

import com.xlei.aiplatform.mapper.HistoryChatMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;

/**
 * @author https://github.com/xuzhixing181
 * 智能客服控制层
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/ai")
public class CustomerServiceController {

    private final ChatClient customerServiceChatClient;

    private final HistoryChatMapper chatHistoryRepository;

    @RequestMapping(value = "/service", produces = "text/html;charset=utf-8")
    public String service(String prompt, String chatId) {
        // Todo 根据登录的用户信息获取userId
        String userId = "101";
        // 1.保存会话id
        chatHistoryRepository.save(userId, chatId);
        // 2.请求模型
        return customerServiceChatClient.prompt()
                .user(prompt)
                .advisors(a -> a.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId))
                .call()
                .content();
    }
}