package uz.pdp.cascade_types_annotation.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.pdp.cascade_types_annotation.entity.TourniquetCard;

import java.util.UUID;

@Repository
public interface TourniquetCardRepository extends JpaRepository<TourniquetCard, UUID> {

   // Optional<TourniquetCard> findByEmployeeId(UUID uuid);
}
