package uz.pdp.cascade_types_annotation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.pdp.cascade_types_annotation.controller.FilialEmployeeController;
import uz.pdp.cascade_types_annotation.entity.EmpCustomer;
import uz.pdp.cascade_types_annotation.entity.enums.RoleName;
import uz.pdp.cascade_types_annotation.payload.UserRegisterDto;
import uz.pdp.cascade_types_annotation.payload.LoginDto;
import uz.pdp.cascade_types_annotation.payload.RegisterDto;
import uz.pdp.cascade_types_annotation.repository.EmpCustomerRepository;
import uz.pdp.cascade_types_annotation.repository.FilialRepository;
import uz.pdp.cascade_types_annotation.repository.RoleRepository;
import uz.pdp.cascade_types_annotation.repository.TourniquetCardRepository;
import uz.pdp.cascade_types_annotation.security.JwtProvider;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

@Service
public class FilialEmployeeService {


    @Autowired
    EmpCustomerRepository employeeRepository;

    @Autowired
    FilialRepository filialRepository;
    @Autowired
    TourniquetCardRepository tourniquetCardRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JavaMailSender javaMailSender;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtProvider jwtProvider;

    @Autowired
    FilialEmployeeController filialEmployeeController;


    public UUID getEmployeeById(){
          EmpCustomer principal = (EmpCustomer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
          return principal.getId();
          /*  SpringSecurityAuditAwareImpl springSecurityAuditAware=new SpringSecurityAuditAwareImpl();
          if (springSecurityAuditAware.getCurrentAuditor().isPresent()) return springSecurityAuditAware.getCurrentAuditor().get();
          else return null;*/
      }

    public ApiResponse registerEmployee(UserRegisterDto registerDto){

        boolean existsByEmail = employeeRepository.existsByEmail(registerDto.getEmail());
        if (existsByEmail){
            return new ApiResponse("this email already exist",false);
        }

           EmpCustomer employee=new EmpCustomer();
        employee.setFirstname(registerDto.getFirstname());
        employee.setLastname(registerDto.getLastname());
        employee.setEmail(registerDto.getEmail());
        employee.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        employee.setRoles(Collections.singleton(roleRepository.findByRoleName(RoleName.FILIAL_EMPLOYEE)));
        employee.setEmailCode(UUID.randomUUID().toString());
        employeeRepository.save(employee);


        sendEMail(employee.getEmailCode(),employee.getEmail());
        return new ApiResponse("You have successfully registered, confirm your email to activate the account",true);
    }

    public ApiResponse getFilialEmployeeById(UUID uuid) {
         if (!uuid.equals(getEmployeeById()) ) {
             return new ApiResponse("You do not have access to this information",false);
         }
         Optional<EmpCustomer> byId = employeeRepository.findById(uuid);
        return byId.map(employee -> new ApiResponse("", true, employee)).orElseGet(() -> new ApiResponse("Employee was not found", false));
    }

    public ApiResponse editFilialEmployee(UUID id, RegisterDto registerDto) {

        if ( !id.equals(getEmployeeById()) ) {
            return new ApiResponse("You do not have access to this information",false);
        }

        Optional<EmpCustomer> byId = employeeRepository.findById(id);
        if (!byId.isPresent()) {
            return new ApiResponse("Employee  not found",false);
        }

        boolean exists = employeeRepository.existsByEmailNot(registerDto.getEmail());
        if (exists) {
            return new ApiResponse("Such email  already exist",false);
        }

          EmpCustomer employee = byId.get();
        employee.setFirstname(registerDto.getFirstname());
        employee.setLastname(registerDto.getLastname());
        employee.setEmail(registerDto.getEmail());
        employee.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        employee.setRoles(Collections.singleton(roleRepository.findByRoleName(RoleName.FILIAL_EMPLOYEE)));
        employee.setEmailCode(UUID.randomUUID().toString());
        employeeRepository.save(employee);

        sendEMail(employee.getEmail(), employee.getEmailCode());
       return new ApiResponse("You have successfully registered, confirm your email to activate the account",true);

    }

    public ApiResponse deleteFilialEmployee(UUID uuid) {
        Optional<EmpCustomer> byId = employeeRepository.findById(uuid);
        if (!byId.isPresent()){
            return new ApiResponse("Employee  not found",false);
        }
        try {
            employeeRepository.deleteById(uuid);
            return new ApiResponse("Employee  deleted",true);
        }catch (Exception e){
            return new ApiResponse("Employee  deleted",true);
        }
    }

    public Boolean sendEMail(String emailCode,String sendingEmail){
      try {
    SimpleMailMessage mailMessage = new SimpleMailMessage();
    mailMessage.setFrom("aslon.dinov@gmail.com");
    mailMessage.setTo(sendingEmail);
    mailMessage.setSubject("Account confirm");
    mailMessage.setText("<a href='http://localhost:8080/api/auth/verifyEmail/filialEmployee?emailCode="+emailCode+"&email="+sendingEmail+"'>confirm</a>");
    javaMailSender.send(mailMessage);
    return true;
    }catch (Exception e){
      return false;
      }
}


    public ApiResponse employeeVerifyEmail(String emailCode,String email){
        Optional<EmpCustomer> optionalUser = employeeRepository.findByEmailCodeAndEmail(emailCode,email);
        if (optionalUser.isPresent()){
            EmpCustomer employee = optionalUser.get();

               employee.setEnabled(true);

               employee.setEmailCode(null);
               employeeRepository.save(employee);
               return new ApiResponse("Account  confirmed",true);
           }
               return new ApiResponse("Account  already confirmed",false);
    }

    // THIS METHOD COMPARES THE USERNAME AND PASSWORD IN THE DB AND CHECKS THAT THEY ARE NOT FALSE AGAINST THE 4 BOOLEAN FIELDS IN THE USER ENTITY.
    public ApiResponse loginEmployee(LoginDto loginDto) {
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


