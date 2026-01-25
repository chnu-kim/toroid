package me.chnu.toroid.domain.user

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter
import java.util.*

data class PublicId(val value: UUID) {
    override fun toString(): String = value.toString()
}


@Converter(autoApply = true)
class PublicIdConverter : AttributeConverter<PublicId, UUID> {
    override fun convertToDatabaseColumn(attribute: PublicId?): UUID? {
        return attribute?.value
    }

    override fun convertToEntityAttribute(dbData: UUID?): PublicId? {
        return dbData?.let { PublicId(it) }
    }
}
