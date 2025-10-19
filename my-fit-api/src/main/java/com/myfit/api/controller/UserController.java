package edu.rutmiit.demo.myfitapi.controller;

import edu.rutmiit.demo.myfit_contract.api.UsersApi;
import edu.rutmiit.demo.myfit_contract.dto.User;
import edu.rutmiit.demo.myfit_contract.dto.UserRequest;
import edu.rutmiit.demo.myfitapi.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;




@RestController
public class UserController implements UsersApi {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Override
    public ResponseEntity<User> createUser(UserCreateRequest userCreateRequest) {
        User user = userService.createUser(userCreateRequest);
        return ResponseEntity.ok(user);
    }

    @Override
    public ResponseEntity<User> getUserById(Long userId) {
        User user = userService.getUserById(userId);
        return ResponseEntity.ok(user);
    }

    @Override
    public ResponseEntity<List<User>> getUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @Override
    public ResponseEntity<Void> deleteUser(Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok().build();
    }
}