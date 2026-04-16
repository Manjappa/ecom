package com.ecom.orderservice.order.api;

import com.ecom.orderservice.order.Order;
import com.ecom.orderservice.order.OrderNotFoundException;
import com.ecom.orderservice.order.OrderService;
import com.ecom.orderservice.product.ProductUnavailableException;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public List<OrderResponse> list() {
        return orderService.list().stream().map(OrderController::toResponse).toList();
    }

    @GetMapping("/{id}")
    public OrderResponse get(@PathVariable Long id) {
        return toResponse(orderService.getRequired(id));
    }

    @PostMapping
    public ResponseEntity<OrderResponse> create(@Valid @RequestBody CreateOrderRequest request) {
        var created = orderService.create(request.productId(), request.quantity());
        return ResponseEntity.created(URI.create("/api/orders/" + created.getId()))
                .body(toResponse(created));
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<String> handleOrderNotFound(OrderNotFoundException ex) {
        return ResponseEntity.status(404).body(ex.getMessage());
    }

    @ExceptionHandler(ProductUnavailableException.class)
    public ResponseEntity<String> handleProductUnavailable(ProductUnavailableException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    private static OrderResponse toResponse(Order o) {
        return new OrderResponse(
                o.getId(),
                o.getProductId(),
                o.getQuantity(),
                o.getUnitPrice(),
                o.getTotalPrice(),
                o.getStatus(),
                o.getCreatedAt()
        );
    }
}

