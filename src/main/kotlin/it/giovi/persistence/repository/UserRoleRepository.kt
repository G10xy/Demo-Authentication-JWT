package it.giovi.persistence.repository;

import it.giovi.persistence.entity.UserRoleEntity
import it.giovi.persistence.entity.UserRoleEntity.UserRoleEnum
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*


interface UserRoleRepository : JpaRepository<UserRoleEntity, Long> {

    fun findByRole(role: UserRoleEnum): Optional<UserRoleEntity>
}