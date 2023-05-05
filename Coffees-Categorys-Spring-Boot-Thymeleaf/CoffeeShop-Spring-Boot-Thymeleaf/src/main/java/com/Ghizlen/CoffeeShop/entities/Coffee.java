package com.Ghizlen.CoffeeShop.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Coffee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCoffee;
    private String nomCoffee;
    private String prixCoffee;
    private Date dateCreation;



    @ManyToOne
    @JoinColumn(name = "id_Category")
    private Category category;
}
