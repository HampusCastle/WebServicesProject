package hampusborg.webservicesproject.config;

import hampusborg.webservicesproject.model.MyUser;
import hampusborg.webservicesproject.repository.UserRepository;
import hampusborg.webservicesproject.service.user.UserService;
import hampusborg.webservicesproject.util.PhotoUtil;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DatabaseInitializer {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserService userService;

    public DatabaseInitializer(PasswordEncoder passwordEncoder, UserRepository userRepository,
                               UserService userService) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @PostConstruct
    public void init() {
        userRepository.deleteAll();

        MyUser admin = createAdminUser();
        userRepository.save(admin);
        log.info("Admin user initialized");

        try {
            userService.fetchContactsFromApi();
            log.info("Contacts fetched successfully");
        } catch (Exception e) {
            log.error("Error fetching contacts: {}", e.getMessage());
        }
    }

    private MyUser createAdminUser() {
        return new MyUser()
                .name("admin")
                .email("admin@gmail.com")
                .username("admin")
                .password(passwordEncoder.encode("admin123"))
                .photoUrl(PhotoUtil.getDefaultAdminPhoto())
                .phone("1234567890")
                .address("Admin State")
                .status("Active")
                .role("Admin");
    }
}