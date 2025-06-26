package com.xlei.aiplatform.config;

import com.xlei.aiplatform.tools.CourseTool;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
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

    /**
     * @param embeddingModel: 百炼平台 通用文本向量text-embedding-v3
     * @return 基于内存实现的向量库
     */
    @Bean
    public VectorStore vectorStore(OpenAiEmbeddingModel embeddingModel) {
        return SimpleVectorStore.builder(embeddingModel).build();
    }

    /**
     * 基于上传的pdf文档构建 RAG知识库
     * @param model
     * @param chatMemory
     * @param vectorStore
     * @return
     */
    @Bean
    public ChatClient pdfChatClient(OpenAiChatModel model, ChatMemory chatMemory, VectorStore vectorStore) {
        SearchRequest searchRequest = SearchRequest.builder() // 向量检索的请求参数
                .similarityThreshold(0.5d) // 相似度阈值
                .topK(3) // 返回的文档片段数量
                .build();
        return ChatClient.builder(model)
                .defaultSystem("请根据提供的上下文回答问题，不要随意猜测和编造")
                .defaultAdvisors(
                        new MessageChatMemoryAdvisor(chatMemory), // 历史会话增强
                        new SimpleLoggerAdvisor(),  // 聊天会话日志增强
                        // 基于RAG & 知识库的提问回答增强
                        new QuestionAnswerAdvisor(vectorStore, searchRequest))
                .build();
    }

}