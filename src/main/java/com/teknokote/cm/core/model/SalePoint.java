package com.teknokote.cm.core.model;

import com.teknokote.core.model.ESSEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

@Getter
@Setter
@Entity
@Table(name = "cm_sale_point")
public class SalePoint extends ESSEntity<Long, User>
{
   @Serial
   private static final long serialVersionUID = 8023792932074153377L;
   private String name;
   private String city;
   private String area;
   private String phone;
   private boolean status;
   @ManyToOne(cascade = CascadeType.ALL)
   private Country country;
   @Column(name = "country_id", insertable = false, updatable = false)
   private Long countryId;
   @ManyToOne(fetch = FetchType.LAZY)
   private Supplier supplier;
   @Column(name = "supplier_id", insertable = false, updatable = false)
   private Long supplierId;
}
