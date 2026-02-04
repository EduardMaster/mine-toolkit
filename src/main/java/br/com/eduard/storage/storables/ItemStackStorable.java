package br.com.eduard.storage.storables;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.*;
import java.util.Map.Entry;

import br.com.eduard.mine_toolkit.kotlin.BukkitExKt;
import com.google.gson.*;
import br.com.eduard.mine_utils.game.ItemBuilder;
import br.com.eduard.storage.StorageAPI;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import br.com.eduard.mine_utils.Mine;
import br.com.eduard.mine_utils.game.EnchantGlow;
import br.com.eduard.java_utils.Extra;
import br.com.eduard.storage.api.Storable;


public class ItemStackStorable implements Storable<ItemStack>, JsonSerializer<ItemStack>, JsonDeserializer<ItemStack> {

    public ItemStack deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return (ItemStack) StorageAPI.restore(ItemStack.class, jsonDeserializationContext.deserialize(jsonElement, Map.class));
    }

    public JsonElement serialize(ItemStack itemStack, Type type, JsonSerializationContext jsonSerializationContext) {
        return jsonSerializationContext.serialize(StorageAPI.store(ItemStack.class, itemStack));
    }

    private static Method isLegacyMethod = null;
    private static Method getTypeIdMethod = null;
    private static final Map<Integer, Material> typesByID = new HashMap<>();

    static {
        try {
            isLegacyMethod = Extra.getMethod(Material.class, "isLegacy");
            Map<String, Material> mats = (Map<String, Material>) Extra.getFieldValue(Material.class, "BY_NAME");
            for (Entry<String, Material> entry : mats.entrySet()) {
                Material mat = entry.getValue();
                if (mat == null) continue;
                boolean isOld = (boolean) isLegacyMethod.invoke(mat);
                //Mine.console("§aMaterialName: "+mat.name());
                //Mine.console("§aMaterialString: "+mat.toString());
                if (isOld) {
                    typesByID.put(mat.getId(), mat);
                    //Mine.console("§eMaterialId: "+mat.getId());
                }
            }
        } catch (Exception ex) {
            // ex.printStackTrace();
        }
        try {
            getTypeIdMethod = Extra.getMethod(ItemStack.class, "getTypeId");
        } catch (Exception ignored) {
        }
    }

    public static int getTypeId(ItemStack itemStack) {
        if (getTypeIdMethod != null) {
            try {
                return (int) getTypeIdMethod.invoke(itemStack);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return itemStack.getType().getId();
    }


    @Override
    public ItemStack newInstance() {
        return new ItemStack(Material.STONE);
    }

    @Override
    public ItemStack restore(Map<String, Object> map) {
        int amount = (map.containsKey("amount")) ? Extra.toInt(map.get("amount")) : 1;
        int data = (map.containsKey("data")) ? Extra.toInt(map.get("data")) : 0;
        Material type = Material.values()[0];
        String typeName = null;
        if (map.containsKey("id")) {
            int id = Extra.toInt(map.get("id"));
            type = typesByID.get(id);
        }
        if (map.containsKey("type")) {
            try {
                typeName = map.get("type").toString()
                        .toUpperCase();
                type = Material.matchMaterial(typeName);
                if (type == null)
                    try {
                        type = (Material) Extra.getFieldValue(Material.class, typeName);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                if (type == null) {
                    type = Material.getMaterial(typeName);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        if (type == null) {
            type = Material.getMaterial("STONE");
        }

        ItemBuilder item = new ItemBuilder(type, amount);

        if (data != 0) {
            item.data(data);
        }

        if (map.containsKey("name")) {
            String name = Extra.toChatMessage((String) map.get("name"));
            if (!name.isEmpty()) {
                Mine.setName(item, name);
            }
        }
        if (map.containsKey("lore")) {
            Object dado = map.get("lore");
            if (dado instanceof List) {
                @SuppressWarnings("unchecked")
                List<String> lore = Extra.toMessages((List<Object>) dado);
                if (!lore.isEmpty()) {
                    Mine.setLore(item, lore);
                }
            }
        }
        if (map.containsKey("enchants")) {
            if (map.get("enchants") instanceof String) {

            } else {
                List<String> enchants = (List<String>) map.get("enchants");

                for (String enchantLine : enchants) {
                    String[] sub = enchantLine.split(";");
                    @SuppressWarnings("deprecation")
                    Enchantment ench = Enchantment.getByKey(NamespacedKey.fromString(sub[0]));
                    Integer level = Extra.toInt(sub[1]);
                    item.addUnsafeEnchantment(ench, level);
                }
            }
        }

        if (map.containsKey("texture")) {
            item.setSkinURL(map.get("texture").toString());
        }
        if (map.containsKey("texture-value")) {
            ItemMeta meta = item.getItemMeta();
            if (meta instanceof SkullMeta) {
                GameProfile profile = new GameProfile(UUID.randomUUID(), null);
                profile.getProperties().put("textures", new Property("textures", (String) map.get("texture-value")));
                Field profileField;
                try {
                    profileField = meta.getClass().getDeclaredField("profile");
                    profileField.setAccessible(true);
                    profileField.set(meta, profile);

                } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e) {
                    e.printStackTrace();
                }
                item.setItemMeta(meta);
            }
        } else if (map.containsKey("texture")) {
            Mine.setSkin(item, (String) map.get("texture"));
        } else if (map.containsKey("head-name")) {
            ItemMeta meta = item.getItemMeta();
            if (meta instanceof SkullMeta) {
                SkullMeta skullmeta = (SkullMeta) meta;
                skullmeta.setOwner("" + map.get("head-name"));
                item.setItemMeta(skullmeta);
            }
        }
        if (map.containsKey("glow")) {
            boolean glowed = Extra.toBoolean(map.get("glow"));
            if (glowed) {
                EnchantGlow.addGlow(item);
            }
        }
        try {
            if (!item.getEnchantments().isEmpty()) {
                BukkitExKt.displayEnchants(item);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return item;
    }


    @Override
    public void store(Map<String, Object> map, ItemStack item) {
        if (isLegacyMethod != null) {
            map.put("type", item.getType().toString());
        } else {
            int id = getTypeId(item);
            map.put("id", id);
            map.put("type", item.getType().toString());
        }
        if (item.getAmount() > 1) {
            map.put("amount", item.getAmount());
        }
        if (item.containsEnchantment(EnchantGlow.getGlow())) {
            map.put("glow", true);
        }
        if (item instanceof ItemBuilder) {
            ItemBuilder itemBuilder = (ItemBuilder) item;
            if (itemBuilder.getSkinURL() != null) {
                map.put("texture", itemBuilder.getSkinURL());
            }
        }
        List<String> enchants = new ArrayList<>();
        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta == null) return;
        if (item.getDurability() != 0) {
            map.put("data", item.getDurability());
        }
        map.put("name", Mine.getName(item));
        map.put("lore", Extra.toConfigMessages(Mine.getLore(item)));
        if (itemMeta.hasEnchants()) {
            try {
                for (Entry<Enchantment, Integer> entry : item.getEnchantments().entrySet()) {
                    Enchantment enchantment = entry.getKey();
                    enchants.add(enchantment.getKey() + ";" + entry.getValue());
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        map.put("enchants", enchants);
        if (itemMeta instanceof SkullMeta) {
            SkullMeta meta = (SkullMeta) itemMeta;
            if (meta.getOwner() != null) {
                map.put("head-name", meta.getOwner());
            }
            try {
                Field profileField = meta.getClass().getDeclaredField("profile");
                profileField.setAccessible(true);
                GameProfile profile = (GameProfile) profileField.get(meta);
                if (profile == null)
                    return;
                if (profile.getProperties() == null)
                    return;
                Collection<Property> textures = profile.getProperties().get("textures");
                if (textures == null)
                    return;
                if (textures.size() == 0) return;
                for (Property texture : textures) {
                    map.put("texture-value", texture.value());
                    map.put("texture-signature", texture.signature());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public ItemStack restore(String text) {
        try {
            String[] split = text.split(";");
            String[] splitData = split[0].split("-");
            Integer qnt = Extra.toInt(splitData[1]);
            String[] splitInfo = splitData[0].split(":");
            Material material = Material.getMaterial(splitInfo[0]);
            short data = Extra.toShort(splitInfo[1]);
            ItemStack item = new ItemStack(Material.AIR);
            item.setType(material);
            item.setDurability(data);
            item.setAmount(qnt);
            if (split.length > 0) {
                if (split[1].contains(",")) {
                    String[] enchs = split[1].split(",");
                    for (String enchant : enchs) {
                        String[] ench = enchant.split("-");
                        NamespacedKey ench_id = NamespacedKey.fromString(ench[0]);
                        Integer ench_level = Extra.toInt(ench[1]);
                        item.addUnsafeEnchantment(Enchantment.getByKey(ench_id), ench_level);
                    }
                } else {
                    if (!split[1].equals(" ")) {
                        String[] ench = split[1].split("-");
                        NamespacedKey ench_id = NamespacedKey.fromString(ench[0]);
                        Integer ench_level = Extra.toInt(ench[1]);
                        item.addUnsafeEnchantment(Enchantment.getByKey(ench_id), ench_level);
                    }

                }
            }
            String nome = split[2];
            if (!nome.equals(" ")) {
                Mine.setName(item, Extra.toChatMessage(nome));
            }
            List<String> lista = new ArrayList<>();
            String descricao = split[3];
            if (descricao.contains(",")) {
                String[] lore = descricao.split(",");
                for (String line : lore) {
                    lista.add(Extra.toChatMessage(line));
                }
            } else {
                if (!descricao.equals(" ")) {
                    lista.add(descricao);
                }
            }
            Mine.setLore(item, lista);
            return item;

        } catch (Exception e) {
            e.printStackTrace();
            return new ItemStack(Material.AIR);
        }


    }

    @SuppressWarnings("deprecation")
    @Override
    public String store(ItemStack item) {
        StringBuilder textao = new StringBuilder();
        textao.append(item.getType().getKey() + ":" + item.getDurability() + "-" + item.getAmount() + ";");
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            if (meta.hasEnchants()) {
                boolean first = true;
                for (Entry<Enchantment, Integer> enchant : item.getItemMeta().getEnchants().entrySet()) {
                    if (!first) {
                        textao.append(",");
                    } else
                        first = false;
                    textao.append(enchant.getKey().toString());
                    textao.append("-");
                    textao.append(enchant.getValue());
                }
            } else {
                textao.append(" ");
            }
            textao.append(";");
            if (item.getItemMeta().hasDisplayName()) {
                textao.append(item.getItemMeta().getDisplayName());
            } else {
                textao.append(" ");
            }
            textao.append(";");
            if (meta.hasLore()) {
                boolean first = true;
                for (String line : meta.getLore()) {
                    if (!first) {
                        textao.append(",");
                    } else
                        first = false;
                    textao.append(line);
                }
            } else {
                textao.append(" ");
            }
            textao.append(";");
        }
        return textao.toString();

    }


}
