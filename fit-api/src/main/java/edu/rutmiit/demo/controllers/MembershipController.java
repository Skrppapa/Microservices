package edu.rutmiit.demo.controllers;

import edu.rutmiit.demo.fit_contract.dto.MembershipRequest;
import edu.rutmiit.demo.fit_contract.dto.MembershipResponse;
import edu.rutmiit.demo.fit_contract.dto.PagedResponse;
import edu.rutmiit.demo.fit_contract.dto.StatusResponse;
import edu.rutmiit.demo.services.MembershipService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/memberships")
public class MembershipController {

    private final MembershipService service;

    public MembershipController(MembershipService service) {
        this.service = service;
    }

    @GetMapping
    public PagedResponse<MembershipResponse> getAllMemberships(
            @RequestParam(required = false) String membershipNumber,
            @RequestParam(required = false) String plan,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return service.findAll(membershipNumber, plan, page, size);
    }

    @GetMapping("/{id}")
    public MembershipResponse getMembershipById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    public ResponseEntity<MembershipResponse> createMembership(@RequestBody MembershipRequest request) {
        MembershipResponse created = service.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public MembershipResponse updateMembership(@PathVariable Long id, @RequestBody MembershipRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    public StatusResponse deleteMembership(@PathVariable Long id) {
        service.delete(id);
        return new StatusResponse("ok", "Membership deleted");
    }
}
