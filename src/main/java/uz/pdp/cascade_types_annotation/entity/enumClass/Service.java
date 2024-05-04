package uz.pdp.cascade_types_annotation.entity.enumClass;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import uz.pdp.cascade_types_annotation.entity.enums.ServiceName;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Service implements GrantedAuthority {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    @Enumerated(EnumType.STRING)
    private ServiceName serviceName;

    @Override
    public String getAuthority() {
        return serviceName.name();  // String qaytaradi
    }

}
