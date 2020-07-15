package zarg.bank.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import zarg.bank.controllers.RegisterCustomerRequest;
import zarg.bank.dao.CustomerDao;
import zarg.bank.domain.Account;
import zarg.bank.domain.Card;
import zarg.bank.domain.Customer;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Collections;

@Service
class DefaultCustomerManager implements CustomerManager {

    private static final DateTimeFormatter expiryDateFormatter = DateTimeFormatter.ofPattern("M/yyyy");

    private final CustomerDao customerDao;
    private final EntityManager entityManager;

    DefaultCustomerManager(CustomerDao customerDao, EntityManager entityManager) {
        this.customerDao = customerDao;
        this.entityManager = entityManager;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Customer registerCustomer(RegisterCustomerRequest request) {

        Account account = Account.builder()
                .balance(new BigDecimal(request.getInitialBalance()))
                .name("Current")
                .build();
        Card card = Card.builder()
                .number(request.getCcNumber())
                .type(request.getCcType())
                .cvv2(request.getCvv2())
                .expires(YearMonth.parse(request.getCcExpires(), expiryDateFormatter).atEndOfMonth())
                .build();
        Customer customer = Customer.builder()
                .givenName(request.getGivenName())
                .surname(request.getSurname())
                .emailAddress(request.getEmailAddress())
                .password(request.getPassword())
                .username(request.getUsername())
                .accounts(Collections.singletonList(account))
                .cards(Collections.singleton(card))
                .build();
        card.setCardHolder(customer);

        return customerDao.save(customer);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Customer findCustomerByUsername(String username) {
        return customerDao.findByUsername(username);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    public Customer findCustomerById(int id) {
        return customerDao.findById(id).orElseThrow(EntityNotFoundException::new);
    }
}
