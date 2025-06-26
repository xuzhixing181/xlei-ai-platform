package com.xlei.aiplatform.model.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author https://github.com/xuzhixing181
 * 响应结果对象
 */
@Data
@NoArgsConstructor
public class CommonResult {
    private Integer ok;
    private String msg;

    private CommonResult(Integer ok, String msg) {
        this.ok = ok;
        this.msg = msg;
    }

    public static CommonResult ok() {
        return new CommonResult(1, "ok");
    }

    public static CommonResult fail(String msg) {
        return new CommonResult(0, msg);
    }
}