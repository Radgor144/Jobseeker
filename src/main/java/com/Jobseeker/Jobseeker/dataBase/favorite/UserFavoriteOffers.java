package com.Jobseeker.Jobseeker.dataBase.favorite;

import com.Jobseeker.Jobseeker.dataBase.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class UserFavoriteOffers {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "offersEntity")
    private OffersEntity offersEntity;

    public UserFavoriteOffers(User user, OffersEntity offersEntity) {
        this.user = user;
        this.offersEntity = offersEntity;
    }
}
