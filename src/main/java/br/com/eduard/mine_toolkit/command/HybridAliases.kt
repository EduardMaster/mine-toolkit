package br.com.eduard.mine_toolkit.command

import br.com.eduard.mine_toolkit.hybrid.BukkitPlayer
import br.com.eduard.mine_toolkit.hybrid.BungeePlayer
import br.com.eduard.mine_toolkit.hybrid.IPlayer
import br.com.eduard.mine_toolkit.hybrid.ISender
import br.com.eduard.mine_toolkit.hybrid.PlayerUser

typealias Sender = ISender
typealias PlayerBungee = BungeePlayer
typealias PlayerBukkit = BukkitPlayer
typealias PlayerOffline = PlayerUser
typealias PlayerOnline<T> = IPlayer<T>
