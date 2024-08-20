package com.icare.mapper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.icare.dto.jsonB.Location;
import com.icare.dto.jsonB.Measurement;
import com.icare.dto.response.DeliveryDetailsResponse;
import com.icare.dto.response.DeliveryLegResponse;
import com.icare.enums.DeliveryMethod;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface DeliveryMapper {
    DeliveryMapper INSTANCE = Mappers.getMapper(DeliveryMapper.class);

    DeliveryDetailsResponse legToDetails(DeliveryLegResponse leg, DeliveryMethod method, Double amount);

    static DeliveryLegResponse extractLegFromResponse(String response) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(response);

            if (!rootNode.has("routes") || rootNode.path("routes").isEmpty()) {
                throw new IllegalArgumentException("No routes found in the response");
            }

            JsonNode routesNode = rootNode.path("routes").get(0);

            if (!routesNode.has("legs") || routesNode.path("legs").isEmpty()) {
                throw new IllegalArgumentException("No legs found in the route");
            }

            JsonNode legNode = routesNode.path("legs").get(0);

            DeliveryLegResponse deliveryLegResponse = new DeliveryLegResponse();

            JsonNode distanceNode = legNode.path("distance");
            deliveryLegResponse.setDistance(new Measurement(
                    distanceNode.path("text").asText(),
                    distanceNode.path("value").asInt()
            ));

            JsonNode durationNode = legNode.path("duration");
            deliveryLegResponse.setDuration(new Measurement(
                    durationNode.path("text").asText(),
                    durationNode.path("value").asInt()
            ));

            deliveryLegResponse.setStartAddress(legNode.path("start_address").asText());
            deliveryLegResponse.setEndAddress(legNode.path("end_address").asText());

            JsonNode startLocationNode = legNode.path("start_location");
            deliveryLegResponse.setStartLocation(new Location(
                    startLocationNode.path("lat").asDouble(),
                    startLocationNode.path("lng").asDouble()
            ));

            JsonNode endLocationNode = legNode.path("end_location");
            deliveryLegResponse.setEndLocation(new Location(
                    endLocationNode.path("lat").asDouble(),
                    endLocationNode.path("lng").asDouble()
            ));

            return deliveryLegResponse;
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new IllegalArgumentException("Could not extract leg data from response", e);
        }
    }
}
