package br.com.eduard.mine_utils.game;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * API de criação de novos Encantamentos
 *
 * @author Eduard
 * @version 1.0
 */
@SuppressWarnings("unused")
public abstract class CustomEnchant extends EnchantmentWrapper {

    private String name;
    private boolean registred;
    private int maxLevel = 1;
    private int startLevel = 1;

    public CustomEnchant() {
        this("custom_enchant");
    }
    public CustomEnchant(String enchantName ) {
        super(enchantName);
        this.name = enchantName;
    }

    public boolean unregister() {
        if (!isRegistred()) return false;
        try {

            Field byNameField = Enchantment.class.getDeclaredField("byName");
            byNameField.setAccessible(true);
            Map<?, ?> byName = (Map<?, ?>) byNameField.get(null);
            byName.remove(getName());

            Field byIdField = Enchantment.class.getDeclaredField("byId");
            byIdField.setAccessible(true);
            Map<?, ?> byId = (Map<?, ?>) byIdField.get(null);
           // byId.remove(getId());

            setRegistred(false);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean canEnchantItem(ItemStack item) {
        return true;
    }

    public ItemStack enchant(ItemStack item, int level) {

        item.addUnsafeEnchantment(this, level);

        ItemMeta meta = item.getItemMeta();
        String enchamentname = "§7" + getName() + " " + level;
        if (meta.getLore() == null) {

            meta.setLore(Collections.singletonList(enchamentname));
        } else {
            List<String> lore = meta.getLore();
            lore.add(0, enchamentname);
            meta.setLore(lore);
        }
        item.setItemMeta(meta);
        return item;
    }


    public boolean register() {
        if (Enchantment.getByName(getName()) != null) return false;
        try {
            Field acceptingNew = Enchantment.class.getDeclaredField("acceptingNew");
            acceptingNew.setAccessible(true);
            acceptingNew.set(null, true);
            Enchantment.registerEnchantment(this);
            setRegistred(true);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;

    }


    @Override
    public String getName() {
        return name;
    }

    public boolean isRegistred() {
        return registred;
    }

    public void setRegistred(boolean registred) {
        this.registred = registred;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public void setMaxLevel(int maxLevel) {
        this.maxLevel = maxLevel;
    }

    public int getStartLevel() {
        return startLevel;
    }

    public void setStartLevel(int startLevel) {
        this.startLevel = startLevel;
    }

}
