package com.Jobseeker.Jobseeker.database.user;

import com.Jobseeker.Jobseeker.dataBase.repositories.UserRepository;
import com.Jobseeker.Jobseeker.dataBase.user.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class AddingUserToDBTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void shouldAddUserToDB() {
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Snow");
        user.setEmail("John@gmail.com");
        user.setPassword("password");

        User savedUser = userRepository.save(user);

        assertThat(entityManager.find(User.class, savedUser.getId()) ).isEqualTo(user);
    }
}