package uz.pdp.cascade_types_annotation.payload;
import lombok.Data;

import java.util.UUID;

@Data
public class CompanyDto {

    private String      name;
    private UUID        companyDirectorID;
}
