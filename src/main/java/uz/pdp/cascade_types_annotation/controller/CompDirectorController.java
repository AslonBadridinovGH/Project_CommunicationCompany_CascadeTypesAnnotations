package uz.pdp.cascade_types_annotation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.cascade_types_annotation.payload.LoginDto;
import uz.pdp.cascade_types_annotation.payload.UserRegisterDto;
import uz.pdp.cascade_types_annotation.service.ApiResponse;
import uz.pdp.cascade_types_annotation.service.ComDirectorService;


@RestController
@RequestMapping("/api/auth")
public class CompDirectorController {


     @Autowired
     ComDirectorService companyDirectorService;


     @PostMapping("/register/companyDirector")
     public HttpEntity<?>registerDirector(@RequestBody UserRegisterDto registerDto){
        ApiResponse apiResponse = companyDirectorService.registerDirector(registerDto);
        return ResponseEntity.status(apiResponse.isSuccess()?201:409).body(apiResponse);
     }

     @PostMapping("/login/companyDirector")
     public HttpEntity<?>loginDirector(@RequestBody LoginDto loginDto){
        ApiResponse apiResponse=companyDirectorService.loginDirector(loginDto);
        return ResponseEntity.status(apiResponse.isSuccess()?200:401).body(apiResponse);
     }


     @GetMapping("/verifyEmail/companyDirector")
     public HttpEntity<?>employeeVerifyEmail(@RequestParam String emailCode, @RequestParam String email){
         ApiResponse apiResponse=companyDirectorService.directorVerifyEmail(emailCode,email);
         return ResponseEntity.status(apiResponse.isSuccess()?200:409).body(apiResponse);
     }


}










