package com.Jobseeker.Jobseeker.dataBase.repositories;

import com.Jobseeker.Jobseeker.dataBase.favorite.OffersEntity;
import com.Jobseeker.Jobseeker.dataBase.favorite.UserFavoriteOffers;
import com.Jobseeker.Jobseeker.dataBase.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserFavoriteOffersRepository extends JpaRepository<UserFavoriteOffers, Long> {
    List<OffersEntity> findByUserId(Long userId);
    Optional<UserFavoriteOffers> findByUserAndOffersEntity(User user, OffersEntity offersEntity);

    Optional<UserFavoriteOffers> findByUserIdAndOffersEntityId(Long userId, Long favoriteOfferId);
}
