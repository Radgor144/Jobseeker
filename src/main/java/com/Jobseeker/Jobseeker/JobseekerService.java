package com.Jobseeker.Jobseeker;

import com.Jobseeker.Jobseeker.dataBase.Favorite.ListOfOffers;
import com.Jobseeker.Jobseeker.dataBase.Favorite.UserFavoriteOffers;
import com.Jobseeker.Jobseeker.dataBase.Repositories.ListOfOffersRepository;
import com.Jobseeker.Jobseeker.dataBase.Repositories.UserFavoriteOffersRepository;
import com.Jobseeker.Jobseeker.dataBase.Repositories.UserRepository;
import com.Jobseeker.Jobseeker.dataBase.User.User;
import org.springframework.stereotype.Service;

import java.util.List;

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


//    @Transactional
//    public void addToFavoriteList(Offers offer) {
//        if (!favoriteOffersRepository.existsByNameAndSalaryAndLink(offer.name(), offer.salary(), offer.link())) {
//            FavoriteOffers favoriteOffer = new FavoriteOffers(offer.name(), offer.salary(), offer.link());
//            favoriteOffersRepository.save(favoriteOffer);
//        }
//    }

    public void addFavorite(Long userId, Long favoriteOfferId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        ListOfOffers listOfOffers = listOfOffersRepository.findById(favoriteOfferId)
                .orElseThrow(() -> new RuntimeException("Favorite Offer not found"));

        UserFavoriteOffers userFavoriteOffers = new UserFavoriteOffers(user, listOfOffers);
        userFavoriteOffersRepository.save(userFavoriteOffers);
    }

    public List<ListOfOffers> getFavorites(Long userId) {
        return listOfOffersRepository.findByUserFavoriteOffers_UserId(userId);
    }

}
