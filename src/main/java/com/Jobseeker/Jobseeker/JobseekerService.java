package com.Jobseeker.Jobseeker;

import com.Jobseeker.Jobseeker.dataBase.Favorite.OffersInDB;
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
        List<OffersInDB> offersInDBList = offers.stream()
                .map(this::mapToOffersInDB)
                .collect(Collectors.toList());
        listOfOffersRepository.saveAll(offersInDBList);
    }

    private OffersInDB mapToOffersInDB(Offers offer) {
        return new OffersInDB(null, offer.name(), offer.salary(), offer.link(), null);
    }

    public void addFavorite(Long userId, Long favoriteOfferId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        OffersInDB offersInDB = listOfOffersRepository.findById(favoriteOfferId)
                .orElseThrow(() -> new RuntimeException("Favorite Offer not found"));

        UserFavoriteOffers userFavoriteOffers = new UserFavoriteOffers(user, offersInDB);
        userFavoriteOffersRepository.save(userFavoriteOffers);
    }
    public void deleteFavorite(Long userId, Long favoriteOfferId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        OffersInDB offersInDB = listOfOffersRepository.findById(favoriteOfferId)
                .orElseThrow(() -> new RuntimeException("Favorite Offer not found"));
        userRepository.deleteById(favoriteOfferId);

        UserFavoriteOffers userFavoriteOffers = userFavoriteOffersRepository.findByUserAndOffersInDB(user, offersInDB)
                .orElseThrow(() -> new RuntimeException("Favorite Offer not found in user's favorites"));

        userFavoriteOffersRepository.delete(userFavoriteOffers);
    }

    public List<OffersInDB> getFavorites(Long userId) {
        return listOfOffersRepository.findByUserFavoriteOffers_UserId(userId);
    }

}
