package com.teknokote.cm.core.model;

import com.teknokote.core.model.ActivatableEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
public class Customer extends ActivatableEntity<Long, User>
{
   @Serial
   private static final long serialVersionUID = -7987812682702682139L;
   @Column(nullable = false)
   private String name;
   @ManyToOne(fetch = FetchType.LAZY)
   private Customer parent;
   @Column(name = "parent_id", insertable = false, updatable = false)
   private Long parentId;
   @OneToMany(mappedBy = "customer")
   private Set<User> users = new HashSet<>();
   @OneToMany(mappedBy = "customer")
   private Set<Account> accounts = new HashSet<>();
}
