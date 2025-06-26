package com.xlei.aiplatform.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.ai.chat.messages.Message;

/**
 * @author https://github.com/xuzhixing181
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageVo {

    /**
     * 对话的角色(用户 or AI助手)
     */
    private String role;

    /**
     * 聊天的内容
     */
    private String content;

    public MessageVo(Message message){
        this.role = switch (message.getMessageType()) {
            case USER -> "user";
            case ASSISTANT -> "assistant";
            case SYSTEM -> "system";
            default -> "";
        };
        this.content = message.getText();

    }
}