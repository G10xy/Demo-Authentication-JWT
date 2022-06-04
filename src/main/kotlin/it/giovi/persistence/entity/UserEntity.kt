package it.giovi.persistence.entity

import java.time.LocalDateTime
import javax.persistence.*
import javax.validation.constraints.Email

@Entity
@Table(name = "USER", schema="PUBLIC", uniqueConstraints = [UniqueConstraint(columnNames = ["USERNAME", "ROLE_ID" ])])
class UserEntity(

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "operator_Seq")
    @SequenceGenerator(name = "operator_Seq", sequenceName = "OPERATOR_SEQ", allocationSize = 1, initialValue = 1)
    var id: Long?,

    @Email
    @Column(name = "USERNAME", nullable = false)
    var username: String,

    @Column(name = "NAME", nullable = false)
    var name: String,

    @Column(name = "SURNAME", nullable = false)
    var surname: String,

    @Column(name = "PASSWORD")
    var password: String?,

    @Column(name = "SEC_QUESTION", length = 4000)
    var secQuestion: String?,

    @Column(name = "SEC_ANSWER", length = 4000)
    var secAnswer: String?,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ROLE_ID", referencedColumnName = "ID", nullable = false)
    var userRole: UserRoleEntity,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STATE_ID", referencedColumnName = "ID", nullable = false)
    var userState: UserStateEntity,

    @Column(name = "ENABLED_AT")
    var enableDate: LocalDateTime?,

    @Column(name = "SUSPENDED_AT")
    var suspensionDate: LocalDateTime?,

    @Column(name = "PWD_EXP_DATE")
    var pwdExpirationDate: LocalDateTime?,

    @Column(name = "LAST_PWD_RESET_AT")
    var lastPasswordResetDate: LocalDateTime?,

    @Column(name = "LAST_PWD_CHANGE_AT")
    var lastPasswordChange: LocalDateTime?,

    @Column(name = "LAST_LOGIN_AT")
    var lastLoginDate: LocalDateTime?,

    @Column(name = "LAST_LOGOUT_AT")
    var lastLogoutDate: LocalDateTime?
) {

    constructor(
        username: String,
        name: String,
        surname: String,
        userRole: UserRoleEntity,
        userState: UserStateEntity,
        ) : this(null, username, name, surname, null, null, null, userRole, userState, null, null,  null, null, null, null, null)
}