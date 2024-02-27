package com.teknokote.cm.core.model;

import com.teknokote.core.model.ActivatableEntity;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

@Getter
@Setter
@Entity
public class Product extends ActivatableEntity<Long, User>
{
   @Serial
   private static final long serialVersionUID = -5846775621723991874L;
   private String code;
   private String name;
}
