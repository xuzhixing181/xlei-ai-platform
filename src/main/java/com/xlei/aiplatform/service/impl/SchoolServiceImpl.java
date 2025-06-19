package com.xlei.aiplatform.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xlei.aiplatform.mapper.db.SchoolMapper;
import com.xlei.aiplatform.model.entity.CourseReserve;
import com.xlei.aiplatform.model.entity.School;
import com.xlei.aiplatform.service.SchoolService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author https://github.com/xuzhixing181
* @description 针对表【school(校区表)】的数据库操作Service实现
*/
@Service
public class SchoolServiceImpl extends ServiceImpl<SchoolMapper, School>
    implements SchoolService {


    public List<School> testList(){
        return baseMapper.selectList(null);
    }
}




