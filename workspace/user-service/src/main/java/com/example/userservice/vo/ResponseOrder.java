package com.example.userservice.vo;

import lombok.Data;

import java.util.Date;

//고객 주문 내역
@Data
public class ResponseOrder {
    private String productId;
    private Integer qty;
    private Integer unitPrice;
    private Integer totalPrice;
    private Date createdAt;

    private String orderId;
}
