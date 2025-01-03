package vin.lucas.imdlibrary

import android.app.Application
import vin.lucas.imdlibrary.contracts.dependencies.ServiceContainer
import vin.lucas.imdlibrary.dependencies.DefaultServiceContainer

class IMDLibraryApplication : Application() {
    val serviceContainer: ServiceContainer by lazy {
        DefaultServiceContainer(this)
    }
}
