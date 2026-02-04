package br.com.eduard.mine_toolkit.plugin;


import java.io.File;

public interface IPluginInstance {

    Object getPlugin();

    String getSystemName();

    File getPluginFolder();
}
