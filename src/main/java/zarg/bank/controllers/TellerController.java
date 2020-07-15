package zarg.bank.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import zarg.bank.service.TellerService;

import java.math.BigDecimal;

@RestController
@RequestMapping("/teller")
@Slf4j
public class TellerController {

    private final TellerService teller;

    public TellerController(TellerService teller) {
        this.teller = teller;
    }

    @GetMapping(value = "/{accountBid}/balance")
    @ResponseBody
    public BigDecimal balance(@PathVariable("accountBid") String accountBid) {
        log.debug("Balance for account " + accountBid);
        return teller.balance(accountBid);
    }

    @PostMapping(value = "/{accountBid}/credit")
    @ResponseBody
    public void credit(@PathVariable("accountBid") String accountBid, @RequestBody CreditRequest request) {
        log.debug("Crediting " + request.getAmount() + " to account " + accountBid);
        validateAmount(request.getAmount());
        teller.credit(accountBid, request.getAmount());
    }

    @PostMapping(value = "/{accountBid}/debit")
    @ResponseBody
    public void debit(@PathVariable("accountBid") String accountBid, @RequestBody DebitRequest request) {
        log.debug("Debiting " + request.getAmount() + " to account " + accountBid);
        validateAmount(request.getAmount());
        teller.debit(accountBid, request.getAmount());
    }

    private void validateAmount(BigDecimal amount) {
        Assert.notNull(amount, "Null amount");
        Assert.isTrue(amount.doubleValue() > 0, "amount not greater than zero");
    }
}
