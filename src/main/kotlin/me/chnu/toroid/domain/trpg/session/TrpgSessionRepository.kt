package me.chnu.toroid.domain.trpg.session

import org.springframework.data.jpa.repository.JpaRepository

interface TrpgSessionRepository : JpaRepository<TrpgSession, Long>
