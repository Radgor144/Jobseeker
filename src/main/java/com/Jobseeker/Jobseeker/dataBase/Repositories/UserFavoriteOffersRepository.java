package com.Jobseeker.Jobseeker.dataBase.Repositories;

import com.Jobseeker.Jobseeker.dataBase.Favorite.OffersEntity;
import com.Jobseeker.Jobseeker.dataBase.Favorite.UserFavoriteOffers;
import com.Jobseeker.Jobseeker.dataBase.User.User;
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
