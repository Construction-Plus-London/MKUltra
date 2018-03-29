package com.chaosbuffalo.mkultra.core.classes;

import com.chaosbuffalo.mkultra.api.ArmorClass;
import com.chaosbuffalo.mkultra.api.BaseAbility;
import com.chaosbuffalo.mkultra.api.BaseClass;
import com.chaosbuffalo.mkultra.api.IArmorClass;
import com.chaosbuffalo.mkultra.core.CoreClass;
import com.chaosbuffalo.mkultra.core.abilities.*;
import com.chaosbuffalo.mkultra.init.ModItems;
import net.minecraft.item.Item;

import java.util.ArrayList;
import java.util.List;

public class Cleric extends CoreClass {

    private static final List<BaseAbility> abilities = new ArrayList<>(5);

    static {
        abilities.add(new Smite());
        abilities.add(new Heal());
        abilities.add(new Galvanize());
        abilities.add(new PowerWordSummon());
        abilities.add(new Inspire());
    }

    public Cleric() {
        super("class.cleric", "Cleric");
    }

    @Override
    public List<BaseAbility> getAbilities() {
        return abilities;
    }

    @Override
    public int getIconU(){return 0;}

    @Override
    public int getIconV(){return 0;}

    @Override
    public int getHealthPerLevel(){
        return 2;
    }

    @Override
    public int getBaseHealth(){
        return 20;
    }

    @Override
    public float getBaseManaRegen(){
        return 1;
    }

    @Override
    public float getManaRegenPerLevel(){
        return 0.2f;
    }

    @Override
    public int getBaseMana(){
        return 12;
    }

    @Override
    public int getManaPerLevel(){
        return 1;
    }

    @Override
    public IArmorClass getArmorClass() {
        return ArmorClass.HEAVY;
    }

    @Override
    public Item getUnlockItem() {
        return ModItems.sunicon;
    }
}
