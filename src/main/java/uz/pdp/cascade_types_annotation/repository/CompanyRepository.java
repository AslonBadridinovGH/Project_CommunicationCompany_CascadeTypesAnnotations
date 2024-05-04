package uz.pdp.cascade_types_annotation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.pdp.cascade_types_annotation.entity.Company;

import java.util.Optional;

// @RepositoryRestResource(path = "company")
@Repository
public interface CompanyRepository extends JpaRepository<Company, Integer> {

     Optional<Company> findByName(String name);
}
