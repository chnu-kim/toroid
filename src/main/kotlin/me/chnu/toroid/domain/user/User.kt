package me.chnu.toroid.domain.user

import jakarta.persistence.*
import java.time.OffsetDateTime
import java.util.*

private const val USER_SEQ_GENERATOR = "USER_SEQ_GENERATOR"

@SequenceGenerator(
    name = USER_SEQ_GENERATOR,
    sequenceName = "user_seq",
    initialValue = 1,
    allocationSize = 50,
)
@Table(name = "users")
@Entity
class User(
    @Id
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = USER_SEQ_GENERATOR,
    )
    val id: Long = 0L,
    var name: String,
    val publicId: PublicId,
    val createdAt: OffsetDateTime,
) {

    init {
        require(name.isNotBlank()) { "User name cannot be blank" }
    }

    fun changeName(name: String) {
        require(name.isNotBlank()) { "User name cannot be blank" }
        this.name = name
    }

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