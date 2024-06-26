package uz.pdp.cascade_types_annotation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.pdp.cascade_types_annotation.entity.Tariff;
import uz.pdp.cascade_types_annotation.payload.TariffDto;
import uz.pdp.cascade_types_annotation.repository.SimCardRepository;
import uz.pdp.cascade_types_annotation.repository.TariffRepository;

import java.time.LocalDate;
import java.util.*;

@Service
public class TariffService {

    @Autowired
    TariffRepository tariffRepository;


    @Autowired
    SimCardRepository simCardRepository;

    private static final Double  MB = 0.5;
    private static final Double EXTERN_CALL =0.3;
    private static final Double SMS =0.1;
    private static final Double INTERN_CALL =0.1;


    public ApiResponse addTariff(TariffDto tariffDto) {

           Tariff tariff=new Tariff();

        tariff.setName(tariffDto.getName());

        LocalDate ldExpire=LocalDate.parse(tariffDto.getExpireDate());
        tariff.setExpireDate(ldExpire);
        tariff.setDeadLine(tariffDto.getDeadLine());
        tariff.setMb(tariffDto.getMb());
        tariff.setSms(tariffDto.getSms());
        tariff.setMinuteBetweenInternSet(tariffDto.getMinuteBetweenInternSet());
        tariff.setMinuteBetweenExternSet(tariffDto.getMinuteBetweenExternSet());

        tariff.setIsForJusticePerson(tariffDto.getIsForJusticePerson());

        tariff.setTransitionPrice(tariffDto.getTransitionPrice());
        tariff.setPrice(tariffDto.getPrice());

        tariff.setPriceForMb(tariffDto.getPrice()*MB);
        tariff.setPriceForSms(tariffDto.getPrice()*SMS);
        tariff.setPriceForMinuteBetweenInternSet(tariffDto.getPrice()* INTERN_CALL);
        tariff.setPriceForMinuteBetweenExternSet(tariffDto.getPrice()* EXTERN_CALL);

         // tariff.setSimCards(null);
         tariffRepository.save(tariff);
        return new ApiResponse("Tariff was saved",true);
    }


    public ApiResponse getTariff() {
        List<Tariff> all = tariffRepository.findAll();
        return new ApiResponse("",true,all);
    }

    public ApiResponse editTariff(UUID id, TariffDto tariffDto) {

         Optional<Tariff> byId1 = tariffRepository.findById(id);
        if (!byId1.isPresent())
            return new ApiResponse("Tariff was not found",false);

         Tariff tariff = byId1.get();
        tariff.setName(tariffDto.getName());

         LocalDate ldExpire=LocalDate.parse(tariffDto.getExpireDate());
        tariff.setExpireDate(ldExpire);
        tariff.setDeadLine(tariffDto.getDeadLine());

        tariff.setMb(tariffDto.getMb());
        tariff.setSms(tariffDto.getSms());
        tariff.setMinuteBetweenInternSet(tariffDto.getMinuteBetweenInternSet());
        tariff.setMinuteBetweenExternSet(tariffDto.getMinuteBetweenExternSet());

        tariff.setIsForJusticePerson(tariffDto.getIsForJusticePerson());

        tariff.setTransitionPrice(tariffDto.getTransitionPrice());
        tariff.setPrice(tariffDto.getPrice());

        tariff.setPriceForMb(tariffDto.getPrice() * MB);
        tariff.setPriceForSms(tariffDto.getPrice() * SMS);
        tariff.setPriceForMinuteBetweenInternSet(tariffDto.getPrice() * INTERN_CALL);
        tariff.setPriceForMinuteBetweenExternSet(tariffDto.getPrice() * EXTERN_CALL);

        tariffRepository.save(tariff);
        return new ApiResponse("Tariff was saved",true);
    }

    public ApiResponse deleteTariff(UUID id) {

        Optional<Tariff> byId1 = tariffRepository.findById(id);
        if (!byId1.isPresent())
            return new ApiResponse("Tariff was not found",false);
       try {
           tariffRepository.deleteById(id);
           return new ApiResponse("Tariff was deleted",true);
       }catch (Exception e)
       {
           return new ApiResponse("Tariff was not found",false);
      }
    }

}



