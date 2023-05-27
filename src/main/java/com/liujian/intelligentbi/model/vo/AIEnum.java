package com.liujian.intelligentbi.model.vo;

/**
 * AI模型枚举
 * @author Asynchronous
 */

public enum AIEnum {
    /**
     * 智能BI
     */
    BI(1662301492104785921L,"智能BI")
    ;

    /**
     * 模型ID
     */
    private Long modelId;
    /**
     * 模型名称
     */
    private String modelName;

    public Long getModelId() {
        return modelId;
    }

    public void setModelId(Long modelId) {
        this.modelId = modelId;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    AIEnum(Long modelId, String modelName) {
        this.modelId = modelId;
        this.modelName = modelName;
    }
}
