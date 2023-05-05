package com.Ghizlen.CoffeeShop.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCategory;
    private String nom;
    private Date dateCreation;

    @JsonIgnore
    @OneToMany(mappedBy = "category")
    private List<Coffee> coffees;

    public void addCoffee(Coffee coffee) {
        coffee.setCategory(this);
        this.coffees.add(coffee);
    }

    public void removeCoffee(Coffee coffee) {
        coffee.setCategory(this);
        this.coffees.remove(coffee);
    }
}
