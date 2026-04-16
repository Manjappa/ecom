package com.ecom.orderservice.product;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
public class ProductClient {
    private final RestTemplate restTemplate;
    private final String baseUrl;

    public ProductClient(RestTemplate restTemplate,
                         @Value("${product-service.base-url:http://localhost:8081}") String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    public ProductResponse getRequired(Long productId) {
        try {
            ResponseEntity<ProductResponse> response =
                    restTemplate.getForEntity(baseUrl + "/api/products/{id}", ProductResponse.class, productId);
            var body = response.getBody();
            if (body == null) {
                throw new ProductUnavailableException("Product response body was empty");
            }
            return body;
        } catch (HttpClientErrorException.NotFound ex) {
            throw new ProductUnavailableException("Product not found: " + productId);
        }
    }
}

