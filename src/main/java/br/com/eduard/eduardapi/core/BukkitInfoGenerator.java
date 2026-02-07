package br.com.eduard.eduardapi.core;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

import org.bukkit.*;
import org.bukkit.World.Environment;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityTargetEvent.TargetReason;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import br.com.eduard.eduardapi.EduardAPI;
import br.com.eduard.mine_toolkit.config.Config;
import br.com.eduard.mine_toolkit.config.ConfigSection;

/**
 * Sistema que gera informações do Bukkit
 *
 * @author Eduard
 * @version 4.0
 * @since 1.1
 */
public class BukkitInfoGenerator {
    private EduardAPI plugin;

    public BukkitInfoGenerator(EduardAPI plugin) {
        setPlugin(plugin);
        File pasta = new File(plugin.getPluginFolder(), "database/");
        pasta.mkdirs();
        plugin.log("DataBase sendo verificada...");
        //if (Objects.requireNonNull(pasta.listFiles()).length == 0) {
            saveEnum(DamageCause.class);
            saveEnum(Effect.class);
            saveEnum(EntityType.class, "getKey");
            saveEnum(TargetReason.class);
            saveEnum(Sound.class);
            saveEnum(EntityEffect.class);
            saveEnum(Environment.class);
            saveEnum(PotionType.class);
            saveEnum(Material.class);
            saveClassLikeEnum();
            plugin.log("DataBase do Bukkit processada!");
       // } else  plugin.log("DataBase ja foi gerada!");


    }

    private void saveClassLikeEnum() {
        var ignoreList = new ArrayList<String>();
        ignoreList.add("getHandle");
        ignoreList.add("getKeyOrThrow");
        ignoreList.add("getKeyOrNull");

        try {
            Config config = new Config(plugin, "database/" + PotionEffectType.class.getSimpleName() + ".yml");
            for (Field field : PotionEffectType.class.getFields()) {
                if (field.getType().equals(PotionEffectType.class)) {
                    Object obj = field.get(PotionEffectType.class);
                    ConfigSection section = config.getSection(field.getName());
                    for (Method method : obj.getClass().getDeclaredMethods()) {
                        String name = method.getName();
                        if (ignoreList.contains(name))continue;
                        if ((method.getParameterCount() == 0)
                                && name.startsWith("get") | name.startsWith("is") | name.startsWith("can")) {
                            method.setAccessible(true);
                            Object fieldValue = method.invoke(obj);
                            if (fieldValue instanceof NamespacedKey){
                                var key = ((NamespacedKey) fieldValue).toString();
                                section.add(method.getName(), key);
                                continue;
                            }
                            if (fieldValue instanceof Class)
                                continue;
                            section.add(method.getName(), fieldValue);
                        }
                    }
                }
            }
            config.saveConfig();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    private void saveEnum(Class<?> classMaybeEnum, String... ignoredMethods) {
        try {

            if (!classMaybeEnum.isEnum()){
                getPlugin().log("Skipping not Enum: "+classMaybeEnum);
                return;
            }

            Config config = new Config(plugin, "database/" + classMaybeEnum.getSimpleName() + ".yml");
            if (config.existConfig()){
                getPlugin().log("Skipping Enum already saved: "+classMaybeEnum);
                return;
            }
            getPlugin().log("Saving Enum: "+classMaybeEnum);
            boolean used = false;
            Field idField = null;

            try {
                idField = classMaybeEnum.getDeclaredField("id");
                idField.setAccessible(true);
            }catch (Exception ex){
            }

            for (Object part : classMaybeEnum.getEnumConstants()) {
                try {

                    Enum<?> obj = (Enum<?>) part;
                    config.add(obj.name()+".ordinal", obj.ordinal());
                    ConfigSection section = config.getSection(obj.name());
                    if (idField !=null){
                        int id = (int) idField.get(part);
                        config.add(obj.name()+".id", id);
                    }
                            inicial:
                    for (Method method : obj.getClass().getDeclaredMethods()) {
                        String name = method.getName();
                        if (Modifier.isStatic(method.getModifiers())) continue;
                        for (String metName : ignoredMethods) {
                            if (metName.equals(name)) {
                                continue inicial;
                            }
                        }

                        if ((method.getParameterCount() == 0)
                                && name.startsWith("get") | name.startsWith("is") | name.startsWith("can")) {

                            try {
                                method.setAccessible(true);
                                Object fieldValue = method.invoke(obj);
                                if (fieldValue == null)
                                    continue;
                                if (fieldValue instanceof NamespacedKey){
                                    var key = ((NamespacedKey) fieldValue).toString();
                                    section.add(method.getName(), key);
                                    continue;
                                }
                                if (fieldValue instanceof Class)
                                    continue;
                                section.add(method.getName(), fieldValue);
                                used = true;
                            } catch (Exception ex) {
                                //getPlugin().log("O metodo §c" + name + "§f causou erro! §f" + ex.getMessage());
                                //ex.printStackTrace();

                            }

                        }
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            if (!used)
                config.setIndent(0);
            config.saveConfig();
				/*
			}
			*/


        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public EduardAPI getPlugin() {
        return plugin;
    }

    public void setPlugin(EduardAPI plugin) {
        this.plugin = plugin;
    }

}
