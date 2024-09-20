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
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return response.getBody().getResults().stream()
                        .map(user -> {
                            ApiUserDto apiUserDto = new ApiUserDto();
                            apiUserDto.setFirstName(user.getName().getFirst());
                            apiUserDto.setLastName(user.getName().getLast());
                            apiUserDto.setEmail(user.getEmail());
                            apiUserDto.setUsername(user.getLogin().getUsername());
                            apiUserDto.setPassword(user.getLogin().getPassword());
                            apiUserDto.setPhone(user.getPhone());
                            apiUserDto.setAddress(user.getLocation().getStreet().getName() + ", " + user.getLocation().getCity());
                            apiUserDto.setPhotoUrl(user.getPicture().getLarge());
                            return apiUserDto;
                        })
                        .collect(Collectors.toList());
            } else {
                throw new RuntimeException("Failed to fetch data from API");
            }
        } catch (RestClientException e) {
            throw new RuntimeException("Error while calling API: " + e.getMessage(), e);
        }
    }
}