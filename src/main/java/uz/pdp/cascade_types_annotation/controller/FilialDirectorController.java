package uz.pdp.cascade_types_annotation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.pdp.cascade_types_annotation.payload.LoginDto;
import uz.pdp.cascade_types_annotation.payload.UserRegisterDto;
import uz.pdp.cascade_types_annotation.service.ApiResponse;
import uz.pdp.cascade_types_annotation.service.FilialDirectorService;


@RestController
@RequestMapping("/api/auth")
public class FilialDirectorController {


     @Autowired
     FilialDirectorService filialDirectorService;


     @PreAuthorize(value ="hasRole('FILIAL_MANAGER')")
     @PostMapping("/register/filialDirector")
     public HttpEntity<?>registerDirector( @RequestBody UserRegisterDto filialDirectorDto){
        ApiResponse apiResponse = filialDirectorService.registerDirector(filialDirectorDto);
        return ResponseEntity.status(apiResponse.isSuccess()?201:409).body(apiResponse);
     }

     @PostMapping("/login/filialDirector")
     public HttpEntity<?>loginDirector(@RequestBody LoginDto loginDto){
        ApiResponse apiResponse=filialDirectorService.loginDirector(loginDto);
        return ResponseEntity.status(apiResponse.isSuccess()?200:401).body(apiResponse);
     }

     // THIS METHOD WORKS WHEN LINK IS CLICKED AND EXTRACTS EMAIL AND EMAIL CODE FROM THE LINK.
     @GetMapping("/verifyEmail/filialDirector")
     public HttpEntity<?>employeeVerifyEmail(@RequestParam String emailCode, @RequestParam String email){
         ApiResponse apiResponse=filialDirectorService.directorVerifyEmail(emailCode,email);
         return ResponseEntity.status(apiResponse.isSuccess()?200:409).body(apiResponse);
     }

}










