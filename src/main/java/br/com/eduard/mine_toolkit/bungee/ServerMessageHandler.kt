package br.com.eduard.mine_toolkit.bungee

interface ServerMessageHandler {
    fun onMessage(server: String, tag: String, line: String)
}