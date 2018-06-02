package com.isedykh.profiles.dao.entity;

import com.isedykh.profiles.service.entity.Term;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "prices", schema = "public")
public class PriceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade={CascadeType.ALL},  fetch = FetchType.EAGER, optional = false)
    private TermEntity term;

    //1 penny step
    @Column(nullable = false)
    private Integer priceValue;

    public PriceEntity(TermEntity term, int purchasePrice) {
        this.term = term;
        this.priceValue = purchasePrice * term.getCoefficient();
    }
}
