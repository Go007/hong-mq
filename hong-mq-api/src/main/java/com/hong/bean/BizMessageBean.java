package com.hong.bean;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 业务方消息bean
 */
public class BizMessageBean implements Serializable {
    /**
     * 微服务id
     */
    @NotBlank(message = "serviceId不能为空")
    private String serviceId;

    /**
     * 请求地址
     */
    @NotBlank(message = "url不能为空")
    private String url;

    /**
     * 请求参数
     */
    private String param;

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }
}
