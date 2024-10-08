package com.Jobseeker.Jobseeker.dataBase.favorite;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class OffersEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String salary;
    private String link;
    private boolean current;
    private LocalDateTime creationDate;

    @OneToMany(mappedBy = "offersEntity", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<UserFavoriteOffers> userFavoriteOffers;
}

