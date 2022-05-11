package it.giovi.persistence.entity

import javax.persistence.*

@Entity
@Table(name = "USER_STATE", schema="DEMO")
class UserStateEntity(

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "operatorState_Seq")
    @SequenceGenerator(name = "operatorState_Seq", sequenceName = "OPERATOR_STATE_SEQ", allocationSize = 1, initialValue = 1)
    var id: Long,

    @OneToMany(mappedBy = "userState")
    @Enumerated(EnumType.STRING)
    @Column(name = "STATE", nullable = false)
    var state: UserStateEnum

) {

    enum class UserStateEnum {
        ACTIVE, INACTIVE, SUSPENDED, DELETED
    }
}