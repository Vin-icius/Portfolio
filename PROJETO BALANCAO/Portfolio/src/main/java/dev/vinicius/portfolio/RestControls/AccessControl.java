package dev.vinicius.portfolio.RestControls;

import dev.vinicius.portfolio.DataBase.Entities.User;
import dev.vinicius.portfolio.DataBase.Models.UserRepository;
import dev.vinicius.portfolio.Security.JWTTokenProvider;
import dev.vinicius.portfolio.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/apis/access")
public class AccessControl {

    @Autowired
    private UserService userService;
    @Autowired
    private JWTTokenProvider jwtTokenProvider;

    @Autowired
    private UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // Endpoint para login
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestParam String username, @RequestParam String password) {
        Optional<User> userOptional = userRepository.findByUsername(username);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            // Verifica se a senha fornecida corresponde à senha armazenada
            if (passwordEncoder.matches(password, user.getPassword())) {
                String token = jwtTokenProvider.getToken(username, String.valueOf(user.getLevel()));
                Map<String, String> response = new HashMap<>();
                response.put("token", token);
                return ResponseEntity.ok(response);
            }
        }

        // Retorna erro 401 para credenciais inválidas
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "Credenciais inválidas");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    // Endpoint para registro de novo usuário
    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> registerUser(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam int level) {

        // Verifica se o nome de usuário já está cadastrado
        if (userRepository.findByUsername(username).isPresent()) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Nome de usuário já cadastrado");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        // Cria e salva o novo usuário
        User newUser = new User();
        userService.createUser(username,password,level);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Usuário cadastrado com sucesso");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Endpoint protegido
    @GetMapping("/secured-endpoint")
    public ResponseEntity<String> securedEndpoint() {
        return ResponseEntity.ok("Este é um endpoint protegido");
    }

    // Endpoint público
    @GetMapping("/public-endpoint")
    public ResponseEntity<String> publicEndpoint() {
        return ResponseEntity.ok("Este é um endpoint público");
    }
}
