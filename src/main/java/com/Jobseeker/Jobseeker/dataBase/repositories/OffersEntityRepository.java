package com.Jobseeker.Jobseeker.dataBase.repositories;

import com.Jobseeker.Jobseeker.dataBase.favorite.OffersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OffersEntityRepository extends JpaRepository<OffersEntity, Long> {
    List<OffersEntity> findByUserFavoriteOffers_UserId(Long userId);
    boolean existsByLink(String link);
}

