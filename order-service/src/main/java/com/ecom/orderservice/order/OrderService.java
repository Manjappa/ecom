package com.ecom.orderservice.order;

import com.ecom.orderservice.product.ProductClient;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductClient productClient;

    public OrderService(OrderRepository orderRepository, ProductClient productClient) {
        this.orderRepository = orderRepository;
        this.productClient = productClient;
    }

    public List<Order> list() {
        return orderRepository.findAll();
    }

    public Order getRequired(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));
    }

    @Transactional
    public Order create(Long productId, int quantity) {
        var product = productClient.getRequired(productId);

        BigDecimal unitPrice = product.price();
        BigDecimal totalPrice = unitPrice.multiply(BigDecimal.valueOf(quantity));

        return orderRepository.save(new Order(
                productId,
                quantity,
                unitPrice,
                totalPrice,
                "CREATED",
                Instant.now()
        ));
    }
}

