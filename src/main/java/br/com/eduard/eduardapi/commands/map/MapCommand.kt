package br.com.eduard.eduardapi.commands.map

import br.com.eduard.mine_toolkit.manager.CommandManager

class MapCommand : CommandManager("map") {
    init {
        register(MapCopyCommand())
        register(MapPasteCommand())
        register(MapPos1Command())
        register(MapPos2Command())
        register(MapLoadCommand())
        register(MapSaveCommand())
        register(MapSetCommand())
        register(MapListCommand())
        register(MapHelpCommand())
    }
}