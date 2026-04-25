package com.example.taobao.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class QuantityRequest {

    @NotNull(message = "购买数量不能为空")
    @Min(value = 0, message = "购买数量不能小于0")
    private Integer quantity;

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
