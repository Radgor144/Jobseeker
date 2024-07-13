package com.Jobseeker.Jobseeker;

import com.Jobseeker.Jobseeker.favoriteOffers.FavoriteOffers;
import com.Jobseeker.Jobseeker.favoriteOffers.FavoriteOffersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class JobseekerService {
    private final FavoriteOffersRepository favoriteOffersRepository;

    @Autowired
    public JobseekerService(FavoriteOffersRepository favoriteOffersRepository) {
        this.favoriteOffersRepository = favoriteOffersRepository;
    }

    @Transactional
    public void addToFavoriteList(Offers offer) {
        if (!favoriteOffersRepository.existsByNameAndSalaryAndLink(offer.name(), offer.salary(), offer.link())) {
            FavoriteOffers favoriteOffer = new FavoriteOffers(offer.name(), offer.salary(), offer.link());
            favoriteOffersRepository.save(favoriteOffer);
        }
    }

    public Page<FavoriteOffers> getTenFavoriteOffers() {
        Pageable pageable = PageRequest.of(0, 10);
        return favoriteOffersRepository.findAll(pageable);
    }
}
