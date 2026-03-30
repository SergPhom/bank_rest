package com.example.bankcards.dto.mapper;

import com.example.bankcards.dto.user.UserResponse;
import com.example.bankcards.dto.user.UserUpdateRequest;
import com.example.bankcards.entity.User;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-29T23:15:13+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.10 (BellSoft)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserResponse toDto(User user) {
        if ( user == null ) {
            return null;
        }

        UserResponse userResponse = new UserResponse();

        userResponse.setUuid( user.getId() );
        userResponse.setName( user.getName() );
        userResponse.setEmail( user.getEmail() );
        userResponse.setRoles( mapRoles( user.getRoles() ) );
        userResponse.setActive( user.isActive() );

        return userResponse;
    }

    @Override
    public void updateEntityFromDto(UserUpdateRequest request, User entity) {
        if ( request == null ) {
            return;
        }

        if ( request.getName() != null ) {
            entity.setName( request.getName() );
        }
        if ( request.getEmail() != null ) {
            entity.setEmail( request.getEmail() );
        }
        entity.setActive( request.isActive() );
    }
}
