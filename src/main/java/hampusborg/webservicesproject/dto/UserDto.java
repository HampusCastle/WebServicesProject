package hampusborg.webservicesproject.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(NON_NULL)
public class UserDto {

    private String id;
    private String email;
    private String name;
    private String phone;
    private String address;
    private String username;
    private String status;
    private String photoUrl;
    private String role;

}