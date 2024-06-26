package uz.pdp.cascade_types_annotation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.cascade_types_annotation.entity.Filial;

import java.util.UUID;

public interface FilialRepository extends JpaRepository<Filial, UUID> {
}
