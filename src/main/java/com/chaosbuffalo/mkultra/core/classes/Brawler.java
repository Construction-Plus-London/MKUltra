package com.chaosbuffalo.mkultra.core.classes;

import com.chaosbuffalo.mkultra.api.BaseAbility;
import com.chaosbuffalo.mkultra.api.BaseClass;
import com.chaosbuffalo.mkultra.api.IArmorClass;
import com.chaosbuffalo.mkultra.core.CoreClass;
import com.chaosbuffalo.mkultra.core.abilities.*;
import com.chaosbuffalo.mkultra.api.ArmorClass;
import com.chaosbuffalo.mkultra.init.ModItems;
import net.minecraft.item.Item;

import java.util.ArrayList;
import java.util.List;

public class Brawler extends CoreClass {

    public static final List<BaseAbility> abilities = new ArrayList<>(5);

    static {
        abilities.add(new Yank());
        abilities.add(new FuriousBrooding());
        abilities.add(new Yaup());
        abilities.add(new StunningShout());
        abilities.add(new WhirlwindBlades());
    }

    public Brawler() {
        super("class.brawler", "Brawler");
    }

    @Override
    public IArmorClass getArmorClass() {
        return ArmorClass.MEDIUM;
    }

    @Override
    public List<BaseAbility> getAbilities() {
        return abilities;
    }

    @Override
    public int getIconU() {
        return 0;
    }

    @Override
    public int getIconV() {
        return 0;
    }

    @Override
    public int getHealthPerLevel() {
        return 2;
    }

    @Override
    public int getBaseHealth() {
        return 26;
    }

    @Override
    public float getBaseManaRegen() {
        return 1;
    }

    @Override
    public float getManaRegenPerLevel() {
        return 0.2f;
    }

    @Override
    public int getBaseMana() {
        return 8;
    }

    @Override
    public int getManaPerLevel() {
        return 1;
    }

    @Override
    public Item getUnlockItem() {
        return ModItems.sunicon;
    }
}
