package br.com.eduard.eduardapi.server

interface ChatSystem :  PluginSystem {

    fun isMuted(playerName : String) : Boolean
    fun mute(playerName: String)
    fun getPlayersMuted() : List<String>

}