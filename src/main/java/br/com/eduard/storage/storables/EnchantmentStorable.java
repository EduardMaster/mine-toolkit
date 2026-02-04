package br.com.eduard.storage.storables;

import br.com.eduard.storage.api.annotations.StorageAttributes;
import org.bukkit.enchantments.Enchantment;

import br.com.eduard.java_utils.Extra;
import br.com.eduard.storage.api.Storable;

@StorageAttributes(inline = true)
public class EnchantmentStorable implements Storable<Enchantment> {
    public String store(Enchantment enchantment) {
        return "" + enchantment.getId();
    }

    public Enchantment restore(String string) {
        return Enchantment.getById(Extra.toInt(string));
    }

}
