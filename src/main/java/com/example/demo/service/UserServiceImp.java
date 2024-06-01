package com.example.demo.service;

import com.example.demo.model.Users;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImp implements UserService {
    private final UserRepository userRepository;

    @Override
    public Users createUser(Users users) {
        try {
            if (!users.getFirstName().isEmpty()){
                Users newUsers = Users.builder()
                        .firstName(users.getFirstName())
                        .lastName(users.getLastName())
                        .email(users.getEmail())
                        .build();

                userRepository.save(newUsers);
                log.info("User with id = {} created Successfully", newUsers.getId());
                return newUsers;
            } else {
                log.error("Unable to create User");
                return new Users();
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    @Override
    public Optional<Users> getUserById(Long id) {
        try {
            Optional<Users> user = userRepository.findById(id);

            if (user.isPresent()){
                log.info("User with id = {} retrieved", id);
                return userRepository.findById(id);
            } else {
                log.error("No User with id = {} found", id);
                throw new RuntimeException("No user with id = "+ id + " found.");
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Optional.of("No user with id = "+ id + " found."));
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Optional.of(e.getMessage()));
        }
    }

    @Override
    public Boolean deleteUser(Long id) {
        try {
            Optional<Users> optionalUser = userRepository.findById(id);

            if (optionalUser.isPresent()){
                userRepository.deleteById(id);
                log.info("Successfully deleted User with id = {}", id);
                return true;
//                return ResponseEntity.ok(Boolean.TRUE);
            } else {
                log.error("User with id = {} doesn't exist", id);
                return false;
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Boolean.FALSE);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
