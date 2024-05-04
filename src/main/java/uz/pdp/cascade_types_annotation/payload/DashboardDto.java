package uz.pdp.cascade_types_annotation.payload;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class DashboardDto {

    private Double dailyIncome;
    private Double monthlyIncome;
    private Double halfQuarterIncome;

    private List<UUID>simCartsIDS;


    private List<UUID>tariffsIDS;


    private List<UUID>packagesIDS;

}
