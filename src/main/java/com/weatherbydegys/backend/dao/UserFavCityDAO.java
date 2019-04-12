package com.weatherbydegys.backend.dao;

import com.weatherbydegys.backend.dto.FavCityDTO;
import com.weatherbydegys.backend.model.FavCity;
import com.weatherbydegys.backend.model.User;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserFavCityDAO {
    List<FavCity> addFavCityToUser(long userId, FavCity favCity);
    List<FavCity> getByUserID(long userId);
    List<FavCity> updateCitiesByUserID(long userId, List<FavCity> favCities);
//    void delete(FavCity favCity);
    FavCity findOne(long id);
    List<FavCity> list();
}
