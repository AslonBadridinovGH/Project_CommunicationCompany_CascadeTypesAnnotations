package uz.pdp.cascade_types_annotation.payload;
import lombok.Data;

import java.util.UUID;

@Data
public class TariffDto {

    private String  name;
    private Integer deadLine;
    private String  expireDate;

    private Double  mb;
    private Integer sms;
    private Double  minuteBetweenInternSet;
    private Double  minuteBetweenExternSet;

    private Boolean isForJusticePerson;
    private Double  transitionPrice;
    private Double  price;

     private UUID simCardID;
}





