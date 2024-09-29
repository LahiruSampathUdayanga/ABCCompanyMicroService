package com.order.order.service;

import com.inventory.inventory.dto.InventoryDTO;
import com.order.order.common.OrderResponse;
import com.order.order.common.impl.ErrorOrderResponse;
import com.order.order.common.impl.SuccessOrderResponse;
import com.order.order.dto.OrderDTO;
import com.order.order.model.Orders;
import com.order.order.repo.OrderRepo;
import com.product.product.dto.ProductDTO;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;

@Service
@Transactional
public class OrderService {

    private final WebClient inventryWebClient;
    private final WebClient productWebClient;

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private ModelMapper modelMapper;

    public OrderService(WebClient inventryWebClient, WebClient productWebClient,
                        ModelMapper modelMapper, OrderRepo orderRepo) {
        this.inventryWebClient = inventryWebClient;
        this.productWebClient = productWebClient;
        this.modelMapper = modelMapper;
        this.orderRepo = orderRepo;
    }


    public List<OrderDTO> getAllOrders() {
        List<Orders> orderList = orderRepo.findAll();
        return modelMapper.map(orderList, new TypeToken<List<OrderDTO>>() {
        }.getType());
    }

    public OrderResponse saveOrder(OrderDTO OrderDTO) {

        Integer itemId = OrderDTO.getItemId();

        try {
            InventoryDTO inventoryResponse = inventryWebClient.get()
                    .uri(uriBuilder -> uriBuilder.path("/item/{itemId}").build(itemId))
                    .retrieve()
                    .bodyToMono(InventoryDTO.class)
                    .block();

            assert inventoryResponse != null;

            Integer productid = inventoryResponse.getProductId();

            ProductDTO productResponse = productWebClient.get()
                    .uri(uriBuilder -> uriBuilder.path("/product/{productId}").build(productid))
                    .retrieve()
                    .bodyToMono(ProductDTO.class)
                    .block();
            assert productResponse != null;
            if (inventoryResponse.getQuantity() > 0) {
                if (productResponse.getForSale() == 1) {
                    orderRepo.save(modelMapper.map(OrderDTO, Orders.class));
                    return new SuccessOrderResponse(OrderDTO);
                } else {
                    return new ErrorOrderResponse("Item is Not for sale at the moment");
                }
            } else {
                return new ErrorOrderResponse("Item Not Availble");
            }
        } catch (WebClientResponseException e) {
            if (e.getStatusCode().is5xxServerError()) {
                return new ErrorOrderResponse("Item is not found");
            }
        }
        return new ErrorOrderResponse("Please try again");
    }

    public OrderDTO updateOrder(OrderDTO OrderDTO) {
        orderRepo.save(modelMapper.map(OrderDTO, Orders.class));
        return OrderDTO;
    }

    public String deleteOrder(Integer orderId) {
        orderRepo.deleteById(orderId);
        return "Order deleted";
    }

    public OrderDTO getOrderById(Integer orderId) {
        Orders order = orderRepo.getOrderById(orderId);
        return modelMapper.map(order, OrderDTO.class);
    }
}
