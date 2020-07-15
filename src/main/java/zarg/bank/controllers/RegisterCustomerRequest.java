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
public class RegisterCustomerRequest {
    private String givenName;
    private String surname;
    private String emailAddress;
    private String username;
    private String password;
    private String ccType;
    private String ccNumber;
    private String cvv2;
    private String ccExpires;
    private String initialBalance;
}
