package uz.pdp.cascade_types_annotation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.pdp.cascade_types_annotation.entity.enumClass.Service;
import uz.pdp.cascade_types_annotation.entity.enums.ServiceName;

@Repository
public interface ServiceRepository extends JpaRepository<Service,Integer> {

     Service findByServiceName(ServiceName serviceName);
}
