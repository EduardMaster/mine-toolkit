package br.com.eduard.storage.storables;

import br.com.eduard.storage.api.annotations.StorageAttributes;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;

import br.com.eduard.java_utils.Extra;
import br.com.eduard.storage.api.Storable;

@StorageAttributes(inline = true)
public class ChunkStorable implements Storable<Chunk> {

    public Chunk restore(String string) {
        String[] split = string.split(";");
        return Bukkit.getWorld(split[0]).getChunkAt(Extra.toInt(split[1]), Extra.toInt(split[2]));
    }

    public String store(Chunk chunk) {
        return chunk.getWorld().getName() + ";" + chunk.getX() + ";" + chunk.getZ();
    }

}
