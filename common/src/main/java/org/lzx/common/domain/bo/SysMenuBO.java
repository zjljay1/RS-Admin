package org.lzx.common.domain.bo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.lzx.common.domain.model.Meta;
import org.lzx.common.domain.vo.QueryVo;
import org.lzx.common.domain.vo.SysMenuVO;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
@EqualsAndHashCode(callSuper = true)
@Schema(description = "菜单VO")
@Data
public class SysMenuBO extends Meta implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "资源ID")
    private Long id;

    @Schema(description = "父节点ID")
    private Integer parentId;

    @Schema(description = "唯一标识路径")
    private String uiPath;

    @Schema(description = "资源类型：1-菜单路由，2-资源（按钮等）", example = "1")
    private String menuType;

    @Schema(description = "状态：1-可用，2-禁用", example = "1")
    @NotBlank(message = "状态不能为空")
    private String status;

    @Schema(description = "名称")
    @NotBlank(message = "菜单名称不能为空")
    private String menuName;

    @Schema(description = "路由名称")
    @NotBlank(message = "路由名称不能为空")
    private String routeName;

    @Schema(description = "菜单路由路径或其他资源的唯一标识")
    @NotBlank(message = "路由路径不能为空")
    private String routePath;

    @Schema(description = "布局方式")
    private String component;

    @Schema(description = "权重顺序")
    private Integer weight;

    @Schema(description = "路由参数")
    private String query;

    @Schema(description = "数据库存储的元数据json")
    private String meta;

    @Schema(description = "children")
    private List<SysMenuVO> children;

}
