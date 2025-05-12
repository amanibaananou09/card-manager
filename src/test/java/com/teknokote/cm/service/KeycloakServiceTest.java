package com.teknokote.cm.service;

import com.teknokote.cm.core.dao.mappers.UserMapper;
import com.teknokote.cm.core.model.User;
import com.teknokote.cm.core.service.impl.KeycloakService;
import com.teknokote.cm.dto.UserDto;
import com.teknokote.cm.exception.KeycloakUserCreationException;
import com.teknokote.core.exceptions.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.*;
import org.keycloak.representations.idm.UserRepresentation;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KeycloakServiceTest {

    @Mock
    private Keycloak keycloak;
    @Mock
    private UserMapper userMapper;
    @Mock
    private RealmResource realmResource;
    @Mock
    private Response response;
    @InjectMocks
    private KeycloakService keycloakService;
    @Mock
    private SecurityContext securityContext;
    @Mock
    private Authentication authentication;
    @Mock
    private UserResource userResource;
    @Mock
    private UsersResource usersResource;
    @Mock
    private UserRepresentation userRepresentation;
    @Mock
    private ClientsResource clientsResource;
    @Mock
    private RolesResource rolesResource;
    @Mock
    private RoleResource roleResource;
    @Mock
    private RoleMappingResource roleMappingResource;
    @Mock
    private RoleScopeResource roleScopeResource;
    private User user;
    private UserDto userDto;

    @Value("${spring.security.oauth2.client.provider.keycloak.realm}")
    String realm;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.setContext(securityContext);
        user = new User();
        user.setUsername("loggedUser");
        userDto = UserDto.builder()
                .username("johndoe")
                .password("securepassword")
                .firstName("John")
                .lastName("Doe")
                .email("johndoe@example.com")
                .phone("23517353")
                .build();

        lenient().when(keycloak.realm(realm)).thenReturn(realmResource);
        lenient().when(realmResource.users()).thenReturn(usersResource);
        lenient().when(realmResource.clients()).thenReturn(mock(ClientsResource.class));
        lenient().when(realmResource.roles()).thenReturn(mock(RolesResource.class));
        lenient().when(realmResource.roles()).thenReturn(rolesResource);

        lenient().when(usersResource.searchByUsername(anyString(), anyBoolean()))
                .thenReturn(Collections.emptyList());

        userRepresentation = new UserRepresentation();
        userRepresentation.setUsername(userDto.getUsername());
        lenient().when(usersResource.get("123")).thenReturn(userResource);
        lenient().when(usersResource.create(any())).thenReturn(response);
        lenient().when(response.getStatus()).thenReturn(201);
        lenient().when(response.getLocation()).thenReturn(URI.create("/users/123"));
        lenient().when(userMapper.toUserRepresentation(any())).thenReturn(userRepresentation);
        lenient().when(realmResource.clients()).thenReturn(clientsResource);

        realm = "ess-realm";
    }

    @Test
    void testCreateUser_Success() {
        // Arrange
        userDto = UserDto.builder().build();
        userDto.setUsername("testuser");
        userDto.setPassword("password");
        userDto.setEmail("testuser@example.com");

        userRepresentation = new UserRepresentation();
        userRepresentation.setId("1");

        authentication = mock(Authentication.class);
        securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(userMapper.toUserRepresentation(any(UserDto.class))).thenReturn(userRepresentation);
        when(realmResource.users().create(any())).thenReturn(response);
        when(response.getStatusInfo()).thenReturn(Response.Status.CREATED);

        // Act
        UserRepresentation createdUser = keycloakService.createUser(userDto);

        // Assert
        assertNotNull(createdUser);
        assertEquals("1", createdUser.getId());
        verify(realmResource.users()).create(userRepresentation);
    }
    @Test
    void testCreateUser_UserCreationException() {
        // Arrange
        userDto = UserDto.builder().build();
        userDto.setUsername("testuser");
        userDto.setPassword("password");

        authentication = mock(Authentication.class);
        securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        userRepresentation = new UserRepresentation();
        when(userMapper.toUserRepresentation(userDto)).thenReturn(userRepresentation);
        when(realmResource.users().create(userRepresentation)).thenReturn(response);
        when(response.getStatusInfo()).thenReturn(Response.Status.OK);

        // Act & Assert
        assertThrows(KeycloakUserCreationException.class, () -> {
            keycloakService.createUser(userDto);
        });
    }
    @Test
    void testGetUserIdentity_UserFound() {
        // Arrange
        String username = "existingUser";
        userRepresentation = new UserRepresentation();
        userRepresentation.setUsername(username);

        when(realmResource.users().searchByUsername(username, true)).thenReturn(Collections.singletonList(userRepresentation));

        // Act
        Optional<UserRepresentation> foundUser = keycloakService.getUserIdentity(username);

        // Assert
        assertTrue(foundUser.isPresent());
        assertEquals(username, foundUser.get().getUsername());
    }
    @Test
    void testGetUserIdentity_UserNotFound() {
        // Arrange
        String username = "nonExistingUser";

        when(realmResource.users().searchByUsername(username, true)).thenReturn(Collections.emptyList());

        // Act
        Optional<UserRepresentation> foundUser = keycloakService.getUserIdentity(username);

        // Assert
        assertFalse(foundUser.isPresent());
    }
    @Test
    void testUpdateUser_UserNotFound() {
        // Arrange
        String usernameToUpdate = "nonExistingUser";

        when(usersResource.searchByUsername(usernameToUpdate, true)).thenReturn(Collections.emptyList());

        // Act & Assert
        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            keycloakService.updateUser(usernameToUpdate, userDto);
        });

        assertEquals("Keycloak keycloakUser with userUsernameToUpdate  " + usernameToUpdate + " not found", exception.getMessage());
    }
    @Test
    void testGetUserIdentities() {
        // Arrange
        List<String> usernames = Arrays.asList("user1", "user2");
        UserRepresentation userRepresentation1 = new UserRepresentation();
        userRepresentation1.setUsername("user1");
        UserRepresentation userRepresentation2 = new UserRepresentation();
        userRepresentation2.setUsername("user2");

        when(usersResource.searchByUsername("user1", true)).thenReturn(Collections.singletonList(userRepresentation1));
        when(usersResource.searchByUsername("user2", true)).thenReturn(Collections.singletonList(userRepresentation2));

        // Act
        List<UserRepresentation> result = keycloakService.getUserIdentities(usernames);

        // Assert
        assertEquals(2, result.size());
        assertEquals("user1", result.get(0).getUsername());
        assertEquals("user2", result.get(1).getUsername());
    }
    @Test
    void testListUsers() {
        // Arrange
        userRepresentation = new UserRepresentation();
        userRepresentation.setId("123");
        userRepresentation.setUsername("user1");

        // Mocking the user resource and its roles
        UserResource userResourceMock = mock(UserResource.class);
        RoleMappingResource roleMappingResourceMock = mock(RoleMappingResource.class);
        when(usersResource.list()).thenReturn(Collections.singletonList(userRepresentation));

        // Mock the behavior for userResource.roles()
        when(usersResource.get(userRepresentation.getId())).thenReturn(userResourceMock);
        when(userResourceMock.roles()).thenReturn(roleMappingResourceMock);
        when(roleMappingResourceMock.realmLevel()).thenReturn(mock(RoleScopeResource.class));

        when(roleMappingResourceMock.realmLevel().listEffective()).thenReturn(Collections.emptyList());

        // Act
        ResponseEntity<List<UserRepresentation>> responseEntity = keycloakService.listUsers();

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(1, responseEntity.getBody().size());
        assertEquals("user1", responseEntity.getBody().get(0).getUsername());
    }
}