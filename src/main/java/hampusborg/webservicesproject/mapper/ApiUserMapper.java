package hampusborg.webservicesproject.mapper;

import hampusborg.webservicesproject.dto.ApiUserDto;
import hampusborg.webservicesproject.model.MyUser;

public class ApiUserMapper {

    public static MyUser fromApiUserDto(ApiUserDto dto) {
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