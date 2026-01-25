package me.chnu.toroid.domain.user

import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long> {
    fun findByPublicId(publicId: PublicId): User?
}