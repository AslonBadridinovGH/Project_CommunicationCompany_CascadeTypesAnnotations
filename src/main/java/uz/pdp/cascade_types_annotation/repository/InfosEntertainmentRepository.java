package uz.pdp.cascade_types_annotation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.pdp.cascade_types_annotation.entity.InfosEntertainment;

import java.util.List;
import java.util.UUID;

@Repository
public interface InfosEntertainmentRepository extends JpaRepository<InfosEntertainment, UUID> {


    List<InfosEntertainment> findAllByIsActiveTrue();
}
