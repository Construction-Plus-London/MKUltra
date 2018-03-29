package com.chaosbuffalo.mkultra.api;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.List;

public abstract class BaseClass extends IForgeRegistryEntry.Impl<BaseClass> {

    private String className;
    private ResourceLocation classId;

    public BaseClass(ResourceLocation classId, String className) {
        this.classId = classId;
        this.className = className;
    }

    public ResourceLocation getClassId() {
        return classId;
    }

    public String getClassName() {
        return className;
    }

    public abstract int getBaseHealth();

    public abstract int getHealthPerLevel();

    public abstract float getBaseManaRegen();

    public abstract float getManaRegenPerLevel();

    public abstract int getBaseMana();

    public abstract int getManaPerLevel();

    public int getIconU() {
        return 18;
    }

    public int getIconV() {
        return 0;
    }

    public abstract Item getUnlockItem();

    public abstract IArmorClass getArmorClass();

    public BaseAbility getOfferedAbilityBySlot(int slotIndex) {
        List<BaseAbility> abilities = getAbilities();
        if (slotIndex < abilities.size()) {
            return abilities.get(slotIndex);
        }
        return null;
    }

    public abstract List<BaseAbility> getAbilities();
}
