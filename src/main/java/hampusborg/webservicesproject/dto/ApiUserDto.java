package hampusborg.webservicesproject.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonInclude(NON_NULL)
public class ApiUserDto {
    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private String password;
    private String phone;
    private String address;
    private String photoUrl;
}