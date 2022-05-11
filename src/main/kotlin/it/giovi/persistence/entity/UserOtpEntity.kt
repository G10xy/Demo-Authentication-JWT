package it.giovi.persistence.entity


import java.time.LocalDate
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "USER_OTP", schema="DEMO")
class UserOtpEntity(

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "operatorOtp_Seq")
    @SequenceGenerator(name = "operatorOtp_Seq", sequenceName = "OPERATOR_OTP_SEQ", allocationSize = 1, initialValue = 1)
    val id: Long?,

    @Column(name = "TOKEN")
    val token: String,

    @OneToOne(fetch = FetchType.EAGER)
    @MapsId
    val user: UserEntity,

    @Column(name = "EXPIRY_DATE")
    val expiryDate: LocalDateTime
) {

    constructor(token: String, user: UserEntity) : this(null, token, user, LocalDateTime.now().plusDays(3))

}