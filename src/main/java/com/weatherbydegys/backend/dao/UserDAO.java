package com.weatherbydegys.backend.dao;

//import com.weatherbydegys.backend.model.FavCity;
import com.weatherbydegys.backend.model.User;

import java.util.List;

public interface UserDAO {
    void add(User user);
    void delete(User user);
    User findOne(long id);
    List<User> list();
}
