package edu.rutmiit.demo.controllers;

import edu.rutmiit.demo.assemblers.ClientModelAssembler;
import edu.rutmiit.demo.fit_contract.dto.ClientRequest;
import edu.rutmiit.demo.fit_contract.dto.ClientResponse;
import edu.rutmiit.demo.fit_contract.dto.PagedResponse;
import edu.rutmiit.demo.fit_contract.dto.MembershipResponse;
import edu.rutmiit.demo.assemblers.MembershipModelAssembler;
import edu.rutmiit.demo.services.ClientService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/clients")
public class ClientController {

    private final ClientService service;
    private final ClientModelAssembler clientModelAssembler;
    private final PagedResourcesAssembler<ClientResponse> pagedResourcesAssembler;
    private final MembershipModelAssembler membershipModelAssembler;

    public ClientController(ClientService service,
                            ClientModelAssembler clientModelAssembler,
                            PagedResourcesAssembler<ClientResponse> pagedResourcesAssembler,
                            MembershipModelAssembler membershipModelAssembler) {
        this.service = service;
        this.clientModelAssembler = clientModelAssembler;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
        this.membershipModelAssembler = membershipModelAssembler;
    }

    @GetMapping
    public PagedModel<EntityModel<ClientResponse>> getAllClients(
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String city,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        PagedResponse<ClientResponse> pagedResponse = service.findAll(firstName, lastName, city, page, size);
        Page<ClientResponse> clientPage = new PageImpl<>(
                pagedResponse.content(),
                PageRequest.of(pagedResponse.pageNumber(), pagedResponse.pageSize()),
                pagedResponse.totalElements()
        );

        return pagedResourcesAssembler.toModel(clientPage, clientModelAssembler);
    }

    @GetMapping("/{id}")
    public EntityModel<ClientResponse> getClientById(@PathVariable Long id) {
        ClientResponse client = service.findById(id);
        return clientModelAssembler.toModel(client);
    }

    @PostMapping
    public ResponseEntity<EntityModel<ClientResponse>> createClient(@RequestBody ClientRequest request) {
        ClientResponse createdClient = service.create(request);
        EntityModel<ClientResponse> entityModel = clientModelAssembler.toModel(createdClient);

        return ResponseEntity
                .created(entityModel.getRequiredLink("self").toUri())
                .body(entityModel);
    }

    @PutMapping("/{id}")
    public EntityModel<ClientResponse> updateClient(@PathVariable Long id, @RequestBody ClientRequest request) {
        ClientResponse updatedClient = service.update(id, request);
        return clientModelAssembler.toModel(updatedClient);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Дополнительный метод для связи с memberships
    @GetMapping("/{clientId}/memberships")
    public PagedModel<EntityModel<MembershipResponse>> getMembershipsByClientId(
            @PathVariable Long clientId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PagedResponse<MembershipResponse> pagedResponse = service.findMembershipsByClientId(clientId, page, size);
        Page<MembershipResponse> membershipPage = new PageImpl<>(
                pagedResponse.content(),
                PageRequest.of(pagedResponse.pageNumber(), pagedResponse.pageSize()),
                pagedResponse.totalElements()
        );

        return pagedResourcesAssembler.toModel(membershipPage, membershipModelAssembler);
    }
}