package hampusborg.webservicesproject.config;

import hampusborg.webservicesproject.model.MyUser;
import hampusborg.webservicesproject.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
public class DatabaseInitializer {

    private final UserRepository userRepository;

    @Value("${default.admin.username}")
    private String adminUsername;

    @Value("${default.admin.password}")
    private String adminPassword;

    @Value("${default.user.username}")
    private String userUsername;

    @Value("${default.user.password}")
    private String userPassword;

    public DatabaseInitializer(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostConstruct
    public void init() {
        Optional<MyUser> admin = userRepository.findByUsername(adminUsername);
        if (admin.isEmpty()) {
            MyUser adminUser = new MyUser();
            adminUser.setUsername(adminUsername);
            adminUser.setPassword(adminPassword);
            adminUser.setRole("ADMIN");
            adminUser.setName("Admin User");
            userRepository.save(adminUser);
        }

        Optional<MyUser> user = userRepository.findByUsername(userUsername);
        if (user.isEmpty()) {
            MyUser defaultUser = new MyUser();
            defaultUser.setUsername(userUsername);
            defaultUser.setPassword(userPassword);
            defaultUser.setRole("USER");
            defaultUser.setName("Default User");
            userRepository.save(defaultUser);
        }
    }
}
