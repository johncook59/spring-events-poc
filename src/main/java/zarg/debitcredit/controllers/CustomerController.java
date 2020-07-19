package zarg.debitcredit.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import zarg.debitcredit.domain.Customer;
import zarg.debitcredit.service.CustomerManager;

@RestController
@RequestMapping("customer")
@Slf4j
public class CustomerController {

    private final CustomerManager customerManager;

    public CustomerController(CustomerManager customerManager) {
        this.customerManager = customerManager;
    }

    @GetMapping(value = "/{customerId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Customer findById(@PathVariable("customerId") int customerId) {
        log.info("Finding " + customerId);

        return customerManager.findCustomerById(customerId);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Customer findByUsername(@RequestParam("username") String name) {
        log.info("Finding " + name);
        Assert.hasText(name, "Empty name");

        return customerManager.findCustomerByUsername(name);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Customer register(@RequestBody RegisterCustomerRequest request) {
        log.info("Registering {} {}", request.getGivenName(), request.getSurname());

        return customerManager.registerCustomer(request);
    }
}
