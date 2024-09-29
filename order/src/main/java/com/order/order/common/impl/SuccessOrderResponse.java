package com.order.order.common.impl;

import com.order.order.common.OrderResponse;
import com.order.order.dto.OrderDTO;
import lombok.Getter;

@Getter
public class SuccessOrderResponse implements OrderResponse {
    private final OrderDTO order;

    public SuccessOrderResponse(OrderDTO orderDto){
        this.order = orderDto;
    }
}
