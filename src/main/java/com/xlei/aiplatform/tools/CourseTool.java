package com.xlei.aiplatform.tools;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.xlei.aiplatform.model.dto.CourseQuery;
import com.xlei.aiplatform.model.entity.Course;
import com.xlei.aiplatform.model.entity.CourseReserve;
import com.xlei.aiplatform.model.entity.School;
import com.xlei.aiplatform.service.CourseReserveService;
import com.xlei.aiplatform.service.CourseService;
import com.xlei.aiplatform.service.SchoolService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

/**
 * @author https://github.com/xuzhixing181
 */
@Component
@RequiredArgsConstructor
public class CourseTool {

    private final CourseService courseService;

    private final CourseReserveService courseReserveService;

    private final SchoolService schoolService;

    @Tool(description = "根据条件查询课程")
    public List<Course> getCourseByCondition(@ToolParam(required = false, description = "查询课程的条件") CourseQuery courseQuery) {
        // 1.参数校验
        if (courseQuery == null) {
            return Collections.emptyList();
        }
        // 2.构建查询条件
        QueryChainWrapper<Course> wrapper = courseService.query()
                .eq(courseQuery.getType() != null, "type", courseQuery.getType())
                .le(courseQuery.getEdu() != null, "edu", courseQuery.getEdu())
                .le(courseQuery.getPrice() != null, "price", courseQuery.getPrice())
                .le(courseQuery.getDuration() != null, "duration", courseQuery.getDuration());
        // 处理排序
        if (!CollectionUtils.isEmpty(courseQuery.getSorts())) {
            for (CourseQuery.Sort sort : courseQuery.getSorts()) {
                wrapper.orderBy(true, sort.getAsc(), sort.getField());
            }
        }
        return wrapper.list();
    }

    @Tool(description = "查询所有校区")
    public List<School> getAllSchools() {
        return schoolService.list();
    }
    @Tool(description = "生成课程预约单,并返回预约单号")
    public String createCourseReserve(String courseName, String studentName, String contactInfo, String school, String remark){
        CourseReserve courseReserve = CourseReserve.builder()
                .course(courseName).studentName(studentName)
                .contactInfo(contactInfo).school(school).remark(remark)
                .build();
        courseReserveService.save(courseReserve);
        return String.valueOf(courseReserve.getId());

    }
}



