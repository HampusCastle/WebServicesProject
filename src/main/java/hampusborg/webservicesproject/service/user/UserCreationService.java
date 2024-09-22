package hampusborg.webservicesproject.service.user;

import hampusborg.webservicesproject.dto.ApiUserDto;
import hampusborg.webservicesproject.mapper.UserMapper;
import hampusborg.webservicesproject.model.MyUser;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Random;

public class UserCreationService {

    public void updateUserDetails(MyUser existingUser, MyUser userToUpdate, PasswordEncoder passwordEncoder) {
        existingUser.name(userToUpdate.name())
                .email(userToUpdate.email())
                .phone(userToUpdate.phone())
                .address(userToUpdate.address())
                .username(userToUpdate.username())
                .photoUrl(userToUpdate.photoUrl())
                .status(userToUpdate.status())
                .role(userToUpdate.role());

        if (userToUpdate.password() != null && !userToUpdate.password().isEmpty()) {
            existingUser.password(passwordEncoder.encode(userToUpdate.password()));
        }
    }

    public MyUser createMyUserFromDto(ApiUserDto dto, Random random, PasswordEncoder passwordEncoder) {
        return UserMapper.fromApiUserDto(dto)
                .password(passwordEncoder.encode(dto.getPassword() != null ? dto.getPassword() : "defaultPassword"))
                .status(random.nextBoolean() ? "Active" : "Inactive")
                .role("user");
    }
}