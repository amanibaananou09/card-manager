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
import org.keycloak.representations.idm.RoleRepresentation;
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
    @Test
    void updateUser_Success() {
        // Arrange: Mock the existing user to be updated
        UserRepresentation existingUser = new UserRepresentation();
        existingUser.setId("123");
        existingUser.setUsername(userDto.getUsername());

        // Mocking the Keycloak interactions
        lenient().when(keycloak.realm(realm)).thenReturn(realmResource);
        when(realmResource.users()).thenReturn(usersResource);
        when(usersResource.searchByUsername(userDto.getUsername(), true)).thenReturn(Collections.singletonList(existingUser));

        // Mocking UserResource return when get is called with ID
        when(usersResource.get(existingUser.getId())).thenReturn(userResource);
        when(userMapper.toUserRepresentation(userDto)).thenReturn(userRepresentation);

        // Act: Call the updateUser method
        keycloakService.updateUser(userDto.getUsername(), userDto);

        // Verify that the user was updated with the expected details
        verify(userResource, times(1)).update(any(UserRepresentation.class));
    }
    @Test
    void testGetInstance_ReturnsExistingInstance() {
        // Arrange
        Keycloak existingInstance = keycloakService.getInstance();

        // When invoked again, it should return the same instance
        Keycloak laterInstance = keycloakService.getInstance();

        // Assert
        assertSame(existingInstance, laterInstance);
    }
    @Test
    void testGetInstance_CreatesKeycloakInstance() {
        // Act
        Keycloak keycloakInstance = keycloakService.getInstance();

        // Assert
        assertNotNull(keycloakInstance);
    }
    @Test
    void testCreateUser_WithRole_RoleExists() {
        // Arrange
        userDto = UserDto.builder()
                .username("testuser")
                .password("password")
                .email("testuser@example.com")
                .role("existing-role")
                .build();

        userRepresentation = new UserRepresentation();
        userRepresentation.setId("1");

        authentication = mock(Authentication.class);
        securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(userMapper.toUserRepresentation(any(UserDto.class))).thenReturn(userRepresentation);
        when(realmResource.users().create(any())).thenReturn(response);
        when(response.getStatusInfo()).thenReturn(Response.Status.CREATED);
        when(response.getLocation()).thenReturn(URI.create("/users/1"));

        // Mock role handling when the role exists
        RoleRepresentation existingRole = new RoleRepresentation();
        existingRole.setName("existing-role");

        when(rolesResource.list()).thenReturn(Collections.singletonList(existingRole));
        when(rolesResource.get("existing-role")).thenReturn(roleResource);
        when(roleResource.toRepresentation()).thenReturn(existingRole);

        when(usersResource.get("1")).thenReturn(userResource);
        when(userResource.roles()).thenReturn(roleMappingResource);
        when(roleMappingResource.realmLevel()).thenReturn(roleScopeResource);
        when(roleScopeResource.listEffective()).thenReturn(Collections.emptyList());

        // Act
        UserRepresentation createdUser = keycloakService.createUser(userDto);

        // Assert
        assertNotNull(createdUser);
        assertEquals("1", createdUser.getId());
        verify(realmResource.users()).create(userRepresentation);
        verify(roleScopeResource).add(Collections.singletonList(existingRole));
        verify(rolesResource, never()).create(any(RoleRepresentation.class));
    }
    @Test
    void testCreateUser_WithRole_RoleDoesNotExist() {
        // Arrange
        userDto = UserDto.builder()
                .username("testuser")
                .password("password")
                .email("testuser@example.com")
                .role("new-role")
                .build();

        userRepresentation = new UserRepresentation();
        userRepresentation.setId("1");

        authentication = mock(Authentication.class);
        securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(userMapper.toUserRepresentation(any(UserDto.class))).thenReturn(userRepresentation);
        when(realmResource.users().create(any())).thenReturn(response);
        when(response.getStatusInfo()).thenReturn(Response.Status.CREATED);
        when(response.getLocation()).thenReturn(URI.create("/users/1"));

        // Mock role handling when the role does not exist
        when(rolesResource.list()).thenReturn(Collections.emptyList());

        RoleRepresentation newRoleToCreate = new RoleRepresentation();
        newRoleToCreate.setName("new-role");

        // Mock the behavior when retrieving the newly created role
        RoleRepresentation newlyCreatedRole = new RoleRepresentation();
        newlyCreatedRole.setName("new-role");
        when(rolesResource.get("new-role")).thenReturn(roleResource);
        when(roleResource.toRepresentation()).thenReturn(newlyCreatedRole);

        when(usersResource.get("1")).thenReturn(userResource);
        when(userResource.roles()).thenReturn(roleMappingResource);
        when(roleMappingResource.realmLevel()).thenReturn(roleScopeResource);
        when(roleScopeResource.listEffective()).thenReturn(Collections.emptyList());

        // Act
        UserRepresentation createdUser = keycloakService.createUser(userDto);

        // Assert
        assertNotNull(createdUser);
        assertEquals("1", createdUser.getId());
        verify(realmResource.users()).create(userRepresentation);
        verify(rolesResource).create(argThat(role -> "new-role".equals(role.getName())));
        verify(roleScopeResource).add(Collections.singletonList(newlyCreatedRole));
    }
    @Test
    void testCreateUser_WithRole_RemovesDefaultRoles() {
        // Arrange
        userDto = UserDto.builder()
                .username("testuser")
                .password("password")
                .email("testuser@example.com")
                .role("user-role")
                .build();

        userRepresentation = new UserRepresentation();
        userRepresentation.setId("1");

        authentication = mock(Authentication.class);
        securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(userMapper.toUserRepresentation(any(UserDto.class))).thenReturn(userRepresentation);
        when(realmResource.users().create(any())).thenReturn(response);
        when(response.getStatusInfo()).thenReturn(Response.Status.CREATED);
        when(response.getLocation()).thenReturn(URI.create("/users/1"));

        // Mock role handling
        RoleRepresentation userRole = new RoleRepresentation();
        userRole.setName("user-role");
        RoleRepresentation defaultRole1 = new RoleRepresentation();
        defaultRole1.setName("default-role-1");
        RoleRepresentation defaultRole2 = new RoleRepresentation();
        defaultRole2.setName("default-role-2");
        List<RoleRepresentation> defaultRoles = Arrays.asList(defaultRole1, defaultRole2);

        when(rolesResource.list()).thenReturn(Collections.singletonList(userRole));
        when(rolesResource.get("user-role")).thenReturn(roleResource);
        when(roleResource.toRepresentation()).thenReturn(userRole);

        when(usersResource.get("1")).thenReturn(userResource);
        when(userResource.roles()).thenReturn(roleMappingResource);
        when(roleMappingResource.realmLevel()).thenReturn(roleScopeResource);
        when(roleScopeResource.listEffective()).thenReturn(defaultRoles);

        // Act
        UserRepresentation createdUser = keycloakService.createUser(userDto);

        // Assert
        assertNotNull(createdUser);
        assertEquals("1", createdUser.getId());
        verify(realmResource.users()).create(userRepresentation);
        verify(roleScopeResource).remove(Collections.singletonList(defaultRole1));
        verify(roleScopeResource).remove(Collections.singletonList(defaultRole2));
        verify(roleScopeResource).add(Collections.singletonList(userRole));
    }
    @Test
    void testCreateUser_WithoutRole() {
        // Arrange
        userDto = UserDto.builder()
                .username("testuser")
                .password("password")
                .email("testuser@example.com")
                .role(null)
                .build();

        userRepresentation = new UserRepresentation();
        userRepresentation.setId("1");

        authentication = mock(Authentication.class);
        securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(userMapper.toUserRepresentation(any(UserDto.class))).thenReturn(userRepresentation);
        when(realmResource.users().create(any())).thenReturn(response);
        when(response.getStatusInfo()).thenReturn(Response.Status.CREATED);
        when(response.getLocation()).thenReturn(URI.create("/users/1"));

        // Act
        UserRepresentation createdUser = keycloakService.createUser(userDto);

        // Assert
        assertNotNull(createdUser);
        assertEquals("1", createdUser.getId());
        verify(realmResource.users()).create(userRepresentation);
        verify(realmResource.roles(), never()).list();
    }
    @Test
    void testCreateUser_WithEmptyRole() {
        // Arrange
        userDto = UserDto.builder()
                .username("testuser")
                .password("password")
                .email("testuser@example.com")
                .role("")
                .build();

        userRepresentation = new UserRepresentation();
        userRepresentation.setId("1");

        authentication = mock(Authentication.class);
        securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(userMapper.toUserRepresentation(any(UserDto.class))).thenReturn(userRepresentation);
        when(realmResource.users().create(any())).thenReturn(response);
        when(response.getStatusInfo()).thenReturn(Response.Status.CREATED);
        when(response.getLocation()).thenReturn(URI.create("/users/1"));

        // Act
        UserRepresentation createdUser = keycloakService.createUser(userDto);

        // Assert
        assertNotNull(createdUser);
        assertEquals("1", createdUser.getId());
        verify(realmResource.users()).create(userRepresentation);
        verify(realmResource.roles(), never()).list();
    }
}