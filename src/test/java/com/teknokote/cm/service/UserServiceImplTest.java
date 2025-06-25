package com.teknokote.cm.service;

import com.teknokote.cm.core.dao.SupplierDao;
import com.teknokote.cm.core.dao.UserDao;
import com.teknokote.cm.core.dao.mappers.UserMapper;
import com.teknokote.cm.core.service.impl.KeycloakService;
import com.teknokote.cm.core.service.impl.UserServiceImpl;
import com.teknokote.cm.dto.SupplierDto;
import com.teknokote.cm.dto.UserDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.representations.idm.UserRepresentation;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userServiceImpl;
    @Mock
    private UserDao userDao;
    @Mock
    private SupplierDao supplierDao;
    @Mock
    private KeycloakService keycloakService;
    @Mock
    private UserMapper userMapper;
    @Test
    void testUpdateLastConnection() {
        String userName = "testUser";

        userServiceImpl.updateLastConnection(userName);

        verify(userDao, times(1)).updateLastConnection(eq(userName), any(LocalDateTime.class));
    }
    @Test
    void testFindBySupplier() {
        Long supplierId = 1L;
        SupplierDto supplierDto = SupplierDto.builder().build();

        UserDto userDto1 = UserDto.builder().build();
        userDto1.setUsername("user1");

        UserDto userDto2 = UserDto.builder().build();
        userDto2.setUsername("user2");

        Set<UserDto> users = new HashSet<>(Arrays.asList(userDto1, userDto2));
        supplierDto.setUsers(users);

        when(supplierDao.findById(supplierId)).thenReturn(Optional.of(supplierDto));

        UserRepresentation userRepresentation1 = new UserRepresentation();
        userRepresentation1.setUsername("user1");

        UserRepresentation userRepresentation2 = new UserRepresentation();
        userRepresentation2.setUsername("user2");

        when(keycloakService.getUserIdentities(anyList())).thenReturn(Arrays.asList(userRepresentation1, userRepresentation2));

        userServiceImpl.findBySupplier(supplierId);

        verify(userMapper, times(1)).enrichDtoFromUserRepresentation(eq(userRepresentation1), eq(userDto1));
        verify(userMapper, times(1)).enrichDtoFromUserRepresentation(eq(userRepresentation2), eq(userDto2));
    }
    @Test
    void testFindByUsername() {
        String username = "testUser";
        UserDto userDto = UserDto.builder().build();
        userDto.setUsername(username);

        when(userDao.findAllByUsername(username)).thenReturn(Optional.of(userDto));

        Optional<UserDto> foundUser = userServiceImpl.findByUsername(username);

        assertTrue(foundUser.isPresent());
        assertEquals(username, foundUser.get().getUsername());
    }
}