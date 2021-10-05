package com.example.billingservice;

import com.example.billingservice.dao.BillRepository;
import com.example.billingservice.dao.ProductItemRepository;
import com.example.billingservice.enitities.Bill;
import com.example.billingservice.enitities.ProductItem;
import com.example.billingservice.feign.CustomerRestClient;
import com.example.billingservice.feign.ProductRestClient;
import com.example.billingservice.model.Customer;
import com.example.billingservice.model.Product;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.hateoas.PagedModel;

import java.util.Date;
import java.util.List;
import java.util.Random;

@SpringBootApplication
@EnableFeignClients
public class BillingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BillingServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner start(BillRepository billRepository,
                            ProductItemRepository productItemRepository,
                            CustomerRestClient customerRestClient,
                            ProductRestClient productRestClient){
        return args -> {
            Customer customer = customerRestClient.getCustomerById(1L);
            Bill bill =  billRepository.save(new Bill(null,new Date(),null,customer.getId(),null));
            PagedModel<Product> products = productRestClient.pageProducts();
            products.forEach(product -> {
                ProductItem productItem = new ProductItem();
                productItem.setPrice(product.getPrice());
                productItem.setQuantity(1+ new Random().nextInt(100));
                productItem.setBill(bill);
                productItem.setProductID(product.getId());

                productItemRepository.save(productItem);
            });
        };
    }
}
