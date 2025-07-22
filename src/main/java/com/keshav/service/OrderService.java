package com.keshav.service;

import com.keshav.Domain.OrderType;
import com.keshav.model.Order;
import com.keshav.model.OrderItem;
import com.keshav.model.User;
import com.keshav.model.Coin;

import java.util.List;

public interface OrderService {

     Order createOrder(User user, OrderItem orderItem, OrderType orderType);

     Order getOrderById(Long orderId) throws Exception;

     List<Order> getAllOrdersOfUser(Long userId,OrderType orderType,String assetSymbol);

     Order processOrder(Coin coin,double quantity,OrderType orderType,User user) throws Exception;


}
