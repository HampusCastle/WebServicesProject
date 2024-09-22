package hampusborg.webservicesproject.mapper;

import hampusborg.webservicesproject.dto.UserDto;
import hampusborg.webservicesproject.dto.ApiUserDto;
import hampusborg.webservicesproject.model.MyUser;

public class UserMapper {

    public static MyUser fromUserDto(UserDto dto) {
        if (dto == null) {
            return null;
        }
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

    public static UserDto toUserDto(MyUser user) {
        if (user == null) {
            return null;
        }
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

    public static MyUser fromApiUserDto(ApiUserDto dto) {
        if (dto == null) {
            return null;
        }
        return new MyUser()
                .name(dto.getFirstName() + " " + dto.getLastName())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .username(dto.getUsername())
                .password(dto.getPassword())
                .address(dto.getAddress())
                .photoUrl(dto.getPhotoUrl());
    }
}