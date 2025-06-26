package com.xlei.aiplatform.controller;

import com.xlei.aiplatform.mapper.FileMapper;
import com.xlei.aiplatform.mapper.HistoryChatMapper;
import com.xlei.aiplatform.model.vo.CommonResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URLEncoder;
import java.util.Objects;

import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;

/**
 * @author https://github.com/xuzhixing181
 * 文件控制层
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/ai/file")
public class FileController {

    private final FileMapper fileMapper;

    private final VectorStore vectorStore;

    private final ChatClient pdfChatClient;

    private final HistoryChatMapper historyChatMapper;

    /**
     * 上传PDF文件,构建RAG知识库
     * @param chatId
     * @return
     */
    @PostMapping(value = "/uploadPdf")
    public CommonResult uploadPdf(@RequestParam("chatId")  String chatId, @RequestParam("file") MultipartFile file) {
        // 1.校验文件格式是否为pdf
        if (!Objects.equals(file.getContentType(), MediaType.APPLICATION_PDF_VALUE)) {
            return CommonResult.fail("请上传pdf文件");
        }
        try {
            // 2.保存文件
            boolean success = fileMapper.save(chatId, file.getResource());
            if (!success) {
                return CommonResult.fail("保存pdf文件失败!");
            }
            // 3.写入向量库
            fileMapper.pdfWriteToVectorStore(file.getResource());
            return CommonResult.ok();
        } catch (Exception e) {
            log.error("Failed to upload PDF.", e);
            return CommonResult.fail("上传文件失败！");
        }
    }

    /**
     * PDF文件下载
     * @param chatId
     * @return
     */
    @GetMapping("/downloadPdf/{chatId}")
    public ResponseEntity<Resource> downloadPdf(@PathVariable("chatId") String chatId) {
        // 1.读取文件
        Resource resource = fileMapper.getFile(chatId);
        if (!resource.exists()) {
            return ResponseEntity.notFound().build();
        }
        // 2.文件名编码,写入响应头
        String fileName = URLEncoder.encode(Objects.requireNonNull(resource.getFilename()));
        // 3.下载的PDF文件
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header("Content-Disposition", "attachment; filename=\"" + fileName + "\"")
                .body(resource);
    }


    @GetMapping(value = "/chat")
    public String chat(String prompt, String chatId) {
        // Todo 根据真实的业务场景获取userId
        String userId = String.valueOf(101);
        historyChatMapper.save(userId, chatId);
        Resource file = fileMapper.getFile(chatId);
        return pdfChatClient
                .prompt(prompt)
                .advisors(a -> a.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId))
                .advisors(a -> a.param(QuestionAnswerAdvisor.FILTER_EXPRESSION, "file_name == '" + file.getFilename()+"'"))
                .call()
                .content();
    }
}