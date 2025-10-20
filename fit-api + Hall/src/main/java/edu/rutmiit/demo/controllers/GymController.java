package edu.rutmiit.demo.controllers;

import edu.rutmiit.demo.assemblers.GymModelAssembler;
import edu.rutmiit.demo.fit_contract.dto.GymRequest;
import edu.rutmiit.demo.fit_contract.dto.GymResponse;
import edu.rutmiit.demo.fit_contract.dto.PagedResponse;
import edu.rutmiit.demo.fit_contract.dto.ClientResponse;
import edu.rutmiit.demo.assemblers.ClientModelAssembler;
import edu.rutmiit.demo.services.GymService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/gyms")
public class GymController {

    private final GymService service;
    private final GymModelAssembler gymModelAssembler;
    private final PagedResourcesAssembler<GymResponse> pagedResourcesAssembler;
    private final ClientModelAssembler clientModelAssembler;
    private final PagedResourcesAssembler<ClientResponse> clientPagedResourcesAssembler;

    public GymController(GymService service,
                         GymModelAssembler gymModelAssembler,
                         PagedResourcesAssembler<GymResponse> pagedResourcesAssembler,
                         ClientModelAssembler clientModelAssembler,
                         PagedResourcesAssembler<ClientResponse> clientPagedResourcesAssembler) {
        this.service = service;
        this.gymModelAssembler = gymModelAssembler;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
        this.clientModelAssembler = clientModelAssembler;
        this.clientPagedResourcesAssembler = clientPagedResourcesAssembler;
    }

    @GetMapping
    public PagedModel<EntityModel<GymResponse>> getAllGyms(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) Integer capacity,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        PagedResponse<GymResponse> pagedResponse = service.findAll(query, capacity, page, size);
        Page<GymResponse> gymPage = new PageImpl<>(
                pagedResponse.content(),
                PageRequest.of(pagedResponse.pageNumber(), pagedResponse.pageSize()),
                pagedResponse.totalElements()
        );

        return pagedResourcesAssembler.toModel(gymPage, gymModelAssembler);
    }

    @GetMapping("/{id}")
    public EntityModel<GymResponse> getGymById(@PathVariable Long id) {
        GymResponse gym = service.findById(id);
        return gymModelAssembler.toModel(gym);
    }

    @PostMapping
    public ResponseEntity<EntityModel<GymResponse>> createGym(@RequestBody GymRequest request) {
        GymResponse createdGym = service.create(request);
        EntityModel<GymResponse> entityModel = gymModelAssembler.toModel(createdGym);

        return ResponseEntity
                .created(entityModel.getRequiredLink("self").toUri())
                .body(entityModel);
    }

    @PutMapping("/{id}")
    public EntityModel<GymResponse> updateGym(@PathVariable Long id, @RequestBody GymRequest request) {
        GymResponse updatedGym = service.update(id, request);
        return gymModelAssembler.toModel(updatedGym);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGym(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Дополнительный метод для связи с clients
    @GetMapping("/{gymId}/clients")
    public PagedModel<EntityModel<ClientResponse>> getClientsByGymId(
            @PathVariable Long gymId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        PagedResponse<ClientResponse> pagedResponse = service.findClientsByGymId(gymId, page, size);
        Page<ClientResponse> clientPage = new PageImpl<>(
                pagedResponse.content(),
                PageRequest.of(pagedResponse.pageNumber(), pagedResponse.pageSize()),
                pagedResponse.totalElements()
        );

        return clientPagedResourcesAssembler.toModel(clientPage, clientModelAssembler);
    }
}