package com.Jobseeker.Jobseeker.favoriteOffers;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class FavoriteOffers {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String salary;
    private String link;

    public FavoriteOffers(String name, String salary, String link) {
        this.name = name;
        this.salary = salary;
        this.link = link;
    }
}

