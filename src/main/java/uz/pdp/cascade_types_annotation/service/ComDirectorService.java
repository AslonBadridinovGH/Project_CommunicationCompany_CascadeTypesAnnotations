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
public class ComDirectorService {


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


    public ApiResponse registerDirector(UserRegisterDto registerDto){

        boolean existsByEmail3 = employeeRepository.existsByEmail(registerDto.getEmail());
        if (existsByEmail3){
            return new ApiResponse("such email is already exist",false);
        }

          EmpCustomer director=new EmpCustomer();
        director.setFirstname(registerDto.getFirstname());
        director.setLastname(registerDto.getLastname());
        director.setEmail(registerDto.getEmail());
        director.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        director.setRoles(Collections.singleton(roleRepository.findByRoleName(RoleName.COMPANY_DIRECTOR)));
        director.setEmailCode(UUID.randomUUID().toString());

//        director.setEnabled(true);
//        director.setEmailCode(null);

        employeeRepository.save(director);

       sendEMail(director.getEmailCode(),director.getEmail());
        return new ApiResponse("you are Successfully registered, Confirm your email to activate Accounting",true);
    }


    public Boolean sendEMail(String emailCode, String sendingEmail){
      try {
    SimpleMailMessage mailMessage = new SimpleMailMessage();
    mailMessage.setFrom("test@gmail.com");  // JÖNATILADIGAN EMAIL(IXTIYORIY EMAILNI YOZSA BÖLADI)
    mailMessage.setTo(sendingEmail);
    mailMessage.setSubject("Please Confirm Account");
    mailMessage.setText("<a href='http://localhost:8080/api/auth/verifyEmail/companyDirector?emailCode="+emailCode+"&email="+sendingEmail+"'>Please Confirm Account</a>");
    javaMailSender.send(mailMessage);
    return true;

    }catch (Exception e){
      return false;
      }
}


    public ApiResponse directorVerifyEmail(String emailCode, String email){
        Optional<EmpCustomer> optionalUser = employeeRepository.findByEmailCodeAndEmail(emailCode, email);
        if (optionalUser.isPresent()){
            EmpCustomer director = optionalUser.get();

               director.setEnabled(true);

               director.setEmailCode(null);
               employeeRepository.save(director);
               return new ApiResponse("Account was confirmed",true);
           }
               return new ApiResponse("Account was not confirmed",false);
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




