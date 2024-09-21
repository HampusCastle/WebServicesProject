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

        MyUser admin = createAdminUser();
        userRepository.save(admin);

        userService.fetchContactsFromApi();
        log.info("Admin user initialized");
    }

    private MyUser createAdminUser() {
        return new MyUser()
                .name("admin")
                .email("admin@gmail.com")
                .username("admin")
                .password(passwordEncoder.encode("admin123"))
                .photoUrl(postConstructPhotos.fetchDefaultAdminPhoto())
                .phone("1234567890")
                .address("Admin State")
                .status("Active")
                .role("Admin");
    }
}