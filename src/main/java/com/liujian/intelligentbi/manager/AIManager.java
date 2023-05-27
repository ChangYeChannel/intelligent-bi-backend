package com.liujian.intelligentbi.manager;

import com.liujian.intelligentbi.common.ErrorCode;
import com.liujian.intelligentbi.exception.BusinessException;
import com.yupi.yucongming.dev.client.YuCongMingClient;
import com.yupi.yucongming.dev.common.BaseResponse;
import com.yupi.yucongming.dev.model.DevChatRequest;
import com.yupi.yucongming.dev.model.DevChatResponse;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * AI助手管理
 * @author Asynchronous
 */
@Component
public class AIManager {
    @Resource
    private YuCongMingClient yuCongMingClient;

    /**
     * 根据传入的问题和指定使用的模型ID，调用AI生成对应的回答
     * @param question  问题
     * @param modelId  模型ID
     * @return  AI回答
     */
    public String doChart(String question,Long modelId) {
        // 创建请求参数
        DevChatRequest devChatRequest = new DevChatRequest();
        devChatRequest.setMessage(question);
        devChatRequest.setModelId(modelId);

        BaseResponse<DevChatResponse> response = yuCongMingClient.doChat(devChatRequest);

        if (response == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"AI 生成数据失败！ 请联系系统管理员~~");
        }

        return response.getData().getContent();
    }
}
