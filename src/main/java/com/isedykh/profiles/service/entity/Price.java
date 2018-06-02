package com.isedykh.profiles.service.entity;


import com.isedykh.profiles.service.Identifiable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Price implements Identifiable {

    public static final double WEEK_COEFFICIENT = 0.15;

    public static final double TWO_WEEKS_COEFFICIENT = 0.2;

    public static final double MONTH_COEFFICIENT = 0.35;

    public static final double NEW_WEEK_COEFFICIENT = 0.2;

    public static final double NEW_TWO_WEEKS_COEFFICIENT = 0.25;

    public static final double NEW_MONTH_COEFFICIENT = 0.4;

    private Long id;

    private Term term;

    private Integer priceValue;

    public Price(Term term, int purchasePrice) {
        this.term = term;
        this.priceValue = purchasePrice * term.getCoefficient();
    }

    @Override
    public String toString() {
        return term + ", " + priceValue / 100 + "," + priceValue % 100;
    }
}
