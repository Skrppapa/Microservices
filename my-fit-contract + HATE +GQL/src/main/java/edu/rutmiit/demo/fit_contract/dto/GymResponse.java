package edu.rutmiit.demo.fit_contract.dto;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;
import java.util.List;
import java.util.Objects;

@Relation(collectionRelation = "gyms", itemRelation = "gym")
public class GymResponse extends RepresentationModel<GymResponse> {

    private final Long id;
    private final String address;
    private final Integer capacity;
    private final List<String> amenities;

    public GymResponse(Long id, String address, Integer capacity, List<String> amenities) {
        this.id = id;
        this.address = address;
        this.capacity = capacity;
        this.amenities = amenities;
    }

    public Long getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public List<String> getAmenities() {
        return amenities;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        GymResponse that = (GymResponse) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(address, that.address) &&
                Objects.equals(capacity, that.capacity) &&
                Objects.equals(amenities, that.amenities);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, address, capacity, amenities);
    }
}