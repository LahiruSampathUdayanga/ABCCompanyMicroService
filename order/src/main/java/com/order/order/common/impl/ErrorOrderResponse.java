package com.order.order.common.impl;

import com.order.order.common.OrderResponse;
import lombok.Getter;

@Getter
public class ErrorOrderResponse implements OrderResponse{
    private final String errorMessage;

    public ErrorOrderResponse(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
