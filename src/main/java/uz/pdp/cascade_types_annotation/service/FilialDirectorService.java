package uz.pdp.cascade_types_annotation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.pdp.cascade_types_annotation.entity.EmpCustomer;
import uz.pdp.cascade_types_annotation.entity.enums.RoleName;
import uz.pdp.cascade_types_annotation.payload.LoginDto;
import uz.pdp.cascade_types_annotation.payload.UserRegisterDto;
import uz.pdp.cascade_types_annotation.repository.EmpCustomerRepository;
import uz.pdp.cascade_types_annotation.repository.FilialRepository;
import uz.pdp.cascade_types_annotation.repository.RoleRepository;
import uz.pdp.cascade_types_annotation.security.JwtProvider;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

@Service
public class FilialDirectorService {


    @Autowired
    EmpCustomerRepository employeeRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    FilialRepository filialRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JavaMailSender javaMailSender;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtProvider jwtProvider;


    public ApiResponse registerDirector(UserRegisterDto filialDirectorDto){

         boolean existsByEmail3 = employeeRepository.existsByEmail(filialDirectorDto.getEmail());
        if (existsByEmail3){
            return new ApiResponse("this Email is already exist",false);
        }

          EmpCustomer director=new EmpCustomer();
        director.setFirstname(filialDirectorDto.getFirstname());
        director.setLastname(filialDirectorDto.getLastname());
        director.setEmail(filialDirectorDto.getEmail());
        director.setPassword(passwordEncoder.encode(filialDirectorDto.getPassword()));
        // director.setFilial(optionalFilial.get());
        director.setRoles(Collections.singleton(roleRepository.findByRoleName(RoleName.FILIAL_DIRECTOR)));
        director.setEmailCode(UUID.randomUUID().toString());
        employeeRepository.save(director);

        sendEMail(director.getEmailCode(), director.getEmail());
        return new ApiResponse("You have successfully registered, confirm your email to activate the account",true);

    }


    public Boolean sendEMail(String emailCode, String sendingEmail){
      try {
    SimpleMailMessage mailMessage = new SimpleMailMessage();
    mailMessage.setFrom("aslon.dinov@gmail.com");
    mailMessage.setTo(sendingEmail);
    mailMessage.setSubject("confirm account");
    mailMessage.setText("<a href='http://localhost:8080/api/auth/verifyEmail/filialDirector?emailCode="+emailCode+"&email="+sendingEmail+"'>confirm</a>");
    javaMailSender.send(mailMessage);
    return true;

    }catch (Exception e){
      return false;
      }
}


    public ApiResponse directorVerifyEmail(String emailCode, String email){
        Optional<EmpCustomer> optionalUser = employeeRepository.findByEmailCodeAndEmail(emailCode, email);
        if (optionalUser.isPresent())
        {
            EmpCustomer director = optionalUser.get();

               director.setEnabled(true);
               director.setEmailCode(null);
               employeeRepository.save(director);
               return new ApiResponse("Account  confirmed",true);
           }
               return new ApiResponse("Account  already confirmed",false);
    }


    public ApiResponse loginDirector(LoginDto loginDto) {
    try {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
        loginDto.getEmail(), loginDto.getPassword()));

      //  User user = (User) authentication.getPrincipal();
        EmpCustomer user = (EmpCustomer) authentication.getPrincipal();

        String token = jwtProvider.generateToken(loginDto.getEmail(),user.getRoles());
        return new ApiResponse("Token",true,token);

    }catch (BadCredentialsException  badCredentialsException){
        return new ApiResponse("password or login is error",false);
    }}


}







