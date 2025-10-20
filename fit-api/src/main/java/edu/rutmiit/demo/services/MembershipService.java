package edu.rutmiit.demo.services;

import edu.rutmiit.demo.fit_contract.dto.MembershipRequest;
import edu.rutmiit.demo.fit_contract.dto.MembershipResponse;
import edu.rutmiit.demo.fit_contract.dto.PagedResponse;
import edu.rutmiit.demo.fit_contract.exeption.ResourceNotFoundException;
import edu.rutmiit.demo.storage.InMemoryStorage;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MembershipService {
    private final InMemoryStorage storage;

    public MembershipService(InMemoryStorage storage) {
        this.storage = storage;
    }

    public PagedResponse<MembershipResponse> findAll(String membershipNumber, String level, int page, int size) {
        List<MembershipResponse> all = storage.memberships.values().stream()
                .filter(m -> membershipNumber == null || (m.membershipNumber() != null && m.membershipNumber().contains(membershipNumber)))
                .filter(m -> level == null || (m.level() != null && m.level().toLowerCase().contains(level.toLowerCase())))
                .collect(Collectors.toList());

        int from = Math.max(0, page * size);
        int to = Math.min(all.size(), from + size);
        List<MembershipResponse> pageContent = all.subList(from, to);
        int totalPages = (int) Math.ceil((double) all.size() / size);
        boolean last = page >= totalPages - 1;

        return new PagedResponse<>(pageContent, page, size, all.size(), totalPages, last);
    }

    public MembershipResponse findById(Long id) {
        MembershipResponse m = storage.memberships.get(id);
        if (m == null) {
            throw new ResourceNotFoundException("Membership not found with id: " + id, null);
        }
        return m;
    }

    public MembershipResponse create(MembershipRequest request) {
        Long id = storage.membershipSequence.incrementAndGet();
        MembershipResponse created = new MembershipResponse(id,
                request.membershipNumber(),
                request.duration(),
                request.level()
        );
        storage.memberships.put(id, created);
        return created;
    }

    public MembershipResponse update(Long id, MembershipRequest request) {
        MembershipResponse existing = findById(id);
        MembershipResponse updated = new MembershipResponse(
                id,
                request.membershipNumber() == null ? existing.membershipNumber() : request.membershipNumber(),
                request.duration() == null ? existing.duration() : request.duration(),
                request.level() == null ? existing.level() : request.level()
        );
        storage.memberships.put(id, updated);
        return updated;
    }

    public void delete(Long id) {
        findById(id);
        storage.memberships.remove(id);
    }
}
