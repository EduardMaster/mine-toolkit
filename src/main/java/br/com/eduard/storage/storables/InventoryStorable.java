package br.com.eduard.storage.storables;

import br.com.eduard.java_utils.Extra;
import br.com.eduard.storage.annotations.StorageAttributes;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import br.com.eduard.mine_utils.Mine;
import br.com.eduard.storage.Storable;

@StorageAttributes(inline = true)
public class InventoryStorable implements Storable<Inventory> {

    public Inventory newInstance() {
        return Bukkit.createInventory(null, 6 * 9);
    }

    public Inventory restore(String string) {
        String[] split = string.split("//");
        try {
            Integer lines = Extra.toInt(split[0]);
            ItemStack[] contents = Mine.fromBase64toItems(split[0]);
            Inventory inv = Bukkit.createInventory(null, lines * 9);
            inv.setContents(contents);
            return inv;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return newInstance();
    }

    public String store(Inventory inventory) {
        int lines = inventory.getSize() / 9;
        ItemStack[] content = inventory.getContents();
        return lines + "//" + Mine.fromItemsToBase64(content);
    }

}
