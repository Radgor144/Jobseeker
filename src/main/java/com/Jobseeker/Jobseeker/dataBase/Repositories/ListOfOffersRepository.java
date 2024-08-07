package com.Jobseeker.Jobseeker.dataBase.Repositories;

import com.Jobseeker.Jobseeker.dataBase.Favorite.OffersInDB;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ListOfOffersRepository extends JpaRepository<OffersInDB, Long> {
    List<OffersInDB> findByUserFavoriteOffers_UserId(Long userId);
}

