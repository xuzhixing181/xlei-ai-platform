package com.xlei.aiplatform.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

/**
 * @author https://github.com/xuzhixing181
 * @description 课程预定表
 * @TableName course_reserve
 */
@TableName(value ="course_reserve")
@Builder
@Data
public class CourseReserve implements Serializable {
    /**
     * 预约单号
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 预约课程
     */
    @TableField(value = "course")
    private String course;

    /**
     * 学生姓名
     */
    @TableField(value = "student_name")
    private String studentName;

    /**
     * 联系方式
     */
    @TableField(value = "contact_info")
    private String contactInfo;

    /**
     * 预约校区
     */
    @TableField(value = "school")
    private String school;

    /**
     * 备注
     */
    @TableField(value = "remark")
    private String remark;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}