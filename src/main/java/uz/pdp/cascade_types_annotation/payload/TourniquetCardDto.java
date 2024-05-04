package uz.pdp.cascade_types_annotation.payload;

import lombok.Data;

import java.util.UUID;

@Data
public class TourniquetCardDto {


    private  String     expireDate;

    private  UUID       employeeId;

    private  Integer    companyId;

}
