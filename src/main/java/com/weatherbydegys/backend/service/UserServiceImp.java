package com.weatherbydegys.backend.service;

import com.weatherbydegys.backend.model.User;
import com.weatherbydegys.backend.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional()
public class UserServiceImp implements UserService {

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<User> getAll() {
        return (List<User>) userRepo.findAll();
    }

    @Override
    public User getOneByEmail(String email) {
        return userRepo.findAllByEmail(email);
    }

    @Override
    public User add(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepo.save(user);
    }
}
