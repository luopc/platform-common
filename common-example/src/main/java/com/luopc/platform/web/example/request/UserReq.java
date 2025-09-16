package com.luopc.platform.web.example.request;

import com.luopc.platform.web.result.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UserReq {

    @Schema(description = "姓名")
    private String name;
    @Schema(description = "分页参数")
    private PageParam pageParam;

}
