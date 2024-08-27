package org.lzx.common.domain.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.lzx.common.domain.model.Meta;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserParam extends Meta {

    @Schema(description = "昵称")
    private String nickName;

    @Schema(description = "用户名")
    @NotEmpty(message = "用户名不能为空")
    private String userName;

    @Schema(description = "密码")
    private String password;

    @Schema(description = "性别")
    private String userGender;

    @Schema(description = "状态：1-可用，2-禁用", example = "1")
    @NotEmpty(message = "用户名状态不能为空")
    private String status;

    @Schema(description = "电话")
    private String userPhone;

    @Schema(description = "电子邮箱")
    private String userEmail;

    @Schema(description = "用户角色")
    private List<String> userRoles;
}
