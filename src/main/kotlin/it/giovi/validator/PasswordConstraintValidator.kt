package it.giovi.validator

import it.giovi.annotation.ValidPassword
import it.giovi.errorhandling.exception.UserExceptionReason
import it.giovi.util.Utility
import org.springframework.beans.factory.annotation.Value
import java.util.regex.Pattern
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext


class PasswordConstraintValidator : ConstraintValidator<ValidPassword, String> {

    @Value("\${banned-password}")
    private lateinit var bannedPasswordFile: String

    private var failedValidationReason: String? = null

    override fun initialize(arg0: ValidPassword?) {}

    override fun isValid(password: String, context: ConstraintValidatorContext): Boolean {
        if (!this.isPasswordBanned(password) &&
            hasLowerCase(password) &&
            hasUpperCase(password) &&
            hasNumber(password) &&
            hasSpecialCharacters(password) &&
            hasWhiteSpace(password) &&
            checkPasswordLength(password, 14, 30) &&
            checkHowManyConsecutiveRepeatedCharacters(password, 2)
        ) {
            return true
        }
        context.disableDefaultConstraintViolation()
        context.buildConstraintViolationWithTemplate(failedValidationReason).addConstraintViolation()
        return false
    }

    private fun isPasswordBanned(password: String): Boolean {
        if (Utility.readResourceFile(bannedPasswordFile).split("\\r?\\n").contains(password)) {
            failedValidationReason = UserExceptionReason.PASSWORD_BANNED.message
            return true
        }
        return false
    }

    private fun hasNumber(password: String): Boolean {
        if (Pattern.matches("^(?!.*[0-9]).+$", password)) {
            failedValidationReason = "Password must contain at least a number"
            return false
        }
        return true
    }

    private fun hasLowerCase(password: String): Boolean {
        if (Pattern.matches("^(?!.*[a-z]).+$", password)) {
            failedValidationReason = "Password must contain at least a lower case letter"
            return false
        }
        return true
    }

    private fun hasUpperCase(password: String): Boolean {
        if (Pattern.matches("^(?!.*[A-Z]).+$", password)) {
            failedValidationReason = "Password must contain at least an upper case letter"
            return false
        }
        return true
    }

    private fun hasWhiteSpace(password: String): Boolean {
        if (Pattern.matches("^(?=.*[\\s]).+$", password)) {
            failedValidationReason = "Password must not contain any white space"
            return false
        }
        return true
    }

    private fun checkHowManyConsecutiveRepeatedCharacters(password: String, maxAllowed: Int): Boolean {
        if (Pattern.matches("^(?=.*(.)\\1{$maxAllowed,}).+$", password)) {
            failedValidationReason =
                "Password must not contain more than $maxAllowed consecutive repeated characters"
            return false
        }
        return true
    }

    private fun hasSpecialCharacters(password: String): Boolean {
        if (Pattern.matches("^(?!.*[!@#&()–[{}]:'?/*~$€_^+=<>]).+$", password)) {
            failedValidationReason = "Password must not contain at least a special character"
            return false
        }
        return true
    }

    private fun checkPasswordLength(password: String, min: Int, max: Int): Boolean {
        if (password.length < min || password.length > max) {
            failedValidationReason = "Password length must be between $min and $max"
            return false
        }
        return true
    }
}