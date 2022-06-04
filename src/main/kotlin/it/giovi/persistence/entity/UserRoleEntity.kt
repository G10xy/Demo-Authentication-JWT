package it.giovi.persistence.entity

import javax.persistence.*


@Entity
@Table(name = "USER_ROLE", schema = "DEMO")
class UserRoleEntity(

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "operatorRole_Seq")
    @SequenceGenerator(name = "operatorRole_Seq", sequenceName = "OPERATOR_ROLE_SEQ", allocationSize = 1, initialValue = 1)
    var id: Long,

    @Enumerated(EnumType.STRING)
    @Column(name = "ROLE", nullable = false)
    var role: UserRoleEnum,

    @OneToMany(mappedBy = "userRole", fetch = FetchType.LAZY)
    var userEntities: MutableSet<UserEntity>
) {

    enum class UserRoleEnum {
        ROLE_ADMIN, ROLE_IAM, ROLE_EDITOR
    }
}