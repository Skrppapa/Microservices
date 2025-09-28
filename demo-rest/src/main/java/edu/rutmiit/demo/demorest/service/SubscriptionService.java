package edu.rutmiit.demo.demorest.service;

import edu.rutmiit.demo.fitapicontract.dto.*;
import edu.rutmiit.demo.fitapicontract.exception.CodeAlreadyExistsException;
import edu.rutmiit.demo.fitapicontract.exception.ResourceNotFoundException;
import edu.rutmiit.demo.demorest.storage.InMemoryStorage;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class SubscriptionService {

    private final InMemoryStorage storage;
    private final ClientService clientService;

    public SubscriptionService(InMemoryStorage storage, @Lazy ClientService clientService) {
        this.storage = storage;
        this.clientService = clientService;
    }

    public SubscriptionResponse findSubscriptionById(Long id) {
        return Optional.ofNullable(storage.subscriptions.get(id))
                .orElseThrow(() -> new ResourceNotFoundException("Subscription", id));
    }

    public PagedResponse<SubscriptionResponse> findAllSubscriptions(Long clientId, int page, int size) {
        // Получаем стрим всех абонементов
        Stream<SubscriptionResponse> subscriptionsStream = storage.subscriptions.values().stream()
                .sorted((b1, b2) -> b1.id().compareTo(b2.id())); // Сортируем для консистентности

        // Фильтруем, если указан clientId
        if (clientId != null) {
            subscriptionsStream = subscriptionsStream.filter(subscription -> subscription.client() != null && subscription.client().id().equals(clientId));
        }

        List<SubscriptionResponse> allSubscriptions = subscriptionsStream.toList();

        // Выполняем пагинацию
        int totalElements = allSubscriptions.size();
        int totalPages = (int) Math.ceil((double) totalElements / size);
        int fromIndex = page * size;
        int toIndex = Math.min(fromIndex + size, totalElements);

        List<SubscriptionResponse> pageContent = (fromIndex > toIndex) ? List.of() : allSubscriptions.subList(fromIndex, toIndex);

        return new PagedResponse<>(pageContent, page, size, totalElements, totalPages, page >= totalPages - 1);
    }

    public SubscriptionResponse createSubscription(SubscriptionRequest request) {
        // Проверка на существующий Code
        validateCode(request.code(), null);

        // Находим клиента, если не найден - будет исключение
        ClientResponse client = clientService.findById(request.clientId());

        long id = storage.subscriptionSequence.incrementAndGet();
        var subscription = new SubscriptionResponse(
                id,
                request.length(),
                request.code(),
                client,
                LocalDateTime.now()
        );
        storage.subscriptions.put(id, subscription);
        return subscription;
    }

    public SubscriptionResponse updateSubscription(Long id, UpdateSubscriptionRequest request) {
        SubscriptionResponse existingSubscription = findSubscriptionById(id); // Проверяем, что абонемент существует
        validateCode(request.code(), id); // Проверяем Code, исключая текущий абонемент

        var updatedSubscription = new SubscriptionResponse(
                id,
                request.length(),
                request.code(),
                existingSubscription.client(), // Клиент не меняется
                existingSubscription.createdAt() // Дата создания не меняется
        );
        storage.subscriptions.put(id, updatedSubscription);
        return updatedSubscription;
    }

    public void deleteSubscription(Long id) {
        findSubscriptionById(id); // Проверяем, что абонемент существует
        storage.subscriptions.remove(id);
    }

    public void deleteSubscriptionsByClientId(Long clientId) {
        // Находим ID всех абонементов, которые нужно удалить
        List<Long> subscriptionResponseIdsToDelete = storage.subscriptions.values().stream()
                .filter(subscription -> subscription.client() != null && subscription.client().id().equals(clientId))
                .map(SubscriptionResponse::id)
                .toList();

        // Удаляем их из хранилища
        subscriptionResponseIdsToDelete.forEach(storage.subscriptions::remove);
    }

    private void validateCode(String code, Long currentSubscriptionId) {
        storage.subscriptions.values().stream()
                .filter(subscription -> subscription.code().equalsIgnoreCase(code))
                .filter(subscription -> !subscription.id().equals(currentSubscriptionId)) // Игнорируем абонемент, который обновляем
                .findAny()
                .ifPresent(subscription -> {
                    throw new CodeAlreadyExistsException(code);
                });
    }
}
