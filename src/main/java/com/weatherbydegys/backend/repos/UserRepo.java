package com.weatherbydegys.backend.repos;

import com.weatherbydegys.backend.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepo extends CrudRepository<User, Integer> {

    List<User> findByName(String name);
    User findById(int id);
    User findAllByEmail(String email);

}
