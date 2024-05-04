package uz.pdp.cascade_types_annotation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.pdp.cascade_types_annotation.entity.TourniquetCard;
import uz.pdp.cascade_types_annotation.entity.TourniquetCardHistory;
import uz.pdp.cascade_types_annotation.payload.TourniquetHistoryDto;
import uz.pdp.cascade_types_annotation.repository.TourniquetCardRepository;
import uz.pdp.cascade_types_annotation.repository.TourniquetHistoryRepository;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TourniquetHistoryService {

    @Autowired
    TourniquetHistoryRepository  tourniquetHistoryRepository;

    @Autowired
    TourniquetCardRepository     tourniquetCardRepository;


    public ApiResponse addTourniquetHistory(TourniquetHistoryDto historyDto) {

          Optional<TourniquetCard> cardOptional = tourniquetCardRepository.findById(historyDto.getCardId());
        if (!cardOptional.isPresent())
            return new ApiResponse("TourniquetCard was not found",false);

            TourniquetCardHistory history=new TourniquetCardHistory();
           history.setTourniquetCard(cardOptional.get());

          Date date1, date2;
          DateFormat dateFormat2=new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");
      try {
            date1=dateFormat2.parse(historyDto.getGetInTime());
            Timestamp sql_Timestamp1=new Timestamp(date1.getTime());
            history.setGetInTime(sql_Timestamp1);

            date2=dateFormat2.parse(historyDto.getGetOutTime());
            Timestamp sql_Timestamp2=new Timestamp(date2.getTime());
            history.setGetOutTime(sql_Timestamp2);
         } catch (Exception exception) {
           return new ApiResponse("Please give Date in this form:  dd.MM.yyyy hh:mm:ss",false);
         }
         tourniquetHistoryRepository.save(history);
         return new ApiResponse("TourniquetCard History was saved",true);
    }

    public ApiResponse editTourniquetHistory(UUID id, TourniquetHistoryDto historyDto) {

          Optional<TourniquetCardHistory> tourniquetCardHistory = tourniquetHistoryRepository.findById(id);
        if (!tourniquetCardHistory.isPresent()) {
           return new ApiResponse("tourniquetHistory was not found", false);}

          Optional<TourniquetCard> cardOptional = tourniquetCardRepository.findById(historyDto.getCardId());
        if (!cardOptional.isPresent())
           return new ApiResponse("TourniquetCard was not found",false);

          TourniquetCardHistory history = tourniquetCardHistory.get();
          history.setTourniquetCard(cardOptional.get());

        Date date1, date2;
        DateFormat dateFormat2=new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");
        try {
            date1=dateFormat2.parse(historyDto.getGetInTime());
            Timestamp sql_Timestamp1=new Timestamp(date1.getTime());
            history.setGetInTime(sql_Timestamp1);

            date2=dateFormat2.parse(historyDto.getGetOutTime());
            Timestamp sql_Timestamp2=new Timestamp(date2.getTime());
            history.setGetOutTime(sql_Timestamp2);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        tourniquetHistoryRepository.save(history);
        return new ApiResponse("History successful edited", true);
   }

    public ApiResponse getTourniquetHistory(UUID id) {
        Optional<TourniquetCardHistory> optionalTurnKet = tourniquetHistoryRepository.findById(id);
        return optionalTurnKet.map(tourniquetHistory -> new ApiResponse("History was found", true, tourniquetHistory)).orElseGet(() -> new ApiResponse("History was not found", false));
    }

    public ApiResponse deleteTourniquetHistory(UUID id) {

        Optional<TourniquetCardHistory> optionalTurnKet = tourniquetHistoryRepository.findById(id);
        if (optionalTurnKet.isPresent()){
            try {
                tourniquetHistoryRepository.deleteById(id);
                return new ApiResponse("TourniquetHistory  was deleted", true);
            } catch (Exception e) {
                return new ApiResponse("TourniquetHistory  was not deleted", false);
            }
        }
        return new ApiResponse("TourniquetHistory was not found", false);
    }

    public ApiResponse getTourniquetHistories() {
        List<TourniquetCardHistory> all = tourniquetHistoryRepository.findAll();
        return new ApiResponse("tourniquetHistory", true, all);

    }

}
