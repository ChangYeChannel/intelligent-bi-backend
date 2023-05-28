package com.liujian.intelligentbi.mapper;

import com.liujian.intelligentbi.model.entity.Chart;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.Map;

/**
* @author Asynchronous
* @description 针对表【chart(图表信息)】的数据库操作Mapper
* @createDate 2023-04-30 14:54:54
* @Entity com.liujian.intelligentbi.model.entity.Chart
*/
public interface ChartMapper extends BaseMapper<Chart> {

    /**
     * 根据传入的参数，创建图表表
     * @param params  建表参数，Map中应该包含表名（tableName）和字段集合（fields）
     */
    void createTable(Map<String, Object> params);

    /**
     * 根据传入的参数，插入图表表数据
     * @param params 插入参数，Map中应该包含表名（tableName）和字段集合（headerList）和数据（data）
     */
    void insetTableValue(Map<String, Object> params);
}




