package it.giovi.annotation

import it.giovi.validator.PasswordConstraintValidator
import kotlin.reflect.KClass
import javax.validation.Constraint
import javax.validation.Payload
import java.lang.annotation.Retention
import java.lang.annotation.Target
import java.lang.annotation.Documented
import java.lang.annotation.ElementType.*
import java.lang.annotation.RetentionPolicy.RUNTIME

@Documented
@Constraint(validatedBy = [PasswordConstraintValidator::class])
@Target(*[TYPE, FIELD, ANNOTATION_TYPE])
@Retention(RUNTIME)
annotation class ValidPassword(
    val message: String = "Invalid Password",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)