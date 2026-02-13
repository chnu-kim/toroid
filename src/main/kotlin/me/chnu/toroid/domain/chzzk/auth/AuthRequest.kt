package me.chnu.toroid.domain.chzzk.auth

import java.net.URI

data class AuthRequest(val uri: URI, val state: String)
