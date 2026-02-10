package br.com.eduard.mine_toolkit.plugin;


import java.io.File;

public interface IPluginInstance<PluginType> {

    PluginType getPlugin();

    String getSystemName();

    File getPluginFolder();
}
