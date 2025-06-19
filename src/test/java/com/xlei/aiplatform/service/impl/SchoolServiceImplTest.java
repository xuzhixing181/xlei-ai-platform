package com.xlei.aiplatform.service.impl;

import com.xlei.aiplatform.mapper.db.SchoolMapper;
import com.xlei.aiplatform.model.entity.School;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SchoolServiceImplTest {

    @Resource
    private SchoolMapper schoolMapper;

    @Test
    void testList() {
        List<School> schools = schoolMapper.selectList(null);
        Assertions.assertNotNull(schools);
    }
}