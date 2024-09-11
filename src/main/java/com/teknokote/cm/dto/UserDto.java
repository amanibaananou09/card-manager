package com.teknokote.cm.dto;

import com.teknokote.core.dto.ESSActivatableDto;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class UserDto extends ESSActivatableDto<Long> {
    @NotEmpty
    private String identifier;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private String role;
    private LocalDateTime lastConnectionDate;
    private String reference;
    private List<String> suggestions;

    @Builder
    public UserDto(Long id, Long version, Boolean actif, LocalDateTime dateStatusChange, List<String> suggestions, String username, String firstName, String lastName, String email, String phone, LocalDateTime lastConnectionDate, String reference, String password, String role,String identifier) {
        super(id, version, actif, dateStatusChange);
        this.identifier=identifier;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.lastConnectionDate = lastConnectionDate;
        this.reference = reference;
        this.password = password;
        this.role = role;
        this.suggestions = suggestions;
    }
}

