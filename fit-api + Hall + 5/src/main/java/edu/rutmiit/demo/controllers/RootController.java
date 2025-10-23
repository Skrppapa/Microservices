package edu.rutmiit.demo.controllers;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api")
public class RootController {

    @GetMapping
    public RepresentationModel<?> getRoot() {
        RepresentationModel<?> rootModel = new RepresentationModel<>();
        rootModel.add(
                linkTo(methodOn(ClientController.class).getAllClients(null, null, null, 0, 10)).withRel("clients"),
                linkTo(methodOn(GymController.class).getAllGyms(null, null, 0, 10)).withRel("gyms"),
                linkTo(methodOn(MembershipController.class).getAllMemberships(null, null, 0, 10)).withRel("memberships")
        );
        return rootModel;
    }
}
