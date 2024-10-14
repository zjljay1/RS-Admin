package org.lzx.common.domain.vo;

import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.lzx.common.domain.BaseEntity;
import org.lzx.common.domain.entity.SysRole;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "用户视图VO")
@Data
@EqualsAndHashCode(callSuper = true)
public class SysUserVO extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "用户ID")
    private Long id;

    @Schema(description = "昵称")
    private String nickName;

    @Schema(description = "登录用户名")
    @NotEmpty(message = "用户名不能为空")
    private String userName;

    @Schema(description = "性别")
    private String userGender;

    @Schema(description = "状态：1-可用，2-禁用", example = "1")
    @NotEmpty(message = "用户名状态不能为空")
    private String status;

    @Schema(description = "电话")
    private String userPhone;

    @Schema(description = "电子邮箱")
    private String userEmail;

    @Schema(description = "最后登录时间")
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN, timezone = "GMT+8")
    private LocalDateTime lastLoginTime;

    @Schema(description = "最后登录IP")
    private String lastLoginIp;

    @Schema(description = "用户角色")
    private List<String> userRoles;

    @Schema(description = "用户角色对象")
    private List<SysRoleVO> roles;

}