package com.xlei.aiplatform.service;

import reactor.core.publisher.Flux;

/**
 * @author https://github.com/xuzhixing181
 */
public interface AIChatService {

    /**
     * 阻塞式,简单地多轮对话 (支持会话记忆)
     * @param prompt:提问提示词
     * @param chatId:会话 ID
     * @return
     */
    String simpleChat(String prompt, String chatId);

    /**
     * SSE流式输出,多轮对话
     * @param prompt:提问提示词
     * @param chatId:会话 ID
     * @return
     */
    Flux<String> chatByStream(String prompt, String chatId);
}