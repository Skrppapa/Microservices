package com.myfit.api.storage;

import com.myfit.contract.model.User;
import com.myfit.contract.model.Gym;
import com.myfit.contract.model.Membership;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class InMemoryStorage {

    private final ConcurrentHashMap<Long, User> users = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Long, Gym> gyms = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Long, Membership> memberships = new ConcurrentHashMap<>();

    private final AtomicLong userIdCounter = new AtomicLong(1);
    private final AtomicLong gymIdCounter = new AtomicLong(1);
    private final AtomicLong membershipIdCounter = new AtomicLong(1);

    // User methods
    public User saveUser(User user) {
        if (user.getId() == null) {
            user.setId(userIdCounter.getAndIncrement());
        }
        users.put(user.getId(), user);
        return user;
    }

    public User getUserById(Long id) {
        return users.get(id);
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    public void deleteUser(Long id) {
        users.remove(id);
    }

    // Gym methods
    public Gym saveGym(Gym gym) {
        if (gym.getId() == null) {
            gym.setId(gymIdCounter.getAndIncrement());
        }
        gyms.put(gym.getId(), gym);
        return gym;
    }

    public Gym getGymById(Long id) {
        return gyms.get(id);
    }

    public List<Gym> getAllGyms() {
        return new ArrayList<>(gyms.values());
    }

    public void deleteGym(Long id) {
        gyms.remove(id);
    }

    // Membership methods
    public Membership saveMembership(Membership membership) {
        if (membership.getId() == null) {
            membership.setId(membershipIdCounter.getAndIncrement());
        }
        memberships.put(membership.getId(), membership);
        return membership;
    }

    public Membership getMembershipById(Long id) {
        return memberships.get(id);
    }

    public List<Membership> getAllMemberships() {
        return new ArrayList<>(memberships.values());
    }

    public List<Membership> getMembershipsByUserId(Long userId) {
        return memberships.values().stream()
                .filter(membership -> membership.getUserId().equals(userId))
                .toList();
    }

    public List<Membership> getMembershipsByGymId(Long gymId) {
        return memberships.values().stream()
                .filter(membership -> membership.getGymId().equals(gymId))
                .toList();
    }

    public void deleteMembership(Long id) {
        memberships.remove(id);
    }
}