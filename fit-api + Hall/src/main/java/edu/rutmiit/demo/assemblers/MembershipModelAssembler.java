package edu.rutmiit.demo.assemblers;

import edu.rutmiit.demo.controllers.ClientController;
import edu.rutmiit.demo.controllers.GymController;
import edu.rutmiit.demo.controllers.MembershipController;
import edu.rutmiit.demo.fit_contract.dto.MembershipResponse;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class MembershipModelAssembler implements RepresentationModelAssembler<MembershipResponse, EntityModel<MembershipResponse>> {

    @Override
    public EntityModel<MembershipResponse> toModel(MembershipResponse membership) {
        return EntityModel.of(membership,
                linkTo(methodOn(MembershipController.class).getMembershipById(membership.getId())).withSelfRel(),
                linkTo(methodOn(ClientController.class).getClientById(membership.getId())).withRel("client"),
                linkTo(methodOn(GymController.class).getGymById(membership.getId())).withRel("gym"),
                linkTo(methodOn(MembershipController.class).getAllMemberships()).withRel("collection")
        );
    }
}