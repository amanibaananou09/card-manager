package com.teknokote.cm.core.model;

import com.teknokote.core.model.EssUser;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.io.Serial;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cm_user")
public class User extends EssUser<Long, User>
{
   @Serial
   private static final long serialVersionUID = 1334158047026037242L;
   private String username;
   private LocalDateTime lastConnectionDate;
}
