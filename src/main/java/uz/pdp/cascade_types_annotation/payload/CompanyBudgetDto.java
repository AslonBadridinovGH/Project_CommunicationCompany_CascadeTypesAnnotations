package uz.pdp.cascade_types_annotation.payload;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class CompanyBudgetDto {


    private  String     incomeType;

    private  Double      income;

    private  LocalDate   localDate;

}
