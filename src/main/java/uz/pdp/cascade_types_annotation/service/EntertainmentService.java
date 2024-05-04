package uz.pdp.cascade_types_annotation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.pdp.cascade_types_annotation.entity.InfosEntertainment;
import uz.pdp.cascade_types_annotation.entity.SimCard;
import uz.pdp.cascade_types_annotation.entity.enums.ServiceName;
import uz.pdp.cascade_types_annotation.payload.InfosEntertainmentDto;
import uz.pdp.cascade_types_annotation.repository.InfosEntertainmentRepository;
import uz.pdp.cascade_types_annotation.repository.SimCardRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class EntertainmentService {

    @Autowired
    InfosEntertainmentRepository infoRepository;

    @Autowired
    SimCardRepository simCardRepository;


    public ApiResponse getEntertainmentPopular() {
        int mb=0, sms=0, voice=0;

        List<InfosEntertainment> all = infoRepository.findAllByIsActiveTrue();
        for (InfosEntertainment infosEntertainment : all) {
            if (infosEntertainment.getCategory().equals(ServiceName.MB.name())){
                mb++;
            }
            else if (infosEntertainment.getCategory().equals(ServiceName.SMS.name())){
                sms++;
            }else if (infosEntertainment.getCategory().equals(ServiceName.VOICE.name())){
                voice++;
            }
        }
        return new ApiResponse("MB : "+mb+"SMS : "+sms+"voice : "+voice,true);
    }

    public ApiResponse addEntertainment(InfosEntertainmentDto infDto) {

          InfosEntertainment inf=new InfosEntertainment();

        inf.setName(infDto.getName());
        inf.setPriceProTypeOfPeriod(infDto.getPriceProTypeOfPeriod());
        inf.setDeadline(infDto.getDeadline());

        if (infDto.getCategory()==1){
            inf.setCategory(ServiceName.MB.name());
        }else if (infDto.getCategory()==2){
            inf.setCategory(ServiceName.SMS.name());
        }else if (infDto.getCategory()==3){
            inf.setCategory(ServiceName.VOICE.name());
        }else {
            return new ApiResponse("1==MB; 2=SMS; 3=VOICE",false);
        }

        if (infDto.getTypeOfPeriod()==1){
            inf.setTypeOfPeriod(ServiceName.DAILY.name());
        }else if (infDto.getTypeOfPeriod()==2){
            inf.setTypeOfPeriod(ServiceName.MONTHLY.name());
        }else {
            return new ApiResponse("1==DAILY; 2=MONTHLY ",false);
        }
        infoRepository.save(inf);
        return new ApiResponse("Entertainment saved",true);
    }

    public ApiResponse editEntertainment(UUID id, InfosEntertainmentDto infDto) {

        Optional<InfosEntertainment> byId = infoRepository.findById(id);
        if (!byId.isPresent()) {
            return new ApiResponse("Entertainment  not found",false);
        }


        Optional<SimCard> byPackagesId = simCardRepository.findByEntertainmentsId(id);
        if(!byPackagesId.isPresent()) return new ApiResponse("simCard  not found", false);


          InfosEntertainment inf = byId.get();
        inf.setName(infDto.getName());
        inf.setPriceProTypeOfPeriod(infDto.getPriceProTypeOfPeriod());
        inf.setDeadline(infDto.getDeadline());
        inf.setSimCard(byPackagesId.get());

        if (infDto.getCategory()==1){
            inf.setCategory(ServiceName.MB.name());
        }else if (infDto.getCategory()==2){
            inf.setCategory(ServiceName.SMS.name());
        }else if (infDto.getCategory()==3){
            inf.setCategory(ServiceName.VOICE.name());
        }else {
            return new ApiResponse("1==MB; 2=SMS; 3=VOICE",false);
        }

        if (infDto.getTypeOfPeriod()==1){
            inf.setTypeOfPeriod(ServiceName.DAILY.name());
        }else if (infDto.getCategory()==2){
            inf.setTypeOfPeriod(ServiceName.MONTHLY.name());
        }else {
            return new ApiResponse("1==DAILY; 2=MONTHLY ",false);
        }
        infoRepository.save(inf);
        return new ApiResponse("Entertainment  edited",true);
    }

    public ApiResponse deleteEntertainment(UUID id) {

        Optional<InfosEntertainment> byId1 = infoRepository.findById(id);
        if (!byId1.isPresent())
            return new ApiResponse("Entertainment  not found",false);
       try {
           infoRepository.deleteById(id);
           return new ApiResponse("Entertainment  deleted",true);
       }catch (Exception e)
       {
           return new ApiResponse("Entertainment  not found",false);
      }

    }

}
