package me.chnu.toroid.domain.user

import jakarta.persistence.Column
import jakarta.persistence.ConstraintMode
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.ForeignKey
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.SequenceGenerator
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import java.time.OffsetDateTime

private const val SOCIAL_ACCOUNT_ID_SEQ_GENERATOR = "USER_ID_SEQ_GENERATOR"

@SequenceGenerator(
    name = SOCIAL_ACCOUNT_ID_SEQ_GENERATOR,
    sequenceName = "social_account_id_seq",
    initialValue = 1,
    allocationSize = 50,
)
@Entity
@Table(
    name = "social_accounts",
    uniqueConstraints = [
        UniqueConstraint(
            name = "social_accounts_provider_provider_id_key",
            columnNames = ["provider", "providerId"]
        )
    ]
)
class SocialAccount(
    @Id
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = SOCIAL_ACCOUNT_ID_SEQ_GENERATOR,
    )
    val id: Long = 0L,

    @ManyToOne
    @JoinColumn(
        name = "user_id",
        foreignKey = ForeignKey(ConstraintMode.NO_CONSTRAINT),
        nullable = false,
    )
    val user: User,
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val provider: SocialAccountProvider,
    @Column(nullable = false)
    val providerId: String,
    @Column(nullable = false)
    val createdAt: OffsetDateTime,
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (javaClass != other?.javaClass) {
            return false
        }
        other as SocialAccount
        return id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}

