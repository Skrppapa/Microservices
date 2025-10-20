package edu.rutmiit.demo.assemblers;

import edu.rutmiit.demo.controllers.ClientController;
import edu.rutmiit.demo.controllers.MembershipController;
import edu.rutmiit.demo.fit_contract.dto.ClientResponse;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class ClientModelAssembler implements RepresentationModelAssembler<ClientResponse, EntityModel<ClientResponse>> {

    @Override
    public EntityModel<ClientResponse> toModel(ClientResponse client) {
        return EntityModel.of(client,
                linkTo(methodOn(ClientController.class).getClientById(client.getId())).withSelfRel(),
                linkTo(methodOn(MembershipController.class).getMembershipsByClientId(client.getId())).withRel("memberships"),
                linkTo(methodOn(ClientController.class).getAllClients()).withRel("collection")
        );
    }

    @Override
    public CollectionModel<EntityModel<ClientResponse>> toCollectionModel(Iterable<? extends ClientResponse> entities) {
        return RepresentationModelAssembler.super.toCollectionModel(entities)
                .add(linkTo(methodOn(ClientController.class).getAllClients()).withSelfRel());
    }
}