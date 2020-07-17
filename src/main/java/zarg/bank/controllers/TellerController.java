package zarg.bank.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import zarg.bank.service.TellerService;

import java.math.BigDecimal;

@RestController
@RequestMapping("/teller")
@Slf4j
class TellerController {

    private final TellerService teller;

    public TellerController(@Qualifier("ShardingTellerService") TellerService teller) {
        this.teller = teller;
    }

    @GetMapping(value = "/{accountBid}/balance")
    @ResponseBody
    public BigDecimal balance(@PathVariable("accountBid") String accountBid) {
        log.debug("Requesting balance for account {}", accountBid);
        return teller.balance(accountBid);
    }

    @PatchMapping(value = "/{accountBid}")
    @ResponseBody
    public void update(@PathVariable("accountBid") String accountBid, @RequestBody BalanceUpdateRequest request) {
        log.debug("Requesting {} {} to account {}", request.getDirection(), request.getAmount(), accountBid);
        validateAmount(request.getAmount());

        switch (request.getDirection()) {
            case CREDIT:
                teller.credit(accountBid, request.getAmount());
                break;
            case DEBIT:
                teller.debit(accountBid, request.getAmount());
                break;
        }
    }

    private void validateAmount(BigDecimal amount) {
        Assert.notNull(amount, "Null amount");
        Assert.isTrue(amount.doubleValue() > 0, "amount not greater than zero");
    }
}
