package org.example.mapper;

import org.example.pojo.Customer;
import org.example.pojo.Order;

public interface OrderMapper {
    public Order getOrderWithCustomer(int id);
    public Customer getCustomerWithOrder(int id);
}
