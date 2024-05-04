package uz.pdp.cascade_types_annotation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.pdp.cascade_types_annotation.entity.Package;
import uz.pdp.cascade_types_annotation.entity.SimCard;
import uz.pdp.cascade_types_annotation.entity.Tariff;
import uz.pdp.cascade_types_annotation.entity.enums.ServiceName;
import uz.pdp.cascade_types_annotation.payload.PackageDto;
import uz.pdp.cascade_types_annotation.repository.PackageRepository;
import uz.pdp.cascade_types_annotation.repository.SimCardRepository;
import uz.pdp.cascade_types_annotation.repository.TariffRepository;

import java.util.*;

@Service
public class PackageService
{

    @Autowired
    TariffRepository tariffRepository;

    @Autowired
    PackageRepository packageRepository;

    @Autowired
    SimCardRepository simCardRepository;

        public ApiResponse addPackage(PackageDto packageDto) {


        Optional<Tariff> optionalTariff = tariffRepository.findById(packageDto.getTariffID());
        if (!optionalTariff.isPresent()) return new ApiResponse("Tariff was not found", false);

         Package aPackage = new Package();

        aPackage.setPrice(packageDto.getPrice());
        aPackage.setAmount(packageDto.getAmount());
        aPackage.setValidityDays(packageDto.getValidityDays());
        aPackage.setTariff(optionalTariff.get());

        aPackage.setAddToRestOffPackage(packageDto.getAddToRestOffPackage());

             if (packageDto.getTypeOfPackage() == 1) {
            aPackage.setTypeOfPackage(ServiceName.SMS.name());
        }
        else if (packageDto.getTypeOfPackage() == 2) {
            aPackage.setTypeOfPackage(ServiceName.MB.name());
        }
        else if (packageDto.getTypeOfPackage() == 3) {
            aPackage.setTypeOfPackage(ServiceName.MINUTE.name());
        }
        else {
        return new ApiResponse("1- SMS, 2- MB, 3- MINUTE", false);
        }

        packageRepository.save(aPackage);
        return new ApiResponse("package was saved", true);
    }

        public ApiResponse editPackage (UUID id, PackageDto packageDto){

            Optional<Package> optionalPackage = packageRepository.findById(id);
            if (!optionalPackage.isPresent())
                return new ApiResponse("Package was not found", false);

            Optional<Tariff> optionalTariff = tariffRepository.findById(packageDto.getTariffID());
            if (!optionalTariff.isPresent()) return new ApiResponse("Tariff not found", false);

                Package aPackage = optionalPackage.get();
            aPackage.setPrice(packageDto.getPrice());
            aPackage.setAmount(packageDto.getAmount());
            aPackage.setIsPackageSold(packageDto.getIsPackageSold());
            aPackage.setDayOfPackageSold(packageDto.getDayOfPackageSold());
            aPackage.setValidityDays(packageDto.getValidityDays());
            aPackage.setTariff(optionalTariff.get());

            Optional<SimCard> byPackagesId = simCardRepository.findByPackagesId(id);
            if(!byPackagesId.isPresent()) return new ApiResponse("simCard was not found", false);
            aPackage.setSimCard(byPackagesId.get());

            aPackage.setAddToRestOffPackage(packageDto.getAddToRestOffPackage());
            aPackage.setDayOfPackageSold(packageDto.getDayOfPackageSold());

             switch (packageDto.getTypeOfPackage())
            {
                case 1:
                    aPackage.setTypeOfPackage(ServiceName.SMS.name());break;
                case 2:
                    aPackage.setTypeOfPackage(ServiceName.MB.name());break;
                case 3:
                    aPackage.setTypeOfPackage(ServiceName.MINUTE.name());break;
                default:
                    return new ApiResponse("1- SMS, 2- MB, 3- MINUTE", false);
            }
            packageRepository.save(aPackage);
            return new ApiResponse("package was saved", true);
        }


        public ApiResponse getPackages() {
        List<Package> all = packageRepository.findAll();
        return new ApiResponse("", true, all);
    }

        public ApiResponse deletePackage (UUID id){

            Optional<Package> byId1 = packageRepository.findById(id);
            if (!byId1.isPresent())
                return new ApiResponse("package was not found", false);
            try {
                packageRepository.deleteById(id);
                return new ApiResponse("package was deleted", true);
            } catch (Exception e) {
                return new ApiResponse("package was not found", false);
            }
        }

}

