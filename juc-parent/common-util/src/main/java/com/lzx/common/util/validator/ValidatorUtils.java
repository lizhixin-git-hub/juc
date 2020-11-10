package com.lzx.common.util.validator;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 校验工具类
 */
public class ValidatorUtils {

    private static Validator validator;

    static {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    /**
     * 校验对象
     *
     * @param object 待校验对象
     * @param groups 待校验的组
     */
    public static Map<String, Object> validateEntity(Object object, Class<?>... groups) {

        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(object, groups);

        if (!constraintViolations.isEmpty()) {
            Map<String, Object> map = new HashMap<>();
            constraintViolations.forEach(constraint ->
                    map.put(constraint.getPropertyPath().toString(), constraint.getMessage())
            );
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("result", map);
            return resultMap;
        }

        return null;
    }
}
