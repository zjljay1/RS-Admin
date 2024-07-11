package org.lzx.common.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.lzx.common.domain.model.Meta;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Schema(description = "菜单VO")
@Data
public class SysMenuTreeVO extends Meta implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "资源ID")
    private Long id;

    @Schema(description = "标签")
    private String label;

    @Schema(description = "父ID")
    private Long pId;

    @Schema(description = "子元素")
    private List<SysMenuTreeVO> children;
}
