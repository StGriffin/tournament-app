package dev.mmkpc.tournamentapp.controller;

import dev.mmkpc.tournamentapp.dto.LoginRequestDTO;
import dev.mmkpc.tournamentapp.dto.MessageResponseDto;
import dev.mmkpc.tournamentapp.dto.RegisterRequestDTO;
import dev.mmkpc.tournamentapp.model.User;
import dev.mmkpc.tournamentapp.repository.UserRepository;
import dev.mmkpc.tournamentapp.security.jwt.JwtUtils;
import dev.mmkpc.tournamentapp.security.services.UserDetailsImpl;
import dev.mmkpc.tournamentapp.dto.JwtResponseDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.stream.Collectors;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequestDTO loginRequest) {

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUserName(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity
                .ok(new JwtResponseDto(jwt, userDetails.getId(), userDetails.getUsername(), userDetails.getFullName(), roles));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequestDTO registerRequestDTO) {
        if (userRepository.existsByuserName(registerRequestDTO.getUserName())) {
            return ResponseEntity.badRequest().body(new MessageResponseDto("Error: Username is already taken!"));
        }

        User user = new User(registerRequestDTO.getUserName(), encoder.encode(registerRequestDTO.getPassword()),
                registerRequestDTO.getFullName(),registerRequestDTO.getAge()
                );


        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponseDto("User registered successfully!"));
    }
}