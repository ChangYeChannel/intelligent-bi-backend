package com.liujian.intelligentbi.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.liujian.intelligentbi.model.dto.chart.ChartByAiRequest;
import com.liujian.intelligentbi.model.dto.chart.ChartQueryRequest;
import com.liujian.intelligentbi.model.entity.Chart;
import com.baomidou.mybatisplus.extension.service.IService;
import com.liujian.intelligentbi.model.vo.BIResponse;
import com.liujian.intelligentbi.model.vo.ChartVO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
* @author Asynchronous
*/
public interface ChartService extends IService<Chart> {
    /**
     * 获取查询条件
     *
     * @param chartQueryRequest  封装参数
     * @return  查询条件
     */
    QueryWrapper<Chart> getQueryWrapper(ChartQueryRequest chartQueryRequest);


    /**
     * 参数校验
     * @param chart  检验对象
     * @param flag  是否进行参数校验
     */
    void validChart(Chart chart, boolean flag);

    /**
     * 获取图表封装类
     * @param chart  图表信息
     * @param request  请求信息
     * @return  封装信息
     */
    ChartVO getChartVO(Chart chart, HttpServletRequest request);

    /**
     * 获取图表封装类分页对象
     * @param chartPage  图表信息封装类
     * @param request  请求信息
     * @return  分页对象
     */
    Page<ChartVO> getChartVOPage(Page<Chart> chartPage, HttpServletRequest request);

    /**
     * 根据传入的数据，利用AI生成问题回答
     * @param multipartFile  原始数据
     * @param chartByAiRequest  问题封装
     * @param request  请求对象
     * @return  问题回答
     */
    BIResponse genChartByAiAsync(MultipartFile multipartFile, ChartByAiRequest chartByAiRequest, HttpServletRequest request);

    /**
     * 根据传入的数据，利用AI生成问题回答
     * @param multipartFile  原始数据
     * @param chartByAiRequest  问题封装
     * @param request  请求对象
     * @return  问题回答
     */
    BIResponse genChartByAi(MultipartFile multipartFile, ChartByAiRequest chartByAiRequest, HttpServletRequest request);
}
