package id.my.agungdh.nat1_website_api.mapper;

import id.my.agungdh.nat1_website_api.dto.UserDto;
import id.my.agungdh.nat1_website_api.entity.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User entity);
    List<UserDto> toDtoList(List<User> entities);
}
