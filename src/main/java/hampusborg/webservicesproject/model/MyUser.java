package hampusborg.webservicesproject.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "users")
public class MyUser {
    @Id
    private String id;
    private String name;
    private String email;
    private String phone;
    private String address;
    private String username;
    private String password;
    private String photoUrl;
    private String status;
    private String role;
}