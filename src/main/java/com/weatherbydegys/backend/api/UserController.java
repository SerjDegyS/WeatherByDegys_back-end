package com.weatherbydegys.backend.api;

import com.weatherbydegys.backend.dto.MapperUserDTO;
import com.weatherbydegys.backend.dto.UserDTO;
import com.weatherbydegys.backend.model.User;
import com.weatherbydegys.backend.repos.UserRepo;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.xml.ws.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private static final Logger LOGGER = Logger.getLogger(UserController.class);

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private MapperUserDTO mapperUserDTO;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/all")
    public Iterable<UserDTO> all() {
        Iterable<User> allUsers = userRepo.findAll();

        List<UserDTO> result = ((List<User>) allUsers).stream()
                .map(user -> mapperUserDTO.convertToDTO(user))
                .collect(Collectors.toList());

        LOGGER.info(allUsers);

        return (allUsers != null) ? result : null;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/create")
    public UserDTO createUser(@RequestBody UserDTO userDTO) {
        User newUser = mapperUserDTO.convertToEntity(userDTO);

        if(!userRepo.findByName(newUser.getName()).isEmpty()) {
            throw new IllegalArgumentException("The username is not available!!!");
        }
        userRepo.save(newUser);
        LOGGER.info("CREATE new User: " + newUser.toString());

        return mapperUserDTO.convertToDTO(newUser);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/update")
    public UserDTO update(@RequestBody UserDTO newUserDTO) {
        User oldUser = userRepo.findById(newUserDTO.getId());

        if(oldUser == null) {
            throw new IllegalArgumentException("The userID is not available!!!");
        }
        oldUser.setUid(newUserDTO.getUid());
        oldUser.setName(newUserDTO.getName());
        oldUser.setEmail(newUserDTO.getEmail());
        oldUser.setPhotoURL(newUserDTO.getPhotoURL());

        userRepo.save(oldUser);
        LOGGER.info("UPDATE User: " + oldUser.toString());
        return mapperUserDTO.convertToDTO(oldUser);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> errorHandler(Exception exc) {
        LOGGER.error(exc.getMessage());
        return new ResponseEntity<>(exc.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
