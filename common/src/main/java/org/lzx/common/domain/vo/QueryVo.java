package org.lzx.common.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueryVo implements Serializable {


    @Serial
    private static final long serialVersionUID = 1L;

    private String key;

    private String value;

}
