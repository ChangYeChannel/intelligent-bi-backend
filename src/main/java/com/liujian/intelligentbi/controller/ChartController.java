package com.liujian.intelligentbi.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.liujian.intelligentbi.annotation.AuthCheck;
import com.liujian.intelligentbi.common.BaseResponse;
import com.liujian.intelligentbi.common.DeleteRequest;
import com.liujian.intelligentbi.common.ErrorCode;
import com.liujian.intelligentbi.common.ResultUtils;
import com.liujian.intelligentbi.constant.UserConstant;
import com.liujian.intelligentbi.exception.BusinessException;
import com.liujian.intelligentbi.exception.ThrowUtils;
import com.liujian.intelligentbi.model.dto.chart.ChartAddRequest;
import com.liujian.intelligentbi.model.dto.chart.ChartByAiRequest;
import com.liujian.intelligentbi.model.dto.chart.ChartQueryRequest;
import com.liujian.intelligentbi.model.dto.chart.ChartUpdateRequest;
import com.liujian.intelligentbi.model.entity.Chart;
import com.liujian.intelligentbi.model.entity.User;
import com.liujian.intelligentbi.model.vo.ChartVO;
import com.liujian.intelligentbi.service.ChartService;
import com.liujian.intelligentbi.service.UserService;
import com.liujian.intelligentbi.utils.ExcelUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;

/**
 * 图表信息接口
 * @author Asynchronous
 */
@RestController
@RequestMapping("/chart")
@Slf4j
public class ChartController {

    @Resource
    private ChartService chartService;

    @Resource
    private UserService userService;

    /**
     * 创建
     */
    @PostMapping("/add")
    public BaseResponse<Long> addChart(@RequestBody ChartAddRequest chartAddRequest, HttpServletRequest request) {
        if (chartAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Chart chart = new Chart();
        BeanUtils.copyProperties(chartAddRequest, chart);
        chartService.validChart(chart, true);
        User loginUser = userService.getLoginUser(request);
        chart.setUserId(loginUser.getId());
        boolean result = chartService.save(chart);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        long newPostId = chart.getId();
        return ResultUtils.success(newPostId);
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteChart(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        Chart oldPost = chartService.getById(id);
        ThrowUtils.throwIf(oldPost == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅用户本身和管理员可删除
        if (oldPost.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean flag = chartService.removeById(id);
        return ResultUtils.success(flag);
    }

    /**
     * 更新（仅管理员）
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateChart(@RequestBody ChartUpdateRequest chartUpdateRequest) {
        if (chartUpdateRequest == null || chartUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Chart chart = new Chart();
        BeanUtils.copyProperties(chartUpdateRequest, chart);
        // 参数校验
        chartService.validChart(chart, false);
        long id = chartUpdateRequest.getId();
        // 判断是否存在
        Chart oldChart = chartService.getById(id);
        ThrowUtils.throwIf(oldChart == null, ErrorCode.NOT_FOUND_ERROR);
        boolean result = chartService.updateById(chart);
        return ResultUtils.success(result);
    }

    /**
     * 根据 id 获取
     */
    @GetMapping("/get")
    public BaseResponse<ChartVO> getChartVoById(long id, HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Chart chart = chartService.getById(id);
        if (chart == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        return ResultUtils.success(chartService.getChartVO(chart, request));
    }

    /**
     * 分页获取列表（封装类）
     */
    @PostMapping("/list/chart")
    public BaseResponse<Page<ChartVO>> listChartVoByPage(@RequestBody ChartQueryRequest chartQueryRequest, HttpServletRequest request) {
        long current = chartQueryRequest.getCurrent();
        long size = chartQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<Chart> chartPage = chartService.page(new Page<>(current, size),
                chartService.getQueryWrapper(chartQueryRequest));
        return ResultUtils.success(chartService.getChartVOPage(chartPage, request));
    }

    /**
     * 接受图表数据生成分析数据方法
     */
    @PostMapping("/genChart")
    public BaseResponse<String> genChartByAi(@RequestPart("file") MultipartFile multipartFile,
                                             ChartByAiRequest chartByAiRequest, HttpServletRequest request) {
        String chartName = chartByAiRequest.getChartName();
        String chartType = chartByAiRequest.getChartType();
        String goal = chartByAiRequest.getGoal();

        // 校验数据
        ThrowUtils.throwIf(StringUtils.isBlank(goal) || goal.length() > 400, ErrorCode.PARAMS_ERROR, "分析目标为空或分析目标字数过长");
        ThrowUtils.throwIf(StringUtils.isBlank(chartName) || chartName.length() > 100, ErrorCode.PARAMS_ERROR, "图标名称为空或图标名称字数过长");

        // 将传入的Excel转为CSV
        String csvData = ExcelUtils.excelToCsv(multipartFile);

        // 组装数据准备喂给AI
        StringBuilder userInput = new StringBuilder();
        userInput.append("你是一个拥有两年半分析经验的数据分析师,请根据我给出的分析目标和数据,对数据进行分析工作").append("\n");
        userInput.append("分析目标:").append(goal).append("\n");
        userInput.append("数据:").append(csvData).append("\n");


        System.out.println(userInput);
        return null;
    }
}