package uz.pdp.cascade_types_annotation.payload;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class PackageDto {

    private double      price;

    private double      amount;

    private Boolean     isPackageSold;
    private LocalDate   dayOfPackageSold;

    private Integer     typeOfPackage;

    private Integer     validityDays;

    private Boolean     addToRestOffPackage;

    private UUID        tariffID;
    private UUID        simCardID;

    // private List<UUID>  simCardIDs;
}
