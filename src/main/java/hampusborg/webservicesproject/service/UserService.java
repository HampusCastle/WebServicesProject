package hampusborg.webservicesproject.service;

import hampusborg.webservicesproject.dto.ApiUserDto;
import hampusborg.webservicesproject.dto.UserDto;
import hampusborg.webservicesproject.exception.ApiFetchException;
import hampusborg.webservicesproject.exception.EntityNotFoundException;
import hampusborg.webservicesproject.mapper.UserMapper;
import hampusborg.webservicesproject.model.MyUser;
import hampusborg.webservicesproject.repository.UserRepository;
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

        updateUserDetails(existingUser, userToUpdate);
        return userRepository.save(existingUser);
    }

    private void updateUserDetails(MyUser existingUser, MyUser userToUpdate) {
        existingUser.name(userToUpdate.name())
                .email(userToUpdate.email())
                .phone(userToUpdate.phone())
                .address(userToUpdate.address())
                .username(userToUpdate.username())
                .photoUrl(userToUpdate.photoUrl())
                .status(userToUpdate.status())
                .role(userToUpdate.role());

        if (userToUpdate.password() != null && !userToUpdate.password().isEmpty()) {
            existingUser.password(passwordEncoder.encode(userToUpdate.password()));
        }
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
            MyUser myUser = createMyUserFromDto(dto, random);
            userRepository.save(myUser);
        }
    }

    private MyUser createMyUserFromDto(ApiUserDto dto, Random random) {
        return UserMapper.fromApiUserDto(dto)
                .password(passwordEncoder.encode(dto.getPassword() != null ? dto.getPassword() : "defaultPassword"))
                .status(random.nextBoolean() ? "Active" : "Inactive")
                .role("user");
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