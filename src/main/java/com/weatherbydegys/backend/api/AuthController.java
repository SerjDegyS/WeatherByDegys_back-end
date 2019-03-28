package com.weatherbydegys.backend.api;

//import com.weatherbydegys.backend.CreetingController;
import com.weatherbydegys.backend.model.Role;
import com.weatherbydegys.backend.model.User;
import com.weatherbydegys.backend.service.UserService;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;

@Controller()
public class AuthController {

    private static final Logger LOGGER = Logger.getLogger(AuthController.class);

    @Autowired
    private UserService userService;

    @GetMapping("/registration")
    public String registration(){
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(User user, Map<String, Object> model){

        /* User data validator */
        if (user.getEmail()== null || user.getEmail().length() < 4
                || user.getPassword() == null || user.getPassword().length() < 6){
            model.put("message", "Bad criteria!!!");
            return "registration";
        }

        User userFromDB = userService.getOneByEmail(user.getEmail());

        /* User validator */
        if (userFromDB != null){
            model.put("message", "USER exist!");
            return "registration";
        }

        user.setLastVisit(LocalDateTime.now());
        user.setRoles(Collections.singleton(Role.USER));
        user.setPhotoURL("");
        userService.add(user);

        return "redirect:/login";
    }

    @RequestMapping("/login")
    public String login (@RequestParam(name = "error", required = false) String error,
                         Map<String, Object> model){
        if(error != null){
            model.put("message", error);
        }
        return "login";
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> errorHandler (Exception exc) {
        LOGGER.error(exc.getMessage(), exc);
        return new ResponseEntity<>(exc.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
