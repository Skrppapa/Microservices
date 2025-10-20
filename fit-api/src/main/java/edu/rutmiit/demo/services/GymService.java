package edu.rutmiit.demo.services;

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
                .filter(g -> query == null || (g.address() != null && g.address().toLowerCase().contains(query.toLowerCase())))
                .filter(g -> capacity == null || g.capacity() >= capacity)
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
            throw new ResourceNotFoundException("Gym not found with id: " + id, null);
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
        GymResponse existing = findById(id);
        GymResponse updated = new GymResponse(
                id,
                request.address() == null ? existing.address() : request.address(),
                request.capacity() == null ? existing.capacity() : request.capacity(),
                request.amenities() == null ? existing.amenities() : request.amenities()
        );
        storage.gyms.put(id, updated);
        return updated;
    }

    public void delete(Long id) {
        findById(id);
        // Note: MembershipResponse in contract does not reference gymId, so we do not remove memberships here.
        storage.gyms.remove(id);
    }
}
