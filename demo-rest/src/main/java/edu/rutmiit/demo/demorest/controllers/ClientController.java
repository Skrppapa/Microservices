package edu.rutmiit.demo.demorest.controllers;

import edu.rutmiit.demo.fitapicontract.dto.ClientRequest;
import edu.rutmiit.demo.fitapicontract.dto.ClientResponse;
import edu.rutmiit.demo.demorest.service.ClientService;
import edu.rutmiit.demo.fitapicontract.endpoints.СlientApi;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ClientController implements СlientApi {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    public List<ClientResponse> getAllClients() {
        return clientService.findAll();
    }

    public ClientResponse getClientById(Long id) {
        return clientService.findById(id);
    }

    public ClientResponse createClient(ClientRequest request) {
        return clientService.create(request);
    }

    public ClientResponse updateClient(Long id, ClientRequest request) {
        return clientService.update(id, request);
    }

    public void deleteClient(Long id) {
        clientService.delete(id);
    }
}
