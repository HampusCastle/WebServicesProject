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
        try {
            MyUser myUser = UserMapper.fromDto(userDTO);
            MyUser savedUser = userService.saveUser(myUser);
            return ResponseEntity.status(HttpStatus.CREATED).body(UserMapper.toDto(savedUser));
        } catch (Exception e) {
            log.error("Error saving user: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/users")
    public ResponseEntity<Page<UserDto>> getUsers(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "12") int size) {
        try {
            Page<MyUser> myUserPage = userService.getAllUsers(page, size);
            return ResponseEntity.ok(myUserPage.map(UserMapper::toDto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("users/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable String id) {
        MyUser myUser = userService.getUser(id);
        return ResponseEntity.ok(UserMapper.toDto(myUser));
    }

    @DeleteMapping("users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/photo")
    public ResponseEntity<String> uploadPhoto(@RequestParam("id") String id, @RequestParam("file") MultipartFile file) throws Exception {
        String photoUrl = userService.uploadPhoto(id, file);
        return ResponseEntity.ok(photoUrl);
    }

    @GetMapping(path = "/image/{filename}", produces = {IMAGE_PNG_VALUE, IMAGE_JPEG_VALUE})
    public byte[] getPhoto(@PathVariable("filename") String filename) throws IOException {
        return Files.readAllBytes(Paths.get(PHOTO_DIRECTORY + filename));
    }

    @PostMapping("/fetch")
    public ResponseEntity<String> fetchUsersFromApi() {
        try {
            userService.fetchContactsFromApi();
            return ResponseEntity.ok("Contacts fetched successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to fetch contacts from API: " + e.getMessage());
        }
    }
    @PutMapping("/users/{id}")
    public ResponseEntity<UserDto> updateUser(
            @PathVariable String id,
            @RequestBody UserDto userDTO) {
        try {
            MyUser myUser = UserMapper.fromDto(userDTO);
            myUser.setId(id);
            MyUser updatedUser = userService.updateUser(myUser);
            return ResponseEntity.ok(UserMapper.toDto(updatedUser));
        } catch (Exception e) {
            log.error("Error updating user: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

}