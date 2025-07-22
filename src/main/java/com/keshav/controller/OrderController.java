package com.keshav.controller;

import com.keshav.Domain.OrderType;
import com.keshav.model.Coin;
import com.keshav.model.Order;
import com.keshav.model.User;
import com.keshav.request.CreateOrderRequest;
import com.keshav.service.CoinService;
import com.keshav.service.OrderService;
import com.keshav.service.UserService;
import com.keshav.service.WalletService;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @Autowired
    private CoinService coinService;

//    @Autowired
//    private WalletTransactionService walletTransactionService;

    @PostMapping("/pay")
    public ResponseEntity<Order>payOrderPayment(
            @RequestHeader("Authorization") String jwt,
            @RequestBody CreateOrderRequest req
    )throws Exception{
        User user = userService.findUserByJwt(jwt);
         Coin coin=coinService.findById(req.getCoinId());

        Order order=orderService.processOrder(coin,
                req.getQuantity(),
                req.getOrderType(),user);

        return ResponseEntity.ok(order);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrderById(@RequestHeader("Authorization") String jwt,
                                              @PathVariable Long orderId
                                              )throws Exception{

        User user = userService.findUserByJwt(jwt);

        Order order=orderService.getOrderById(orderId);
        if(order.getUser().getId().equals(user.getId()))
        {
            return ResponseEntity.ok(order);
        }
        else {
            throw new Exception("You don't have access to this order");
        }
    }
    @GetMapping()
    public ResponseEntity<List<Order>> getAllOrdersForUser(
            @RequestHeader("Authorization") String jwt,
            @RequestParam(required = false) OrderType order_type,
            @RequestParam(required = false)String asset_symbol
    )throws Exception{
        Long userId=userService.findUserByJwt(jwt).getId();

        List<Order>userOrders=orderService.getAllOrdersOfUser(userId,order_type,asset_symbol);
        return ResponseEntity.ok(userOrders);
    }


}
