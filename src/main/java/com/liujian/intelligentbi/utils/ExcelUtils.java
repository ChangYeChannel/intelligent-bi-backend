package com.liujian.intelligentbi.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.sun.deploy.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Excel 工具类
 *
 * @author Asynchronous
 */
@Slf4j
public class ExcelUtils {

    /**
     * 将传入的Excel文件，转为CSV格式文件输出
     *
     * @param file 传入的文件
     * @return 输出文件
     */
    public static String excelToCsv(MultipartFile file) {
        // 读取数据
        List<Map<Integer,String>> list = null;
        try {
            list = EasyExcel.read(file.getInputStream())
                    .excelType(ExcelTypeEnum.XLSX)
                    .sheet()
                    .headRowNumber(0)
                    .doReadSync();
        } catch (IOException e) {
            log.error("Excel数据读取异常：{}",e.getMessage());
        }

        // 校验参数,如果表数据为空则不返回CSV文件
        if (CollUtil.isEmpty(list)) {
            return "";
        }

        // 创建返回的CSV对象
        StringBuilder csv = new StringBuilder();

        // 表头
        LinkedHashMap<Integer,String> headerMap = (LinkedHashMap<Integer, String>) list.get(0);
        // 过滤数据(因为有些数据可能为null，但是会null数据会参与表列中)
        List<String> headerList = headerMap.values().stream().filter(ObjectUtil::isNotEmpty).collect(Collectors.toList());
        // 添加到CSV文件中
        csv.append(StringUtils.join(headerList, ",")).append("\n");

        // 行数据
        for (int i = 1; i < list.size(); i++) {
            LinkedHashMap<Integer,String> dataMap = (LinkedHashMap<Integer, String>) list.get(i);
            // 过滤数据(因为有些数据可能为null，但是会null数据会参与表列中)
            List<String> dataList = dataMap.values().stream().filter(ObjectUtil::isNotEmpty).collect(Collectors.toList());
            // 添加到CSV文件中
            csv.append(StringUtils.join(dataList, ",")).append("\n");
        }

        // 返回数据
        return csv.toString();
    }
}
