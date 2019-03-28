package com.weatherbydegys.backend;

import com.weatherbydegys.backend.dto.FavCityDTO;
import com.weatherbydegys.backend.dto.MapperUserDTO;
import com.weatherbydegys.backend.dto.UserDTO;
import com.weatherbydegys.backend.model.FavCity;
import com.weatherbydegys.backend.model.Role;
import com.weatherbydegys.backend.model.User;
import com.weatherbydegys.backend.repos.FavCityRepo;
import com.weatherbydegys.backend.repos.UserRepo;
import com.weatherbydegys.backend.service.UserServiceImp;
import org.apache.catalina.core.ApplicationContext;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.persistence.CollectionTable;
import java.time.LocalDateTime;
import java.util.*;

@Controller
public class CreetingController {

    private static final Logger LOGGER = Logger.getLogger(CreetingController.class);

    @Autowired
    private MapperUserDTO mapperUserDTO;

    @Autowired
    private UserRepo userRepo;
//    @Autowired
//    private UserServiceImp userService;

    @Autowired
    private FavCityRepo favCityRepo;

    @GetMapping("/")
    public String greeting(
            @RequestParam(name = "name", required = false, defaultValue = "World") String name,
            Map<String, Object> model) {
        model.put("name", name);
        return "greeting";
    }

    @GetMapping("/main")
    public String main(Map<String, Object> model){
        Iterable<User> users = userRepo.findAll();
        Iterable<FavCity> favCities = favCityRepo.findAll();
        model.put("users", users);
        model.put("favCities", favCities);
        return "main";
    }

    @RequestMapping(method = RequestMethod.POST, value = "greater", consumes = MediaType.ALL_VALUE)
    public String add(@RequestParam String uid,
                      @RequestParam String name,
                      @RequestParam String email,
                      @RequestParam String photoURL,
                      Map<String, Object> model) {
        User user = new User(uid, name, email, photoURL);

        userRepo.save(user);

        Iterable<User> users = userRepo.findAll();
        model.put("users", users);

        return "main";
    }

    @PostMapping("/filter")
    public String filter(@RequestParam String filter, Map<String, Object> model) {
        Iterable<User> users;

        if(filter != null && !filter.isEmpty()) {
            users = userRepo.findByName(filter);
        } else {
            users = userRepo.findAll();
        }
        model.put("users", users);
        return "main";
    }

//    @ResponseBody
//    @ResponseStatus(HttpStatus.OK)
//    @PostMapping(value = "addCity")
//    public UserDTO addFavCity( @RequestBody FavCityDTO favCity, @RequestParam("id") String id, Map<String, Object> model) {
//        User newUser = new User("serj", "serj", "sadf", "sdsdv");
//        userRepo.save(newUser);
////        System.out.println(favCity.toString());
//        User user = userRepo.findById(Long.parseLong(id));
//        FavCity newFavCity = new FavCity(Integer.parseInt(favCity.getId()), favCity.getName(), favCity.getCountry());
//
//        //If user don't has new Favorite City
//        if(!user.getFavCities().contains(newFavCity)){
//            newFavCity.addUser(user);
//            user.addFavCity(newFavCity);
//            LOGGER.info(user.getFavCities().toString());
//            LOGGER.info(newFavCity.toString());
//
//            userRepo.save(user);
//        }else LOGGER.info("User: " + user.toString() + "already contains favorite city " + newFavCity.toString());
//
//        return mapperUserDTO.convertToDTO(user);
//    }


    @GetMapping("/json")
    @ResponseBody
    public Iterable<User> getJson() {
        return userRepo.findAll();
    }

//    @GetMapping("/registration")
//    public String registration(){
//        return "registration";
//    }
//
//    @PostMapping("/registration")
//    public String addUser(User user, Map<String, Object> model){
//        User userFromDB = userService.getOneByEmail(user.getEmail());
//
//        /* User validator */
//        if (userFromDB != null){
//            model.put("message", "USER exist!");
//            return "registration";
//        }
//
//        user.setLastVisit(LocalDateTime.now());
//        user.setRoles(Collections.singleton(Role.USER));
//        user.setPhotoURL("");
//        userService.add(user);
//
//        return "redirect:/login";
//    }
//
//    @RequestMapping("/login")
//    public String login (@RequestParam(name = "error", required = false) String error,
//                         Map<String, Object> model){
//        if(error != null){
//            model.put("message", error);
//        }
//        return "login";
//    }
//
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> errorHandler (Exception exc) {
        LOGGER.error(exc.getMessage(), exc);
        return new ResponseEntity<>(exc.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
