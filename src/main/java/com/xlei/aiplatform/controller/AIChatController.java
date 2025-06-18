package com.xlei.aiplatform.controller;

import com.xlei.aiplatform.mapper.HistoryChatMapper;
import com.xlei.aiplatform.service.AIChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;
import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY;

/**
 * @author https://github.com/xuzhixing181
 * AI交互控制层
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/ai")
public class AIChatController {

    private final ChatClient chatClient;

    private final HistoryChatMapper historyChatMapper;

    private final AIChatService aiChatService;

    /**
     * 阻塞式,简单地多轮对话 (支持会话记忆)
     * @param prompt
     * @param chatId
     * @return
     */
    @GetMapping(value = "/chat")
    public String simpleChat(String prompt, String chatId){
//        ChatResponse chatResponse = chatClient.prompt().user(prompt).
//                advisors(a -> a.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
//                        // CHAT_MEMORY_CONVERSATION_ID_KEY: 聊天会话的唯一标识符
//                        // CHAT_MEMORY_RETRIEVE_SIZE_KEY: 指定从记忆中检索的历史消息数量,即上下文的长度
//                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 5)).call().chatResponse();
//        String content = chatResponse.getResult().getOutput().getText();
//        return content;
        return aiChatService.simpleChat(prompt,chatId);
    }

    /**
     * 打字机式地向用户响应回答 (非阻塞式,SSE流式输出)
     * @param prompt
     * @param chatId
     *   需要通过 produces 指定字符编码,不然会返回给客户端的会是乱码
     * @return
     */
    @GetMapping(value = "/chatByStream",produces = "text/html;charset=utf-8")
    public Flux<String> chatByStream(String prompt, String chatId){
//        // 1.保存会话id
//        historyChatMapper.save(101+"",chatId);
//        // 2.调用大模型
//        Flux<String> content = chatClient.prompt().user(prompt).
//                advisors(a -> a.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
//                        // CHAT_MEMORY_CONVERSATION_ID_KEY: 聊天会话的唯一标识符
//                        // CHAT_MEMORY_RETRIEVE_SIZE_KEY: 指定从记忆中检索的历史消息数量,即上下文的长度
//                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 5)).stream().content();
//        return content;
        return aiChatService.chatByStream(prompt,chatId);
    }
}