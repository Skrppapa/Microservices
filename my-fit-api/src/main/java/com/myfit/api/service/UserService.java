package com.myfit.api.service;

import com.myfit.api.storage.InMemoryStorage;
import com.myfit.api.exception.NotFoundException;
import com.myfit.api.exception.ValidationException;
import com.myfit.contract.model.User;
import com.myfit.contract.model.UserCreateRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final InMemoryStorage storage;

    public UserService(InMemoryStorage storage) {
        this.storage = storage;
    }

    public User createUser(UserCreateRequest request) {
        if (request.getName() == null || request.getName().trim().isEmpty()) {
            throw new ValidationException("User name cannot be empty");
        }
        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            throw new ValidationException("User email cannot be empty");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());

        return storage.saveUser(user);
    }

    public User getUserById(Long userId) {
        User user = storage.getUserById(userId);
        if (user == null) {
            throw new NotFoundException("User not found with id: " + userId);
        }
        return user;
    }

    public List<User> getAllUsers() {
        return storage.getAllUsers();
    }

    public void deleteUser(Long userId) {
        User user = storage.getUserById(userId);
        if (user == null) {
            throw new NotFoundException("User not found with id: " + userId);
        }
        storage.deleteUser(userId);
    }
}