package com.example.bankcards.dto.mapper;

import com.example.bankcards.dto.card.CardResponse;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.User;
import com.example.bankcards.util.MaskingUtil;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-28T17:19:48+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.10 (BellSoft)"
)
@Component
public class CardMapperImpl implements CardMapper {

    @Override
    public CardResponse toDto(Card card) {
        if ( card == null ) {
            return null;
        }

        CardResponse.CardResponseBuilder cardResponse = CardResponse.builder();

        cardResponse.cardHolderName( cardCardHolderName( card ) );
        cardResponse.cardHolderId( cardCardHolderId( card ) );
        cardResponse.id( card.getId() );
        cardResponse.validThrough( card.getValidThrough() );
        cardResponse.balance( card.getBalance() );
        cardResponse.hasBlockRequest( card.isHasBlockRequest() );

        cardResponse.number( MaskingUtil.maskCardNumber(card.getNumber()) );
        cardResponse.status( card.getStatus().name() );

        return cardResponse.build();
    }

    private String cardCardHolderName(Card card) {
        if ( card == null ) {
            return null;
        }
        User cardHolder = card.getCardHolder();
        if ( cardHolder == null ) {
            return null;
        }
        String name = cardHolder.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }

    private UUID cardCardHolderId(Card card) {
        if ( card == null ) {
            return null;
        }
        User cardHolder = card.getCardHolder();
        if ( cardHolder == null ) {
            return null;
        }
        UUID id = cardHolder.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
