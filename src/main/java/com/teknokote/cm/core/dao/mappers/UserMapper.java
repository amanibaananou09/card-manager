package com.teknokote.cm.core.dao.mappers;

import com.teknokote.cm.core.model.User;
import com.teknokote.cm.dto.UserDto;
import com.teknokote.core.config.MapperConfiguration;
import com.teknokote.core.mappers.BidirectionalEntityDtoMapper;
import org.keycloak.representations.idm.UserRepresentation;
import org.mapstruct.*;

import java.util.List;
import java.util.Map;

@Mapper(config = MapperConfiguration.class)
public interface UserMapper extends BidirectionalEntityDtoMapper<Long, User, UserDto>
{
    @Mapping(source = "username", target = "username")
    @Mapping(source = "firstName", target = "firstName") // Add more attributes as needed
    @Mapping(source = "lastName", target = "lastName")
    @Mapping(source = "email", target = "email")
    @Mapping(target = "phone", expression = "java(mapPhoneAttribute(userRepresentation))")
    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    UserDto enrichDtoFromUserRepresentation(UserRepresentation userRepresentation, @MappingTarget UserDto target);
    default String mapPhoneAttribute(UserRepresentation userRepresentation) {
        // Retrieve the phone attribute from UserRepresentation
        Map<String, List<String>> attributes = userRepresentation.getAttributes();
        if (attributes != null && attributes.containsKey("phone")) {
            return userRepresentation.getAttributes().get("phone").get(0);
        } else {
            return null;
        }
    }
}
