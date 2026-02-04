package br.com.eduard.database

import br.com.eduard.mine_toolkit.hybrid.PlayerUser
import java.util.*

object HybridTypes {

    init{
        customType<PlayerUser> {
            saveMethod={
                "$name;$uniqueId"
            }
            reloadMethod={
                val split = split(";")
                PlayerUser(split[0], UUID.fromString(split[1]))
            }
        }

    }

}