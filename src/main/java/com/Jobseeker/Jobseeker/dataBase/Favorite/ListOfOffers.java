package com.Jobseeker.Jobseeker.dataBase.Favorite;

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

import java.util.Set;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class ListOfOffers {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String salary;
    private String link;

    @OneToMany(mappedBy = "listOfOffers", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<UserFavoriteOffers> userFavoriteOffers;
}

