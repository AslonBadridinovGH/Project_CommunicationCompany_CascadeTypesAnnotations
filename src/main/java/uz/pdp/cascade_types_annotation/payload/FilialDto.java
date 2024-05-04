package uz.pdp.cascade_types_annotation.payload;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class FilialDto {


    private String     name;

    private String     city;

    private UUID       filialManagerId;

    private UUID       filialDirectorId;

    private List<UUID> filialEmployeeIds;

    private Integer  companyId;

}
