package uz.pdp.cascade_types_annotation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.pdp.cascade_types_annotation.payload.CustomerDto;
import uz.pdp.cascade_types_annotation.payload.LoginDto;
import uz.pdp.cascade_types_annotation.service.ApiResponse;
import uz.pdp.cascade_types_annotation.service.CustomerService;

@RestController
@RequestMapping("/api/auth")
public class CustomerController {

    @Autowired
    CustomerService customerService;


    @PreAuthorize(value ="hasAnyRole('MANAGER_OF_FILIAL','FILIAL_EMPLOYEE')")
    @PostMapping(value = "/register/customer")
    public HttpEntity<?> addCustomer(@RequestBody CustomerDto registerDto) {
        ApiResponse apiResponse = customerService.registerCustomer(registerDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 201 : 409).body(apiResponse);
    }

    @PostMapping(value = "/login/customer")
    public HttpEntity<?> loginCustomer(@RequestBody LoginDto loginDto) {
        ApiResponse apiResponse = customerService.loginCustomer(loginDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 401).body(apiResponse);
    }


    @GetMapping(value = "/verifyEmail/customer")
    public HttpEntity<?> verifyEmail(@RequestParam String emailCode, @RequestParam String email) {
        ApiResponse apiResponse = customerService.managerVerifyEmail(emailCode, email);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }


}






