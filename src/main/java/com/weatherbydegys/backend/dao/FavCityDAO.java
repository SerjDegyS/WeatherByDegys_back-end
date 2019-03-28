package com.weatherbydegys.backend.dao;

import com.weatherbydegys.backend.model.FavCity;
import com.weatherbydegys.backend.model.User;

import java.util.List;

public interface FavCityDAO {
    void add(FavCity favCity);
    void delete(FavCity favCity);
    FavCity findOne(long id);
    List<FavCity> list();
}
