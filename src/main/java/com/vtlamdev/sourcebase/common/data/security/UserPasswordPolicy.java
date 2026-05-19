package com.vtlamdev.sourcebase.common.data.security;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;

@Schema
public class UserPasswordPolicy implements Serializable {

    private Integer minimumLength;
    private Integer maximumLength;
    private Integer minimumUppercaseLetters;
    private Integer minimumLowercaseLetters;
    private Integer minimumDigits;
    private Integer minimumSpecialCharacters;
    private Boolean allowWhitespaces = true;
    private Boolean forceUserToResetPasswordIfNotValid = false;
    private Integer passwordExpirationPeriodDays;
    private Integer passwordReuseFrequencyDays;

    public Integer getMinimumLength() {
        return minimumLength;
    }

    public void setMinimumLength(Integer minimumLength) {
        this.minimumLength = minimumLength;
    }

    public Integer getMaximumLength() {
        return maximumLength;
    }

    public void setMaximumLength(Integer maximumLength) {
        this.maximumLength = maximumLength;
    }

    public Integer getMinimumUppercaseLetters() {
        return minimumUppercaseLetters;
    }

    public void setMinimumUppercaseLetters(Integer minimumUppercaseLetters) {
        this.minimumUppercaseLetters = minimumUppercaseLetters;
    }

    public Integer getMinimumLowercaseLetters() {
        return minimumLowercaseLetters;
    }

    public void setMinimumLowercaseLetters(Integer minimumLowercaseLetters) {
        this.minimumLowercaseLetters = minimumLowercaseLetters;
    }

    public Integer getMinimumDigits() {
        return minimumDigits;
    }

    public void setMinimumDigits(Integer minimumDigits) {
        this.minimumDigits = minimumDigits;
    }

    public Integer getMinimumSpecialCharacters() {
        return minimumSpecialCharacters;
    }

    public void setMinimumSpecialCharacters(Integer minimumSpecialCharacters) {
        this.minimumSpecialCharacters = minimumSpecialCharacters;
    }

    public Boolean getAllowWhitespaces() {
        return allowWhitespaces;
    }

    public void setAllowWhitespaces(Boolean allowWhitespaces) {
        this.allowWhitespaces = allowWhitespaces;
    }

    public Boolean getForceUserToResetPasswordIfNotValid() {
        return forceUserToResetPasswordIfNotValid;
    }

    public void setForceUserToResetPasswordIfNotValid(Boolean forceUserToResetPasswordIfNotValid) {
        this.forceUserToResetPasswordIfNotValid = forceUserToResetPasswordIfNotValid;
    }

    public Integer getPasswordExpirationPeriodDays() {
        return passwordExpirationPeriodDays;
    }

    public void setPasswordExpirationPeriodDays(Integer passwordExpirationPeriodDays) {
        this.passwordExpirationPeriodDays = passwordExpirationPeriodDays;
    }

    public Integer getPasswordReuseFrequencyDays() {
        return passwordReuseFrequencyDays;
    }

    public void setPasswordReuseFrequencyDays(Integer passwordReuseFrequencyDays) {
        this.passwordReuseFrequencyDays = passwordReuseFrequencyDays;
    }

}
