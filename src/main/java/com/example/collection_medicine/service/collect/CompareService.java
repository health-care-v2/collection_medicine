package com.example.collection_medicine.service.collect;

import com.example.collection_medicine.dto.MedicineCompareDto;
import com.example.collection_medicine.dto.constant.DmFlag;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class CompareService {

    public List<MedicineCompareDto> medicineCompare(List<MedicineCompareDto> dbDtoList, List<MedicineCompareDto> apiDtoList) {
        List<MedicineCompareDto> dtoList = new ArrayList<>();

        for (MedicineCompareDto apiDto : apiDtoList) {
            boolean exist = false;

            for (MedicineCompareDto dbDto : dbDtoList) {
                if(apiDto.getKey().equals(dbDto.getKey())) {
                    exist = true;
                    checkUpdateColumn(apiDto, dbDto);
                    break;
                }
            }

            if(!exist) {
                apiDto.setDmFlag(DmFlag.CREATE);
            }

            if(apiDto.getDmFlag() != DmFlag.NONE) {
                dtoList.add(apiDto);
            }
        }

        return dtoList;
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
