package edu.rutmiit.demo.demorest.service;

import edu.rutmiit.demo.fitapicontract.dto.ClientRequest;
import edu.rutmiit.demo.fitapicontract.dto.ClientResponse;
import edu.rutmiit.demo.fitapicontract.exception.ResourceNotFoundException;
import edu.rutmiit.demo.demorest.storage.InMemoryStorage;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientService {
    private final InMemoryStorage storage;
    private final SubscriptionService subscriptionService;

    public ClientService(InMemoryStorage storage, @Lazy SubscriptionService subscriptionService) {
        this.storage = storage;
        this.subscriptionService = subscriptionService;
    }

    public List<ClientResponse> findAll() {
        return storage.clients.values().stream().toList();
    }

    public ClientResponse findById(Long id) {
        return Optional.ofNullable(storage.clients.get(id))
                .orElseThrow(() -> new ResourceNotFoundException("Client", id));
    }

    public ClientResponse create(ClientRequest request) {
        long id = storage.clientSequence.incrementAndGet();
        ClientResponse client = new ClientResponse(id, request.firstName(), request.lastName());
        storage.clients.put(id, client);
        return client;
    }

    public ClientResponse update(Long id, ClientRequest request) {
        findById(id); // Проверяем, что клиент существует
        ClientResponse updatedClient = new ClientResponse(id, request.firstName(), request.lastName());
        storage.clients.put(id, updatedClient);
        return updatedClient;
    }

    public void delete(Long id) {
        findById(id); // Проверяем, что клиент существует

        // Перед удалением клиента удаляем все связанные с ним книги
        subscriptionService.deleteSubscriptionsByClientId(id);

        // Удаляем самого клиента
        storage.clients.remove(id);
    }
}
