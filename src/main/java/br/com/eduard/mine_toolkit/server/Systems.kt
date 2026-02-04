package br.com.eduard.mine_toolkit.server

import java.util.function.Consumer

/**
 * Classe onde possui Métodos de Acesso as APIs do Servidor<br>
 * e também possui as Variaveis de algumas APIs definidas na classe
 */
object Systems {

    @JvmStatic
    private val pluginsAPI = mutableMapOf<Class<*>, PluginSystem>()

    @JvmStatic
    fun <T : PluginSystem> getAPI(classAPI: Class<T>): T? {
        val system = pluginsAPI[classAPI];
        return system as T?
    }

    @JvmStatic
    fun <T : PluginSystem> useAPI(classAPI: Class<T>, action: Consumer<T>): T? {
        val api = getAPI(classAPI) as T?
        if (api != null) {
            action.accept(api)
        }
        return api
    }

    @JvmStatic
    fun registerAPI(api: PluginSystem) {
        registerAPI(api)
    }

    @JvmStatic
    fun <T : PluginSystem> registerAPI(classAPI: Class<T>, api: T) {
        pluginsAPI[classAPI] = api
    }

    @JvmStatic
    fun unregisterAPI(api: PluginSystem) {
        unregisterAPI(api)
    }

    @JvmStatic
    fun <T : PluginSystem> unregisterAPI(classAPI: Class<T>, api: T) {
        pluginsAPI.remove(classAPI, api);
    }

    @JvmStatic
    var clanSystem: ClanSystem? = null

    @JvmStatic
    var playTimeSystem: PlayTimeSystem? = null

    @JvmStatic
    var arenaSystem: ArenaSystem? = null

    @JvmStatic
    var cashSystem: CashSystem? = null

    @JvmStatic
    var soulSystem: SoulSystem? = null

    @JvmStatic
    var scoreSystem: ScoreSystem? = null

    @JvmStatic
    var tagSystem: TagSystem? = null

    @JvmStatic
    var dropsSystem: DropSystem? = null

    @JvmStatic
    var mineSystem: MineSystem? = null


}

private fun PluginSystem.getChildrenAPIClass(): Class<*> {
    for (interfaceClass in javaClass.interfaces) {
        if (PluginSystem::class.java.isAssignableFrom(interfaceClass)
            && PluginSystem::class.java != interfaceClass
        ) return interfaceClass as Class<*>
    }
    throw IllegalArgumentException("Precisa ser uma classe que Implementa PluginSystem")
}

inline fun <reified T : PluginSystem> useAPI(): T {
    return Systems.getAPI(T::class.java)!!
}

inline fun <reified T : PluginSystem> useAPI(apply : T.() -> Unit): T? {
    return Systems.getAPI(T::class.java)?.apply(apply)
}



inline fun <reified T : PluginSystem> hasAPI(): Boolean {
    return Systems.getAPI(T::class.java) != null
}
fun <E: PluginSystem> PluginSystem.registerAPI() {
    val classAPI = getChildrenAPIClass()
    registerAPI(classAPI as Class<E>)
}
fun <E: PluginSystem> PluginSystem.registerAPI(apiClass : Class<E>) {
    Systems.registerAPI(apiClass, this as E)
}
fun <E: PluginSystem> PluginSystem.unregisterAPI(apiClass : Class<E>) {
    Systems.unregisterAPI(apiClass, this as E)
}
fun <E: PluginSystem>  PluginSystem.unregisterAPI() {
    val classAPI = getChildrenAPIClass()
   unregisterAPI(classAPI as Class<E>)
}

