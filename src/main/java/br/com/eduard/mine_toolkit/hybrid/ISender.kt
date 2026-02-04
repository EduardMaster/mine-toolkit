package br.com.eduard.mine_toolkit.hybrid

interface ISender {
    val name : String
    fun sendMessage(message : String)
    fun hasPermission(permission : String) : Boolean
    fun performCommand(command : String)
}