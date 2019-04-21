package com.chaosbuffalo.mkultra.core.abilities;

import com.chaosbuffalo.mkultra.GameConstants;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.effects.AreaEffectBuilder;
import com.chaosbuffalo.mkultra.effects.SpellCast;
import com.chaosbuffalo.mkultra.effects.spells.CriticalChancePotion;
import com.chaosbuffalo.mkultra.effects.spells.ParticlePotion;
import com.chaosbuffalo.mkultra.core.PlayerAbility;
import com.chaosbuffalo.mkultra.core.IPlayerData;
import com.chaosbuffalo.mkultra.fx.ParticleEffects;
import com.chaosbuffalo.mkultra.network.packets.ParticleEffectSpawnPacket;
import com.chaosbuffalo.mkultra.fx.ParticleStyle;
import com.chaosbuffalo.targeting_api.Targeting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class Yaup extends PlayerAbility {

    public Yaup() {
        super(MKUltra.MODID, "ability.yaup");
    }

    @Override
    public int getCooldown(int currentRank) {
        return 45;
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
    public int getManaCost(int currentRank) {
        return 2 + currentRank * 2;
    }

    @Override
    public float getDistance(int currentRank) {
        return 10.0f + (currentRank * 5.0f);
    }

    @Override
    public int getRequiredLevel(int currentRank) {
        return 4 + currentRank * 2;
    }

    private ParticleStyle castStyle = new ParticleStyle.Builder(EnumParticleTypes.CRIT)
            .motion(ParticleEffects.SPHERE_MOTION, 5).count(50)
            .speed(0.5f)
            .radius(RADIUS_ONE).offset(OFFSET_Y_ONE)
            .build();
    private ParticleStyle effStyle = new ParticleStyle.Builder(EnumParticleTypes.CRIT)
            .motion(ParticleEffects.SPHERE_MOTION, 5).count(50)
            .speed(0.5f)
            .radius(RADIUS_ONE).offset(OFFSET_Y_ONE)
            .build();

    @Override
    public void execute(EntityPlayer entity, IPlayerData pData, World theWorld) {
        pData.startAbility(this);

        int level = pData.getAbilityRank(getAbilityId());

        int duration = 15 + (level * 15);
        duration *= GameConstants.TICKS_PER_SECOND;

        PotionEffect hasteEffect = new PotionEffect(MobEffects.HASTE, duration, level - 1, false, true);
        PotionEffect damageEffect = new PotionEffect(MobEffects.STRENGTH, duration, level - 1, false, true);
        SpellCast particlePotion = ParticlePotion.Create(entity, effStyle, false);

        AreaEffectBuilder.Create(entity, entity)
                .effect(hasteEffect, getTargetType())
                .effect(damageEffect, getTargetType())
                .spellCast(CriticalChancePotion.Create(entity), level + 1, Targeting.TargetType.SELF)
                .spellCast(particlePotion, 0, getTargetType())
                .instant().color(16751360).radius(getDistance(level), true)
                .spawn();

        performCastAnimation(castStyle, entity, entity.getLookVec());
    }
}