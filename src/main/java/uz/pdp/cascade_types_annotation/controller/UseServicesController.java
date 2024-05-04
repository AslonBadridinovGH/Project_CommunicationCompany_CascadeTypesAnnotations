
package uz.pdp.cascade_types_annotation.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import uz.pdp.cascade_types_annotation.payload.UseServicesDto;
import uz.pdp.cascade_types_annotation.service.ApiResponse;
import uz.pdp.cascade_types_annotation.service.SimCardService;
import uz.pdp.cascade_types_annotation.service.UseServicesService;

@RestController
@RequestMapping("/api/auth")
public class UseServicesController {

    @Autowired
    SimCardService simCardService;
    @Autowired
    UseServicesService actionService;


    @Transactional
    @PreAuthorize(value ="hasAnyRole('CUSTOMER')")
    @PostMapping(value = "/add/action")
    public HttpEntity<?> addAction(@RequestBody UseServicesDto actionDto) {
        ApiResponse apiResponse = actionService.addAction(actionDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 201 : 409).body(apiResponse);
    }

    @PreAuthorize(value ="hasAnyRole('COMPANY_DIRECTOR','TARIFF_MANAGER')")
    @GetMapping(value = "/get/action")
    public HttpEntity<?> getAction() {
        ApiResponse apiResponse = actionService.getAction();
        return ResponseEntity.status(apiResponse.isSuccess() ? 201 : 409).body(apiResponse);
    }

}

