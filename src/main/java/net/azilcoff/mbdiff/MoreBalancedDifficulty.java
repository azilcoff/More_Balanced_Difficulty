package net.azilcoff.mbdiff;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.equipment.ArmorMaterial;
import net.minecraft.item.equipment.ArmorMaterials;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Set;


public class MoreBalancedDifficulty implements ModInitializer {
	public static final String MOD_ID = "mbdiff";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private static boolean pullChance(ServerWorld world, int chance){
        if (chance == 100) return true;
        else if (chance == 0) return false;

        return world.random.nextBetween(0, 99) < chance;
    }

    private static int getPercentageOf(int num, int percentage){
        return percentage * num / 100;
    }

    private static final Set<EntityType<?>> HOSTILE_EQUIPPABLE_ENTITIES = Set.of(
            EntityType.ZOMBIE,
            EntityType.DROWNED,
            EntityType.HUSK,
            EntityType.SKELETON,
            EntityType.WITHER_SKELETON,
            EntityType.STRAY,
            EntityType.BOGGED,
            EntityType.PILLAGER,
            EntityType.VINDICATOR,
            EntityType.EVOKER,
            EntityType.WITCH,
            EntityType.PIGLIN,
            EntityType.PIGLIN_BRUTE,
            EntityType.HOGLIN
    );

    private static final Map<ArmorMaterial, Map<EquipmentSlot, Item>> ARMOR_BY_MATERIAL = Map.of(
            ArmorMaterials.LEATHER, Map.of(
                    EquipmentSlot.HEAD, Items.LEATHER_HELMET,
                    EquipmentSlot.CHEST, Items.LEATHER_CHESTPLATE,
                    EquipmentSlot.LEGS, Items.LEATHER_LEGGINGS,
                    EquipmentSlot.FEET, Items.LEATHER_BOOTS
            ),
            ArmorMaterials.IRON, Map.of(
                    EquipmentSlot.HEAD, Items.IRON_HELMET,
                    EquipmentSlot.CHEST, Items.IRON_CHESTPLATE,
                    EquipmentSlot.LEGS, Items.IRON_LEGGINGS,
                    EquipmentSlot.FEET, Items.IRON_BOOTS
            ),
            ArmorMaterials.GOLD, Map.of(
                    EquipmentSlot.HEAD, Items.GOLDEN_HELMET,
                    EquipmentSlot.CHEST, Items.GOLDEN_CHESTPLATE,
                    EquipmentSlot.LEGS, Items.GOLDEN_LEGGINGS,
                    EquipmentSlot.FEET, Items.GOLDEN_BOOTS
            ),
            ArmorMaterials.DIAMOND, Map.of(
                    EquipmentSlot.HEAD, Items.DIAMOND_HELMET,
                    EquipmentSlot.CHEST, Items.DIAMOND_CHESTPLATE,
                    EquipmentSlot.LEGS, Items.DIAMOND_LEGGINGS,
                    EquipmentSlot.FEET, Items.DIAMOND_BOOTS
            ),
            ArmorMaterials.NETHERITE, Map.of(
                    EquipmentSlot.HEAD, Items.NETHERITE_HELMET,
                    EquipmentSlot.CHEST, Items.NETHERITE_CHESTPLATE,
                    EquipmentSlot.LEGS, Items.NETHERITE_LEGGINGS,
                    EquipmentSlot.FEET, Items.NETHERITE_BOOTS
            ),
            ArmorMaterials.COPPER, Map.of(
                    EquipmentSlot.HEAD, Items.COPPER_HELMET,
                    EquipmentSlot.CHEST, Items.COPPER_CHESTPLATE,
                    EquipmentSlot.LEGS, Items.COPPER_LEGGINGS,
                    EquipmentSlot.FEET, Items.COPPER_BOOTS
            )
    );

    private static final Map<ToolMaterial, Item> SWORDS_BY_MATERIAL = Map.of(
            ToolMaterial.WOOD, Items.WOODEN_SWORD,
            ToolMaterial.STONE, Items.STONE_SWORD,
            ToolMaterial.IRON, Items.IRON_SWORD,
            ToolMaterial.GOLD, Items.GOLDEN_SWORD,
            ToolMaterial.DIAMOND, Items.DIAMOND_SWORD,
            ToolMaterial.NETHERITE, Items.NETHERITE_SWORD,
            ToolMaterial.COPPER, Items.COPPER_SWORD
    );

    private static final Map<EquipmentSlot, List<RegistryKey<Enchantment>>> ENCHANTMENTS_BY_SLOT = Map.of(
            EquipmentSlot.HEAD, List.of(
                    Enchantments.PROTECTION,
                    Enchantments.FIRE_PROTECTION,
                    Enchantments.BLAST_PROTECTION,
                    Enchantments.PROJECTILE_PROTECTION,
                    Enchantments.RESPIRATION,
                    Enchantments.AQUA_AFFINITY,
                    Enchantments.THORNS
            ),
            EquipmentSlot.CHEST, List.of(
                    Enchantments.PROTECTION,
                    Enchantments.FIRE_PROTECTION,
                    Enchantments.BLAST_PROTECTION,
                    Enchantments.PROJECTILE_PROTECTION,
                    Enchantments.THORNS
            ),
            EquipmentSlot.LEGS, List.of(
                    Enchantments.PROTECTION,
                    Enchantments.FIRE_PROTECTION,
                    Enchantments.BLAST_PROTECTION,
                    Enchantments.PROJECTILE_PROTECTION
            ),
            EquipmentSlot.FEET, List.of(
                    Enchantments.PROTECTION,
                    Enchantments.FIRE_PROTECTION,
                    Enchantments.BLAST_PROTECTION,
                    Enchantments.PROJECTILE_PROTECTION,
                    Enchantments.FEATHER_FALLING,
                    Enchantments.DEPTH_STRIDER,
                    Enchantments.FROST_WALKER
            ),
            EquipmentSlot.MAINHAND, List.of(
                    Enchantments.SHARPNESS,
                    Enchantments.SMITE,
                    Enchantments.BANE_OF_ARTHROPODS,
                    Enchantments.KNOCKBACK,
                    Enchantments.FIRE_ASPECT,
                    Enchantments.LOOTING,
                    Enchantments.SWEEPING_EDGE,
                    Enchantments.UNBREAKING,
                    Enchantments.MENDING,
                    Enchantments.VANISHING_CURSE
            )
    );

