package com.xlei.aiplatform.service.impl;

import com.xlei.aiplatform.mapper.db.CourseReserveMapper;
import com.xlei.aiplatform.model.entity.CourseReserve;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class CourseReserveServiceImplTest {

    @Resource
    private CourseReserveMapper courseReserveMapper;

    @Test
    void testList() {
        List<CourseReserve> courseReserves = courseReserveMapper.selectList(null);
        Assertions.assertNotNull(courseReserves);

    }
}