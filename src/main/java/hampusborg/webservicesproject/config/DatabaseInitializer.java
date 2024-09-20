package hampusborg.webservicesproject.config;

import hampusborg.webservicesproject.constant.PostConstructPhotos;
import hampusborg.webservicesproject.model.MyUser;
import hampusborg.webservicesproject.repository.UserRepository;
import hampusborg.webservicesproject.service.UserService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DatabaseInitializer {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final PostConstructPhotos postConstructPhotos;
    private final UserService userService;

    public DatabaseInitializer(PasswordEncoder passwordEncoder, UserRepository userRepository,
                               PostConstructPhotos postConstructPhotos, UserService userService) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.postConstructPhotos = postConstructPhotos;
        this.userService = userService;
    }

    @PostConstruct
    public void init() {
        userRepository.deleteAll();

        MyUser admin = new MyUser();
        admin.setName("admin");
        admin.setEmail("admin@gmail.com");
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setPhotoUrl(postConstructPhotos.fetchDefaultAdminPhoto());
        admin.setPhone("1234567890");
        admin.setAddress("Admin State");
        admin.setStatus("Active");
        admin.setRole("ROLE_ADMIN");
        userRepository.save(admin);

        MyUser user = new MyUser();
        user.setName("user");
        user.setEmail("user@gmail.com");
        user.setUsername("user");
        user.setPassword(passwordEncoder.encode("user123"));
        user.setPhotoUrl(postConstructPhotos.fetchDefaultUserPhoto());
        user.setPhone("0987654321");
        user.setAddress("User State");
        user.setStatus("Active");
        user.setRole("ROLE_USER");
        userRepository.save(user);

        userService.fetchContactsFromApi();

        log.info("Admin and default user initialized");
    }
}