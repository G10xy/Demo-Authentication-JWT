package it.giovi.persistence.repository;

import it.giovi.persistence.entity.UserStateEntity
import it.giovi.persistence.entity.UserStateEntity.UserStateEnum
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*


interface UserStateRepository : JpaRepository<UserStateEntity, Long> {

    fun findByState(state: UserStateEnum): Optional<UserStateEntity>
}