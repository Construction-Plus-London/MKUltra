package com.chaosbuffalo.mkultra.core.classes;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.*;
import com.chaosbuffalo.mkultra.core.abilities.*;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;


public class Druid extends PlayerClass {
    public static ResourceLocation ID = new ResourceLocation(MKUltra.MODID, "class.druid");

    public static final List<PlayerAbility> abilities = new ArrayList<>(5);
    static {
        abilities.add(new BallLightning());
        abilities.add(new FlameBlade());
        abilities.add(new SpiritOfWolf());
        abilities.add(new LifeSpring());
        abilities.add(new PhoenixAspect());
    }

    public Druid() {
        super(ID);
    }

    @Override
    public ArmorClass getArmorClass() {
        return ArmorClass.LIGHT;
    }

    @Override
    public List<PlayerAbility> getAbilities() {
        return abilities;
    }

    @Override
    public int getHealthPerLevel(){
        return 2;
    }

    @Override
    public int getBaseHealth(){
        return 18;
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
        return 15;
    }

    @Override
    public int getManaPerLevel(){
        return 1;
    }

    @Override
    public IClassClientData getClientData() {
        return ClassClientData.SunIcon.INSTANCE;
    }
}
