package com.example.collection_medicine.service;

import com.example.collection_medicine.dto.MedicineCompareDto;
import com.example.collection_medicine.dto.constant.DmFlag;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class CompareService {

    public void medicineCompare(List<MedicineCompareDto> dbDtoList, List<MedicineCompareDto> apiDtoList) {
        for (MedicineCompareDto apiDto : apiDtoList) {
            boolean exist = false;

            for (MedicineCompareDto dbDto : dbDtoList) {
                if(apiDto.getKey().equals(dbDto.getKey())) {
                    exist = true;
                    apiDto.setDmFlag(DmFlag.NONE);
                    checkUpdateColumn(apiDto, dbDto);
                    break;
                }
            }

            if(!exist) {
                apiDto.setDmFlag(DmFlag.CREATE);
            }
        }
    }

    private void checkUpdateColumn(MedicineCompareDto apiDto, MedicineCompareDto dbDto) {
        if (!Objects.equals(apiDto.getEfficacy(), dbDto.getEfficacy()) ||
                !Objects.equals(apiDto.getUseMethod(), dbDto.getUseMethod()) ||
                !Objects.equals(apiDto.getPrecautionsWarning(), dbDto.getPrecautionsWarning()) ||
                !Objects.equals(apiDto.getCaution(), dbDto.getCaution()) ||
                !Objects.equals(apiDto.getInteraction(), dbDto.getInteraction()) ||
                !Objects.equals(apiDto.getSideEffect(), dbDto.getSideEffect()) ||
                !Objects.equals(apiDto.getStorageMethod(), dbDto.getStorageMethod()) ||
                !Objects.equals(apiDto.getImage(), dbDto.getImage())) {
            apiDto.setDmFlag(DmFlag.UPDATE);
        }
    }

}
