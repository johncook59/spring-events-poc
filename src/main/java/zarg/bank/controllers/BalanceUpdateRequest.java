package zarg.bank.controllers;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class BalanceUpdateRequest {

    public enum Direction {CREDIT, DEBIT}

    private Direction direction;

    private BigDecimal amount;
}
