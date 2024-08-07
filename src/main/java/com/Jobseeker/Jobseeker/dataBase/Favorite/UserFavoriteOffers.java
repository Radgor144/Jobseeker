package com.Jobseeker.Jobseeker.dataBase.Favorite;

import com.Jobseeker.Jobseeker.dataBase.User.User;
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
    @JoinColumn(name = "offersInDB_id")
    private OffersInDB offersInDB;

    public UserFavoriteOffers(User user, OffersInDB offersInDB) {
        this.user = user;
        this.offersInDB = offersInDB;
    }
}
