package com.liujian.intelligentbi.model.dto.chart;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Asynchronous
 */
@Data
public class ChartUpdateRequest implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 表名
     */
    private String chartName;

    /**
     * 分析目标
     */
    private String goal;

    /**
     * 分析数据
     */
    private String chartData;

    /**
     * 图表类型
     */
    private String chartType;

    /**
     * 生成的图表数据
     */
    private String generateChart;

    /**
     * 生成的分析结论
     */
    private String generateResult;
    private static final long serialVersionUID = 1L;
}
