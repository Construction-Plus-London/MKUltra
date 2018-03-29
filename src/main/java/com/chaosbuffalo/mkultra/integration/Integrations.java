package com.chaosbuffalo.mkultra.integration;

import com.chaosbuffalo.mkultra.api.Targeting;
import com.chaosbuffalo.mkultra.api.ArmorClass;
import com.mcmoddev.basemetals.data.MaterialNames;
import com.mcmoddev.basemetals.init.Materials;

public class Integrations {

    private static void setupLootableBodies() {
        Targeting.registerFriendlyEntity("cyano.lootable.entities.EntityLootableBody");
    }

    private static void setupMinecolonies() {
        Targeting.registerFriendlyEntity("com.minecolonies.coremod.entity.EntityCitizen");
    }

    private static void setupLycanites() {
        Targeting.registerFriendlyEntity("com.lycanitesmobs.elementalmobs.entity.EntityNymph");
    }

    private static void setupBasemetals() {
        ArmorClass.ROBES
                .register(Materials.getArmorMaterialFor(Materials.getMaterialByName(MaterialNames.STARSTEEL)));

        ArmorClass.LIGHT
                .register(Materials.getArmorMaterialFor(Materials.getMaterialByName(MaterialNames.AQUARIUM)))
                .register(Materials.getArmorMaterialFor(Materials.getMaterialByName(MaterialNames.TIN)))
                .register(Materials.getArmorMaterialFor(Materials.getMaterialByName(MaterialNames.PEWTER)))
                .register(Materials.getArmorMaterialFor(Materials.getMaterialByName(MaterialNames.BISMUTH)));

        ArmorClass.MEDIUM
                .register(Materials.getArmorMaterialFor(Materials.getMaterialByName(MaterialNames.TIN)))
                .register(Materials.getArmorMaterialFor(Materials.getMaterialByName(MaterialNames.COPPER)))
                .register(Materials.getArmorMaterialFor(Materials.getMaterialByName(MaterialNames.BRASS)))
                .register(Materials.getArmorMaterialFor(Materials.getMaterialByName(MaterialNames.SILVER)))
                .register(Materials.getArmorMaterialFor(Materials.getMaterialByName(MaterialNames.ELECTRUM)))
                .register(Materials.getArmorMaterialFor(Materials.getMaterialByName(MaterialNames.NICKEL)))
                .register(Materials.getArmorMaterialFor(Materials.getMaterialByName(MaterialNames.MITHRIL)))
                .register(Materials.getArmorMaterialFor(Materials.getMaterialByName(MaterialNames.QUARTZ)))
                .register(Materials.getArmorMaterialFor(Materials.getMaterialByName(MaterialNames.CUPRONICKEL)))
                .register(Materials.getArmorMaterialFor(Materials.getMaterialByName(MaterialNames.ANTIMONY)));

        ArmorClass.HEAVY
                .register(Materials.getArmorMaterialFor(Materials.getMaterialByName(MaterialNames.LEAD)))
                .register(Materials.getArmorMaterialFor(Materials.getMaterialByName(MaterialNames.STEEL)))
                .register(Materials.getArmorMaterialFor(Materials.getMaterialByName(MaterialNames.INVAR)))
                .register(Materials.getArmorMaterialFor(Materials.getMaterialByName(MaterialNames.COLDIRON)))
                .register(Materials.getArmorMaterialFor(Materials.getMaterialByName(MaterialNames.ADAMANTINE)))
                .register(Materials.getArmorMaterialFor(Materials.getMaterialByName(MaterialNames.OBSIDIAN)))
                .register(Materials.getArmorMaterialFor(Materials.getMaterialByName(MaterialNames.ZINC)))
                .register(Materials.getArmorMaterialFor(Materials.getMaterialByName(MaterialNames.EMERALD)))
                .register(Materials.getArmorMaterialFor(Materials.getMaterialByName(MaterialNames.PLATINUM)))
                .register(Materials.getArmorMaterialFor(Materials.getMaterialByName(MaterialNames.BRONZE)));
    }

    public static void setup() {
        setupBasemetals();
        setupLootableBodies();
        setupMinecolonies();
        setupLycanites();
    }
}
