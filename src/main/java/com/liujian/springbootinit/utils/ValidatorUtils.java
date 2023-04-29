package com.liujian.springbootinit.utils;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.HibernateValidator;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

@Slf4j
public class ValidatorUtils {
    //获取 validator 校验对象
    private static Validator validator =
            Validation.byProvider(HibernateValidator.class)
                    .configure()
                    .failFast(true)
                    .buildValidatorFactory()
                    .getValidator();

    //创建泛型Bean对象校验方法
    public static <T> boolean validate(T entity) {
        //调用校验器的校验方法，对传入的对象的参数进行校验，返回值为不符合校验规则的属性的错误信息
        Set<ConstraintViolation<T>> validate = validator.validate(entity);
        if (validate.size() > 0) {
            log.error(validate.iterator().next().getMessage() + "参数校验未通过！");
            return false;
        }
        return true;
    }
}
