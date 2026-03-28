package com.example.bankcards.dto.mapper;

import com.example.bankcards.dto.card.CardResponse;
import com.example.bankcards.entity.Card;
import com.example.bankcards.util.MaskingUtil;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", imports = MaskingUtil.class)
public interface CardMapper {
    @Mapping(target = "cardHolderName", source = "cardHolder.name")
    @Mapping(target = "cardHolderId", source = "cardHolder.id")
    @Mapping(target = "number", expression = "java(MaskingUtil.maskCardNumber(card.getNumber()))")
    @Mapping(target = "status", expression = "java(card.getStatus().name())")
    CardResponse toDto(Card card);
}
