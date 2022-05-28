package it.giovi.persistence.repository

import it.giovi.persistence.entity.UserEntity
import it.giovi.persistence.entity.UserOtpEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.util.*


@Repository
interface UserOtpRepository : JpaRepository<UserOtpEntity, Long> {
    @Query("select otp from UserOtpEntity otp where otp.user = :user and otp.expiryDate >= :now")
    fun findValidTokenByUser(@Param("user") user: UserEntity, @Param("now") now: LocalDateTime): UserOtpEntity?

    @Modifying
    @Query("delete from UserOtpEntity otp where otp.expiryDate <= :now")
    fun deleteAllExpiredSince(@Param("now") now: LocalDateTime)
}