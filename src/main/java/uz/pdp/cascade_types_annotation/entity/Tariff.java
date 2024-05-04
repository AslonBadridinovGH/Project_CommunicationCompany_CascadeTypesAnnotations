package uz.pdp.cascade_types_annotation.entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Tariff {

    @Id
    @GeneratedValue
    private UUID       id;

    private String     name;

    private LocalDate  expireDate;
    private Integer    deadLine;

    private Boolean    isForJusticePerson;
    private Double     transitionPrice;
    private Double     price;
    private Boolean    isActive;



    private Double  mb;
    private Integer sms;
    private Double  minuteBetweenInternSet;
    private Double  minuteBetweenExternSet;


    private Double  priceForMb;  // 50%
    private Double  priceForSms; // 5%
    private Double  priceForMinuteBetweenInternSet; // 5%
    private Double  priceForMinuteBetweenExternSet; // 30%


    @CreatedBy
    private UUID createdBy;

    @LastModifiedBy
    private UUID updatedBy;

    @CreationTimestamp
    private Timestamp createdAt;

    @UpdateTimestamp
    private Timestamp updatedAt;

}



