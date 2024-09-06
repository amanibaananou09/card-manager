package com.teknokote.cm.core.service.impl;

import com.teknokote.cm.exception.KeycloakUserCreationException;
import com.teknokote.cm.core.dao.mappers.UserMapper;
import com.teknokote.cm.dto.UserDto;
import com.teknokote.core.exceptions.EntityNotFoundException;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.RolesResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;

import javax.ws.rs.core.Response;
import java.util.*;

@Service
public class KeycloakService {
    @Value("${spring.security.oauth2.client.provider.keycloak.realm}")
    public String realm;
    @Value("${spring.security.oauth2.client.provider.keycloak.serverUrl}")
    private String serverUrl;
    @Value("${spring.security.oauth2.client.registration.oauth2-client-credentials.client-id}")
    private String clientId;
    @Value("${spring.security.oauth2.client.registration.oauth2-client-credentials.client-secret}")
    private String clientSecret;
    @Autowired
    private UserMapper userMapper;
    private Keycloak instance;

    public Keycloak getInstance() {
        if (Objects.isNull(this.instance)) {
            this.instance = KeycloakBuilder.builder()
                    .serverUrl(serverUrl)
                    .realm(realm)
                    .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                    .clientId(clientId)
                    .clientSecret(clientSecret)
                    .build();
        }
        return this.instance;
    }

    public Optional<UserRepresentation> getUserIdentity(String username) {
        Keycloak keycloak = this.getInstance();
        return keycloak.realm(realm).users().searchByUsername(username, true).stream().findFirst();
    }

    public List<UserRepresentation> getUserIdentities(List<String> usernames) {
        Keycloak keycloak = this.getInstance();
        List<UserRepresentation> userRepresentations = new ArrayList<>();
        // Iterate through each username and query Keycloak for user details
        for (String username : usernames) {
            Optional<UserRepresentation> user = keycloak.realm(realm).users().searchByUsername(username, true).stream().findFirst();
            if (user.isPresent()) {
                userRepresentations.add(user.get());
            }
        }
        return userRepresentations;
    }

    public ResponseEntity<List<UserRepresentation>> listUsers() {
        Keycloak keycloak = this.getInstance();
        // Retrieve the list of users from Keycloak
        List<UserRepresentation> users = keycloak.realm(realm).users().list();
        // Retrieve roles for each user
        for (UserRepresentation user : users) {
            List<RoleRepresentation> userRoles = keycloak.realm(realm).users().get(user.getId()).roles().realmLevel().listEffective();
            List<String> roleNames = userRoles.stream().map(RoleRepresentation::getName).toList();
            user.setRealmRoles(roleNames);
        }
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    public UserRepresentation createUser(@RequestBody UserDto createUserRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUsername = authentication.getName();
        // Create the user in Keycloak
        Keycloak keycloak = this.getInstance();
        UserRepresentation user = userMapper.toUserRepresentation(createUserRequest);
        user.setFirstName(createUserRequest.getFirstName());
        user.setLastName(createUserRequest.getLastName());
        user.setUsername(createUserRequest.getUsername());
        user.setEmail(createUserRequest.getEmail());
        user.setRealmRoles(Collections.emptyList());
        user.setEnabled(true);
        if (createUserRequest.getPhone() != null) {
            //  Set User Attributes
            user.singleAttribute("phone", createUserRequest.getPhone());
        }
        // Set user password
        CredentialRepresentation passwordCred = new CredentialRepresentation();
        passwordCred.setTemporary(false);
        passwordCred.setType(CredentialRepresentation.PASSWORD);
        passwordCred.setValue(createUserRequest.getPassword());
        user.setCredentials(Collections.singletonList(passwordCred));
        // Set user role
        RealmResource realmResource = keycloak.realm(realm);

        try (Response response = realmResource.users().create(user)) {
            if (response.getStatusInfo().getStatusCode() != 201) {
                throw new KeycloakUserCreationException(response.getStatusInfo().getReasonPhrase());
            }
            String userId = response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");
            if (StringUtils.hasText(createUserRequest.getRole())) {
                UserResource userResource = realmResource.users().get(userId);
                RolesResource rolesResource = realmResource.roles();
                List<RoleRepresentation> defaultRoles = userResource.roles().realmLevel().listEffective();
                for (RoleRepresentation defaultRole : defaultRoles) {
                    userResource.roles().realmLevel().remove(Collections.singletonList(defaultRole));
                }
                // Check if the role exists
                boolean roleExists = false;
                List<RoleRepresentation> roles = rolesResource.list();
                for (RoleRepresentation role : roles) {
                    if (role.getName().equals(createUserRequest.getRole())) {
                        roleExists = true;
                        break;
                    }
                }

                if (roleExists) {
                    RoleRepresentation role = rolesResource.get(createUserRequest.getRole()).toRepresentation();
                    realmResource.users().get(userId).roles().realmLevel().add(Collections.singletonList(role));
                } else {
                    // Role doesn't exist, create it
                    RoleRepresentation role = new RoleRepresentation();
                    role.setName(createUserRequest.getRole());
                    rolesResource.create(role);
                    RoleRepresentation roleRepresentation = rolesResource.get(createUserRequest.getRole()).toRepresentation();
                    realmResource.users().get(userId).roles().realmLevel().add(Collections.singletonList(roleRepresentation));
                }
            }
        } catch (Exception exception) {
            throw new KeycloakUserCreationException(exception);
        }

        return user;
    }

    /**
     * Updates the user with the given username in Keycloak.
     *
     * @param userUsernameToUpdate the username of the user to update
     * @param userDto              the updated user information
     * @throws EntityNotFoundException if the user with the given username is not found in Keycloak
     */
    public void updateUser(String userUsernameToUpdate, UserDto userDto) {
        Keycloak keycloak = this.getInstance();
        RealmResource realmResource = keycloak.realm(realm);
        Optional<UserRepresentation> keycloakUser = getUserIdentity(userUsernameToUpdate).stream().findFirst();
        UserRepresentation keycloakUserToUpdate = keycloakUser.orElseThrow(() -> new EntityNotFoundException("Keycloak keycloakUser with userUsernameToUpdate  " + userUsernameToUpdate + " not found"));
        UserResource userResource = realmResource.users().get(keycloakUserToUpdate.getId());
        UserRepresentation userRepresentation = userMapper.toUserRepresentation(userDto);
        userRepresentation.setId(keycloakUserToUpdate.getId());
        if (userDto.getPassword() != null) {
            CredentialRepresentation passwordCred = new CredentialRepresentation();
            passwordCred.setTemporary(false);
            passwordCred.setType(CredentialRepresentation.PASSWORD);
            passwordCred.setValue(userDto.getPassword());
            userRepresentation.setCredentials(Collections.singletonList(passwordCred));
        }
        Map<String, List<String>> attributes = new HashMap<>();
        attributes.put("phone", Collections.singletonList(userDto.getPhone()));
        userRepresentation.setAttributes(attributes);

        userResource.update(userRepresentation);
    }

}
