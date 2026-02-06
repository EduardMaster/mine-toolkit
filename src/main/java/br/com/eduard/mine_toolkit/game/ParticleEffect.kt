package br.com.eduard.mine_toolkit.game

import br.com.eduard.mine_utils.Mine
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.Potion
import org.bukkit.potion.PotionType

class ParticleEffect(
    var particle: Particle =Particle.HEART,
    var amount: Int=1,
    var speed: Float=0f,
    var xRandom: Float=0f,
    var yRandom: Float=0f,
    var zRandom: Float=0f
) {

    constructor() : this(Particle.HEART);
    constructor(particle: Particle) : this(particle , 1,0f,0f,0f,0f)
    constructor(particle: Particle, amount: Int) : this(particle , amount,0f,0f,0f,0f)
    constructor(particle: Particle, amount: Int,speed: Float) : this(particle , amount,speed,0f,0f,0f)



    fun create(player: Player, local: Location): ParticleEffect {
       // Minecraft.instance.sendParticle(player, particle.particleName, local, amount, xRandom, yRandom, zRandom, speed)
        player.spawnParticle(this.particle, local, amount, xRandom.toDouble(),yRandom.toDouble(),zRandom.toDouble(), speed)
        return this
    }

    fun create(local: Location): ParticleEffect {
        for (player in Mine.getPlayers()) {
            create(player,local)
            /*
            Minecraft.instance.sendParticle(
                player,
                particle.particleName,
                local,
                amount,
                xRandom,
                yRandom,
                zRandom,
                speed
            )*/
        }
        return this
    }

    enum class ParticleType(var particleName: String,
                            var id: Int,
                            var ptName : String
                            , var icon : ItemStack = ItemStack(Material.PAPER)
    ) {

        HUGE_EXPLOSION("hugeexplosion", 0," Enorme explosão",
            ItemStack(Material.TNT)),
        LARGE_EXPLODE("largeexplode", 1,"Grande explosão",
            ItemStack(Material.TNT_MINECART)),
        FIREWORKS_SPARK("fireworksSpark", 2,"Faíscas de fogos de artifício"
            ,ItemStack(Material.LEGACY_FIREWORK_CHARGE))
        , BUBBLE("bubble", 3,"Bolha",
            ItemStack(Material.LEGACY_INK_SACK,1,12)),
        SUSPEND("suspend", 4,"Invalido" ,
            ItemStack(Material.BARRIER)),
        DEPTH_SUSPEND("depthSuspend", 5,"Profundidade suspensa"
            , ItemStack(Material.FLINT_AND_STEEL)),
        TOWN_AURA("townaura", 6,"Aura da cidade"
            , ItemStack(Material.BEDROCK)),
        CRIT("crit", 7,"Critico",ItemStack(Material.IRON_SWORD)),
        MAGIC_CRIT("magicCrit", 8,"Crítico mágico", ItemStack(Material.ENCHANTED_BOOK)),
        MOB_SPELL("mobSpell", 9,
            "Feitiço de monstro",ItemStack(Material.LEGACY_FIREWORK_CHARGE)),

        MOB_SPELL_AMBIENT("mobSpellAmbient", 10,
            "Feitiço de monstro ambiental",Potion(PotionType.NIGHT_VISION).toItemStack(1)),
        SPELL("spell", 11,"Feitiço",ItemStack(Material.GLASS_BOTTLE)),
        INSTANT_SPELL("instantSpell", 12,"Feitiço instantaneo",Potion(PotionType.INSTANT_HEAL,1, true,false).toItemStack(1)),
        WITCH_MAGIC("witchMagic", 13,"Bruxa",Potion(PotionType.JUMP,1, true,false).toItemStack(1)),
        NOTE("note", 14,"Nota",ItemStack(Material.LEGACY_RECORD_9)),
        PORTAL("portal", 15,"Portal", ItemStack(Material.LEGACY_EYE_OF_ENDER)),
        ENCHANTMENT_TABLE("enchantmenttable", 16, "Mesa de encantamento"
            ,ItemStack(Material.LEGACY_ENCHANTMENT_TABLE)),

        EXPLODE("explode", 17,"Explosão",ItemStack(Material.LEGACY_EXPLOSIVE_MINECART)),
        FLAME("flame", 18,"Chama",ItemStack(Material.FLINT_AND_STEEL)),
        LAVA("lava", 19,"Lava",ItemStack(Material.BLAZE_POWDER)),
        FOOTSTEP("footstep",
            20,"Movimento",ItemStack(Material.DIAMOND_BOOTS)),
        SPLASH("splash", 21
            ,"Respingo",ItemStack(Material.LEGACY_WATER_LILY)),
        LARGE_SMOKE("largesmoke", 22,"Grande fumaça",ItemStack(Material.FEATHER)),
        CLOUD("cloud", 23,"Nuvem",ItemStack(Material.SUGAR)),
        RED_DUST("reddust", 24,"Poeira vermelha",ItemStack(Material.REDSTONE)),
        SNOWBALL_POOF("snowballpoof", 25,"Bola de Neve Poof",
            ItemStack(Material.LEGACY_SNOW_BALL)),
        DRIP_WATER("dripWater", 26,"Gota d'água",ItemStack(Material.GHAST_TEAR)),
        DRIP_LAVA("dripLava", 27,"Gota de Lava",ItemStack(Material.LAVA_BUCKET))
        , SNOW_SHOVEL("snowshovel", 28,"Bola de neve",ItemStack(Material.SNOW_BLOCK)),
        SLIME("slime", 29,"Slime",ItemStack(Material.SLIME_BALL)),
        HEART("heart", 30,"Coração", ItemStack(Material.APPLE)),
        ANGRY_VILLAGER("angryVillager", 31,"Villager Bravo", ItemStack(Material.LEGACY_FIREBALL)),
        HAPPY_VILLAGER("happyVillager", 32,"Villager Feliz"
            , ItemStack(Material.EMERALD)),
        WAKE("wake",33,"Despertado",ItemStack(Material.GREEN_BED))
    }
}