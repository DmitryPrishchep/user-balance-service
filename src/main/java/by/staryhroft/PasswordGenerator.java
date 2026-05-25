package by.staryhroft;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordGenerator {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "password"; // или любой другой
        String hash = encoder.encode(rawPassword);
        System.out.println(hash);
    }
}