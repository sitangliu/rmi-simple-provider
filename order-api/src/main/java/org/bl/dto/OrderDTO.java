package org.bl.dto;

import lombok.Builder;
import lombok.Data;

/**
 * @Description：create
 * @author: ext.liukai3
 * @date: 2021/11/8 15:32
 */
@Data
@Builder
public class OrderDTO {

    private Long orderId;

    private String orderName;

    private Integer price;

}
