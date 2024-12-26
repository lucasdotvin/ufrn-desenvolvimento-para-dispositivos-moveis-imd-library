package vin.lucas.imdlibrary.cases.auth

import vin.lucas.imdlibrary.contracts.cases.auth.SignOutUseCase
import vin.lucas.imdlibrary.contracts.services.SessionService

class DefaultSignOutUseCase(
    private val sessionService: SessionService,
) : SignOutUseCase {
    override fun execute() {
        sessionService.clear()
    }
}
