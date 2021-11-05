package com.xxx.common.valid;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.HashSet;
import java.util.Set;

public class ListValuesConstrainValidator implements ConstraintValidator<ListValues, Integer> {

    Set<Integer> set = new HashSet<>();
    @Override
    public void initialize(ListValues constraintAnnotation) {
        int[] values = constraintAnnotation.values();
        for (int v: values) {
            set.add(v);
        }
    }

    @Override
    public boolean isValid(Integer integer, ConstraintValidatorContext constraintValidatorContext) {
        return set.contains(integer);
    }
}
