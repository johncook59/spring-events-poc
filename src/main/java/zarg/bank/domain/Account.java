package zarg.bank.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.Version;
import java.math.BigDecimal;

@Entity
@Table(name = "account", indexes = {@Index(name = "idx_account_bid", columnList = "bid")})
@Data
@NoArgsConstructor
public class Account {

    @Id
    @Column(name = "id", columnDefinition = "serial", nullable = false, updatable = false)
    @GeneratedValue
    private int id;

    @Column(name = "bid", updatable = false, insertable = false)
    @ColumnDefault("concat('A', lpad(nextval('hibernate_sequence'::regclass)::text, 8, '0'))")
    @Generated(GenerationTime.INSERT)
    private String bid;

    @Version
    private int version = 0;

    @Column(nullable = false)
    private BigDecimal balance;

    @Column(length = 20, nullable = false)
    private String name;

    private Account(Builder builder) {
        setBalance(builder.balance);
        setName(builder.name);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builder(Account copy) {
        Builder builder = new Builder();
        builder.balance = copy.getBalance();
        builder.name = copy.getName();
        return builder;
    }

    public static final class Builder {
        private BigDecimal balance;
        private String name;

        private Builder() {
        }

        public Builder balance(BigDecimal balance) {
            this.balance = balance;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Account build() {
            return new Account(this);
        }
    }
}
