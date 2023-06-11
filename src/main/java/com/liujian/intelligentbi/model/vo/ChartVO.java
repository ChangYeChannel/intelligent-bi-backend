package com.liujian.intelligentbi.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.liujian.intelligentbi.model.entity.Chart;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.List;

@Data
public class ChartVO implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 表名
     */
    private String chartName;

    /**
     * 用户ID
     */
    private Long userId;

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

    /**
     * 执行状态
     */
    private String status;

    /**
     * 执行信息
     */
    private String execMessage;

    /**
     * 用户信息
     */
    private UserVO userVO;

    /**
     * 包装类转对象
     */
    public static Chart voToObj(ChartVO chartVO) {
        if (chartVO == null) {
            return null;
        }
        Chart chart = new Chart();
        BeanUtils.copyProperties(chartVO, chart);
        return chart;
    }

    /**
     * 对象转包装类
     */
    public static ChartVO objToVo(Chart chart) {
        if (chart == null) {
            return null;
        }
        ChartVO chartVO = new ChartVO();
        BeanUtils.copyProperties(chart, chartVO);
        return chartVO;
    }
    private static final long serialVersionUID = 1L;
}
