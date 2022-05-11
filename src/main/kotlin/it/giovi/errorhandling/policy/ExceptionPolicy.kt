package it.giovi.errorhandling.policy

interface ExceptionPolicy {
    val code: String?
    val message: String?
}
