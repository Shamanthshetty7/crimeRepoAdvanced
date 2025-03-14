package com.endava.CrimeReportingSystem.service.utility;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import org.springframework.stereotype.Component;

import com.endava.CrimeReportingSystem.constants.ExternalApiConstants;
import com.endava.CrimeReportingSystem.entity.dto.LocationAddressesDTO;
import com.endava.CrimeReportingSystem.response.ApiGenericResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
@Component
public class ExternalAPIs {
	ObjectMapper objectMapper = new ObjectMapper();
	//for city selection
	public ApiGenericResponse<Object> getMatchedCitiesByCity(String city) {
		ApiGenericResponse<Object> response = new ApiGenericResponse<>(null, null);
		String postOfficeUrl = ExternalApiConstants.POST_OFFICE_URL + city;

		try {
			HttpClient client = HttpClient.newHttpClient();

			HttpRequest request = HttpRequest.newBuilder()
					.uri(URI.create(postOfficeUrl))
					.build();

			HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());

			response.setData(httpResponse.body());

		} catch (Exception e) {
		
			response.setMessage(ExternalApiConstants.ERROR_UNEXPECTED_FETCHING_INVESTIGATION.getMessage() + e.getMessage());
		}
		return response;


	}
	
	// for fetching address using latitude and longitude
	public String getAddressByLatLong(double latitude, double longitude) {
	    
	    String nominatimUrl = ExternalApiConstants.OPENSTREETMAP_URL+"reverse?lat=" + latitude + "&lon=" + longitude + "&format=json&addressdetails=1";

	    try {
	        HttpClient client = HttpClient.newHttpClient();

	        HttpRequest request = HttpRequest.newBuilder()
	                .uri(URI.create(nominatimUrl))
	                .build();

	        HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
 
	        JsonNode rootNode = objectMapper.readTree(httpResponse.body());
	        
			JsonNode firstResult = rootNode.get("display_name");
			
			

	        return firstResult.toString();


	    } catch (Exception e) {
	       
	        return null;
	    }
	   
	}
	
	
	


	//coordinates to display it in the leaflet map

	public ApiGenericResponse<Object> getCoordinatesByAddress(List<String> addresses) {
		ApiGenericResponse<Object> response = new ApiGenericResponse<>(null, null);

		try {
			HttpClient client = HttpClient.newHttpClient();
			
			List<Object> resultList = addresses.stream()
					.map(address -> {
						String nominatimUrl = ExternalApiConstants.OPENSTREETMAP_URL+"search?q=" + address.replaceAll("\\s+", "").strip() + "&format=json&addressdetails=1&limit=1";

						// Build the request for each address
						HttpRequest request = HttpRequest.newBuilder()
								.uri(URI.create(nominatimUrl))
								.build();

						try {
							HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());

							if (httpResponse.statusCode() == 200 && !httpResponse.body().isEmpty()) {
								LocationAddressesDTO locationAddressesDTO=new LocationAddressesDTO();

								JsonNode rootNode = objectMapper.readTree(httpResponse.body());
								if (rootNode.isArray() && rootNode.size() > 0) {
									JsonNode firstResult = rootNode.get(0);

									locationAddressesDTO.setLatitude(firstResult.get("lat").asDouble());
									locationAddressesDTO.setLongitude(firstResult.get("lon").asDouble());
									locationAddressesDTO.setLocationName(address);


									
								}else {
									locationAddressesDTO.setLocationName(address);
								}
								return locationAddressesDTO;

							} else {

								return ExternalApiConstants.ERROR_NO_COORDINATES_FOUND.getMessage()+" for: "+ address;
							}
						} catch (Exception e) {

							return ExternalApiConstants.ERROR_UNEXPECTED_FETCHING_COORDINATES.getMessage();
						}
					}).toList();
					


			response.setData(resultList);

		} catch (Exception e) {

			response.setMessage(ExternalApiConstants.ERROR_UNEXPECTED_FETCHING_COORDINATES.getMessage());
		}

		return response;
	}


	//fetching all nearby emergency centers using overpass api (openstreetmap api)
	public ApiGenericResponse<Object> getNearbyEmergencyServices(double lat, double lon) {
		ApiGenericResponse<Object> response = new ApiGenericResponse<>(null, null);
		String overpassUrl = "https://overpass-api.de/api/interpreter?data=[out:json];(" +
				"node[amenity=police](around:5000," + lat + "," + lon + ");" +
				"node[amenity=fire_station](around:5000," + lat + "," + lon + ");" +
				"node[amenity=hospital](around:5000," + lat + "," + lon + ");" +
				");out;";

		try {
			HttpClient client = HttpClient.newHttpClient();

			HttpRequest request = HttpRequest.newBuilder()
					.uri(URI.create(overpassUrl))
					.build();

			HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());


			response.setData(httpResponse.body());

		} catch (Exception e) {
			
			response.setMessage(ExternalApiConstants.ERROR_UNEXPECTED_FETCHING_NEARBY_SERVICES.getMessage() + e.getMessage());
		}

		return response;
	}
	
	

	public ApiGenericResponse<?> checkVerifiedEmail(String email) {
        String apiKey = "live_dafd30cc9ff9af5e1f4f";
        String url = String.format("https://api.emailable.com/v1/verify?email=%s&api_key=%s", email, apiKey);

        HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();
        
        ApiGenericResponse<Object> response=new ApiGenericResponse<>(null,null);

        try {
            HttpResponse<String> clientresponse = client.send(request, HttpResponse.BodyHandlers.ofString());
           

            response.setData(clientresponse.body());
          
           
        } catch (IOException | InterruptedException e) {
           
            response.setMessage(ExternalApiConstants.ERROR_UNEXPECTED_VERIFYING_EMAIL.getMessage());
           
        }
		return response;
    }

}
