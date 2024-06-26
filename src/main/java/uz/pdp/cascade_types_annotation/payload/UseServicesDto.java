package uz.pdp.cascade_types_annotation.payload;

import lombok.Data;

import java.util.UUID;

@Data
public class UseServicesDto {

    private Integer   artOfAction;

    private UUID      simCardCallToUUID;
    private double    durationCallInMinute;    // CALL                      Type 2

    
    private Integer   amountSMS;               // SEND SMS                  Type 1
    private double    spentInternetInMB;       // USE INTERNET              Type 3

    private  Integer  typeOfPackage;           // BUY NEW PACKAGE
    private  UUID     packageUUID;             // BUY NEW PACKAGE

    private Double    amountToFillBalance;
    private UUID      tariffID;                // Change Tariff
    private UUID      entertainmentID;
}



