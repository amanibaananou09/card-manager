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
@Table(name = "cm_supplier")
public class Supplier extends ActivatableEntity<Long, User>
{
   @Serial
   private static final long serialVersionUID = 4397829570013258848L;
   @Column(nullable = false)
   private String identifier;
   @Enumerated(EnumType.STRING)
   private EnumOriginModule origin;
   @Enumerated(EnumType.STRING)
   private EnumSupplierStatus status;
   private String reference;
   private String name;
   private String address;
   private String phone;
   private String city;
   @OneToMany(mappedBy = "supplier")
   private Set<Product> products = new HashSet<>();
   @OneToMany(mappedBy = "supplier",cascade = CascadeType.ALL)
   private Set<SalePoint> salePoints = new HashSet<>();
   @OneToMany(cascade = CascadeType.ALL)
   @JoinTable(name = "cm_supplier_users", joinColumns = @JoinColumn(name = "supplier_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
   private Set<User> users = new HashSet<>();
   // Les clients sont propres à ce module (CM)
   @OneToMany(mappedBy = "supplier",cascade = CascadeType.ALL)
   private Set<Customer> customers = new HashSet<>();
}
