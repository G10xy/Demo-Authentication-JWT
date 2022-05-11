package it.giovi.persistence

import it.giovi.persistence.entity.*
import it.giovi.persistence.entity.UserRoleEntity.UserRoleEnum
import it.giovi.persistence.entity.UserStateEntity.UserStateEnum
import org.springframework.data.jpa.domain.Specification
import java.time.LocalDateTime
import java.util.*
import javax.persistence.criteria.*


class UserSpecifications {

    companion object{
        fun userByUsernameLike(username: String): Specification<UserEntity> {
            return Specification<UserEntity> { root: Root<UserEntity>, query: CriteriaQuery<*>, criteriaBuilder: CriteriaBuilder ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get(UserEntity_.USERNAME)), "%" + username.lowercase(Locale.getDefault()) + "%")
            }
        }

        fun userByUsername(username: String): Specification<UserEntity> {
            return Specification { root: Root<UserEntity>, query: CriteriaQuery<*>, criteriaBuilder: CriteriaBuilder ->
                criteriaBuilder.equal(root.get<Any>(UserEntity_.USERNAME), username)
            }
        }

        fun userBySurnameLike(surname: String): Specification<UserEntity> {
            return Specification { root: Root<UserEntity>, query: CriteriaQuery<*>, criteriaBuilder: CriteriaBuilder ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get(UserEntity_.SURNAME)),"%" + surname.lowercase(Locale.getDefault()) + "%")
            }
        }

        fun userByLastLoginBeforeThan(date: LocalDateTime): Specification<UserEntity> {
            return Specification { root: Root<UserEntity>, query: CriteriaQuery<*>, criteriaBuilder: CriteriaBuilder ->
                criteriaBuilder.lessThanOrEqualTo(root[UserEntity_.LAST_LOGIN_DATE], date)
            }
        }

        fun userNotDeleted(): Specification<UserEntity> {
            return Specification { root: Root<UserEntity>, query: CriteriaQuery<*>, criteriaBuilder: CriteriaBuilder ->
                val join = root.join<UserEntity, UserStateEntity>(UserEntity_.USER_STATE)
                criteriaBuilder.notEqual(join.get<Any>(UserStateEntity_.STATE), UserStateEnum.DELETED)
            }
        }

        fun userByRole(role: UserRoleEnum): Specification<UserEntity> {
            return Specification { root: Root<UserEntity>, query: CriteriaQuery<*>, criteriaBuilder: CriteriaBuilder ->
                val join = root.join<UserEntity, UserRoleEntity>(UserEntity_.USER_ROLE)
                criteriaBuilder.equal(join.get<Any>(UserRoleEntity_.ROLE), role)
            }
        }

        fun userByState(state: UserStateEnum): Specification<UserEntity> {
            return Specification { root: Root<UserEntity>, query: CriteriaQuery<*>, criteriaBuilder: CriteriaBuilder ->
                val join = root.join<UserEntity, UserStateEntity>(UserEntity_.USER_STATE)
                criteriaBuilder.equal(join.get<Any>(UserStateEntity_.STATE), state)
            }
        }
    }
}