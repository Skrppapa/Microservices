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
                // исправлено — добавлены page и size
                linkTo(methodOn(MembershipController.class).getMembershipsByClientId(client.getId(), 0, 10)).withRel("memberships"),
                // исправлено — добавлены параметры для совпадения сигнатуры метода getAllClients
                linkTo(methodOn(ClientController.class).getAllClients(null, null, null, 0, 10)).withRel("collection")
        );
    }

    @Override
    public CollectionModel<EntityModel<ClientResponse>> toCollectionModel(Iterable<? extends ClientResponse> entities) {
        return RepresentationModelAssembler.super.toCollectionModel(entities)
                // тоже добавляем фиктивные аргументы
                .add(linkTo(methodOn(ClientController.class).getAllClients(null, null, null, 0, 10)).withSelfRel());
    }
}
