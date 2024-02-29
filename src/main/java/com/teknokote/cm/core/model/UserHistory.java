package com.teknokote.cm.core.model;

import com.teknokote.core.model.SimpleESSEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "cm_user_history")
public class UserHistory extends SimpleESSEntity<Long>
{
   @Serial
   private static final long serialVersionUID = 5736500306846413972L;

   /**
    * Id du user qui a procédé à l'action
    */
   private Long userId;
   /**
    * Type de l'activité: POST, GET, DELETE, PUT,...
    */
   @Enumerated(EnumType.STRING)
   private EnumActivityType activityType;
   /**
    * URI
    */
   private String requestURI;
   /**
    * Request handler method
    */
   private String requestHandler;
   /**
    * Adresse IP du client
    */
   private String ipAddress;
   private LocalDateTime activityDate;

}
