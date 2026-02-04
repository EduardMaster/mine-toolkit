package br.com.eduard.eduardapi.commands.map

import br.com.eduard.mine_toolkit.manager.CommandManager

class MapCommand : CommandManager("map") {
    init {
        register(_root_ide_package_.br.com.eduard.eduardapi.command.map.MapCopyCommand())
        register(_root_ide_package_.br.com.eduard.eduardapi.command.map.MapPasteCommand())
        register(_root_ide_package_.br.com.eduard.eduardapi.command.map.MapPos1Command())
        register(_root_ide_package_.br.com.eduard.eduardapi.command.map.MapPos2Command())
        register(_root_ide_package_.br.com.eduard.eduardapi.command.map.MapLoadCommand())
        register(_root_ide_package_.br.com.eduard.eduardapi.command.map.MapSaveCommand())
        register(_root_ide_package_.br.com.eduard.eduardapi.command.map.MapSetCommand())
        register(_root_ide_package_.br.com.eduard.eduardapi.command.map.MapListCommand())
        register(_root_ide_package_.br.com.eduard.eduardapi.command.map.MapHelpCommand())
    }
}