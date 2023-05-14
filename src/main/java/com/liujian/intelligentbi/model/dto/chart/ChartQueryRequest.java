package com.liujian.intelligentbi.model.dto.chart;

import com.liujian.intelligentbi.common.PageRequest;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Asynchronous
 */
@Data
public class ChartQueryRequest extends PageRequest implements Serializable {
    /**
     * 表名
     */
    private String chartName;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 图表类型
     */
    private String chartType;
    private static final long serialVersionUID = 1L;
}
