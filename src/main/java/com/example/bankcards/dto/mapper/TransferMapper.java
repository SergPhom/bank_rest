package com.example.bankcards.dto.mapper;

import com.example.bankcards.dto.transfer.TransferRequest;
import com.example.bankcards.dto.transfer.TransferResponse;
import com.example.bankcards.entity.Transfer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface TransferMapper {
    @Mapping(target = "id", ignore = true)
    Transfer toEntity(TransferRequest request);

    TransferResponse toResponse(Transfer transfer);
}
