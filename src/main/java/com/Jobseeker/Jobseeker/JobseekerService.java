package com.Jobseeker.Jobseeker;

import com.Jobseeker.Jobseeker.dataBase.favorite.OffersEntity;
import com.Jobseeker.Jobseeker.dataBase.favorite.UserFavoriteOffers;
import com.Jobseeker.Jobseeker.dataBase.repositories.OffersEntityRepository;
import com.Jobseeker.Jobseeker.dataBase.repositories.UserFavoriteOffersRepository;
import com.Jobseeker.Jobseeker.dataBase.repositories.UserRepository;
import com.Jobseeker.Jobseeker.dataBase.user.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class JobseekerService {
    private final UserFavoriteOffersRepository userFavoriteOffersRepository;
    private final UserRepository userRepository;
    private final OffersEntityRepository offersEntityRepository;

    public JobseekerService(UserFavoriteOffersRepository userFavoriteOffersRepository,
                            UserRepository userRepository,
                            OffersEntityRepository offersEntityRepository) {
        this.userFavoriteOffersRepository = userFavoriteOffersRepository;
        this.userRepository = userRepository;
        this.offersEntityRepository = offersEntityRepository;
    }


    @Transactional
    public void addToDataBase(Set<Offers> offers) {
        Set<OffersEntity> offersEntityList = offers.stream()
                .map(this::mapToOffersEntity)
                .collect(Collectors.toSet());
        offersEntityRepository.saveAll(offersEntityList);
    }

    private OffersEntity mapToOffersEntity(Offers offer) {
        return new OffersEntity(null, offer.name(), offer.salary(), offer.link(), true, null);
    }

    public void addFavorite(Long userId, Long favoriteOfferId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        OffersEntity offersEntity = offersEntityRepository.findById(favoriteOfferId)
                .orElseThrow(() -> new RuntimeException("Favorite Offer not found"));

        UserFavoriteOffers userFavoriteOffers = new UserFavoriteOffers(user, offersEntity);
        userFavoriteOffersRepository.save(userFavoriteOffers);
    }
    public void deleteFavorite(Long userId, Long favoriteOfferId) {
        UserFavoriteOffers userFavoriteOffer = userFavoriteOffersRepository.findByUserIdAndOffersEntityId(userId, favoriteOfferId)
                .orElseThrow(() -> new RuntimeException("Favorite Offer not found in user's favorites"));

        userFavoriteOffersRepository.delete(userFavoriteOffer);
    }

    public List<OffersEntity> getFavorites(Long userId) {
        return offersEntityRepository.findByUserFavoriteOffers_UserId(userId);
    }
    public List<OffersEntity> getAllOffers() {
        return offersEntityRepository.findAll();
    }

}
