package hampusborg.webservicesproject.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiUserDto {
    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private String phone;
    private String address;
    private String photoUrl;
}
