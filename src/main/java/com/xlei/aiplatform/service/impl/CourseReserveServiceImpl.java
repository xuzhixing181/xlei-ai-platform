package com.xlei.aiplatform.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xlei.aiplatform.mapper.db.CourseReserveMapper;
import com.xlei.aiplatform.model.entity.CourseReserve;
import com.xlei.aiplatform.service.CourseReserveService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author https://github.com/xuzhixing181
* @description 针对表【course_reserve】的数据库操作Service实现
*/
@Service
public class CourseReserveServiceImpl extends ServiceImpl<CourseReserveMapper, CourseReserve>
    implements CourseReserveService {

    public List<CourseReserve> testList(){
        return baseMapper.selectList(null);
    }

}




