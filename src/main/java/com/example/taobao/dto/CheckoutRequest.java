package com.example.taobao.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class CheckoutRequest {

    @NotBlank(message = "收货人不能为空")
    private String buyerName;

    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "请输入有效的中国大陆手机号")
    private String phone;

    @NotBlank(message = "收货地址不能为空")
    private String address;

    private String remark;

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
