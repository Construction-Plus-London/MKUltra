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

public class SwiftsRodeoHeartbreak extends BaseAbility {


    public SwiftsRodeoHeartbreak() {
        super(MKUltra.MODID, "ability.swifts_rodeo_heartbreak");
    }

    @Override
    public String getAbilityName() {
        return "Swift's Rodeo";
    }

    @Override
    public String getAbilityDescription() {
        return "Speeds up all your surrounding allies.";
    }

    @Override
    public String getAbilityType() {
        return "Group Buff";
    }

    @Override
    public int getIconU() {
        return 18;
    }

    @Override
    public int getIconV() {
        return 36;
    }

    @Override
    public ResourceLocation getAbilityIcon(){
        return new ResourceLocation(MKUltra.MODID, "textures/class/abilities/swiftsrodeohearthbreak.png");
    }

    @Override
    public int getCooldown(int currentLevel) {
        return 30 - currentLevel * 4;
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
        return 4 + 2 * currentLevel;
    }

    @Override
    public float getDistance(int currentLevel) {
        return 2.0f + currentLevel * 4.0f;
    }

    @Override
    public int getRequiredLevel(int currentLevel) {
        return 4 + currentLevel * 2;
    }

    @Override
    public void execute(EntityPlayer entity, IPlayerData pData, World theWorld) {
        pData.startAbility(this);

        int level = pData.getLevelForAbility(getAbilityId());

        PotionEffect addSpeed = new PotionEffect(MobEffects.SPEED, 30 * GameConstants.TICKS_PER_SECOND, level);
        SpellCast particle = ParticlePotion.Create(entity,
                EnumParticleTypes.NOTE.getParticleID(),
                ParticleEffects.CIRCLE_MOTION, false, new Vec3d(1.0, 1.0, 1.0),
                new Vec3d(0.0, 1.0, 0.0), 40, 5, 1.0);

        AreaEffectBuilder.Create(entity, entity)
                .effect(addSpeed, getTargetType())
                .spellCast(particle, level, getTargetType())
                .instant()
                .color(3338315).radius(getDistance(level), true)
                .particle(EnumParticleTypes.NOTE)
                .spawn();

        Vec3d lookVec = entity.getLookVec();
        MKUltra.packetHandler.sendToAllAround(
                new ParticleEffectSpawnPacket(
                        EnumParticleTypes.NOTE.getParticleID(),
                        ParticleEffects.SPHERE_MOTION, 50, 6,
                        entity.posX, entity.posY + 1.0,
                        entity.posZ, 1.0, 1.0, 1.0, 1.0f,
                        lookVec),
                entity, 50.0f);
    }
}