    private static RegistryEntry<Enchantment> getEntryFromKey (RegistryKey<Enchantment> key,ServerWorld serverWorld){
        Registry<Enchantment> enchantmentRegistry = serverWorld.getRegistryManager().getOrThrow(RegistryKeys.ENCHANTMENT);
        return enchantmentRegistry.getEntry(key.getValue()).orElseThrow();
    }


    private static ItemStack getRandomEquipmentPiece(ServerWorld world, EquipmentSlot slot, HostileEntity hostileEntity){
        if (slot != EquipmentSlot.MAINHAND){
            ArmorMaterial material;

            if (pullChance(world, 1)) material = ArmorMaterials.NETHERITE;
            else if (pullChance(world, 3)) material = ArmorMaterials.DIAMOND;
            else if (pullChance(world, 20)) material = ArmorMaterials.GOLD;
            else if (pullChance(world, 36)) material = ArmorMaterials.COPPER;
            else if (pullChance(world, 43)) material = ArmorMaterials.LEATHER;
            else material = ArmorMaterials.IRON;

            ItemStack armorPiece = ARMOR_BY_MATERIAL.get(material).get(slot).getDefaultStack();

            armorPiece.damage(getPercentageOf(armorPiece.getMaxDamage(), world.random.nextBetween(60,80)), hostileEntity, slot);

            if (pullChance(world, 15)){
                float enchantChance = 25;
                int i = world.random.nextBetween(0, ENCHANTMENTS_BY_SLOT.get(slot).size()-1);
                do{
                    armorPiece.addEnchantment(getEntryFromKey(ENCHANTMENTS_BY_SLOT.get(slot).get(i), world), 1);

                    i = world.random.nextBetween(0, ENCHANTMENTS_BY_SLOT.get(slot).size()-1);
                    enchantChance /= 2;
                }while (pullChance(world, Math.round(enchantChance)));
            }

            return armorPiece;
        }
        else{
            ToolMaterial material;

            if (pullChance(world, 1)) material = ToolMaterial.NETHERITE;
            else if (pullChance(world, 3)) material = ToolMaterial.DIAMOND;
            else if (pullChance(world, 36)) material = ToolMaterial.GOLD;
            else if (pullChance(world, 50)) material = ToolMaterial.COPPER;
            else material = ToolMaterial.IRON;

            ItemStack sword = SWORDS_BY_MATERIAL.get(material).getDefaultStack();

            sword.damage(getPercentageOf(sword.getMaxDamage(), world.random.nextBetween(60,80)), hostileEntity, slot);

            if (pullChance(world, 15)){
                float enchantChance = 25;
                int i = world.random.nextBetween(0, ENCHANTMENTS_BY_SLOT.get(slot).size()-1);
                do{
                    sword.addEnchantment(getEntryFromKey(ENCHANTMENTS_BY_SLOT.get(slot).get(i), world), 1);

                    i = world.random.nextBetween(0, ENCHANTMENTS_BY_SLOT.get(slot).size()-1);
                    enchantChance /= 2;
                }while (pullChance(world, Math.round(enchantChance)));
            }

            return sword;
        }
    }

	@Override
	public void onInitialize() {
        ServerEntityEvents.ENTITY_LOAD.register((entity, serverWorld) -> {
            if (entity instanceof HostileEntity hostileEntity && HOSTILE_EQUIPPABLE_ENTITIES.contains(hostileEntity.getType())){
                hostileEntity.setEquipmentDropChance(EquipmentSlot.MAINHAND, 0);
                hostileEntity.setEquipmentDropChance(EquipmentSlot.HEAD, 0);
                hostileEntity.setEquipmentDropChance(EquipmentSlot.CHEST, 0);
                hostileEntity.setEquipmentDropChance(EquipmentSlot.LEGS, 0);
                hostileEntity.setEquipmentDropChance(EquipmentSlot.FEET, 0);

                if (pullChance(serverWorld, 28)) hostileEntity.equipStack(EquipmentSlot.MAINHAND, getRandomEquipmentPiece(serverWorld, EquipmentSlot.MAINHAND, hostileEntity));
                if (pullChance(serverWorld, 66)) hostileEntity.equipStack(EquipmentSlot.HEAD, getRandomEquipmentPiece(serverWorld, EquipmentSlot.HEAD, hostileEntity));
                if (pullChance(serverWorld, 15)) hostileEntity.equipStack(EquipmentSlot.CHEST, getRandomEquipmentPiece(serverWorld, EquipmentSlot.CHEST, hostileEntity));
                if (pullChance(serverWorld, 34)) hostileEntity.equipStack(EquipmentSlot.LEGS, getRandomEquipmentPiece(serverWorld, EquipmentSlot.LEGS, hostileEntity));
                if (pullChance(serverWorld, 48)) hostileEntity.equipStack(EquipmentSlot.FEET, getRandomEquipmentPiece(serverWorld, EquipmentSlot.FEET, hostileEntity));
            }
        });
	}
}