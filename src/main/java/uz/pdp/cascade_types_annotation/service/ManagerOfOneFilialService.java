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
import uz.pdp.cascade_types_annotation.repository.TourniquetCardRepository;
import uz.pdp.cascade_types_annotation.security.JwtProvider;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

@Service
public class ManagerOfOneFilialService {

    @Autowired
    RoleRepository roleRepository;
    @Autowired
    EmpCustomerRepository employeeRepository;
    @Autowired
    FilialRepository filialRepository;
    @Autowired
    TourniquetCardRepository tourniquetCardRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    JavaMailSender javaMailSender;

    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtProvider jwtProvider;


    public ApiResponse registerManager(UserRegisterDto managerDto){


        boolean existsByEmail = employeeRepository.existsByEmail(managerDto.getEmail());
        if (existsByEmail){
            return new ApiResponse("this email already exist",false);
        }

          EmpCustomer manager=new EmpCustomer();
        manager.setFirstname(managerDto.getFirstname());
        manager.setLastname(managerDto.getLastname());
        manager.setEmail(managerDto.getEmail());
        manager.setPassword(passwordEncoder.encode(managerDto.getPassword()));
        manager.setRoles(Collections.singleton(roleRepository.findByRoleName(RoleName.MANAGER_OF_ONE_FILIAL)));
        manager.setEmailCode(UUID.randomUUID().toString());
        employeeRepository.save(manager);

        sendEMail(manager.getEmailCode(), manager.getEmail());
        return new ApiResponse("You have successfully registered, confirm your email to activate the account",true);
    }

    public ApiResponse managerVerifyEmail(String emailCode, String email){

        Optional<EmpCustomer> optionalManager = employeeRepository.findByEmailCodeAndEmail(emailCode, email);

        if (optionalManager.isPresent()){
            EmpCustomer manager = optionalManager.get();

            manager.setEnabled(true);

            manager.setEmailCode(null);
            employeeRepository.save(manager);
            return new ApiResponse("Account was confirmed",true);
        }
        return new ApiResponse("Account was already confirmed",false);
    }

    public Boolean sendEMail(String emailCode, String sendingEmail){
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom("aslon.dinov@gmail.com");
            mailMessage.setTo(sendingEmail);
            mailMessage.setSubject("Please Confirm Account");
            mailMessage.setText("<a href='http://localhost:8080/api/auth/verifyEmail/managerOfOneFilial?emailCode="+emailCode+"&email="+sendingEmail+"'>Please Confirm Account</a>");
            javaMailSender.send(mailMessage);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public ApiResponse loginManager(LoginDto loginDto) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginDto.getEmail(), loginDto.getPassword()));


            EmpCustomer manager = (EmpCustomer) authentication.getPrincipal();

            String token = jwtProvider.generateToken(loginDto.getEmail(), manager.getRoles());
            return new ApiResponse("Token",true,token);

        }catch (BadCredentialsException  badCredentialsException){
            return new ApiResponse("password or login is error",false);
        }}

}










