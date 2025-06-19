package com.xlei.aiplatform.config;

import com.xlei.aiplatform.tools.CourseTool;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.xlei.aiplatform.constants.SystemConstant.CUSTOMER_SERVICE_SYSTEM;

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

    /**
     * 客服聊天客户端
     * @param model
     * @param chatMemory
     * @param courseTool
     * @return
     */
    @Bean
    public ChatClient customerServiceChatClient(OpenAiChatModel model,
                                        ChatMemory chatMemory, CourseTool courseTool) {
        return ChatClient.builder(model)
                .defaultSystem(CUSTOMER_SERVICE_SYSTEM)
                .defaultAdvisors(
                        new MessageChatMemoryAdvisor(chatMemory), // CHAT MEMORY
                        new SimpleLoggerAdvisor())
                .defaultTools(courseTool)
                .build();
    }


}