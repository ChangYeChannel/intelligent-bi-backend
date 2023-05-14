package com.liujian.intelligentbi.model.dto.chart;


import lombok.Data;

/**
 * @author Asynchronous
 */
@Data
public class ChartByAiRequest {
    /**
     * 表名
     */
    private String chartName;

    /**
     * 分析目标
     */
    private String goal;

    /**
     * 图表类型
     */
    private String chartType;

    private static final long serialVersionUID = 1L;
}
