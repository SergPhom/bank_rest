package com.example.bankcards.dto.mapper;

import com.example.bankcards.dto.transfer.TransferRequest;
import com.example.bankcards.dto.transfer.TransferResponse;
import com.example.bankcards.entity.Transfer;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-29T23:15:12+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.10 (BellSoft)"
)
@Component
public class TransferMapperImpl implements TransferMapper {

    @Override
    public Transfer toEntity(TransferRequest request) {
        if ( request == null ) {
            return null;
        }

        Transfer.TransferBuilder transfer = Transfer.builder();

        transfer.sourceCardId( request.getSourceCardId() );
        transfer.targetCardId( request.getTargetCardId() );
        transfer.transferAmount( request.getTransferAmount() );

        return transfer.build();
    }

    @Override
    public TransferResponse toResponse(Transfer transfer) {
        if ( transfer == null ) {
            return null;
        }

        TransferResponse transferResponse = new TransferResponse();

        if ( transfer.getResult() != null ) {
            transferResponse.setResult( transfer.getResult().name() );
        }
        transferResponse.setRejectionReason( transfer.getRejectionReason() );

        return transferResponse;
    }
}
