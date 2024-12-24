package vin.lucas.imdlibrary.repositories

import org.mindrot.jbcrypt.BCrypt
import vin.lucas.imdlibrary.contracts.repositories.HashRepository

class BcryptHashRepository : HashRepository {
    override fun hash(value: String): String {
        return BCrypt.hashpw(value, BCrypt.gensalt())
    }

    override fun verify(value: String, digest: String): Boolean {
        return BCrypt.checkpw(value, digest)
    }
}
