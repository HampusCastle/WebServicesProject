package hampusborg.webservicesproject.controller;

import hampusborg.webservicesproject.dto.UserDto;
import hampusborg.webservicesproject.mapper.UserMapper;
import hampusborg.webservicesproject.model.MyUser;
import hampusborg.webservicesproject.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
        MyUser myUser = UserMapper.fromUserDto(userDTO);
        MyUser savedUser = userService.saveUser(myUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(UserMapper.toUserDto(savedUser));
    }

    @GetMapping("/users")
    public ResponseEntity<Page<UserDto>> getUsers(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "12") int size) {
        Page<UserDto> users = userService.getAllUsers(page, size);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserDto> getUserById(
            @PathVariable String id,
            @RequestParam(value = "status", required = false) String status) {
        try {
            MyUser myUser = userService.getUser(id);
            if (status != null && !myUser.status().equalsIgnoreCase(status)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok(UserMapper.toUserDto(myUser));
        } catch (Exception e) {
            log.error("Error fetching user with ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(
            @PathVariable String id,
            @RequestParam(value = "confirm", defaultValue = "false") boolean confirm) {

        log.info("Attempting to delete user with ID: {}", id);

        if (!confirm) {
            return ResponseEntity.badRequest().body("Confirmation parameter is required to delete the user.");
        }

        try {
            userService.deleteUser(id);
            log.info("User with ID {} deleted successfully.", id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Error deleting user with ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to delete user: " + e.getMessage());
        }
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable String id, @RequestBody UserDto userDTO) {
        MyUser myUser = UserMapper.fromUserDto(userDTO).id(id);
        MyUser updatedUser = userService.updateUser(myUser);
        return ResponseEntity.ok(UserMapper.toUserDto(updatedUser));
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
        userService.fetchContactsFromApi();
        return ResponseEntity.ok("Contacts fetched successfully");
    }
}