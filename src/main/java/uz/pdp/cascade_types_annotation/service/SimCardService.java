package uz.pdp.cascade_types_annotation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.pdp.cascade_types_annotation.payload.CompanyBudgetDto;
import uz.pdp.cascade_types_annotation.entity.Package;
import uz.pdp.cascade_types_annotation.entity.*;
import uz.pdp.cascade_types_annotation.entity.enums.UssdCodsName;
import uz.pdp.cascade_types_annotation.payload.SimCardDto;
import uz.pdp.cascade_types_annotation.repository.*;

import java.time.LocalDate;
import java.time.Period;
import java.util.*;

@Service
public class SimCardService {

    @Autowired
    SimCardRepository simcardRepository;
    @Autowired
    EmpCustomerRepository customerRepository;
    @Autowired
    PackageRepository packageRepository;
    @Autowired
    TariffRepository tariffRepository;
    @Autowired
    InfosEntertainmentRepository entertainmentRepository;
    @Autowired
    BudgetRepository budgetRepository;

    @Autowired
    CompanyBudgetService budgetService;


    public ApiResponse addSimCard(SimCardDto simCardDto) {

          SimCard simCard=new SimCard();

        simCard.setCompanyCode(simCardDto.getCompanyCode());
        simCard.setBalance(simCardDto.getBalance());
        simCard.setCanBeInMinusBalance(simCardDto.getCanBeInMinusBalance());

         String simNumb = simCard.getUzbCode()+ simCard.getCompanyCode() + simCardDto.getNumber();
        boolean exists = simcardRepository.existsByNumber(simNumb);
        if (exists) {
            return new ApiResponse("Such number already exist",false);
        }

        Optional<EmpCustomer> optionalCustomer = customerRepository.findById(simCardDto.getCustomerId());
        if (!optionalCustomer.isPresent()) return new ApiResponse("Customer was not found",false);

        Optional<Tariff> optionalTariff = tariffRepository.findById(simCardDto.getTariffId());
        if (!optionalTariff.isPresent())   return new ApiResponse("Tariff was not found",false);

        // SIM CARD TARIFF
        Tariff tariff = optionalTariff.get();
        if (tariff.getIsActive()) {
            return new ApiResponse("This Tariff is already active "+tariff.getName(), false);
        }

         if ( (simCard.getBalance() >= (tariff.getPrice() + tariff.getTransitionPrice())) || simCard.getCanBeInMinusBalance() )
         {
            simCard.setTariff(optionalTariff.get());
            simCard.setTariffLastActiveDay(LocalDate.now());
            simCard.setBalance(simCard.getBalance()- (tariff.getPrice()+tariff.getTransitionPrice()) );

            double incomeCompany=tariff.getPrice()+tariff.getTransitionPrice();
            budgetService.addCompanyBudget(new CompanyBudgetDto(UssdCodsName.ACTIVATE_TARIFF.name(),incomeCompany,LocalDate.now()));

         }
        else { return new ApiResponse("Balance of SimCard saved not enough",false); }
        tariff.setIsActive(true);
         // tariffRepository.save(tariff);

         // SIM CARD NING PACKAGE
         if (simCardDto.getPackageIds() != null)
         {
              Set <Package> packages = new HashSet<>();

            List <UUID> aPackageIds = simCardDto.getPackageIds();
            for (UUID aPackageId : aPackageIds)
              {
                 Optional<Package> optionalPackage = packageRepository.findById(aPackageId);
                 if (!optionalPackage.isPresent()) {
                     return new ApiResponse("This package was not found " + optionalPackage, false);
                 }
                 if (optionalPackage.get().getIsPackageSold() && optionalPackage.get().getIsPackageSold() != null){
                     return new ApiResponse("This Package is already active "+optionalPackage.get(), false);
                 }
                  packages.add(optionalPackage.get());
              }


                double packagePrice=0;
                for (Package aPackage : packages) {packagePrice= packagePrice + aPackage.getPrice();}

                // check Balance
                if( (simCard.getBalance() >= packagePrice) || simCard.getCanBeInMinusBalance() )
                {
                    simCard.setPackages(packages);
                    simCard.setBalance(simCard.getBalance() - packagePrice );
                   budgetService.addCompanyBudget(new CompanyBudgetDto(UssdCodsName.BUY_PACKAGE.name(),packagePrice,LocalDate.now()));
                }else {return new ApiResponse("Balance of SimCard not enough",false);}

               for (Package aPackage : packages) {
                   aPackage.setIsPackageSold(true);
                   aPackage.setDayOfPackageSold(LocalDate.now());
                   // aPackage.setSimCards(Collections.singleton(simCard));
                   // packageRepository.save(aPackage);
               }
           }

        // IF InfosEntertainment IS ALSO INCLUDED WHEN SIM CARD IS SOLD
         if (simCardDto.getEntertainmentIds() != null)
         {
                  Set<InfosEntertainment> infosEntertainments = new HashSet<>();
                  List<UUID> entertainmentIds = simCardDto.getEntertainmentIds();
                  for (UUID entertainmentId : entertainmentIds) {
                      Optional<InfosEntertainment> optionalInfosEntertainment = entertainmentRepository.findById(entertainmentId);
                      if (!optionalInfosEntertainment.isPresent()) {
                          return new ApiResponse("This infosEntertainment not found " + optionalInfosEntertainment, false);
                      }
                      InfosEntertainment infosEntertainment = optionalInfosEntertainment.get();
                      if (infosEntertainment.getIsActive() && infosEntertainment.getIsActive() != null)
                          return new ApiResponse("This Entertainment is already active "+infosEntertainment.getName(), false);

                      infosEntertainments.add(optionalInfosEntertainment.get());
                  }

               // JAMI InfosEntertainment / NARXI
               double entertainmentPrice = 0;
               for (InfosEntertainment infosEntertainment : infosEntertainments) {
                   entertainmentPrice = entertainmentPrice + (infosEntertainment.getPriceProTypeOfPeriod() * infosEntertainment.getDeadline());
               }

               // check Balance
               if ((simCard.getBalance() >= entertainmentPrice) || simCard.getCanBeInMinusBalance()) {
                   simCard.setEntertainments(infosEntertainments);
                   simCard.setBalance(simCard.getBalance() - entertainmentPrice);
                   budgetService.addCompanyBudget(new CompanyBudgetDto(UssdCodsName.BUY_INFO_ENTERTAINMENT.name(),
                           entertainmentPrice, LocalDate.now()));
               } else {
                   return new ApiResponse("Balance of SimCard saved not enough", false);
               }

               for (InfosEntertainment infosEntertainment : infosEntertainments) {
                   infosEntertainment.setIsActive(true);
               }
        }

        simCard.setNumber(simNumb);
        simCard.setCustomer(optionalCustomer.get());
        simCard.setEnabled(true);

        simcardRepository.save(simCard);
        return new ApiResponse("SimCard was saved",true);
    }

