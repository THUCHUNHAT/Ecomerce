package com.example.ecomerce.repository.specification;

import com.example.ecomerce.entity.Pet;
import org.springframework.data.jpa.domain.Specification;

public class PetSpecification {
    public static Specification<Pet> genderEquals(String gender) {
        return (root, query, cb) -> {
            if (gender == null || gender.isEmpty()) {
                return cb.conjunction();
            }
            return cb.equal(root.get("gender"), gender);
        };
    }

    public static Specification<Pet> colorEquals(String color) {
        return (root, query, cb) -> {
            if (color == null || color.isEmpty()) {
                return cb.conjunction();
            }
            return cb.equal(root.get("color"), color);
        };
    }
}
