package com.Jobseeker.Jobseeker.favoriteOffers;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FavoriteOffersRepository extends JpaRepository<FavoriteOffers, Long> {
    boolean existsByNameAndSalaryAndLink(String name, String salary, String link);
}

