package com.teknokote.cm.core.model;

import com.teknokote.core.model.ESSEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

@Getter
@Setter
@Entity
@Table(name = "cm_product")
public class Product extends ESSEntity<Long, User>
{
   @Serial
   private static final long serialVersionUID = -5846775621723991874L;
   private String code;
   private String name;
   @ManyToOne(fetch = FetchType.LAZY)
   private Supplier supplier;
   @Column(name = "supplier_id", insertable = false, updatable = false)
   private Long supplierId;
}
