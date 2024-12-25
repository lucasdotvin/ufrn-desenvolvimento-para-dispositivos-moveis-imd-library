package vin.lucas.imdlibrary.ui.activities

import android.content.Intent
import androidx.activity.ComponentActivity
import vin.lucas.imdlibrary.IMDLibraryApplication
import vin.lucas.imdlibrary.contracts.services.SessionService

abstract class GuestActivity : ComponentActivity() {
    private val sessionService: SessionService by lazy {
        (application as IMDLibraryApplication).serviceContainer.sessionService
    }

    override fun onResume() {
        super.onResume()

        if (sessionService.isSignedIn) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}
