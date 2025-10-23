package edu.rutmiit.demo.assemblers;

import edu.rutmiit.demo.controllers.GymController;
import edu.rutmiit.demo.fit_contract.dto.GymResponse;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class GymModelAssembler implements RepresentationModelAssembler<GymResponse, EntityModel<GymResponse>> {

    @Override
    public EntityModel<GymResponse> toModel(GymResponse gym) {
        return EntityModel.of(gym,
                linkTo(methodOn(GymController.class).getGymById(gym.getId())).withSelfRel(),
                linkTo(methodOn(GymController.class).getClientsByGymId(gym.getId(), 0, 10)).withRel("clients"),
                // передаём параметры, чтобы соответствовать сигнатуре getAllGyms(...)
                linkTo(methodOn(GymController.class).getAllGyms(null, null, 0, 10)).withRel("collection")
        );
    }

    @Override
    public CollectionModel<EntityModel<GymResponse>> toCollectionModel(Iterable<? extends GymResponse> entities) {
        return RepresentationModelAssembler.super.toCollectionModel(entities)
                // аналогично здесь
                .add(linkTo(methodOn(GymController.class).getAllGyms(null, null, 0, 10)).withSelfRel());
    }
}
