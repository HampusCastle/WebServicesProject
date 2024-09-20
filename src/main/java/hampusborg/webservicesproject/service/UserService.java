package hampusborg.webservicesproject.service;

import hampusborg.webservicesproject.dto.ApiUserDto;
import hampusborg.webservicesproject.exception.ApiFetchException;
import hampusborg.webservicesproject.exception.EntityNotFoundException;
import hampusborg.webservicesproject.exception.PhotoUploadException;
import hampusborg.webservicesproject.mapper.ApiUserMapper;
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
    private final PhotoService photoService;
    private final ApiFetchService apiFetchService;

    @Transactional(rollbackFor = Exception.class)
    public void fetchContactsFromApi() {
        String apiUrl = "https://randomuser.me/api/?results=24";
        try {
            List<ApiUserDto> apiUserDtos = apiFetchService.fetchUsersFromApi(apiUrl);
            if (apiUserDtos.isEmpty()) {
                throw new ApiFetchException("API response did not contain any users");
            }

            Random random = new Random();
            for (ApiUserDto dto : apiUserDtos) {
                MyUser myUser = ApiUserMapper.fromApiUserDto(dto);
                myUser.setPassword(passwordEncoder.encode(dto.getPassword() != null ? dto.getPassword() : "defaultPassword"));
                myUser.setStatus(random.nextBoolean() ? "Active" : "Inactive");
                myUser.setRole("user");
                userRepository.save(myUser);
            }
        } catch (Exception e) {
            log.error("Error during API fetch: {}", e.getMessage(), e);
            throw new ApiFetchException("Error during API fetch: " + e.getMessage());
        }
    }

    public Page<MyUser> getAllUsers(int page, int size) {
        return userRepository.findAll(PageRequest.of(page, size, Sort.by("name")));
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

    public String uploadPhoto(String id, MultipartFile file) {
        MyUser myUser = getUser(id);
        try {
            String photoUrl = photoService.savePhoto(id, file);
            myUser.setPhotoUrl(photoUrl);
            userRepository.save(myUser);
            return photoUrl;
        } catch (RuntimeException e) {
            throw new PhotoUploadException("Failed to upload photo: " + e.getMessage());
        }
    }
    @Transactional
    public MyUser updateUser(MyUser userToUpdate) {

        MyUser existingUser = userRepository.findById(userToUpdate.getId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userToUpdate.getId()));

        existingUser.setName(userToUpdate.getName());
        existingUser.setEmail(userToUpdate.getEmail());
        existingUser.setPhone(userToUpdate.getPhone());
        existingUser.setAddress(userToUpdate.getAddress());
        existingUser.setUsername(userToUpdate.getUsername());
        if (userToUpdate.getPassword() != null && !userToUpdate.getPassword().isEmpty()) {
            existingUser.setPassword(userToUpdate.getPassword());
        }
        existingUser.setPhotoUrl(userToUpdate.getPhotoUrl());
        existingUser.setStatus(userToUpdate.getStatus());
        existingUser.setRole(userToUpdate.getRole());

        return userRepository.save(existingUser);
    }
}