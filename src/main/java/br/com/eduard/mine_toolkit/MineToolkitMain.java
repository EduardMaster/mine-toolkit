package br.com.eduard.mine_toolkit;

import br.com.eduard.mine_toolkit.core.LibraryLoader;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

/**
 * Main class of Mine Toolkit on Bukkit
 * @author Eduard
 * @since 1.7
 */
public class MineToolkitMain extends JavaPlugin {

    public MineToolkit mineToolkit;
    public MineToolkitMain() {

        new LibraryLoader(getClassLoader(),new File("libs/")).loadLibraries();
    }

    @Override
    public void onLoad() {
        getConfig().addDefault("eduardapi-enabled", true);
        saveConfig();
        if (getConfig().getBoolean(("eduardapi-enabled"))){
            mineToolkit = new MineToolkit(this);
            mineToolkit.onLoad();
        }
    }

    @Override
    public void onEnable() {
        if (mineToolkit == null) {
            mineToolkit = new MineToolkit(this);
            mineToolkit.onLoad();
        }
            mineToolkit.onEnable();
    }

    @Override
    public void onDisable() {
            mineToolkit.onDisable();
    }
}
