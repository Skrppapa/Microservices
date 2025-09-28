package edu.rutmiit.demo.demorest.storage;

import edu.rutmiit.demo.fitapicontract.dto.ClientResponse;
import edu.rutmiit.demo.fitapicontract.dto.SubscriptionResponse;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class InMemoryStorage {
    public final Map<Long, ClientResponse> clients = new ConcurrentHashMap<>();
    public final Map<Long, SubscriptionResponse> subscriptions = new ConcurrentHashMap<>();

    public final AtomicLong clientSequence = new AtomicLong(0);
    public final AtomicLong subscriptionSequence = new AtomicLong(0);

    @PostConstruct
    public void init() {
        // Создаем несколько клиентов
        ClientResponse client1 = new ClientResponse(clientSequence.incrementAndGet(), "Иван", "Иванов");
        ClientResponse client2 = new ClientResponse(clientSequence.incrementAndGet(), "Федор", "Федоров");
        ClientResponse client3 = new ClientResponse(clientSequence.incrementAndGet(), "Петр", "Петров");
        clients.put(client1.id(), client1);
        clients.put(client2.id(), client2);
        clients.put(client3.id(), client3);

        // Создаем несколько книг
        long subscriptionId1 = subscriptionSequence.incrementAndGet();
        subscriptions.put(subscriptionId1, new SubscriptionResponse(subscriptionId1, "12 месяцев", "978-5-389-06259-8", client1, LocalDateTime.now()));

        long subscriptionId2 = subscriptionSequence.incrementAndGet();
        subscriptions.put(subscriptionId2, new SubscriptionResponse(subscriptionId2, "6 месяцев", "978-5-389-06259-9", client2, LocalDateTime.now()));

        long subscriptionId3 = subscriptionSequence.incrementAndGet();
        subscriptions.put(subscriptionId3, new SubscriptionResponse(subscriptionId3, "3 месяца", "978-5-389-06259-7", client3, LocalDateTime.now()));
    }
}