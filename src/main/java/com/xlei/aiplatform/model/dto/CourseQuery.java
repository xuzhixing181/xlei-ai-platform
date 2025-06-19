package com.xlei.aiplatform.model.dto;

/**
 * @author https://github.com/xuzhixing181
 * 课程查询 Dto
 */
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.ai.tool.annotation.ToolParam;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseQuery {
    @ToolParam(required = false, description = "课程类型: 电商、外语、设计、自媒体、其它")
    private String type;
    @ToolParam(required = false, description = "学历要求:0-无、1-初中、2-高中、3-大专、4-本科及本科以上")
    private Integer edu;

    /**
     * 用户对价格敏感，则查询时需要按照价格升序排列 : order by price asc
     */
    @ToolParam(required = false, description = "课程价格")
    private Long price;

    /**
     * 用户对学习时长敏感，则查询时要按照时长升序：order by duration asc
     */
    @ToolParam(required = false, description = "学习时长,单位为天")
    private Integer duration;

    @ToolParam(required = false, description = "排序方式")
    private List<Sort> sorts;

    @Data
    public static class Sort {
        @ToolParam(required = false, description = "排序字段: price或duration")
        private String field;
        @ToolParam(required = false, description = "是否是升序: true/false")
        private Boolean asc;
    }
}