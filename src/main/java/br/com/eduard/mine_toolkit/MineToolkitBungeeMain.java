package br.com.eduard.mine_toolkit;

import br.com.eduard.mine_toolkit.core.LibraryLoader;
import net.md_5.bungee.api.plugin.Plugin;

import java.io.File;

/**
 * Para fazer plugins usando esta dependencia , lembre-se de colocar 'depends: [EduardAPI]'
 * em vez de 'depend: [EduardAPI]' na bungee.yml
 * @author Eduard
 * @since 1.7
 */
public class MineToolkitBungeeMain extends Plugin {

    public MineToolkitBungeeMain() {
        new LibraryLoader(getClass().getClassLoader(),new File("libs/")).loadLibraries();
    }

    public MineToolkitBungee mineToolkitBungee;

    @Override
    public void onLoad() {
        mineToolkitBungee = new MineToolkitBungee(this);
        mineToolkitBungee.onLoad();
    }

    @Override
    public void onEnable() {
        if (mineToolkitBungee == null){
            mineToolkitBungee = new MineToolkitBungee(this);
            mineToolkitBungee.onLoad();
        }
        mineToolkitBungee.onEnable();

    }

    @Override
    public void onDisable() {
        mineToolkitBungee.onDisable();
    }
}
