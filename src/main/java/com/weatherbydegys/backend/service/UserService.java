package com.weatherbydegys.backend.service;

import com.weatherbydegys.backend.model.User;

import java.util.List;

public interface UserService {
    List<User> getAll();

    User getOneByEmail(String email);

    User add(User user);
}
