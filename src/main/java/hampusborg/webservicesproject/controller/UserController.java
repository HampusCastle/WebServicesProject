package hampusborg.webservicesproject.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;
import static org.springframework.http.MediaType.IMAGE_PNG_VALUE;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final PhotoService photoService;

    public UserController(UserService userService, PhotoService photoService) {
        this.userService = userService;
        this.photoService = photoService;
    }

    @PostMapping
    public ResponseEntity<UserDto> saveUser(@RequestBody UserDto userDTO) {
        MyUser myUser = UserMapper.fromDto(userDTO);
        myUser = userService.saveContact(myUser);
        return ResponseEntity.ok(UserMapper.toDto(myUser));
    }

    @GetMapping
    public ResponseEntity<Page<UserDto>> getUsers(@RequestParam(value = "page", defaultValue = "0") int page,
                                                  @RequestParam(value = "size", defaultValue = "12") int size) {
        Page<MyUser> myUserPage = userService.getAllContacts(page, size);
        Page<UserDto> userDTOPage = myUserPage.map(UserMapper::toDto);
        return ResponseEntity.ok(userDTOPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable(value = "id") String id) {
        MyUser myUser = userService.getContact(id);
        return myUser != null ? ResponseEntity.ok(UserMapper.toDto(myUser)) : ResponseEntity.notFound().build();
    }

    @PostMapping("/photo")
    public ResponseEntity<String> uploadPhoto(@RequestParam("id") String id) throws Exception {
        return null;
    }

    @PostMapping("/fetch")
    public ResponseEntity<String> fetchUsersFromApi() {
        try {
            userService.fetchContactsFromApi();
            return ResponseEntity.ok("Contacts fetched successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to fetch contacts from API: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContact(@PathVariable String id) throws Exception {
        userService.deleteContact(id);
        return ResponseEntity.ok().build();
    }
}