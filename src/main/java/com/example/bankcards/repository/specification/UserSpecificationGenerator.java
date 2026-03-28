package com.example.bankcards.repository.specification;

import com.example.bankcards.dto.user.UserFilter;
import com.example.bankcards.entity.User;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class UserSpecificationGenerator {
    public Specification<User> generateUserSpec(UserFilter filter) {
        Specification<User> spec = Specification.where(null);

        if (filter.getUsername() != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("email"), filter.getUsername()));
        }

        if (filter.getIsActive() != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("isActive"), filter.getIsActive()));
        }

        return spec;
    }
}
