package com.chaosbuffalo.mkultra.api;

import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.RangedAttribute;

public class PlayerAttributes {
    public static final IAttribute MAX_MANA = new RangedAttribute(null, "mkultra.maxMana", 0, 0, 1024)
            .setDescription("Max Mana")
            .setShouldWatch(true);

    public static final IAttribute MANA_REGEN = new RangedAttribute(null, "mkultra.manaRegen", 0, 0, 1024)
            .setDescription("Mana Regen")
            .setShouldWatch(true);

    public static final IAttribute MAGIC_ATTACK_DAMAGE = new RangedAttribute(null, "mkultra.magicAttackDamage", 0, 0, 2048)
            .setDescription("Magic Attack Damage")
            .setShouldWatch(true);

    public static final IAttribute MAGIC_ARMOR = new RangedAttribute(null, "mkultra.magicArmor", 0, 0, 64)
            .setDescription("Magic Armor")
            .setShouldWatch(true);

    public static final IAttribute COOLDOWN = new RangedAttribute(null, "mkultra.cooldown_rate", 1, 0.5, 1)
            .setDescription("Cooldown Rate")
            .setShouldWatch(true);

    /**
     * Operation 0: Increment X by Amount
     * The game first sets X = Base, then executes all Operation 0 modifiers, then sets Y = X, then executes all Operation 1 modifiers, and finally executes all Operation 2 modifiers.
     *
     * +- amount
     */
    public static final int OP_INCREMENT = 0;

    /**
     * Operation 1: Increment Y by X * Amount
     * The game first sets X = Base, then executes all Operation 0 modifiers, then sets Y = X, then executes all Operation 1 modifiers, and finally executes all Operation 2 modifiers.
     *
     * +- amount % (additive)
     */
    public static final int OP_SCALE_ADDITIVE = 1;

    /**
     * Operation 2: Y = Y * (1 + Amount) (equivalent to Increment Y by Y * Amount)
     * The game first sets X = Base, then executes all Operation 0 modifiers, then sets Y = X, then executes all Operation 1 modifiers, and finally executes all Operation 2 modifiers.
     *
     * +- amount % (multiplicative)
     */
    public static final int OP_SCALE_MULTIPLICATIVE = 2;
}
