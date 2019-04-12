package com.weatherbydegys.backend.repos;

import com.weatherbydegys.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface UserRepo extends JpaRepository<User, Long> {

    List<User> findByName(String name);
    User findById(long id);
    User findAllByEmail(String email);
    UserDetails findUserByName(String userName);

}
