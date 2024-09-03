package com.Jobseeker.Jobseeker;

import com.Jobseeker.Jobseeker.dataBase.Favorite.OffersEntity;
import com.Jobseeker.Jobseeker.dataBase.Favorite.UserFavoriteOffers;
import com.Jobseeker.Jobseeker.dataBase.Repositories.ListOfOffersRepository;
import com.Jobseeker.Jobseeker.dataBase.Repositories.UserFavoriteOffersRepository;
import com.Jobseeker.Jobseeker.dataBase.Repositories.UserRepository;
import com.Jobseeker.Jobseeker.dataBase.User.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class JobseekerService {
    private final UserFavoriteOffersRepository userFavoriteOffersRepository;
    private final UserRepository userRepository;
    private final ListOfOffersRepository listOfOffersRepository;

    public JobseekerService(UserFavoriteOffersRepository userFavoriteOffersRepository,
                            UserRepository userRepository,
                            ListOfOffersRepository listOfOffersRepository) {
        this.userFavoriteOffersRepository = userFavoriteOffersRepository;
        this.userRepository = userRepository;
        this.listOfOffersRepository = listOfOffersRepository;
    }


    @Transactional
    public void addToDataBase(List<Offers> offers) {
        List<OffersEntity> offersEntityList = offers.stream()
                .map(this::mapToOffersEntity)
                .collect(Collectors.toList());
        listOfOffersRepository.saveAll(offersEntityList);
    }

    private OffersEntity mapToOffersEntity(Offers offer) {
        return new OffersEntity(null, offer.name(), offer.salary(), offer.link(), null);
    }

    public void addFavorite(Long userId, Long favoriteOfferId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        OffersEntity offersEntity = listOfOffersRepository.findById(favoriteOfferId)
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
        return listOfOffersRepository.findByUserFavoriteOffers_UserId(userId);
    }

}
