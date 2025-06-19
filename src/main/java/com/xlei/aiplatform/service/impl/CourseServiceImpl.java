package com.xlei.aiplatform.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xlei.aiplatform.mapper.db.CourseMapper;
import com.xlei.aiplatform.model.entity.Course;
import com.xlei.aiplatform.model.entity.CourseReserve;
import com.xlei.aiplatform.service.CourseService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author https://github.com/xuzhixing181
* @description 针对表【course(学科表)】的数据库操作Service实现
*/
@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course>
    implements CourseService {

    public List<Course> testList(){
        return baseMapper.selectList(null);
    }
}




