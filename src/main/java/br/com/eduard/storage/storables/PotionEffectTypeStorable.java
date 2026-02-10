package br.com.eduard.storage.storables;

import br.com.eduard.storage.annotations.StorageAttributes;
import org.bukkit.NamespacedKey;
import org.bukkit.potion.PotionEffectType;

import br.com.eduard.storage.Storable;

@StorageAttributes(inline = true)
public class PotionEffectTypeStorable implements Storable<PotionEffectType> {

    public PotionEffectType restore(String string) {
        String[] split = string.split(";");
        return PotionEffectType.getByKey(NamespacedKey.fromString(split[0]));
    }

    public String store(PotionEffectType potionEffectType) {
        return potionEffectType.getKey() + ";" + potionEffectType.getName()+";"+potionEffectType.getId();
    }

}
