package br.com.eduard.storage.storables;

import br.com.eduard.java_utils.Extra;
import br.com.eduard.storage.api.annotations.StorageAttributes;
import org.bukkit.Material;
import org.bukkit.material.MaterialData;

import br.com.eduard.storage.api.Storable;

@StorageAttributes(inline = true)
public class MaterialDataStorable implements Storable<MaterialData> {

    @Override
    public MaterialData newInstance() {
        return new MaterialData(Material.AIR);
    }

    @Override
    public String store(MaterialData materialData) {
        return materialData.getItemType().getKey() + ";" + materialData.getData();
    }

    public MaterialData restore(String string) {
        try {
            if (string.contains(";")) {
                String[] split = string.split(";");
                return new MaterialData(Material.getMaterial(split[0]), Extra.toByte(split[1]));
            }else{
                return  new MaterialData(Material.getMaterial(string));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newInstance();
    }

}
