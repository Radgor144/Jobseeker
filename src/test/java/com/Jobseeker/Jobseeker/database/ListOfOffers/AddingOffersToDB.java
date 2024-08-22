package com.Jobseeker.Jobseeker.database.ListOfOffers;

import com.Jobseeker.Jobseeker.dataBase.Repositories.UserFavoriteOffersRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class AddingOffersToDB {

    @Autowired
    private UserFavoriteOffersRepository userFavoriteOffersRepository;

    @Autowired
    TestEntityManager entityManager;

    @Test
    void shouldAddOffersToDB() {

    }
}

