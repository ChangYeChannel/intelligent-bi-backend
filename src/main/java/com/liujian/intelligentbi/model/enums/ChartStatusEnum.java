package com.liujian.intelligentbi.model.enums;

import org.apache.commons.lang3.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 图表状态枚举
 * @author Asynchronous
 */
public enum ChartStatusEnum {

    WAIT("wait","等待中"),
    RUNNING("running","执行中"),
    FAILED("failed","执行失败"),
    SUCCEED("succeed","执行成功");


    /**
     * 状态值
     */
    private final String status;
    /**
     * 状态解释
     */
    private final String value;

    ChartStatusEnum(String status, String value) {
        this.status = status;
        this.value = value;
    }

    public String getStatus() {
        return status;
    }

    public String getValue() {
        return value;
    }
}

