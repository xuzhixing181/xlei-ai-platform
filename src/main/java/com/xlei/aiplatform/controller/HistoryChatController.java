package com.xlei.aiplatform.controller;

import com.xlei.aiplatform.mapper.HistoryChatMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

/**
 * @author https://github.com/xuzhixing181
 * 会话历史控制层
 */
@RestController
@RequestMapping("/ai/history")
@RequiredArgsConstructor
public class HistoryChatController {

    private final HistoryChatMapper historyChatMapper;

    private final ChatMemory chatMemory;

    @GetMapping("/{type}")
    public List<String> getChatIds(@PathVariable("type") String type){
        return historyChatMapper.getChatIds(type);
    }

    @GetMapping("/{type}/{chatId}")
    public List<MessageVo> getHistoryChat(@PathVariable("type") String type,@PathVariable("chatId") String chatId){
        List<Message> messages = chatMemory.get(chatId, Integer.MAX_VALUE);
        if (CollectionUtils.isEmpty(messages)){
            return List.of();
        }
        return messages.stream().map(MessageVo::new).toList();
    }

}