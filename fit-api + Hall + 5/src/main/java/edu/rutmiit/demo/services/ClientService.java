package edu.rutmiit.demo.services;

import edu.rutmiit.demo.fit_contract.dto.ClientRequest;
import edu.rutmiit.demo.fit_contract.dto.ClientResponse;
import edu.rutmiit.demo.fit_contract.dto.MembershipResponse;
import edu.rutmiit.demo.fit_contract.dto.PagedResponse;
import edu.rutmiit.demo.fit_contract.exeption.ResourceNotFoundException;
import edu.rutmiit.demo.storage.InMemoryStorage;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientService {
    private final InMemoryStorage storage;
    private final MembershipService membershipService;

    public ClientService(InMemoryStorage storage, @Lazy MembershipService membershipService) {
        this.storage = storage;
        this.membershipService = membershipService;
    }

    public PagedResponse<ClientResponse> findAll(String firstName, String lastName, String city, int page, int size) {
        List<ClientResponse> all = storage.clients.values().stream()
                .filter(c -> firstName == null || c.getFirstName().toLowerCase().contains(firstName.toLowerCase()))
                .filter(c -> lastName == null || c.getLastName().toLowerCase().contains(lastName.toLowerCase()))
                .filter(c -> city == null || (c.getCity() != null && c.getCity().toLowerCase().contains(city.toLowerCase())))
                .collect(Collectors.toList());

        int from = Math.max(0, page * size);
        int to = Math.min(all.size(), from + size);
        List<ClientResponse> pageContent = all.subList(from, to);
        int totalPages = (int) Math.ceil((double) all.size() / size);
        boolean last = page >= totalPages - 1;

        return new PagedResponse<>(pageContent, page, size, all.size(), totalPages, last);
    }

    public ClientResponse findById(Long id) {
        ClientResponse client = storage.clients.get(id);
        if (client == null) {
            throw new ResourceNotFoundException("Client", id);
        }
        return client;
    }

    public ClientResponse create(ClientRequest request) {
        Long id = storage.clientSequence.incrementAndGet();
        ClientResponse created = new ClientResponse(id,
                request.firstName(),
                request.lastName(),
                request.birthDate(),
                request.city()
        );
        storage.clients.put(id, created);
        return created;
    }

    public ClientResponse update(Long id, ClientRequest request) {
        findById(id); // Проверяем, что клиент существует
        ClientResponse existing = storage.clients.get(id);
        ClientResponse updatedClient = new ClientResponse(id,
                request.firstName() == null ? existing.getFirstName() : request.firstName(),
                request.lastName() == null ? existing.getLastName() : request.lastName(),
                request.birthDate() == null ? existing.getBirthDate() : request.birthDate(),
                request.city() == null ? existing.getCity() : request.city()
        );
        storage.clients.put(id, updatedClient);
        return updatedClient;
    }

    public void delete(Long id) {
        findById(id); // Проверяем, что клиент существует

        // Перед удалением клиента удаляем все связанные с ним memberships
        membershipService.deleteMembershipsByClientId(id);

        // Удаляем самого клиента
        storage.clients.remove(id);
    }

    // Дополнительные методы для связей
    public PagedResponse<ClientResponse> findClientsByGymId(Long gymId, int page, int size) {
        // Реализация поиска клиентов по gymId
        List<ClientResponse> all = storage.clients.values().stream()
                // Здесь должна быть логика фильтрации по gymId
                .collect(Collectors.toList());

        int from = Math.max(0, page * size);
        int to = Math.min(all.size(), from + size);
        List<ClientResponse> pageContent = all.subList(from, to);
        int totalPages = (int) Math.ceil((double) all.size() / size);
        boolean last = page >= totalPages - 1;

        return new PagedResponse<>(pageContent, page, size, all.size(), totalPages, last);
    }

    public PagedResponse<MembershipResponse> findMembershipsByClientId(Long clientId, int page, int size) {
        // Делегируем вызов membershipService
        return membershipService.findByClientId(clientId, page, size);
    }
}