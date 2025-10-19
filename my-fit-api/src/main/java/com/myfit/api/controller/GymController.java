package com.myfit.api.controller;

import edu.rutmiit.demo.fit_contract.dto.GymRequest;
import com.myfit.contract.model.GymCreateRequest;
import com.myfit.api.service.GymService;
import edu.rutmiit.demo.fit_contract.endpoints.GymApi;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GymController implements GymApi {

    private final GymService gymService;

    public GymController(GymService gymService) {
        this.gymService = gymService;
    }

    @Override
    public ResponseEntity<Gym> createGym(GymCreateRequest gymCreateRequest) {
        Gym gym = gymService.createGym(gymCreateRequest);
        return ResponseEntity.ok(gym);
    }

    @Override
    public ResponseEntity<Gym> getGymById(Long gymId) {
        Gym gym = gymService.getGymById(gymId);
        return ResponseEntity.ok(gym);
    }

    @Override
    public ResponseEntity<List<Gym>> getGyms() {
        List<Gym> gyms = gymService.getAllGyms();
        return ResponseEntity.ok(gyms);
    }

    @Override
    public ResponseEntity<Void> deleteGym(Long gymId) {
        gymService.deleteGym(gymId);
        return ResponseEntity.ok().build();
    }
}