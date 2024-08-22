package com.Jobseeker.Jobseeker.dataBase.Repositories;

import com.Jobseeker.Jobseeker.dataBase.Favorite.OffersInDB;
import com.Jobseeker.Jobseeker.dataBase.Favorite.UserFavoriteOffers;
import com.Jobseeker.Jobseeker.dataBase.User.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserFavoriteOffersRepository extends JpaRepository<UserFavoriteOffers, Long> {
    List<OffersInDB> findByUserId(Long userId);
    Optional<UserFavoriteOffers> findByUserAndOffersInDB(User user, OffersInDB offersInDB);
}
