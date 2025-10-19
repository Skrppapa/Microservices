package com.myfit.api.controller;

import com.myfit.contract.api.MembershipsApi;
import com.myfit.contract.model.Membership;
import com.myfit.contract.model.MembershipCreateRequest;
import com.myfit.api.service.MembershipService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MembershipController implements MembershipsApi {

    private final MembershipService membershipService;

    public MembershipController(MembershipService membershipService) {
        this.membershipService = membershipService;
    }

    @Override
    public ResponseEntity<Membership> createMembership(MembershipCreateRequest membershipCreateRequest) {
        Membership membership = membershipService.createMembership(membershipCreateRequest);
        return ResponseEntity.ok(membership);
    }

    @Override
    public ResponseEntity<Membership> getMembershipById(Long membershipId) {
        Membership membership = membershipService.getMembershipById(membershipId);
        return ResponseEntity.ok(membership);
    }

    @Override
    public ResponseEntity<List<Membership>> getMembershipsByUser(Long userId) {
        List<Membership> memberships = membershipService.getMembershipsByUser(userId);
        return ResponseEntity.ok(memberships);
    }

    @Override
    public ResponseEntity<List<Membership>> getMembershipsByGym(Long gymId) {
        List<Membership> memberships = membershipService.getMembershipsByGym(gymId);
        return ResponseEntity.ok(memberships);
    }

    @Override
    public ResponseEntity<Void> deleteMembership(Long membershipId) {
        membershipService.deleteMembership(membershipId);
        return ResponseEntity.ok().build();
    }
}