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
import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class  Package {

    @Id
    @GeneratedValue
    private UUID         id;

    private Double       price;

    private Double       amount;

    private  String      typeOfPackage;

    private  Integer     validityDays;

    private  Boolean     addToRestOffPackage;

    @ManyToOne
    private   Tariff     tariff;

    private   Boolean    isPackageSold;

    private   LocalDate  dayOfPackageSold;

    @ManyToOne
    private  SimCard   simCard;



    @CreatedBy
    private UUID createdBy;

    @LastModifiedBy
    private UUID updatedBy;

    @CreationTimestamp
    private Timestamp createdAt;

    @UpdateTimestamp
    private Timestamp updatedAt;
}