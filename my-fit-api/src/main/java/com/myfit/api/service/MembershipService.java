package com.myfit.api.service;

import com.myfit.api.storage.InMemoryStorage;
import com.myfit.api.exception.NotFoundException;
import com.myfit.api.exception.ValidationException;
import com.myfit.contract.model.Membership;
import com.myfit.contract.model.MembershipCreateRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MembershipService {

    private final InMemoryStorage storage;
    private final UserService userService;
    private final GymService gymService;

    public MembershipService(InMemoryStorage storage, UserService userService, GymService gymService) {
        this.storage = storage;
        this.userService = userService;
        this.gymService = gymService;
    }

    public Membership createMembership(MembershipCreateRequest request) {
        // Validate user exists
        userService.getUserById(request.getUserId());

        // Validate gym exists
        gymService.getGymById(request.getGymId());

        if (request.getStartDate() == null) {
            throw new ValidationException("Membership start date cannot be empty");
        }
        if (request.getEndDate() == null) {
            throw new ValidationException("Membership end date cannot be empty");
        }
        if (request.getStartDate().isAfter(request.getEndDate())) {
            throw new ValidationException("Start date cannot be after end date");
        }

        Membership membership = new Membership();
        membership.setUserId(request.getUserId());
        membership.setGymId(request.getGymId());
        membership.setStartDate(request.getStartDate());
        membership.setEndDate(request.getEndDate());
        membership.setMembershipType(request.getMembershipType());
        membership.setPrice(request.getPrice());

        return storage.saveMembership(membership);
    }

    public Membership getMembershipById(Long membershipId) {
        Membership membership = storage.getMembershipById(membershipId);
        if (membership == null) {
            throw new NotFoundException("Membership not found with id: " + membershipId);
        }
        return membership;
    }

    public List<Membership> getMembershipsByUser(Long userId) {
        // Validate user exists
        userService.getUserById(userId);
        return storage.getMembershipsByUserId(userId);
    }

    public List<Membership> getMembershipsByGym(Long gymId) {
        // Validate gym exists
        gymService.getGymById(gymId);
        return storage.getMembershipsByGymId(gymId);
    }

    public void deleteMembership(Long membershipId) {
        Membership membership = storage.getMembershipById(membershipId);
        if (membership == null) {
            throw new NotFoundException("Membership not found with id: " + membershipId);
        }
        storage.deleteMembership(membershipId);
    }
}