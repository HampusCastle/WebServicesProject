package hampusborg.webservicesproject.mapper;

import hampusborg.webservicesproject.dto.UserDto;
import hampusborg.webservicesproject.model.MyUser;

public class UserMapper {

    public static MyUser fromDto(UserDto dto) {
        MyUser user = new MyUser();
        user.setId(dto.getId());
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setAddress(dto.getAddress());
        user.setUsername(dto.getUsername());
        user.setPhotoUrl(dto.getPhotoUrl());
        user.setStatus(dto.getStatus());
        user.setRole(dto.getRole());
        return user;
    }

    public static UserDto toDto(MyUser user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        dto.setAddress(user.getAddress());
        dto.setUsername(user.getUsername());
        dto.setPhotoUrl(user.getPhotoUrl());
        dto.setStatus(user.getStatus());
        dto.setRole(user.getRole());
        return dto;
    }
}