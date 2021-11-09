package org.bl.service.impl;

import org.bl.annotation.BlRemoteService;
import org.bl.dto.OrderDTO;
import org.bl.service.IOrderService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description：接口实现类
 * @author: ext.liukai3
 * @date: 2021/11/8 15:29
 */
@BlRemoteService
//@Service
public class OrderServiceImpl implements IOrderService {
    @Override
    public String queryOrderList() {
        List<OrderDTO> orderDTOS = new ArrayList<>();
        OrderDTO orderDTO = OrderDTO.builder().orderId(1L).orderName("洗漱用品").price(2000).build();
        OrderDTO orderDTO2 = OrderDTO.builder().orderId(2L).orderName("日常用品").price(3000).build();
        orderDTOS.add(orderDTO);
        orderDTOS.add(orderDTO2);
        return orderDTOS.toString();
    }

    @Override
    public String orderById(String id) {
        OrderDTO orderDTO = OrderDTO.builder().orderId(1L).orderName("洗漱用品").price(2000).build();
        return orderDTO.toString();
    }
}
