package edu.rutmiit.demo.storage;

import edu.rutmiit.demo.fit_contract.dto.ClientRequest;
import edu.rutmiit.demo.fit_contract.dto.ClientResponse;
import edu.rutmiit.demo.fit_contract.dto.GymRequest;
import edu.rutmiit.demo.fit_contract.dto.GymResponse;
import edu.rutmiit.demo.fit_contract.dto.MembershipRequest;
import edu.rutmiit.demo.fit_contract.dto.MembershipResponse;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class InMemoryStorage {

    public final Map<Long, ClientResponse> clients = new ConcurrentHashMap<>();
    public final Map<Long, GymResponse> gyms = new ConcurrentHashMap<>();
    public final Map<Long, MembershipResponse> memberships = new ConcurrentHashMap<>();

    public final AtomicLong clientSequence = new AtomicLong(0);
    public final AtomicLong gymSequence = new AtomicLong(0);
    public final AtomicLong membershipSequence = new AtomicLong(0);

    @PostConstruct
    public void init() {
        // Создаем несколько клиентов
        ClientResponse client1 = new ClientResponse(
                clientSequence.incrementAndGet(),
                "Иван",
                "Иванов",
                LocalDate.of(1990, 5, 15),
                "Москва"
        );

        ClientResponse client2 = new ClientResponse(
                clientSequence.incrementAndGet(),
                "Федор",
                "Федоров",
                LocalDate.of(1985, 8, 22),
                "Санкт-Петербург"
        );

        ClientResponse client3 = new ClientResponse(
                clientSequence.incrementAndGet(),
                "Петр",
                "Петров",
                LocalDate.of(1992, 3, 10),
                "Казань"
        );

        clients.put(client1.id(), client1);
        clients.put(client2.id(), client2);
        clients.put(client3.id(), client3);

        // Создаем несколько тренажерных залов
        GymResponse gym1 = new GymResponse(
                gymSequence.incrementAndGet(),
                "ул. Центральная, 1",
                100,
                List.of("бассейн", "сауна", "тренажерный зал", "групповые занятия")
        );

        GymResponse gym2 = new GymResponse(
                gymSequence.incrementAndGet(),
                "ул. Спортивная, 15",
                50,
                List.of("тренажерный зал", "кардио-зона", "йога")
        );

        GymResponse gym3 = new GymResponse(
                gymSequence.incrementAndGet(),
                "пр. Молодежный, 7",
                75,
                List.of("тренажерный зал", "пилатес", "сауна")
        );

        gyms.put(gym1.id(), gym1);
        gyms.put(gym2.id(), gym2);
        gyms.put(gym3.id(), gym3);

        // Создаем несколько абонементов
        MembershipResponse membership1 = new MembershipResponse(
                membershipSequence.incrementAndGet(),
                "1234567890",
                "12 месяцев",
                "Премиум"
        );

        MembershipResponse membership2 = new MembershipResponse(
                membershipSequence.incrementAndGet(),
                "0987654321",
                "6 месяцев",
                "Стандарт"
        );

        MembershipResponse membership3 = new MembershipResponse(
                membershipSequence.incrementAndGet(),
                "1122334455",
                "3 месяца",
                "Базовый"
        );

        memberships.put(membership1.id(), membership1);
        memberships.put(membership2.id(), membership2);
        memberships.put(membership3.id(), membership3);
    }
}
