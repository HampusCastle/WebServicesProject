package hampusborg.webservicesproject.service.api;

import hampusborg.webservicesproject.dto.ApiUserDto;
import hampusborg.webservicesproject.exception.ApiFetchException;
import hampusborg.webservicesproject.model.ApiUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ApiFetchService {

    private final WebClient webClient;

    public List<ApiUserDto> fetchUsersFromApi(String apiUrl) {
        ApiUser response;
        try {
            response = webClient.get()
                    .uri(apiUrl)
                    .retrieve()
                    .bodyToMono(ApiUser.class)
                    .block();
        } catch (Exception e) {
            throw new ApiFetchException("Error while calling API: " + e.getMessage(), e);
        }

        validateResponse(response);
        return mapToApiUserDto(response.getResults());
    }

    private void validateResponse(ApiUser response) {
        if (response == null || response.getResults() == null || response.getResults().isEmpty()) {
            throw new ApiFetchException("Failed to fetch data from API: No results found");
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