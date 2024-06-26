package uz.pdp.cascade_types_annotation.payload;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.UUID;

@Data
public class RegisterDto {


    @NotNull
    @Size(min = 3,max = 50)
    private String firstname;

    @NotNull
    @Length(min = 3,max = 50)
    private String lastname;

    @NotNull
    @Email
    private String    email;

    private  String   password;

    private  UUID     filialID;

    private  UUID     tourniquetCardID;

}
