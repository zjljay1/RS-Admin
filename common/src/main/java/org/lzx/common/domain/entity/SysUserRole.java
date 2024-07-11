package org.lzx.common.domain.entity;

import cn.hutool.core.date.DatePattern;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.lzx.common.domain.BaseEntity;

import java.io.Serial;
import java.time.LocalDateTime;

@Data
@TableName(value = "sys_user_role", autoResultMap = true)
@EqualsAndHashCode(callSuper = true)
public class SysUserRole extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @Schema(description = "ID")
    private Long id;

    @Schema(description = "用户ID")
    private Long user_id;

    @Schema(description = "角色ID")
    private Long role_id;

}
