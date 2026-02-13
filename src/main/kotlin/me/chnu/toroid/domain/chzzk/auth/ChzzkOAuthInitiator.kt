package me.chnu.toroid.domain.chzzk.auth

interface ChzzkOAuthInitiator {
    /**
     * Issues an authorization URI for initiating the OAuth flow.
     *
     * @return an instance of [AuthRequest] containing the authorization URI and the corresponding state.
     */
    fun issueAuthUri(): AuthRequest
}
