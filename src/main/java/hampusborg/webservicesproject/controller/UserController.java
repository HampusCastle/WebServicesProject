package hampusborg.webservicesproject.controller;

import hampusborg.webservicesproject.dto.UserDto;
import hampusborg.webservicesproject.mapper.UserMapper;
import hampusborg.webservicesproject.model.MyUser;
import hampusborg.webservicesproject.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static hampusborg.webservicesproject.constant.Constant.PHOTO_DIRECTORY;
import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;
import static org.springframework.http.MediaType.IMAGE_PNG_VALUE;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/home")
public class UserController {

    private final UserService userService;

    @PostMapping("/saveUser")
    public ResponseEntity<UserDto> saveUser(@RequestBody UserDto userDTO) {
        return handleUserAction(() -> {
            MyUser myUser = UserMapper.fromDto(userDTO);
            MyUser savedUser = userService.saveUser(myUser);
            return UserMapper.toDto(savedUser);
        });
    }

    @GetMapping("/users")
    public ResponseEntity<Page<UserDto>> getUsers(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "12") int size) {
        return handleUserAction(() -> {
            Page<MyUser> myUserPage = userService.getAllUsers(page, size);
            return myUserPage.map(UserMapper::toDto);
        });
    }

    @GetMapping("users/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable String id) {
        return handleUserAction(() -> {
            MyUser myUser = userService.getUser(id);
            return UserMapper.toDto(myUser);
        });
    }

    @DeleteMapping("users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/photo")
    public ResponseEntity<String> uploadPhoto(@RequestParam("id") String id, @RequestParam("file") MultipartFile file) {
        String photoUrl = userService.uploadPhoto(id, file);
        return ResponseEntity.ok(photoUrl);
    }

    @GetMapping(path = "/image/{filename}", produces = {IMAGE_PNG_VALUE, IMAGE_JPEG_VALUE})
    public byte[] getPhoto(@PathVariable("filename") String filename) throws IOException {
        return Files.readAllBytes(Paths.get(PHOTO_DIRECTORY + filename));
    }

    @PostMapping("/fetch")
    public ResponseEntity<String> fetchUsersFromApi() {
        return handleUserAction(() -> {
            userService.fetchContactsFromApi();
            return "Contacts fetched successfully";
        });
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable String id, @RequestBody UserDto userDTO) {
        return handleUserAction(() -> {
            MyUser myUser = UserMapper.fromDto(userDTO).id(id);
            MyUser updatedUser = userService.updateUser(myUser);
            return UserMapper.toDto(updatedUser);
        });
    }

    private <T> ResponseEntity<T> handleUserAction(UserAction<T> action) {
        try {
            T result = action.execute();
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("Error occurred: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @FunctionalInterface
    private interface UserAction<T> {
        T execute() throws Exception;
    }
}