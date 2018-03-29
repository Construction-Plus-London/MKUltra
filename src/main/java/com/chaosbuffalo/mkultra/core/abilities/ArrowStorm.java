package com.chaosbuffalo.mkultra.core.abilities;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.api.Targeting;
import com.chaosbuffalo.mkultra.api.BaseAbility;
import com.chaosbuffalo.mkultra.api.IPlayerData;
import com.chaosbuffalo.mkultra.fx.ParticleEffects;
import com.chaosbuffalo.mkultra.item.ItemHelper;
import com.chaosbuffalo.mkultra.network.packets.server.ParticleEffectSpawnPacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ArrowStorm extends BaseAbility {

    public static float BASE_DAMAGE = 8.0f;
    public static float DAMAGE_SCALE = -2.0f;
    public static int ARROW_PER_LEVEL = 5;

    public ArrowStorm() {
        super(MKUltra.MODID, "ability.arrow_storm");
    }

    @Override
    public ResourceLocation getAbilityIcon(){
        return new ResourceLocation(MKUltra.MODID, "textures/class/abilities/arrowstorm.png");
    }

    @Override
    public String getAbilityName() {
        return "Arrow Storm";
    }

    @Override
    public String getAbilityDescription() {
        return "Launches many arrows at once.";
    }

    @Override
    public String getAbilityType() {
        return "Projectile";
    }

    @Override
    public int getIconU() {
        return 126;
    }

    @Override
    public int getIconV() {
        return 18;
    }

    @Override
    public int getCooldown(int currentLevel) {
        return 40 - 10 * currentLevel;
    }

    @Override
    public int getType() {
        return ACTIVE_ABILITY;
    }

    @Override
    public Targeting.TargetType getTargetType() {
        return Targeting.TargetType.ENEMY;
    }

    @Override
    public int getManaCost(int currentLevel) {
        return 14 - currentLevel * 3;
    }


    @Override
    public int getRequiredLevel(int currentLevel) {
        return 6 + currentLevel * 2;
    }

    @Override
    public void execute(EntityPlayer entity, IPlayerData pData, World theWorld) {

        ItemStack ammo = ItemHelper.findAmmo(entity);
        if (ammo.isEmpty() || !(entity.getHeldItemMainhand().getItem() instanceof ItemBow)) {
            return;
        }
        int level = pData.getLevelForAbility(getAbilityId());
        int shootCount = ARROW_PER_LEVEL * level;
        pData.startAbility(this);
        for (int i = 0; i < shootCount; i++) {
            ItemArrow itemarrow = (ItemArrow) (ammo.getItem() instanceof ItemArrow ? ammo.getItem() : Items.ARROW);
            EntityArrow entityarrow = itemarrow.createArrow(theWorld, ammo, entity);
            entityarrow.shoot(entity, entity.rotationPitch, entity.rotationYaw, 0.0F, 4.0F, 10.0F);
            entityarrow.setDamage(BASE_DAMAGE + level * DAMAGE_SCALE);
            entityarrow.pickupStatus = EntityArrow.PickupStatus.DISALLOWED;
            theWorld.spawnEntity(entityarrow);
        }

        ammo.shrink(1);
        if (ammo.getCount() == 0) {
            entity.inventory.deleteStack(ammo);
        }

        Vec3d lookVec = entity.getLookVec();
        MKUltra.packetHandler.sendToAllAround(
                new ParticleEffectSpawnPacket(
                        EnumParticleTypes.CRIT.getParticleID(),
                        ParticleEffects.DIRECTED_SPOUT, 40, 10,
                        entity.posX, entity.posY + 1.0,
                        entity.posZ, 1.0, 1.0, 1.0, 1.5,
                        lookVec),
                entity.dimension, entity.posX,
                entity.posY, entity.posZ, 50.0f);

    }
}
