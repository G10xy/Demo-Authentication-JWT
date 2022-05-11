package it.giovi.service

import it.giovi.errorhandling.exception.BusinessException
import it.giovi.errorhandling.exception.BusinessExceptionReason
import it.giovi.persistence.entity.UserRoleEntity
import it.giovi.persistence.entity.UserRoleEntity.UserRoleEnum
import it.giovi.persistence.repository.UserRoleRepository
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service


@Service
class UserRoleService(private val roleRepository: UserRoleRepository) {

    @Cacheable(cacheNames = ["userUtilities"], key = "#role")
    fun getUserRole(role: UserRoleEnum): UserRoleEntity {
        return roleRepository.findByRole(role)
            .orElseThrow { throw BusinessException(BusinessExceptionReason.ROLE_NOT_FOUND) }
    }

    @Cacheable(cacheNames = ["userUtilities"], keyGenerator = "customKeyGenerator")
    fun getAllRoles(): Collection<UserRoleEntity> {
        return roleRepository.findAll()
    }
}
