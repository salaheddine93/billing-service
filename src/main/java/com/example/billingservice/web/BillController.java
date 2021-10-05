package com.example.billingservice.web;

import com.example.billingservice.dao.BillRepository;
import com.example.billingservice.dao.ProductItemRepository;
import com.example.billingservice.enitities.Bill;
import com.example.billingservice.enitities.ProductItem;
import com.example.billingservice.feign.CustomerRestClient;
import com.example.billingservice.feign.ProductRestClient;
import com.example.billingservice.model.Customer;
import com.example.billingservice.model.Product;
import org.springframework.hateoas.PagedModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
public class BillController {

    private BillRepository billRepository;
    private ProductItemRepository productItemRepository;
    private CustomerRestClient customerRestClient;
    private ProductRestClient productRestClient;

    public BillController(BillRepository billRepository, ProductItemRepository productItemRepository,CustomerRestClient customerRestClient, ProductRestClient productRestClient) {
        this.billRepository = billRepository;
        this.productItemRepository = productItemRepository;
        this.customerRestClient = customerRestClient;
        this.productRestClient = productRestClient;
    }

    @GetMapping(path = "/bills/{id}")
    public Bill getBillById(@PathVariable(name = "id") Long id){
        Bill bill = billRepository.findById(id).get();
        Customer customer = customerRestClient.getCustomerById(bill.getCustomerID());
        bill.setCustomer(customer);
        bill.getProductItems().forEach(pi->{
            Product product = productRestClient.getProductById(pi.getProductID());
            pi.setProduct(product);
        });
        return bill;
    }
}
