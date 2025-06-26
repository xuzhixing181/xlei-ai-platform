package com.xlei.aiplatform.mapper;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.SystemProperties;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.ExtractedTextFormatter;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

/**
 * @author https://github.com/xuzhixing181
 * 本地PDF文件持久层
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class LocalPdfMapper implements FileMapper{

    private final VectorStore vectorStore;

    private final Properties chatFileMap = new Properties();

    @Override
    public boolean save(String chatId, Resource resource) {
        String filename = resource.getFilename();
        // 1.文件校验
        File targetFile = new File(Objects.requireNonNull(filename));
        if (!targetFile.exists()){
            try {
                // 2.保存到本地磁盘
                Files.copy(resource.getInputStream(), Path.of(System.getProperty("user.dir") + "/doc/chatPDF/" + targetFile.toPath()));
            } catch (IOException e) {
                log.error("Failed to save PDF resource.", e);
                return false;
            }
        }
        // 3. 建立 会话id 与 文件名的对应关系,方便查询会话历史时重新加载文件
        chatFileMap.putIfAbsent(chatId, targetFile);
        return true;
    }

    @Override
    public Resource getFile(String chatId) {
        return new FileSystemResource(String.valueOf(chatFileMap.get(chatId)));
    }

    @Override
    public void pdfWriteToVectorStore(Resource resource) {
        // 1.创建PDF的读取器
        PagePdfDocumentReader reader = new PagePdfDocumentReader(
                resource, // 文件源
                PdfDocumentReaderConfig.builder()
                        .withPageExtractedTextFormatter(ExtractedTextFormatter.defaults())
                        .withPagesPerDocument(1) // 每1页PDF作为一个Document
                        .build()
        );
        // 2.读取PDF文档，拆分为Document
        List<Document> documents = reader.read();
        // 3.写入向量库
        vectorStore.add(documents);
    }

    @PostConstruct
    private void init() {
        // 1.从pdf文件加载到 Properties存储中
        FileSystemResource pdfResource = new FileSystemResource("chat-pdf.properties");
        if (pdfResource.exists()) {
            try {
                chatFileMap.load(new BufferedReader(new InputStreamReader(pdfResource.getInputStream(), StandardCharsets.UTF_8)));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        // 2.将文件内容加载到 向量数据库
        FileSystemResource vectorResource = new FileSystemResource("chat-pdf.json");
        if (vectorResource.exists()) {
            SimpleVectorStore simpleVectorStore = (SimpleVectorStore) vectorStore;
            simpleVectorStore.load(vectorResource);
        }
    }

    /**
     * 停机前,完成文件的持久化操作
     */
    @PreDestroy
    private void persistent() {
        try {
            chatFileMap.store(new FileWriter("chat-pdf.properties"), LocalDateTime.now().toString());
            SimpleVectorStore simpleVectorStore = (SimpleVectorStore) vectorStore;
            simpleVectorStore.save(new File("chat-pdf.json"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}