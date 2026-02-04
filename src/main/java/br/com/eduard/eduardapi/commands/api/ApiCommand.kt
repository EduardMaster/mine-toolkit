package br.com.eduard.eduardapi.commands.api

import br.com.eduard.mine_toolkit.manager.CommandManager

class ApiCommand : CommandManager("api") {
    init {
        register(_root_ide_package_.br.com.eduard.eduardapi.command.api.ApiHelpCommand())
        register(_root_ide_package_.br.com.eduard.eduardapi.command.api.ApiSQLDebugCommand())
        register(_root_ide_package_.br.com.eduard.eduardapi.command.api.ApiReloadCommand())
        register(_root_ide_package_.br.com.eduard.eduardapi.command.api.ApiUnloadWorldCommand())
        register(_root_ide_package_.br.com.eduard.eduardapi.command.api.ApiLoadWorldCommand())
        register(_root_ide_package_.br.com.eduard.eduardapi.command.api.ApiWorldsCommand())
        register(_root_ide_package_.br.com.eduard.eduardapi.command.api.ApiDeleteWorldCommand())
        register(_root_ide_package_.br.com.eduard.eduardapi.command.api.ApiListPluginsCommand())
        register(_root_ide_package_.br.com.eduard.eduardapi.command.api.ApiDisablePluginCommand())
        register(_root_ide_package_.br.com.eduard.eduardapi.command.api.ApiEnablePluginCommand())
        register(_root_ide_package_.br.com.eduard.eduardapi.command.api.ApiRestartEduardAPICommand())
        register(_root_ide_package_.br.com.eduard.eduardapi.command.api.ApiListPluginsCommand())
        register(_root_ide_package_.br.com.eduard.eduardapi.command.api.ApiSaveCommand())
        register(_root_ide_package_.br.com.eduard.eduardapi.command.api.ApiReloadCommand())
        register(_root_ide_package_.br.com.eduard.eduardapi.command.api.ApiUnloadPluginCommand())
        register(_root_ide_package_.br.com.eduard.eduardapi.command.api.ApiLoadPluginCommand())
    }
}