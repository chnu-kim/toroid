package me.chnu.toroid.domain.user

import jakarta.persistence.*
import java.time.OffsetDateTime

@Entity
@Table(
    name = "social_accounts",
    uniqueConstraints = [
        UniqueConstraint(
            name = "uq_social_accounts_provider_provider_id",
            columnNames = ["provider", "providerId"]
        )
    ]
)
class SocialAccount(
    @Id
    val id: Long = 0L,
    @ManyToOne
    @MapsId
    @JoinColumn(name = "id")
    val user: User,
    @Enumerated(EnumType.STRING)
    val provider: SocialAccountProvider,
    val providerId: String,
    val createdAt: OffsetDateTime,
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (javaClass != other?.javaClass) {
            return false
        }
        other as User
        return id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}

