package uz.pdp.cascade_types_annotation.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.UUID;

@Data
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class InfosEntertainment {


    @Id
    @GeneratedValue
    private UUID     id;

    private String   name;
    private String   category;
    private String   typeOfPeriod;
    private Double   priceProTypeOfPeriod;
    private Integer  deadline;
    private Boolean  isActive;


    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private SimCard simCard;


    @CreatedBy
    private UUID createdBy;

    @LastModifiedBy
    private UUID updatedBy;

    @CreationTimestamp
    private Timestamp createdAt;

    @UpdateTimestamp
    private Timestamp updatedAt;
}
