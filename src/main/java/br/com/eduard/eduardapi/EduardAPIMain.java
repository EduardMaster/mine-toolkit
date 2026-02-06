package br.com.eduard.eduardapi;

import br.com.eduard.eduardapi.core.LibraryLoader;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

/**
 * Classe inicial do EduardAPI que faz carregamento do Kotlin para o restante funcionar e em breve também a baixar o kotlin se não tiver*
 * @author Eduard
 */
public class EduardAPIMain extends JavaPlugin {


    public EduardAPI eduardAPI;
    public EduardAPIMain() {
        new LibraryLoader(getClassLoader(),new File("libs/")).loadLibraries();
    }

    @Override
    public void onLoad() {
        eduardAPI = new EduardAPI(this);
        eduardAPI.onLoad();

    }

    @Override
    public void onEnable() {
        if (eduardAPI == null) {
            eduardAPI = new EduardAPI(this);
            eduardAPI.onLoad();
        }
        eduardAPI.onEnable();

    }

    @Override
    public void onDisable() {
        eduardAPI.onDisable();
    }
}
