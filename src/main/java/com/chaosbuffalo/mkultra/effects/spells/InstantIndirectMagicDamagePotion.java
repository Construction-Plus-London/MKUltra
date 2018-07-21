package com.chaosbuffalo.mkultra.effects.spells;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.BaseAbility;
import com.chaosbuffalo.mkultra.core.MKDamageSource;
import com.chaosbuffalo.mkultra.effects.SpellCast;
import com.chaosbuffalo.mkultra.effects.SpellPotionBase;
import com.chaosbuffalo.targeting_api.Targeting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = MKUltra.MODID)
public class InstantIndirectMagicDamagePotion extends SpellPotionBase {

    public static final InstantIndirectMagicDamagePotion INSTANCE = new InstantIndirectMagicDamagePotion();

    @SubscribeEvent
    public static void register(RegistryEvent.Register<Potion> event) {
        event.getRegistry().register(INSTANCE.finish());
    }

    public static SpellCast Create(BaseAbility ability, Entity source, float baseDamage, float scaling) {
        return INSTANCE.newSpellCast(source, ability).setScalingParameters(baseDamage, scaling);
    }

    private InstantIndirectMagicDamagePotion() {
        super(true, 123);
        setPotionName("effect.instant_indirect_magic_damage");
    }

    @Override
    public Targeting.TargetType getTargetType() {
        return Targeting.TargetType.ENEMY;
    }

    @Override
    public void doEffect(Entity applier, Entity caster, EntityLivingBase target, int amplifier, SpellCast cast) {
        float damage = cast.getScaledValue(amplifier);
        target.attackEntityFrom(MKDamageSource.fromMagicSpell(cast, applier, caster), damage);
    }
}
