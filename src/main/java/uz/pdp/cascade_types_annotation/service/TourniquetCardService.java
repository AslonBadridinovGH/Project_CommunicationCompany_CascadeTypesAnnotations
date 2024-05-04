package uz.pdp.cascade_types_annotation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.pdp.cascade_types_annotation.entity.Company;
import uz.pdp.cascade_types_annotation.entity.EmpCustomer;
import uz.pdp.cascade_types_annotation.entity.TourniquetCard;
import uz.pdp.cascade_types_annotation.entity.enums.ServiceName;
import uz.pdp.cascade_types_annotation.payload.TourniquetCardDto;
import uz.pdp.cascade_types_annotation.repository.CompanyRepository;
import uz.pdp.cascade_types_annotation.repository.EmpCustomerRepository;
import uz.pdp.cascade_types_annotation.repository.TourniquetCardRepository;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TourniquetCardService {

    @Autowired
    TourniquetCardRepository tourniquetCardRepository;
    @Autowired
    CompanyRepository  companyRepository;
    @Autowired
    EmpCustomerRepository empCustomerRepository;


    public ApiResponse addTourniquetCard(TourniquetCardDto turnKetDto) {

        Optional<Company> optionalCompany = companyRepository.findById(turnKetDto.getCompanyId());
        if (!optionalCompany.isPresent())
            return new ApiResponse("company was not found", false);

        Optional<EmpCustomer> optionalEmployee = empCustomerRepository.findById(turnKetDto.getEmployeeId());
        if (!optionalEmployee.isPresent())
            return new ApiResponse("employee was not found", false);

          TourniquetCard turnKet=new TourniquetCard();

        turnKet.setExpireDate(LocalDate.parse(turnKetDto.getExpireDate()));
        turnKet.setCompany(optionalCompany.get());
        turnKet.setEmpCustomer(optionalEmployee.get());

      if (Period.between(LocalDate.parse(turnKetDto.getExpireDate()), LocalDate.now()).getDays() >=0){
           turnKet.setStatus(ServiceName.ACTUAL.name());
       }
      else if (Period.between(LocalDate.now(),LocalDate.parse(turnKetDto.getExpireDate())).getDays()<0){
           turnKet.setStatus(ServiceName.EXPIRED.name());
      }

      tourniquetCardRepository.save(turnKet);
      return new ApiResponse("Successful saved",true);
    }

    public ApiResponse editTourniquetCard(UUID id, TourniquetCardDto turnKetDto) {

         Optional<TourniquetCard> optionalTurnKet = tourniquetCardRepository.findById(id);
        if (!optionalTurnKet.isPresent()) {
            return new ApiResponse("tourniquet was not found", false); }

         Optional<Company> optionalCompany = companyRepository.findById(turnKetDto.getCompanyId());
        if (!optionalCompany.isPresent())
            return new ApiResponse("company  was not found", false);

         Optional<EmpCustomer> optionalEmployee = empCustomerRepository.findById(turnKetDto.getEmployeeId());
        if (!optionalEmployee.isPresent())
            return new ApiResponse("employee  was not found", false);

          TourniquetCard turnKet = optionalTurnKet.get();

         turnKet.setExpireDate(LocalDate.parse(turnKetDto.getExpireDate()));
         turnKet.setCompany(optionalCompany.get());
         turnKet.setEmpCustomer(optionalEmployee.get());

        if (Period.between(LocalDate.parse(turnKetDto.getExpireDate()), LocalDate.now()).getDays() >=0)
        {  turnKet.setStatus(ServiceName.ACTUAL.name()); }
        else if (Period.between(LocalDate.now(),LocalDate.parse(turnKetDto.getExpireDate())).getDays()<0)
        {  turnKet.setStatus(ServiceName.EXPIRED.name()); }

        tourniquetCardRepository.save(turnKet);
        return new ApiResponse("Successful edited", true);
    }

    public ApiResponse getTourniquetCardById(UUID id) {

        Optional<TourniquetCard> optionalTourniquetCard = tourniquetCardRepository.findById(id);
        return optionalTourniquetCard.map( tourniquetCard -> new ApiResponse("tourniquet  not found", true,tourniquetCard)
        ).orElseGet( () -> new ApiResponse("tourniquet was not found", false));
    }

    public ApiResponse getTourniquetCards() {
        List<TourniquetCard> all = tourniquetCardRepository.findAll();
        return new ApiResponse(all,"tourniquetCards",true);
    }

    public ApiResponse deleteTourniquetCard(UUID id) {

        Optional<TourniquetCard> optionalTurnKet = tourniquetCardRepository.findById(id);
        if (optionalTurnKet.isPresent())
            try {
                tourniquetCardRepository.deleteById(id);
                return new ApiResponse("tourniquet was deleted", true);
            } catch (Exception e) {
                return new ApiResponse("tourniquet was not deleted", false);
            }
        return new ApiResponse("tourniquet was found", false);
    }

}

