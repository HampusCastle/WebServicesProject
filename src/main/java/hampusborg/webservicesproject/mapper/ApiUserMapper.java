package hampusborg.webservicesproject.mapper;


import hampusborg.webservicesproject.dto.ApiUserDto;
import hampusborg.webservicesproject.model.MyUser;

public class ApiUserMapper {

    public static MyUser fromApiUserDto(ApiUserDto dto) {
        MyUser myUser = new MyUser();
        myUser.setName(dto.getFirstName() + " " + dto.getLastName());
        myUser.setEmail(dto.getEmail());
        myUser.setPhone(dto.getPhone());
        myUser.setUsername(dto.getUsername());
        myUser.setAddress(dto.getAddress());
        myUser.setPhotoUrl(dto.getPhotoUrl());
        return myUser;
    }
}
