package br.com.eduard.mine_toolkit.command

interface ICommand {

    var name : String
    var usage : String
    var aliases : MutableList<String>
    var description : String
    var permission : String
    var permissionMessage : String
    var playerOnly : Boolean
    var subCommands : MutableList<Command>
}