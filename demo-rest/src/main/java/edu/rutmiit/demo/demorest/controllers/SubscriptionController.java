package edu.rutmiit.demo.demorest.controllers;

import edu.rutmiit.demo.fitapicontract.dto.PagedResponse;
import edu.rutmiit.demo.fitapicontract.dto.SubscriptionRequest;
import edu.rutmiit.demo.fitapicontract.dto.SubscriptionResponse;
import edu.rutmiit.demo.fitapicontract.dto.UpdateSubscriptionRequest;
import edu.rutmiit.demo.fitapicontract.endpoints.SubscriptionApi;
import edu.rutmiit.demo.demorest.service.SubscriptionService;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class SubscriptionController implements SubscriptionApi {

    private final SubscriptionService subscriptionService;

    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @Override
    public SubscriptionResponse getSubscriptionById(Long id) {
        return subscriptionService.findSubscriptionById(id);
    }

    @Override
    public PagedResponse<SubscriptionResponse> getAllSubscriptions(Long clientId, int page, int size) {
        return subscriptionService.findAllSubscriptions(clientId, page, size);
    }

    public SubscriptionResponse createSubscription(SubscriptionRequest request) {
        return subscriptionService.createSubscription(request);
    }

    @Override
    public SubscriptionResponse updateSubscription(Long id, UpdateSubscriptionRequest request) {
        return subscriptionService.updateSubscription(id, request);
    }

    @Override
    public void deleteSubscription(Long id) {
        subscriptionService.deleteSubscription(id);
    }
 }
