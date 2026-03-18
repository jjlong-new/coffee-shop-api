package com.jjcoffee.coffee_shop_api.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = "coffee")
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coffee_id", nullable = false)
    private Coffee coffee;

    @Column(name = "coffee_name", nullable = false)
    private String coffeeName;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name= "total_amount", nullable = false)
    private BigDecimal totalAmount;

    @Builder
    public Order(Coffee coffee, String coffeeName, int quantity, BigDecimal totalAmount) {
        this.coffee = coffee;
        this.coffeeName = coffeeName;
        this.quantity = quantity;
        this.totalAmount = totalAmount;
    }

}
