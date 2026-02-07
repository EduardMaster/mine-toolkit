package br.com.eduard.eduardapi.core;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Objects;

import lombok.val;
import br.com.eduard.java_utils.Extra;
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
                            if (fieldValue instanceof Keyed){
                                var key = ((Keyed) fieldValue).getKey().toString();
                                section.add(method.getName(), key);
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


    private void saveEnum(Class<?> value, String... ignoredMethods) {
        try {
            getPlugin().log("Saving Enum?: "+value);
            if (!value.isEnum()){
                getPlugin().log("Saving canceled cause is not Enum");
                return;
            }
            Config config = new Config(plugin, "database/" + value.getSimpleName() + ".yml");
            if (config.existConfig()){
                getPlugin().log("Skipping this Enum cause already have generated infos!");
                return;
            }
            boolean used = false;
            for (Object part : value.getEnumConstants()) {
                try {
                    Enum<?> obj = (Enum<?>) part;
                    ConfigSection section = config.add(obj.name(), obj.ordinal());
                    if (obj.name().startsWith("LEGACY")) {
                        // getPlugin().log("Ignorando Enum Legacy que não suporta mais ID: "+obj.name());
                        continue;
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
                                Object test = method.invoke(obj);
                                if (test == null)
                                    continue;
                                if (test instanceof Class)
                                    continue;
                                section.add(method.getName(), test);
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
