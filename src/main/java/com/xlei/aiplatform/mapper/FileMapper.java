package com.xlei.aiplatform.mapper;

import org.springframework.core.io.Resource;

/**
 * @author https://github.com/xuzhixing181
 * 文件相关操作的持久层
 */
public interface FileMapper {

    /**
     * 保存文件到本地,并建立chatId和文件的映射关系
     * @param chatId
     * @param resource
     */
     boolean save(String chatId, Resource resource);

    /**
     * 根据chatId获取文件
     * @param chatId: 会话id
     * @return 找到的文件名
     */
    Resource getFile(String chatId);

    /**
     * PDF文件写入到向量库
     * @param resource
     */
    void pdfWriteToVectorStore(Resource resource);

}