package edu.rutmiit.demo.controllers;

import edu.rutmiit.demo.fit_contract.dto.GymRequest;
import edu.rutmiit.demo.fit_contract.dto.GymResponse;
import edu.rutmiit.demo.fit_contract.dto.PagedResponse;
import edu.rutmiit.demo.fit_contract.dto.StatusResponse;
import edu.rutmiit.demo.services.GymService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/gyms")
public class GymController {

    private final GymService service;

    public GymController(GymService service) {
        this.service = service;
    }

    @GetMapping
    public PagedResponse<GymResponse> getAllGyms(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) Integer capacity,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return service.findAll(query, capacity, page, size);
    }

    @GetMapping("/{id}")
    public GymResponse getGymById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    public ResponseEntity<GymResponse> createGym(@RequestBody GymRequest request) {
        GymResponse created = service.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public GymResponse updateGym(@PathVariable Long id, @RequestBody GymRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    public StatusResponse deleteGym(@PathVariable Long id) {
        service.delete(id);
        return new StatusResponse("ok", "Gym deleted");
    }
}
