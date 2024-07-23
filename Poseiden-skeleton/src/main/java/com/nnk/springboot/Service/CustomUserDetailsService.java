package com.nnk.springboot.Service;

import com.nnk.springboot.domain.User;
import jakarta.annotation.PostConstruct;
import org.h2.engine.UserBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import com.nnk.springboot.repositories.UserRepository;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found: " + username);
        }

        org.springframework.security.core.userdetails.User.UserBuilder builder = org.springframework.security.core.userdetails.User.withUsername(username);
        builder.password(user.getPassword());
        builder.roles(user.getRole());

        return builder.build();
    }
//    @PostConstruct
//    public void init() {
//        createAdmin();
//        createUser();
//    }
//
//    public void createAdmin() {
//        User admin = new User();
//        admin.setUsername("admin");
//        admin.setPassword(passwordEncoder.encode("admin")); // Encodage du mot de passe
//        admin.setFullname("Administrator");
//        admin.setRole("ADMIN");
//
//        userRepository.save(admin);
//    }
//
//    public void createUser() {
//        User user = new User();
//        user.setUsername("user");
//        user.setPassword(passwordEncoder.encode("user")); // Encodage du mot de passe
//        user.setFullname("Regular User");
//        user.setRole("USER");
//
//        userRepository.save(user);
//    }

}
