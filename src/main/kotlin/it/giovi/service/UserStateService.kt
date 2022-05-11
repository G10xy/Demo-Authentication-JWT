package it.giovi.service


import it.giovi.errorhandling.exception.BusinessException
import it.giovi.errorhandling.exception.BusinessExceptionReason
import it.giovi.persistence.entity.UserStateEntity
import it.giovi.persistence.entity.UserStateEntity.UserStateEnum
import it.giovi.persistence.repository.UserStateRepository
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service


@Service
class UserStateService(private val stateRepository: UserStateRepository) {

    @Cacheable(cacheNames = ["userUtilities"], key = "#state")
    fun getUserState(state: UserStateEnum): UserStateEntity {
        return stateRepository.findByState(state).orElseThrow<RuntimeException> {
            throw BusinessException(BusinessExceptionReason.STATE_NOT_FOUND)
        }
    }

    @Cacheable(cacheNames = ["userUtilities"], keyGenerator = "customKeyGenerator")
    fun getAllStates(): Collection<UserStateEntity> {
        return stateRepository.findAll()
    }

}