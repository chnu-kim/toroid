package me.chnu.toroid.domain.user

import jakarta.persistence.*
import java.time.OffsetDateTime


private const val USER_ID_SEQ_GENERATOR = "USER_ID_SEQ_GENERATOR"

@SequenceGenerator(
    name = USER_ID_SEQ_GENERATOR,
    sequenceName = "user_id_seq",
    initialValue = 1,
    allocationSize = 50,
)
@Table(
    name = "users",
    uniqueConstraints = [
        UniqueConstraint(
            name = "users_public_id_key",
            columnNames = ["public_id"],
        ),
    ]
)
@Entity
class User(
    @Id
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = USER_ID_SEQ_GENERATOR,
    )
    val id: Long = 0L,
    @Column(nullable = false)
    var name: String,
    @Column(nullable = false)
    val publicId: PublicId,
    @Column(nullable = false)
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