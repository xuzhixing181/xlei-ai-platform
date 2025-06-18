package com.xlei.aiplatform.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author https://github.com/xuzhixing181
 * 基础配置类
 */
@Configuration
public class BaseConfiguration {

    @Bean
    public ChatMemory chatMemory(){
        return new InMemoryChatMemory();
    }

    @Bean
    public ChatClient chatClient(OllamaChatModel model,ChatMemory chatMemory){
        return ChatClient.builder(model)
                .defaultSystem("你是一个博学多才,热心肠,解决问题能力非常强的牛人,请以该身份回答用户问题")
                .defaultAdvisors(
                        new SimpleLoggerAdvisor(),   // 与AI大模型交互时生成日志增强,yaml配置文件中需指定日志级别为debug
                        new MessageChatMemoryAdvisor(chatMemory))  // 会话记忆增强
                .build();

    }


}