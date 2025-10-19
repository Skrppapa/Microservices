package com.myfit.api.service;

import com.myfit.api.storage.InMemoryStorage;
import com.myfit.api.exception.NotFoundException;
import com.myfit.api.exception.ValidationException;
import com.myfit.contract.model.Gym;
import com.myfit.contract.model.GymCreateRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GymService {

    private final InMemoryStorage storage;

    public GymService(InMemoryStorage storage) {
        this.storage = storage;
    }

    public Gym createGym(GymCreateRequest request) {
        if (request.getName() == null || request.getName().trim().isEmpty()) {
            throw new ValidationException("Gym name cannot be empty");
        }
        if (request.getAddress() == null || request.getAddress().trim().isEmpty()) {
            throw new ValidationException("Gym address cannot be empty");
        }

        Gym gym = new Gym();
        gym.setName(request.getName());
        gym.setAddress(request.getAddress());
        gym.setPhone(request.getPhone());
        gym.setWorkingHours(request.getWorkingHours());

        return storage.saveGym(gym);
    }

    public Gym getGymById(Long gymId) {
        Gym gym = storage.getGymById(gymId);
        if (gym == null) {
            throw new NotFoundException("Gym not found with id: " + gymId);
        }
        return gym;
    }

    public List<Gym> getAllGyms() {
        return storage.getAllGyms();
    }

    public void deleteGym(Long gymId) {
        Gym gym = storage.getGymById(gymId);
        if (gym == null) {
            throw new NotFoundException("Gym not found with id: " + gymId);
        }
        storage.deleteGym(gymId);
    }
}