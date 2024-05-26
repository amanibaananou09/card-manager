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
@Table(name = "cm_card_group")
public class CardGroup extends ActivatableEntity<Long, User>
{
   @Serial
   private static final long serialVersionUID = 4192549111488448731L;
   private String name;
   private String condition;
   @Embedded
   private GroupCondition groupCondition;
   @OneToMany(cascade = CascadeType.ALL)
   private Set<Counter> counters=new HashSet<>();
   @ManyToOne(fetch = FetchType.LAZY)
   private Customer customer;
   @Column(name = "customer_id", insertable = false, updatable = false)
   private Long customerId;
}
