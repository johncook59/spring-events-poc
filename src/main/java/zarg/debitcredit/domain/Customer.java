package zarg.debitcredit.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "customer", indexes = {@Index(name = "idx_customer_bid", columnList = "bid")})
@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude="cards")
public class Customer {

    @Id
    @Column(name = "id", columnDefinition = "serial", nullable = false, updatable = false)
    @GeneratedValue
    private int id;

    @Column(name = "bid", updatable = false, insertable = false)
    @ColumnDefault("concat('C', lpad(nextval('hibernate_sequence'::regclass)::text, 8, '0'))")
    @Generated(GenerationTime.INSERT)
    private String bid;

    @Version
    private int version = 0;

    @Column(length = 20, nullable = false)
    private String password;

    @Column(length = 40, nullable = false)
    public String givenName;

    @Column(length = 40, nullable = false)
    public String surname;

    @Column(length = 40, nullable = false)
    public String emailAddress;

    @Column(length = 40, nullable = false)
    public String username;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "customer_account",
            joinColumns = @JoinColumn(name = "customer_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "account_id", referencedColumnName = "id"))
    private List<Account> accounts;

    @OneToMany(mappedBy="cardHolder", cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<Card> cards;

    private Customer(Builder builder) {
        setPassword(builder.password);
        setGivenName(builder.givenName);
        setSurname(builder.surname);
        setEmailAddress(builder.emailAddress);
        setUsername(builder.username);
        setAccounts(builder.accounts);
        setCards(builder.cards);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builder(Customer copy) {
        Builder builder = new Builder();
        builder.password = copy.getPassword();
        builder.givenName = copy.getGivenName();
        builder.surname = copy.getSurname();
        builder.emailAddress = copy.getEmailAddress();
        builder.username = copy.getUsername();
        builder.accounts = copy.getAccounts();
        builder.cards = copy.getCards();
        return builder;
    }

    public static final class Builder {
        private String password;
        private String givenName;
        private String surname;
        private String emailAddress;
        private String username;
        private List<Account> accounts;
        private Set<Card> cards;

        private Builder() {
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder givenName(String givenName) {
            this.givenName = givenName;
            return this;
        }

        public Builder surname(String surname) {
            this.surname = surname;
            return this;
        }

        public Builder emailAddress(String emailAddress) {
            this.emailAddress = emailAddress;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder accounts(List<Account> accounts) {
            this.accounts = accounts;
            return this;
        }

        public Builder cards(Set<Card> cards) {
            this.cards = cards;
            return this;
        }

        public Customer build() {
            return new Customer(this);
        }
    }
}
