package com.chaosbuffalo.mkultra.core.abilities;

import com.chaosbuffalo.mkultra.api.GameConstants;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.effects.AreaEffectBuilder;
import com.chaosbuffalo.mkultra.effects.SpellCast;
import com.chaosbuffalo.mkultra.api.Targeting;
import com.chaosbuffalo.mkultra.effects.spells.FlameBladePotion;
import com.chaosbuffalo.mkultra.effects.spells.ParticlePotion;
import com.chaosbuffalo.mkultra.api.BaseAbility;
import com.chaosbuffalo.mkultra.api.IPlayerData;
import com.chaosbuffalo.mkultra.fx.ParticleEffects;
import com.chaosbuffalo.mkultra.network.packets.server.ParticleEffectSpawnPacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class FlameBlade  extends BaseAbility {

    public static int BASE_DURATION = 10;
    public static int DURATION_SCALE = 5;
    public static float BASE_DAMAGE = 1.0f;
    public static float DAMAGE_SCALE = 1.0f;

    public FlameBlade() {
        super(MKUltra.MODID, "ability.flame_blade");
    }

    @Override
    public String getAbilityName() {
        return "Flame Blade";
    }

    @Override
    public String getAbilityDescription() {
        return "Buffs all surrounding players, giving them burning damage on hit";
    }

    @Override
    public ResourceLocation getAbilityIcon(){
        return new ResourceLocation(MKUltra.MODID, "textures/class/abilities/flameblade.png");
    }


    @Override
    public String getAbilityType() {
        return "Group Buff";
    }

    @Override
    public int getIconU() {
        return 216;
    }

    @Override
    public int getIconV() {
        return 36;
    }

    @Override
    public int getCooldown(int currentLevel) {
        return 18 + currentLevel * 5;
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
        return 2 + currentLevel * 4;
    }

    @Override
    public float getDistance(int currentLevel) {
        return 10.0f + 2.0f * currentLevel;
    }

    @Override
    public int getRequiredLevel(int currentLevel) {
        return currentLevel * 2;
    }

    @Override
    public void execute(EntityPlayer entity, IPlayerData pData, World theWorld) {
        pData.startAbility(this);

        int level = pData.getLevelForAbility(getAbilityId());

        // What to do for each target hit
        int duration = (BASE_DURATION + DURATION_SCALE * level) * GameConstants.TICKS_PER_SECOND;
        SpellCast effect = FlameBladePotion.Create(entity);
        SpellCast particlePotion = ParticlePotion.Create(entity,
                EnumParticleTypes.DRIP_LAVA.getParticleID(),
                ParticleEffects.DIRECTED_SPOUT, false, new Vec3d(1.0, 1.5, 1.0),
                new Vec3d(0.0, 1.0, 0.0), 40, 5, 1.0);

        AreaEffectBuilder.Create(entity, entity)
                .spellCast(effect, duration, level, getTargetType())
                .spellCast(particlePotion, level, getTargetType())
                .instant()
                .particle(EnumParticleTypes.FIREWORKS_SPARK)
                .color(16737330).radius(getDistance(level), true)
                .spawn();

        Vec3d lookVec = entity.getLookVec();
        MKUltra.packetHandler.sendToAllAround(
                new ParticleEffectSpawnPacket(
                        EnumParticleTypes.DRIP_LAVA.getParticleID(),
                        ParticleEffects.CIRCLE_MOTION, 50, 0,
                        entity.posX, entity.posY + 1.5,
                        entity.posZ, 1.0, 1.0, 1.0, 1.0f,
                        lookVec),
                entity, 50.0f);
    }
}
