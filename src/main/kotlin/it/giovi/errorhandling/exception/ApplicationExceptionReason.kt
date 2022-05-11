package it.giovi.errorhandling.exception

import it.giovi.errorhandling.policy.ApplicationExceptionPolicy

enum class ApplicationExceptionReason(override val message: String) : ApplicationExceptionPolicy {
    GENERAL_ERROR("There was an error");

    override val code: String = ApplicationExceptionReason::class.java.simpleName
}
