package com.weatherbydegys.backend.api;

import com.weatherbydegys.backend.dto.FavCityDTO;
import com.weatherbydegys.backend.dto.MapperFavCityDTO;
import com.weatherbydegys.backend.dto.UserDTO;
import com.weatherbydegys.backend.model.FavCity;
import com.weatherbydegys.backend.model.User;
import com.weatherbydegys.backend.repos.FavCityRepo;
import com.weatherbydegys.backend.repos.UserRepo;
import javassist.NotFoundException;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import static com.weatherbydegys.backend.Specification.FavCitySpecification.favCityNameStartBy;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/favCities")
public class FavCityController {
    private static final Logger LOGGER = Logger.getLogger(FavCityController.class);
    @Autowired
    MapperFavCityDTO mapperFavCityDTO;

    @Autowired
    FavCityRepo favCityRepo;
    @Autowired
    UserRepo userRepo;


    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = "addCity")
    public ResponseEntity<List<FavCityDTO>> addFavCity( @RequestBody FavCityDTO favCity, @RequestParam("id") String id, Map<String, Object> model) {

        User user = userRepo.findById(Integer.parseInt(id));
        if(user == null){
            throw new IllegalArgumentException("User with ID: " + id + " is not available!!!");
        }

        FavCity newFavCity = new FavCity(Integer.parseInt(favCity.getId()), favCity.getName(), favCity.getCountry());

        HttpHeaders headers = new HttpHeaders();

        /*If user don't has new Favorite City*/
        if(!user.getFavCities().contains(newFavCity)){
            newFavCity.addUser(user);
            user.addFavCity(newFavCity);
            LOGGER.info("ADD to User ID: " + user.getId() + "FavCity: " + newFavCity.toString());

            userRepo.save(user);
        }else {
            LOGGER.warn("User ID: " + user.getId() + " already contains favorite city " + newFavCity.toString());
            headers.add("Info", "User already has favorite city: " + newFavCity.toString());
            List<FavCityDTO> result = user.getFavCities().stream().map(city -> mapperFavCityDTO.convertToDTO(city)).collect(Collectors.toList());
            return new ResponseEntity<>(result, headers,HttpStatus.ALREADY_REPORTED);
        }

        List<FavCityDTO> result = user.getFavCities().stream().map(city -> mapperFavCityDTO.convertToDTO(city)).collect(Collectors.toList());
        return new ResponseEntity<>(result, headers, HttpStatus.OK);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/byUserID")
    public List<FavCityDTO> getByUserID(@RequestParam String userID) {
        User user = userRepo.findById(Integer.parseInt(userID));
        if(user == null){
            throw new IllegalArgumentException("The userID: " + userID + " is not available!!!");
        }
        return user.getFavCities().stream().map(favCity -> mapperFavCityDTO.convertToDTO(favCity)).collect(Collectors.toList());
    }

    @DeleteMapping()
    @ResponseStatus(HttpStatus.OK)
    public void delete (@RequestParam String userID, @RequestParam String favCityID) {
        User user = userRepo.findById(Integer.parseInt(userID));
        if(user == null){
            throw new IllegalArgumentException("User with ID: " + userID + " not found!!!");
        }
        FavCity favCity = favCityRepo.findById(Integer.parseInt(favCityID));
        if(favCity == null) {
            throw new IllegalArgumentException("FavCity with ID: " + favCityID + " not found!!!");
        }
        user.deleteFavCity(favCity);
        userRepo.save(user);
        LOGGER.info("DELETE FavCity with ID: " + favCityID + " from User ID: " + userID);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/updateCitiesByUserID")
    public void updateCitiesByUserID(@RequestParam String userID, @RequestBody List<FavCityDTO> favCityDTOList) {
        if (favCityDTOList != null){
            User user = userRepo.findById(Integer.parseInt(userID));
            if(user == null){
                throw new IllegalArgumentException("The userID: " + userID + " is not available!!!");
            }
            List<FavCity> favCityList = favCityDTOList.stream().map(favCityDTO -> mapperFavCityDTO.convertToEntity(favCityDTO)).collect(Collectors.toList());
            user.setFavCities(new HashSet<>(favCityList));
            LOGGER.info("UPDATE User ID: " + userID + " by FavCitiesList: " + favCityList.toString());
            userRepo.save(user);
        }
    }

    @GetMapping("/filter")
    public List<FavCity> filter(@RequestParam String filter) throws NotFoundException {
        if (filter == null || filter.length() < 3 ){
            throw new NotFoundException("Bad filter! Min 3 symbols!");
        }

        return favCityRepo.findAll(favCityNameStartBy(filter));
    }



    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> errorHandler(Exception exc) {
        LOGGER.error(exc.getMessage());
        return new ResponseEntity<>(exc.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
