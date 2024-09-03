package com.Jobseeker.Jobseeker.dataBase.Repositories;

import com.Jobseeker.Jobseeker.dataBase.Favorite.OffersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ListOfOffersRepository extends JpaRepository<OffersEntity, Long> {
    List<OffersEntity> findByUserFavoriteOffers_UserId(Long userId);
}

