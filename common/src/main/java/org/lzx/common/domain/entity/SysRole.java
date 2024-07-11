package org.lzx.common.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.lzx.common.domain.BaseEntity;

import java.io.Serial;
import java.io.Serializable;

@Data
@TableName(value = "sys_role", autoResultMap = true)
@EqualsAndHashCode(callSuper = false)
public class SysRole extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @Schema(description = "角色ID")
    private Long id;

    @Schema(description = "角色名称", minLength = 1, maxLength = 16)
    @NotBlank(message = "角色名称不能为空")
    private String roleName;

    @Schema(description = "角色编码", minLength = 1, maxLength = 64)
    @NotBlank(message = "角色编码不能为空")
    private String roleCode;

    @Schema(description = "状态：1-可用，2-禁用", example = "1")
    @NotBlank(message = "角色状态不能为空")
    private String status;

    @Schema(description = "备注信息")
    private String roleDesc;

    @Schema(description = "角色类型：1-公共角色，2-特殊角色", example = "1")
    private Integer type;

    @Schema(description = "角色权限大小：在拥有相同资源下，角色权限越大的才能操作", example = "1")
    private Integer rolePower;

    public boolean isAdmin() {
        return isAdmin(this.id);
    }

    public static boolean isAdmin(Long roleId) {
        return roleId != null && 1L == roleId;
    }

}