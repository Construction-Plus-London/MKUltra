package com.chaosbuffalo.mkultra.item;

import com.chaosbuffalo.mkultra.api.PlayerAttributes;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

import java.util.UUID;

/**
 * Created by Jacob on 3/23/2018.
 */
public class ItemManaRegenArmor extends ItemArmor {
    private final float bonus;
    private final UUID modifier_id;

    public ItemManaRegenArmor(String unlocalizedName, ItemArmor.ArmorMaterial material, int renderIndex,
                         EntityEquipmentSlot armorType, float bonusIn, UUID modIdIn) {
        super(material, renderIndex, armorType);
        this.bonus = bonusIn;
        this.modifier_id = modIdIn;
        this.setUnlocalizedName(unlocalizedName);
    }

    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack) {

        Multimap<String, AttributeModifier> mods = HashMultimap.create();
        if (slot == this.armorType){
            if (this.bonus > 0){
                AttributeModifier mod =
                        new AttributeModifier(this.modifier_id, "Bonus Mana Regen", this.bonus, PlayerAttributes.OP_INCREMENT)
                                .setSaved(false);
                mods.put(PlayerAttributes.MANA_REGEN.getName(), mod);
            }
        }


        return mods;
    }

}
