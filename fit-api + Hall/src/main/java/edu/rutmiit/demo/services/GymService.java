package edu.rutmiit.demo.services;

import edu.rutmiit.demo.fit_contract.dto.ClientResponse;
import edu.rutmiit.demo.fit_contract.dto.GymRequest;
import edu.rutmiit.demo.fit_contract.dto.GymResponse;
import edu.rutmiit.demo.fit_contract.dto.PagedResponse;
import edu.rutmiit.demo.fit_contract.exeption.ResourceNotFoundException;
import edu.rutmiit.demo.storage.InMemoryStorage;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GymService {
    private final InMemoryStorage storage;

    public GymService(InMemoryStorage storage) {
        this.storage = storage;
    }

    public PagedResponse<GymResponse> findAll(String query, Integer capacity, int page, int size) {
        List<GymResponse> all = storage.gyms.values().stream()
                .filter(g -> query == null || (g.getAddress() != null && g.getAddress().toLowerCase().contains(query.toLowerCase())))
                .filter(g -> capacity == null || g.getCapacity() >= capacity)
                .collect(Collectors.toList());

        int from = Math.max(0, page * size);
        int to = Math.min(all.size(), from + size);
        List<GymResponse> pageContent = all.subList(from, to);
        int totalPages = (int) Math.ceil((double) all.size() / size);
        boolean last = page >= totalPages - 1;

        return new PagedResponse<>(pageContent, page, size, all.size(), totalPages, last);
    }

    public GymResponse findById(Long id) {
        GymResponse gym = storage.gyms.get(id);
        if (gym == null) {
            throw new ResourceNotFoundException("Gym", id);
        }
        return gym;
    }

    public GymResponse create(GymRequest request) {
        Long id = storage.gymSequence.incrementAndGet();
        GymResponse created = new GymResponse(id,
                request.address(),
                request.capacity(),
                request.amenities()
        );
        storage.gyms.put(id, created);
        return created;
    }

    public GymResponse update(Long id, GymRequest request) {
        findById(id); // Проверяем, что зал существует
        GymResponse existing = storage.gyms.get(id);
        GymResponse updated = new GymResponse(
                id,
                request.address() == null ? existing.getAddress() : request.address(),
                request.capacity() == null ? existing.getCapacity() : request.capacity(),
                request.amenities() == null ? existing.getAmenities() : request.amenities()
        );
        storage.gyms.put(id, updated);
        return updated;
    }

    public void delete(Long id) {
        findById(id); // Проверяем, что зал существует
        storage.gyms.remove(id);
    }

    // Дополнительный метод для связей
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
}