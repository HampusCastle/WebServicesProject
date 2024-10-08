package hampusborg.webservicesproject.service.user;

import hampusborg.webservicesproject.dto.ApiUserDto;
import hampusborg.webservicesproject.dto.UserDto;
import hampusborg.webservicesproject.exception.ApiFetchException;
import hampusborg.webservicesproject.exception.EntityNotFoundException;
import hampusborg.webservicesproject.mapper.UserMapper;
import hampusborg.webservicesproject.model.MyUser;
import hampusborg.webservicesproject.repository.UserRepository;
import hampusborg.webservicesproject.service.api.ApiFetchService;
import hampusborg.webservicesproject.service.photo.PhotoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Random;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ApiFetchService apiFetchService;
    private final PhotoService photoService;
    private final UserCreationService userCreationService;

    public Page<UserDto> getAllUsers(int page, int size) {
        return userRepository.findAll(PageRequest.of(page, size, Sort.by("name")))
                .map(UserMapper::toUserDto);
    }

    public MyUser getUser(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with ID " + id + " does not exist"));
    }

    public MyUser saveUser(MyUser myUser) {
        return userRepository.save(myUser);
    }

    public void deleteUser(String id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("User with ID " + id + " does not exist");
        }
        userRepository.deleteById(id);
    }

    @Transactional
    public MyUser updateUser(MyUser userToUpdate) {
        MyUser existingUser = userRepository.findById(userToUpdate.id())
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userToUpdate.id()));

        userCreationService.updateUserDetails(existingUser, userToUpdate, passwordEncoder);
        return userRepository.save(existingUser);
    }

    @Transactional(rollbackFor = Exception.class)
    public void fetchContactsFromApi() {
        String apiUrl = "https://randomuser.me/api/?results=24";
        List<ApiUserDto> apiUserDtos = apiFetchService.fetchUsersFromApi(apiUrl);
        if (apiUserDtos.isEmpty()) {
            throw new ApiFetchException("API response did not contain any users");
        }

        Random random = new Random();
        for (ApiUserDto dto : apiUserDtos) {
            MyUser myUser = userCreationService.createMyUserFromDto(dto, random, passwordEncoder);
            userRepository.save(myUser);
        }
    }

    public String uploadPhoto(String id, MultipartFile file) {
        MyUser myUser = getUser(id);
        String photoUrl;
        try {
            photoUrl = photoService.savePhoto(id, file);
            myUser.photoUrl(photoUrl);
            userRepository.save(myUser);
            return photoUrl;
        } catch (RuntimeException e) {
            throw new RuntimeException("Failed to upload photo: " + e.getMessage(), e);
        }
    }
}