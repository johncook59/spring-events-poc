package zarg.debitcredit.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import java.time.LocalDate;

@Entity
@Table(name = "card", indexes = {@Index(name = "idx_card_bid", columnList = "bid")})
@Data
@NoArgsConstructor
public class Card {
    @Id
    @Column(name = "id", columnDefinition = "serial", nullable = false, updatable = false)
    @GeneratedValue
    private int id;

    @Column(name = "bid", updatable = false, insertable = false)
    @ColumnDefault("concat('D', lpad(nextval('hibernate_sequence'::regclass)::text, 8, '0'))")
    @Generated(GenerationTime.INSERT)
    private String bid;

    @Version
    private int version = 0;

    @Column(length = 10, nullable = false)
    public String type;

    @Column(length = 20, nullable = false)
    public String number;

    @Column(length = 3, nullable = false)
    public String cvv2;

    @Column(nullable = false)
    public LocalDate expires;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    @JsonBackReference
    private Customer cardHolder;

    private Card(Builder builder) {
        setType(builder.type);
        setNumber(builder.number);
        setCvv2(builder.cvv2);
        setExpires(builder.expires);
        setCardHolder(builder.cardHolder);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builder(Card copy) {
        Builder builder = new Builder();
        builder.type = copy.getType();
        builder.number = copy.getNumber();
        builder.cvv2 = copy.getCvv2();
        builder.expires = copy.getExpires();
        builder.cardHolder = copy.getCardHolder();
        return builder;
    }

    public static final class Builder {
        private String type;
        private String number;
        private String cvv2;
        private LocalDate expires;
        private Customer cardHolder;

        private Builder() {
        }

        public Builder type(String type) {
            this.type = type;
            return this;
        }

        public Builder number(String number) {
            this.number = number;
            return this;
        }

        public Builder cvv2(String cvv2) {
            this.cvv2 = cvv2;
            return this;
        }

        public Builder expires(LocalDate expires) {
            this.expires = expires;
            return this;
        }

        public Builder cardHolder(Customer cardHolder) {
            this.cardHolder = cardHolder;
            return this;
        }

        public Card build() {
            return new Card(this);
        }
    }
}
