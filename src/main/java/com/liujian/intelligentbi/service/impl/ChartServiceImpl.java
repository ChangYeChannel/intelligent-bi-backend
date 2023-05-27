package com.liujian.intelligentbi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liujian.intelligentbi.common.ErrorCode;
import com.liujian.intelligentbi.constant.CommonConstant;
import com.liujian.intelligentbi.exception.BusinessException;
import com.liujian.intelligentbi.exception.ThrowUtils;
import com.liujian.intelligentbi.manager.AIManager;
import com.liujian.intelligentbi.model.dto.chart.ChartByAiRequest;
import com.liujian.intelligentbi.model.dto.chart.ChartQueryRequest;
import com.liujian.intelligentbi.model.entity.Chart;
import com.liujian.intelligentbi.model.entity.User;
import com.liujian.intelligentbi.model.vo.AIEnum;
import com.liujian.intelligentbi.model.vo.BIResponse;
import com.liujian.intelligentbi.model.vo.ChartVO;
import com.liujian.intelligentbi.model.vo.UserVO;
import com.liujian.intelligentbi.service.ChartService;
import com.liujian.intelligentbi.mapper.ChartMapper;
import com.liujian.intelligentbi.service.UserService;
import com.liujian.intelligentbi.utils.ExcelUtils;
import com.liujian.intelligentbi.utils.SqlUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
* @author Asynchronous
*/
@Service
public class ChartServiceImpl extends ServiceImpl<ChartMapper, Chart>
    implements ChartService{

    @Resource
    private UserService userService;

    @Resource
    private AIManager aiManager;

    @Override
    public QueryWrapper<Chart> getQueryWrapper(ChartQueryRequest chartQueryRequest) {

        if (chartQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        String chartName = chartQueryRequest.getChartName();
        Long userId = chartQueryRequest.getUserId();
        String chartType = chartQueryRequest.getChartType();
        String sortField = chartQueryRequest.getSortField();
        String sortOrder = chartQueryRequest.getSortOrder();
        QueryWrapper<Chart> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(chartName != null, "chart_name", chartName);
        queryWrapper.eq(userId != null, "user_id", userId);
        queryWrapper.eq(StringUtils.isNotBlank(chartType), "chart_type", chartType);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    @Override
    public void validChart(Chart chart, boolean flag) {
        if (chart == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        String chartName = chart.getChartName();
        String goal = chart.getGoal();
        String chartData = chart.getChartData();
        String chartType = chart.getChartType();

        // 创建时，参数不能为空
        if (flag) {
            ThrowUtils.throwIf(StringUtils.isAnyBlank(chartName, goal, chartData, chartType), ErrorCode.PARAMS_ERROR);
        }
        // 有参数则校验
        if (StringUtils.isNotBlank(chartData) && chartData.length() > 8192) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "数据过多");
        }
        if (StringUtils.isNotBlank(goal) && goal.length() > 8192) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "描述过长");
        }
    }

    @Override
    public ChartVO getChartVO(Chart chart, HttpServletRequest request) {
        ChartVO chartVO = ChartVO.objToVo(chart);
        // 1. 关联查询用户信息
        Long userId = chart.getUserId();
        User user = null;
        if (userId != null && userId > 0) {
            user = userService.getById(userId);
        }
        UserVO userVO = userService.getUserVO(user);
        chartVO.setUserVO(userVO);
        return chartVO;
    }

    @Override
    public Page<ChartVO> getChartVOPage(Page<Chart> chartPage, HttpServletRequest request) {
        List<Chart> chartList = chartPage.getRecords();
        Page<ChartVO> chartVOPage = new Page<>(chartPage.getCurrent(), chartPage.getSize(), chartPage.getTotal());
        if (CollectionUtils.isEmpty(chartList)) {
            return chartVOPage;
        }
        // 关联查询用户信息
        Set<Long> userIdSet = chartList.stream().map(Chart::getUserId).collect(Collectors.toSet());
        Map<Long, List<User>> userIdUserListMap = userService.listByIds(userIdSet).stream()
                .collect(Collectors.groupingBy(User::getId));
        // 填充信息
        List<ChartVO> chartVOList = chartList.stream().map(chart -> {
            ChartVO chartVO = ChartVO.objToVo(chart);
            Long userId = chart.getUserId();
            User user = null;
            if (userIdUserListMap.containsKey(userId)) {
                user = userIdUserListMap.get(userId).get(0);
            }
            chartVO.setUserVO(userService.getUserVO(user));
            return chartVO;
        }).collect(Collectors.toList());
        chartVOPage.setRecords(chartVOList);
        return chartVOPage;
    }

    @Override
    public BIResponse genChartByAi(MultipartFile multipartFile, ChartByAiRequest chartByAiRequest, HttpServletRequest request) {
        String chartName = chartByAiRequest.getChartName();
        String chartType = chartByAiRequest.getChartType() == null || "".equals(chartByAiRequest.getChartType()) ? "折线图" : chartByAiRequest.getChartType();
        String goal = chartByAiRequest.getGoal();

        // 校验数据
        ThrowUtils.throwIf(StringUtils.isBlank(goal) || goal.length() > 400, ErrorCode.PARAMS_ERROR, "分析目标为空或分析目标字数过长");
        ThrowUtils.throwIf(StringUtils.isBlank(chartName) || chartName.length() > 100, ErrorCode.PARAMS_ERROR, "图标名称为空或图标名称字数过长");

        // 校验登录状态
        User loginUser = userService.getLoginUser(request);

        // 将传入的Excel转为CSV
        String csvData = ExcelUtils.excelToCsv(multipartFile);

        // 组装数据准备喂给AI
        String userInput = "分析目标：" + "\n" +
                goal  + "，使用" + chartType + "\n" +
                "原始数据：" + "\n" +
                csvData + "\n";

        // 引入AI，进行提问得到回答
        Long modelId = AIEnum.BI.getModelId();
        String answer = aiManager.doChart(userInput, modelId);

        // 拆分结果
        String[] split = answer.split("=====");

        if (split.length < 3) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "AI 生成数据失败");
        }

        // 将生成的图表记录信息保存到数据库
        Chart chart = new Chart();
        chart.setUserId(loginUser.getId());
        chart.setChartName(chartName);
        chart.setGoal(goal);
        chart.setChartData(csvData);
        chart.setChartType(chartType);
        chart.setGenerateChart(split[1].trim());
        chart.setGenerateResult(split[2].trim());

        // 保存
        this.save(chart);

        // 构建返回对象
        BIResponse biResponse = new BIResponse();
        biResponse.setGenerateChart(split[1].trim());
        biResponse.setGenerateResult(split[2].trim());
        biResponse.setChartId(chart.getId());

        return biResponse;
    }

}




