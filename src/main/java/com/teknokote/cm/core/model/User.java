package com.teknokote.cm.core.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.teknokote.core.model.EssUser;
import jakarta.persistence.*;
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
   private String tag;
   @ManyToOne(fetch = FetchType.LAZY)
   @JsonIgnore
   private Customer customer;
   @Column(name = "customer_id", insertable = false, updatable = false)
   private Long customerId;
   private LocalDateTime lastConnectionDate;
}
