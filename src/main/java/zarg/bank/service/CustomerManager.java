package zarg.bank.service;

import zarg.bank.controllers.RegisterCustomerRequest;
import zarg.bank.domain.Customer;

public interface CustomerManager {
    Customer registerCustomer(RegisterCustomerRequest request);

    Customer findCustomerByUsername(String username);

    Customer findCustomerById(int id);
}
