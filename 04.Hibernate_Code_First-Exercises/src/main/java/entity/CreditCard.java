package entity;

import javax.persistence.*;

@Entity
@Table(name = "credit_cards")
public class CreditCard extends BillingDetail{

    private CardType cardType;
    private Integer expMonth;
    private Integer expYear;

    public CreditCard(){
    }

    @Enumerated(EnumType.STRING)
    public CardType getCardType() {
        return cardType;
    }

    public void setCardType(CardType cardType) {
        this.cardType = cardType;
    }

    @Column(name = "exp_month")
    public Integer getExpMonth() {
        return expMonth;
    }

    public void setExpMonth(Integer expMonth) {
        this.expMonth = expMonth;
    }

    @Column(name = "exp_year")
    public Integer getExpYear() {
        return expYear;
    }

    public void setExpYear(Integer expYear) {
        this.expYear = expYear;
    }
}
