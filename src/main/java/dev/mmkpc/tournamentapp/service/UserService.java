package dev.mmkpc.tournamentapp.service;

import dev.mmkpc.tournamentapp.exception.UserNotFoundException;
import dev.mmkpc.tournamentapp.model.User;
import dev.mmkpc.tournamentapp.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

//    public String authenticateUser(User user) throws UserNotFoundException{
//        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
//        Optional<User> opUser = Optional.ofNullable(userRepository.findByUserName(user.getUsername()));
//        if(opUser.isPresent()){
//            User dbUser = opUser.get();
//            if(bCryptPasswordEncoder.matches(user.getPassword(),dbUser.getPassword()))
//                return "Authenticated User";
//            else
//                return "Incorrect Password";
//
//        }
//        throw new UserNotFoundException("No user is not found for this username !!");
//    }

    public String addUser(User user)
    {
//        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
//        String encryptedPassword = bCryptPasswordEncoder.encode(user.getPassword());
//        user.setPassword(encryptedPassword);
        userRepository.save(user);

        return "Registiration Successfull";

    }
}
