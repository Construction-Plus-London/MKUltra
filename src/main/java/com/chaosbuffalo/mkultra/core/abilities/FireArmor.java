package com.chaosbuffalo.mkultra.core.abilities;

import com.chaosbuffalo.mkultra.api.GameConstants;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.effects.AreaEffectBuilder;
import com.chaosbuffalo.mkultra.effects.SpellCast;
import com.chaosbuffalo.mkultra.api.Targeting;
import com.chaosbuffalo.mkultra.effects.spells.ParticlePotion;
import com.chaosbuffalo.mkultra.api.BaseAbility;
import com.chaosbuffalo.mkultra.api.IPlayerData;
import com.chaosbuffalo.mkultra.fx.ParticleEffects;
import com.chaosbuffalo.mkultra.network.packets.server.ParticleEffectSpawnPacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class FireArmor extends BaseAbility {

    public static int BASE_DURATION = 60;
    public static int DURATION_SCALE = 30;

    public FireArmor() {
        super(MKUltra.MODID, "ability.fire_armor");
    }

    @Override
    public String getAbilityName() {
        return "Fire Armor";
    }

    @Override
    public String getAbilityDescription() {
        return "Buffs all surrounding players, giving them absorption and fire resistance.";
    }

    @Override
    public ResourceLocation getAbilityIcon(){
        return new ResourceLocation(MKUltra.MODID, "textures/class/abilities/firearmor.png");
    }


    @Override
    public String getAbilityType() {
        return "Group Buff";
    }

    @Override
    public int getIconU() {
        return 198;
    }

    @Override
    public int getIconV() {
        return 18;
    }

    @Override
    public int getCooldown(int currentLevel) {
        return 150 - currentLevel * 15;
    }

    @Override
    public int getType() {
        return ACTIVE_ABILITY;
    }

    @Override
    public Targeting.TargetType getTargetType() {
        return Targeting.TargetType.FRIENDLY;
    }

    @Override
    public int getManaCost(int currentLevel) {
        return 20 - currentLevel * 4;
    }

    @Override
    public float getDistance(int currentLevel) {
        return 10.0f + 2.0f * currentLevel;
    }

    @Override
    public int getRequiredLevel(int currentLevel) {
        return 4 + currentLevel * 2;
    }

    @Override
    public void execute(EntityPlayer entity, IPlayerData pData, World theWorld) {
        pData.startAbility(this);

        int level = pData.getLevelForAbility(getAbilityId());

        // What to do for each target hit
        int duration = (BASE_DURATION + DURATION_SCALE * level) * GameConstants.TICKS_PER_SECOND;

        PotionEffect absorbEffect = new PotionEffect(MobEffects.ABSORPTION, duration, level + 1, false, true);

        PotionEffect fireResistanceEffect = new PotionEffect(MobEffects.FIRE_RESISTANCE, duration, level, false, true);

        SpellCast particlePotion = ParticlePotion.Create(entity,
                EnumParticleTypes.FLAME.getParticleID(),
                ParticleEffects.CIRCLE_PILLAR_MOTION, false, new Vec3d(1.0, 1.0, 1.0),
                new Vec3d(0.0, 1.0, 0.0), 40, 5, 1.0);

        AreaEffectBuilder.Create(entity, entity)
                .effect(absorbEffect, getTargetType())
                .effect(fireResistanceEffect, getTargetType())
                .spellCast(particlePotion, level, getTargetType())
                .instant()
                .particle(EnumParticleTypes.DRIP_LAVA)
                .color(16762905).radius(getDistance(level), true)
                .spawn();

        Vec3d lookVec = entity.getLookVec();
        MKUltra.packetHandler.sendToAllAround(
                new ParticleEffectSpawnPacket(
                        EnumParticleTypes.FLAME.getParticleID(),
                        ParticleEffects.CIRCLE_MOTION, 50, 0,
                        entity.posX, entity.posY + 1.0,
                        entity.posZ, 1.0, 1.0, 1.0, 1.0f,
                        lookVec),
                entity, 50.0f);
    }
}
