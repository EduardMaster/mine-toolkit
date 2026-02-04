package br.com.eduard.storage.storables;

import br.com.eduard.storage.api.annotations.StorageAttributes;
import org.bukkit.potion.PotionEffectType;

import br.com.eduard.storage.api.Storable;

@StorageAttributes(inline = true)
public class PotionEffectTypeStorable implements Storable<PotionEffectType> {

    public PotionEffectType restore(String string) {
        String[] split = string.split(";");
        return PotionEffectType.getByName(split[0]);
    }

    public String store(PotionEffectType potionEffectType) {
        return potionEffectType.getName() + ";" + potionEffectType.getId();
    }

}
