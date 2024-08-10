package dev.vinicius.portfolio.Services;

import dev.vinicius.portfolio.DataBase.Entities.User;
import dev.vinicius.portfolio.DataBase.Models.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepo;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public User createUser(String username, String password, int level) {
        if (User.isValidPassword(password)) {
            String encodedPassword = passwordEncoder.encode(password);
            User user = new User();
            user.setUsername(username);
            user.setPassword(encodedPassword);
            user.setLevel(level);
            return userRepo.save(user);
        } else {
            throw new IllegalArgumentException("Senha inválida");
        }
    }

    public User getById(Long id) {
        User user = userRepo.findById(id).get();
        return user;
    }

    public List<User> getAll() {
        return userRepo.findAll();
    }

    public boolean deleteById(Long id) {
        try {
            userRepo.deleteById(id);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}