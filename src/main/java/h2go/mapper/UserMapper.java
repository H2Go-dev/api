package h2go.mapper;

import h2go.dto.UserCreationDTO;
import h2go.dto.UserRetrievalDTO;
import h2go.model.User;
import h2go.model.enums.Role;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Mapper(componentModel = "spring", imports = Role.class)
public interface UserMapper {

    @Mapping(target = "passwordHash", expression = "java(passwordEncoder.encode(dto.password()))")
    @Mapping(target = "role", expression = "java(Role.USER)")
    @Mapping(target = "enabled", constant = "true")
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    User toEntity(UserCreationDTO dto, @Context PasswordEncoder passwordEncoder);

    UserRetrievalDTO toDto(User user);

    List<UserRetrievalDTO> toDtoList(List<User> users);

}
