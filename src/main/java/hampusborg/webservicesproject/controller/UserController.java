package hampusborg.webservicesproject.controller;

import hampusborg.webservicesproject.dto.ApiUserDto;
import hampusborg.webservicesproject.dto.UserDto;
import hampusborg.webservicesproject.mapper.UserMapper;
import hampusborg.webservicesproject.model.MyUser;
import hampusborg.webservicesproject.service.ApiFetchService;
import hampusborg.webservicesproject.service.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final ApiFetchService apiFetchService;

    public UserController(UserService userService, ApiFetchService apiFetchService) {
        this.userService = userService;
        this.apiFetchService = apiFetchService;
    }

    @PostMapping
    public ResponseEntity<UserDto> saveUser(@Valid @RequestBody UserDto userDTO) {
        try {
            MyUser myUser = UserMapper.fromDto(userDTO);
            myUser = userService.saveUser(myUser);
            return ResponseEntity.ok(UserMapper.toDto(myUser));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping
    public ResponseEntity<Page<UserDto>> getUsers(@RequestParam(value = "page", defaultValue = "0") int page,
                                                  @RequestParam(value = "size", defaultValue = "12") int size) {
        Page<MyUser> myUserPage = userService.getAllUsers(page, size);
        Page<UserDto> userDTOPage = myUserPage.map(UserMapper::toDto);
        return ResponseEntity.ok(userDTOPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable(value = "id") String id) {
        MyUser myUser = userService.getUser(id);
        return myUser != null ? ResponseEntity.ok(UserMapper.toDto(myUser)) : ResponseEntity.notFound().build();
    }

    @PostMapping("/photo")
    public ResponseEntity<String> uploadPhoto(@RequestParam("id") String id) throws Exception {
        // Implement photo upload logic
        return ResponseEntity.ok("Photo uploaded successfully");
    }

    @PostMapping("/fetch")
    public ResponseEntity<String> fetchUsersFromApi(@RequestParam("apiUrl") String apiUrl) {
        try {
            List<ApiUserDto> apiUserDtos = apiFetchService.fetchUsersFromApi(apiUrl);
            // Optionally save users to database or process further
            return ResponseEntity.ok("Contacts fetched successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to fetch contacts from API: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContact(@PathVariable String id) throws Exception {
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }

}