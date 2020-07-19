package zarg.debitcredit.service;

import zarg.debitcredit.controllers.RegisterCustomerRequest;
import zarg.debitcredit.domain.Customer;

public interface CustomerManager {
    Customer registerCustomer(RegisterCustomerRequest request);

    Customer findCustomerByUsername(String username);

    Customer findCustomerById(int id);
}
