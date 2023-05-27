package com.liujian.intelligentbi.model.vo;

import lombok.Data;

/**
 * BI结果返回
 * @author Asynchronous
 */
@Data
public class BIResponse {

    /**
     * 生成的图表数据
     */
    private String generateChart;

    /**
     * 生成的分析结论
     */
    private String generateResult;
    /**
     * 生成的图表Id
     */
    private Long chartId;
}
