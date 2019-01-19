package com.hong.entity;

import java.util.Date;

/**
 * Created by John on 2019/1/19.
 * 订单分派表实体
 */
public class OrderDispatch {
    private String dispatchId;
    private String orderId;
    private String dispatchContent;
    private Date createTime;
    private Integer dispatchStatus;

    public String getDispatchId() {
        return dispatchId;
    }

    public void setDispatchId(String dispatchId) {
        this.dispatchId = dispatchId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getDispatchContent() {
        return dispatchContent;
    }

    public void setDispatchContent(String dispatchContent) {
        this.dispatchContent = dispatchContent;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getDispatchStatus() {
        return dispatchStatus;
    }

    public void setDispatchStatus(Integer dispatchStatus) {
        this.dispatchStatus = dispatchStatus;
    }
}
