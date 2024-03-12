package com.example.api.owner.dto.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class BusinessRegistrationNumberCheckValidator implements
        ConstraintValidator<BusinessRegistrationNumberCheck, String> {

    private String regexp;

    @Override
    public void initialize(BusinessRegistrationNumberCheck constraintAnnotation) {
        this.regexp = constraintAnnotation.regexp();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if (value == null || value.isEmpty()) {
            return false;
        }
        if (!value.matches(regexp)) {
            return false;
        }

        String[] digits = value.split("-");

        // 일련번호코드
        int serialNumberCode = Integer.parseInt(digits[0]);
        if (!(101 <= serialNumberCode && serialNumberCode <= 999)) {
            return false;
        }

        // 개인 법인 구분코드
        int individualCorporationClassificationCode = Integer.parseInt(digits[1]);
        if (!(1 <= individualCorporationClassificationCode && individualCorporationClassificationCode <= 99)) {
            return false;
        }

        // 일련번호코드
        String serialNumberCode2Value = digits[2].substring(0, 4);
        int serialNumberCode2 = Integer.parseInt(serialNumberCode2Value);
        if (!(1 <= serialNumberCode2 && serialNumberCode2 <= 9999)) {
            return false;
        }

        // 검증번호: 5로 설정
        String verificationNumberValue = digits[2].substring(4);
        int verificationNumber = Integer.parseInt(verificationNumberValue);
        if (verificationNumber != 5) {
            return false;
        }

        return true;
    }
}
