package com.teknokote.cm.authentification.config;

import com.teknokote.cm.core.model.User;
import com.teknokote.cm.core.service.interfaces.UserService;
import com.teknokote.core.exceptions.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UsernamePasswordAuthenticationTokenConverter implements Converter<JwtAuthenticationToken, UsernamePasswordAuthenticationToken>
{
   @Autowired
   private UserService userService;
   @Override
   public UsernamePasswordAuthenticationToken convert(JwtAuthenticationToken source)
   {

      final Optional<User> user = userService.getDao().getRepository().findAllByUsernameIgnoreCase(source.getName());
      return new UsernamePasswordAuthenticationToken(user.orElseThrow(()->new EntityNotFoundException("User not found")),source.getToken(),source.getAuthorities());
   }
}
