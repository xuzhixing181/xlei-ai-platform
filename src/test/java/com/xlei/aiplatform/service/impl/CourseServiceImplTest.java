package com.xlei.aiplatform.service.impl;

import com.xlei.aiplatform.mapper.db.CourseMapper;
import com.xlei.aiplatform.model.entity.Course;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CourseServiceImplTest {

    @Resource
    private CourseMapper courseMapper;

    @Test
    void testList() {
        List<Course> courses = courseMapper.selectList(null);
        Assertions.assertNotNull(courses);
    }
}