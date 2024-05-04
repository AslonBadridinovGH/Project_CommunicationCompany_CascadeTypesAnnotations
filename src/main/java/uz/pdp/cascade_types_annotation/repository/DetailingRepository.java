package uz.pdp.cascade_types_annotation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.pdp.cascade_types_annotation.entity.Detailing;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
@Repository
public interface DetailingRepository extends JpaRepository<Detailing, UUID> {

    List<Detailing> findAllBySimCardFrom_CustomerIdAndLocalDateBetween(
            UUID simCardFrom_customer_id, LocalDate localDate, LocalDate localDate1);
}
