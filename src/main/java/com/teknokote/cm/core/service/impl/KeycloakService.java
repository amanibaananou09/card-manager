package com.teknokote.cm.core.service.impl;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
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
}
