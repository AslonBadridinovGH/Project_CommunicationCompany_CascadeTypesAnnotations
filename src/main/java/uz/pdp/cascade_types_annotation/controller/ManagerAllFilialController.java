package uz.pdp.cascade_types_annotation.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.pdp.cascade_types_annotation.payload.LoginDto;
import uz.pdp.cascade_types_annotation.payload.UserRegisterDto;
import uz.pdp.cascade_types_annotation.service.ApiResponse;
import uz.pdp.cascade_types_annotation.service.ManagerOfAllFilialService;

@RestController
@RequestMapping("/api/auth")
public class ManagerAllFilialController {

    @Autowired
    ManagerOfAllFilialService managerService;

    @PreAuthorize(value ="hasRole('COMPANY_DIRECTOR')")
    @PostMapping(value = "/register/managerOfAllFilial")
    public HttpEntity<?> registerManager(@RequestBody UserRegisterDto managerDto) {
        ApiResponse apiResponse = managerService.registerManager(managerDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 201 : 409).body(apiResponse);
    }


    @PostMapping(value = "/login/managerOfAllFilial")
    public HttpEntity<?> loginManager(@RequestBody LoginDto loginDto) {
        ApiResponse apiResponse = managerService.loginManager(loginDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 401).body(apiResponse);
    }

    @GetMapping(value = "/verifyEmail/managerOfAllFilial")
    public HttpEntity<?> verifyEmail(@RequestParam String emailCode, @RequestParam String email) {
        ApiResponse apiResponse = managerService.managerVerifyEmail(emailCode, email);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

}






