package me.chnu.toroid.domain.trpg.session

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.SequenceGenerator
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import java.time.OffsetDateTime
import java.util.*

private const val SESSION_ID_SEQ_GENERATOR = "SESSION_ID_SEQ_GENERATOR"

@SequenceGenerator(
    schema = "trpg",
    name = SESSION_ID_SEQ_GENERATOR,
    sequenceName = "session_id_seq",
    initialValue = 1,
    allocationSize = 50,
)
@Table(
    schema = "trpg",
    name = "sessions",
    uniqueConstraints = [
        UniqueConstraint(
            name = "sessions_public_id_key",
            columnNames = ["public_id"],
        )
    ]
)
@Entity
class TrpgSession(
    @Id
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = SESSION_ID_SEQ_GENERATOR,
    )
    val id: Long = 0L,
    @Column(nullable = false)
    val publicId: UUID,
    @Column(nullable = false)
    val title: String,
    @Column(nullable = false)
    val genre: String,
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    val status: SessionStatus = SessionStatus.CREATED,
    @Column(nullable = false)
    val createdAt: OffsetDateTime = OffsetDateTime.now(),
    val startedAt: OffsetDateTime? = null,
    val endedAt: OffsetDateTime? = null,
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (javaClass != other?.javaClass) {
            return false
        }
        other as TrpgSession
        return id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}

