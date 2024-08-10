package dev.vinicius.portfolio.DataBase.Entities;

import jakarta.persistence.*;

import java.util.regex.Pattern;

@Entity
@Table(name = "AppUser")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "user_name", nullable = false)
    private String username;

    @Column(name = "user_psw", nullable = false)
    private String password;

    @Column(name = "user_level", nullable = false)
    private int level;

    public User() {
        // Construtor padrão
    }

    public User(Long id, String username, String password, int level) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.level = level;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public static boolean isValidPassword(String password) {
        // A senha deve ter pelo menos 8 caracteres
        // Conter pelo menos uma letra maiúscula, uma letra minúscula, um dígito e um caractere especial
        String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
        Pattern pattern = Pattern.compile(passwordRegex);
        return pattern.matcher(password).matches();
    }
}
