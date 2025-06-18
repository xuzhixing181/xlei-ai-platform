package com.xlei.aiplatform.mapper;

import java.util.List;

/**
 * @author https://github.com/xuzhixing181
 */
public interface HistoryChatMapper {

    /**
     * 保存会话记录
     * @param type 业务类型
     * @param chatId 会话ID
     */
    void save(String type, String chatId);

    /**
     * 获取会话ID列表
     * @param userId
     * @return 会话ID列表
     */
    List<String> getChatIds(String userId);
}