package uz.pdp.cascade_types_annotation.entity.enumClass;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import uz.pdp.cascade_types_annotation.entity.enums.UssdCodsName;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class UssdCods implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // private String serviceCod;

    @Enumerated(EnumType.STRING)
    private UssdCodsName codeName;

    @Override
    public String getAuthority() {
        return codeName.name();            // String qaytaradi
    }

}
