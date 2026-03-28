package com.example.bankcards.service;

import com.example.bankcards.dto.mapper.TransferMapper;
import com.example.bankcards.dto.transfer.TransferRequest;
import com.example.bankcards.dto.transfer.TransferResponse;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.Transfer;
import com.example.bankcards.entity.enums.Status;
import com.example.bankcards.entity.enums.TransferResult;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.TransferRepository;
import com.example.bankcards.service.api.TransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TransferServiceImpl implements TransferService {
    private final CardRepository repository;
    private final TransferRepository transferRepository;
    private final TransferMapper transferMapper;

    @Transactional
    @Override
    public TransferResponse doTransfer(TransferRequest request) {
        var sourceCardOpt = repository.findById(request.getSourceCardId());
        var targetCardOpt = repository.findById(request.getTargetCardId());
        var transfer = transferMapper.toEntity(request);

        if (checkSource(sourceCardOpt, transfer) && checkTarget(targetCardOpt, transfer)) {
            sourceCardOpt.map(c -> {
                c.setBalance(c.getBalance().subtract(transfer.getTransferAmount()));
                return c;
            }).ifPresent(repository::save);
            targetCardOpt.map(c -> {
                c.setBalance(c.getBalance().add(transfer.getTransferAmount()));
                return c;
            }).ifPresent(repository::save);

            transfer.setResult(TransferResult.SUCCESS);
        }

        var savedTransfer = transferRepository.save(transfer);

        return transferMapper.toResponse(savedTransfer);
    }

    private boolean checkSource(Optional<Card> source, Transfer transfer) {
        if (source.isEmpty()) {
            rejectTransfer("Карта источник для перевода не найдена", transfer);

            return false;
        } else if (source.get().getBalance().compareTo(transfer.getTransferAmount()) < 0) {
            rejectTransfer("Недостаточно средств на карте", transfer);

            return false;
        } else if (!source.get().getStatus().equals(Status.ACTIVE)) {
            rejectTransfer("Статус карты источника не позволяет выполнить перевод", transfer);

            return false;
        } else {
            return true;
        }
    }

    private boolean checkTarget(Optional<Card> target, Transfer transfer) {
        if (target.isEmpty()) {
            rejectTransfer("Карта цель для перевода не найдена", transfer);

            return false;
        } else if (!target.get().getStatus().equals(Status.ACTIVE)) {
            rejectTransfer("Статус карты цели не позволяет выполнить перевод", transfer);

            return false;
        } else {
            return true;
        }
    }

    private void rejectTransfer(String reason, Transfer transfer) {
        transfer.setRejectionReason(reason);
        transfer.setResult(TransferResult.REJECTION);
    }
}
