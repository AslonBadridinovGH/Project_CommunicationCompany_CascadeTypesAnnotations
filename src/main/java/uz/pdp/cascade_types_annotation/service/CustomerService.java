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
import uz.pdp.cascade_types_annotation.payload.CustomerDto;
import uz.pdp.cascade_types_annotation.payload.LoginDto;
import uz.pdp.cascade_types_annotation.repository.EmpCustomerRepository;
import uz.pdp.cascade_types_annotation.repository.RoleRepository;
import uz.pdp.cascade_types_annotation.repository.SimCardRepository;
import uz.pdp.cascade_types_annotation.security.JwtProvider;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

@Service
public class CustomerService {


    @Autowired
    RoleRepository roleRepository;

    @Autowired
    EmpCustomerRepository empCustomerRepository;

    @Autowired
    SimCardRepository simCardRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JavaMailSender javaMailSender;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtProvider jwtProvider;


    public ApiResponse registerCustomer(CustomerDto registerDto){


        boolean existsByEmail = empCustomerRepository.existsByEmail(registerDto.getEmail());
        if (existsByEmail){
            return new ApiResponse("such Email is already exist",false);
        }

          EmpCustomer customer=new EmpCustomer();
        customer.setFirstname(registerDto.getFirstname());
        customer.setLastname(registerDto.getLastname());

        // customer.setSimCards(simCards);

        if (registerDto.getKindOffCustomer()==1){
        customer.setKindOffCustomer(RoleName.JuristicPerson.name());}
        else if(registerDto.getKindOffCustomer()==2){
        customer.setKindOffCustomer(RoleName.NaturalPerson.name());}
        else {
            return new ApiResponse( "JuristicPerson =1;  \n" +
                                            "naturalPerson  =2;",false);
        }

        customer.setEmail(registerDto.getEmail());
        customer.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        customer.setRoles(Collections.singleton(roleRepository.findByRoleName(RoleName.CUSTOMER)));
        customer.setEmailCode(UUID.randomUUID().toString());
        empCustomerRepository.save(customer);
        // EMAILGA YUBORISH METHODINI CHAQIRYAPMIZ
        sendEMail(customer.getEmailCode(), customer.getEmail());
        return new ApiResponse("You have successfully registered," +
                "Confirm your email to activate Accounting",true);
    }

    public ApiResponse managerVerifyEmail(String emailCode, String email){

        Optional<EmpCustomer> optionalCustomer = empCustomerRepository.findByEmailCodeAndEmail(emailCode, email);

        if (optionalCustomer.isPresent()){
            EmpCustomer customer = optionalCustomer.get();

            customer.setEnabled(true);
// We will make the EmailCode null, because when clicks the link 2nd time, it will go to else(!optionalUser.isPresent())
// and return the message "Account already verified" without finding the EmailCode from the Link.
            customer.setEmailCode(null);
            empCustomerRepository.save(customer);
            return new ApiResponse("Account is confirmed",true);
        }
        return new ApiResponse("Account is not confirmed",false);
    }

    public void sendEMail(String emailCode, String sendingEmail){
        try {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setFrom("Teat@gmail.com");
            simpleMailMessage.setTo(sendingEmail);
            simpleMailMessage.setText("<a href='http://localhost:8080/api/auth/verifyEmail/customer?emailCode="+emailCode+"&email="+sendingEmail+"'>confirm </a>");
            javaMailSender.send(simpleMailMessage);
        }catch (Exception ignore){

        }
    }

    public ApiResponse loginCustomer(LoginDto loginDto) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginDto.getEmail(), loginDto.getPassword()));

            EmpCustomer customer = (EmpCustomer) authentication.getPrincipal();

            String token = jwtProvider.generateToken(loginDto.getEmail(), customer.getRoles());
            return new ApiResponse("Token",true,token);

        }catch (BadCredentialsException badCredentialsException){
            return new ApiResponse("password or login error",false);
        }}

}


