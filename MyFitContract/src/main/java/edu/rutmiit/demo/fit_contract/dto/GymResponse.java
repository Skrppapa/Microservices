package edu.rutmiit.demo.fit_contract.dto;

import java.util.List;

public record GymResponse(Long id, String address, Integer capacity, List<String> amenities) {}