    public ApiResponse editSimCard(UUID id, SimCardDto simCardDto) {

        Optional<SimCard> byId_ = simcardRepository.findById(id);
        if (!byId_.isPresent()) {
            return new ApiResponse("Sim Card was not found", false);
        }

        SimCard simCard = byId_.get();

        simCard.setCompanyCode(simCardDto.getCompanyCode());
        simCard.setBalance(simCardDto.getBalance());
        simCard.setCanBeInMinusBalance(simCardDto.getCanBeInMinusBalance());

        String simNumb = simCard.getUzbCode() + simCard.getCompanyCode() + simCardDto.getNumber();
        boolean exists = simcardRepository.existsByNumberNot(simNumb);
        if (exists) return new ApiResponse("Such number is already exist", false);

        Optional<EmpCustomer> byId1 = customerRepository.findById(simCardDto.getCustomerId());
        if (!byId1.isPresent()) return new ApiResponse("Customer was not found", false);

        Optional<Tariff> optionalTariff = tariffRepository.findById(simCardDto.getTariffId());
        if (!optionalTariff.isPresent()) return new ApiResponse("Tariff was not found", false);


        // SIM CARD  TARIFF
        Tariff tariff = optionalTariff.get();
        if ((simCard.getEnabled() && simCard.getBalance() >= (tariff.getPrice() + tariff.getTransitionPrice())) || simCard.getCanBeInMinusBalance()) {
            simCard.setTariff(optionalTariff.get());
            simCard.setTariffLastActiveDay(LocalDate.now());
            simCard.setBalance(simCard.getBalance() - (tariff.getPrice() + tariff.getTransitionPrice()));
            tariff.setIsActive(true);
            // tariffRepository.save(tariff);
            double incomeCompany = tariff.getPrice() + tariff.getTransitionPrice();
            budgetService.addCompanyBudget(new CompanyBudgetDto(UssdCodsName.ACTIVATE_TARIFF.name(), incomeCompany, LocalDate.now()));

        } else {
            return new ApiResponse("Balance of SimCard saved not enough", false);
        }

        if (Period.between(tariff.getExpireDate(), LocalDate.now()).getDays() <= 0) {
            tariff.setIsActive(false);
        }
        ;

        // SIM CARD  PACKAGE
        if (simCardDto.getPackageIds() != null) {
            List<UUID> aPackageIds = simCardDto.getPackageIds();
            Set<Package> packages = new HashSet<>();
            for (UUID aPackageId : aPackageIds) {
                Optional<Package> optionalPackage = packageRepository.findById(aPackageId);
                if (!optionalPackage.isPresent()) {
                    return new ApiResponse("This package not found " + optionalPackage, false);
                }
                packages.add(optionalPackage.get());
            }
            double packagePrice = 0;
            for (Package aPackage : packages) {
                packagePrice = packagePrice + aPackage.getPrice();
            }

            if ((simCard.getEnabled() && simCard.getBalance() >= packagePrice) || simCard.getCanBeInMinusBalance()) {
                simCard.setPackages(packages);
                simCard.setBalance(simCard.getBalance() - packagePrice);
                budgetService.addCompanyBudget(new CompanyBudgetDto(UssdCodsName.BUY_PACKAGE.name(), packagePrice, LocalDate.now()));
            } else {
                return new ApiResponse("Balance of SimCard saved not enough", false);
            }

            for (Package aPackage : packages) {
                aPackage.setIsPackageSold(true);
                aPackage.setDayOfPackageSold(LocalDate.now());
                // aPackage.setSimCards(Collections.singleton(simCard));
                // packageRepository.save(aPackage);
            }
        }


        if (simCardDto.getEntertainmentIds() != null) {
            Set<InfosEntertainment> infosEntertainments = new HashSet<>();
            List<UUID> entertainmentIds = simCardDto.getEntertainmentIds();
            for (UUID entertainmentId : entertainmentIds) {
                Optional<InfosEntertainment> optionalInfosEntertainment = entertainmentRepository.findById(entertainmentId);
                if (!optionalInfosEntertainment.isPresent()) {
                    return new ApiResponse("This infosEntertainment not found " + optionalInfosEntertainment, false);
                }
                infosEntertainments.add(optionalInfosEntertainment.get());
            }
            double entertainmentPrice = 0;
            for (InfosEntertainment infosEntertainment : infosEntertainments) {
                entertainmentPrice = entertainmentPrice + (infosEntertainment.getPriceProTypeOfPeriod() * infosEntertainment.getDeadline());
            }

            if ((simCard.getEnabled() && simCard.getBalance() >= entertainmentPrice) || simCard.getCanBeInMinusBalance()) {
                simCard.setEntertainments(infosEntertainments);
                simCard.setBalance(simCard.getBalance() - entertainmentPrice);
                budgetService.addCompanyBudget(new CompanyBudgetDto(UssdCodsName.BUY_INFO_ENTERTAINMENT.name(),
                        entertainmentPrice, LocalDate.now()));
            } else {
                return new ApiResponse("Balance of SimCard saved not enough", false);
            }

            for (InfosEntertainment infosEntertainment : infosEntertainments) {
                infosEntertainment.setIsActive(true);
            }
        }

         simCard.setNumber(simNumb);
         simCard.setCustomer(byId1.get());
         simCard.setEnabled(true);
         simcardRepository.save(simCard);
         return new ApiResponse("SimCard was edited", true);

    }

    public ApiResponse deleteSimCard(UUID id) {
        Optional<SimCard> byId = simcardRepository.findById(id);
        if (!byId.isPresent())
            return new ApiResponse("Sim Card was not found", false);
        try {
            simcardRepository.deleteById(id);
            return new ApiResponse("Sim card was deleted", true);
        } catch (Exception e) {
            return new ApiResponse("Sim card was not deleted", false);
        }
    }

    public ApiResponse getSimCards() {
        return new ApiResponse("",true,
                simcardRepository.findAll());
    }

}


