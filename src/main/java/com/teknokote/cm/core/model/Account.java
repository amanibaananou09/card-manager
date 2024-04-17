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
@Table(name = "cm_account")
public class Account extends ActivatableEntity<Long, User> {
    @Serial
    private static final long serialVersionUID = -2606697440139107863L;

    private String bankName;
    private String code;
    private String label;
    private String description;
    @Enumerated(EnumType.STRING)
    private EnumAccountStatus status;
    @ManyToOne(fetch = FetchType.LAZY)
    private Customer customer;
    @Column(name = "customer_id", insertable = false, updatable = false)
    private Long customerId;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    private Set<Movement> movements = new HashSet<>();
}
