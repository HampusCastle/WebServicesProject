package hampusborg.webservicesproject.service;

import hampusborg.webservicesproject.dto.ApiUserDto;
import hampusborg.webservicesproject.model.ApiUser;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ApiFetchService {

    private final RestTemplate restTemplate;

    public ApiFetchService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<ApiUserDto> fetchUsersFromApi(String apiUrl) {
        try {
            ResponseEntity<ApiUser> response = restTemplate.getForEntity(apiUrl, ApiUser.class);
            validateResponse(response);
            return mapToApiUserDto(response.getBody().getResults());
        } catch (RestClientException e) {
            throw new RuntimeException("Error while calling API: " + e.getMessage(), e);
        }
    }

    private void validateResponse(ResponseEntity<ApiUser> response) {
        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new RuntimeException("Failed to fetch data from API");
        }
    }

    private List<ApiUserDto> mapToApiUserDto(List<ApiUser.User> users) {
        return users.stream()
                .map(user -> new ApiUserDto(
                        user.getName().getFirst(),
                        user.getName().getLast(),
                        user.getEmail(),
                        user.getLogin().getUsername(),
                        user.getLogin().getPassword(),
                        user.getPhone(),
                        user.getLocation().getStreet().getName() + ", " + user.getLocation().getCity(),
                        user.getPicture().getLarge()))
                .collect(Collectors.toList());
    }
}