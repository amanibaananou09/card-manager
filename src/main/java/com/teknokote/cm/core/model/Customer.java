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
@Table(name = "cm_customer")
public class Customer extends ActivatableEntity<Long, User> {
    @Serial
    private static final long serialVersionUID = -7987812682702682139L;
    @Column(nullable = false)
    private String identifier;
    private String address;
    private String phone;
    private String email;
    private String companyName;
    @ManyToOne(fetch = FetchType.LAZY)
    private Supplier supplier;
    @Column(name = "supplier_id", insertable = false, updatable = false)
    private Long supplaierId;
    @OneToMany
    @JoinTable(name = "cm_customer_users", joinColumns = @JoinColumn(name = "customer_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> users = new HashSet<>();
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private Set<Account> accounts = new HashSet<>();
    @OneToMany(mappedBy = "customer")
    private Set<CardGroup> cardGroups = new HashSet<>();
}
