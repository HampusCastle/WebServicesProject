package hampusborg.webservicesproject.mapper;

import hampusborg.webservicesproject.dto.UserDto;
import hampusborg.webservicesproject.model.MyUser;

public class UserMapper {

    public static MyUser fromDto(UserDto dto) {
        return new MyUser()
                .id(dto.getId())
                .name(dto.getName())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .address(dto.getAddress())
                .username(dto.getUsername())
                .photoUrl(dto.getPhotoUrl())
                .status(dto.getStatus())
                .role(dto.getRole());
    }

    public static UserDto toDto(MyUser user) {
        return new UserDto(
                user.id(),
                user.name(),
                user.email(),
                user.phone(),
                user.address(),
                user.username(),
                null,
                user.photoUrl(),
                user.status(),
                user.role()
        );
    }
}