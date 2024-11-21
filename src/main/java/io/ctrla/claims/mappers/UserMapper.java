package io.ctrla.claims.mappers;

import io.ctrla.claims.dto.hospitaladmin.HospitalAdminUserDto;
import io.ctrla.claims.dto.insurance.InsuranceResponseDto;
import io.ctrla.claims.dto.user.UserDto;
import io.ctrla.claims.dto.user.UserResponseDto;
import io.ctrla.claims.entity.User;
import org.springframework.stereotype.Service;


@Service
public class UserMapper {

    public User toUser(UserDto userDto) {
        User user = new User();

        user.setEmail(userDto.getEmail());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setPassword(userDto.getPassword());

        return user;
    }

    public UserResponseDto toUserResponseDto(User user) {
        UserResponseDto userResponseDto = new UserResponseDto();

        userResponseDto.setEmail(user.getEmail());
        userResponseDto.setFirstName(user.getFirstName());
        userResponseDto.setLastName(user.getLastName());

        return userResponseDto;
    }

    public HospitalAdminUserDto toUserResDto(User user) {
        HospitalAdminUserDto userRes = new HospitalAdminUserDto();

        userRes.setEmail(user.getEmail());
        userRes.setFirstName(user.getFirstName());
        userRes.setLastName(user.getLastName());
        userRes.setVerified(user.getIsVerified());

        return userRes;
    }
}
