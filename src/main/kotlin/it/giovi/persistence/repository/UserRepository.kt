package it.giovi.persistence.repository;

import it.giovi.persistence.entity.UserEntity
import it.giovi.persistence.entity.UserStateEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.*


interface UserRepository : JpaRepository<UserEntity, Long>, JpaSpecificationExecutor<UserEntity> {
    @Modifying
    @Query("update UserEntity user set user.lastLoginDate = CURRENT_DATE where user.username = :username and user.userState = :userState")
    fun setUserLastSignIn(@Param("username") username: String, @Param("userState") userState: UserStateEntity)

    @Modifying
    @Query("update UserEntity user set user.lastLogoutDate = CURRENT_DATE where user.username = :username and user.userState = :userState")
    fun setUserLastSignOut(@Param("username") username: String, @Param("userState") userState: UserStateEntity)
}