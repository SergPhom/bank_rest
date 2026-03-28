package com.example.bankcards.repository.specification;

import com.example.bankcards.dto.card.CardFilter;
import com.example.bankcards.entity.Card;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class CardSpecificationGenerator {
    public Specification<Card> generateCardSpec(CardFilter filter) {
        Specification<Card> spec = Specification.where(null);

        if (filter.getUsername() != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("cardHolder").get("email"), filter.getUsername()));
        }

        var statuses = filter.getStatuses();
        if (statuses != null && !statuses.isEmpty()) {
            spec = spec.and((root, query, cb) -> root.get("status").in(statuses));
        }

        return spec;
    }
}
