package com.chaosbuffalo.mkultra.core;


import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.api.BaseAbility;
import com.chaosbuffalo.mkultra.api.BaseClass;
import com.google.common.collect.Lists;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber
public class ClassData {

    public static BaseClass getClass(ResourceLocation classId) {
        return REGISTRY_CLASSES.getValue(classId);
    }

    public static List<ResourceLocation> getClassesProvidedByItem(Item held) {
        return REGISTRY_CLASSES.getEntries().stream()
                .filter(kv -> kv.getValue().getUnlockItem() == held)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    public static List<ResourceLocation> getValidClasses(List<ResourceLocation> classes) {
        return REGISTRY_CLASSES.getKeys().stream()
                .filter(classes::contains)
                .collect(Collectors.toList());
    }

    public static String getClassName(ResourceLocation classId) {
        BaseClass cls = getClass(classId);
        return cls != null ? cls.getClassName() : "<NULL CLASS>";
    }

    public static BaseAbility getAbility(ResourceLocation abilityId) {
        return REGISTRY_ABILITIES.getValue(abilityId);
    }

    public static IForgeRegistry<BaseClass> REGISTRY_CLASSES = null;
    public static IForgeRegistry<BaseAbility> REGISTRY_ABILITIES = null;

    private static List<BaseClass> BUILTIN_CLASSES = Lists.newArrayList();

    @SuppressWarnings("unused")
    @SubscribeEvent
    public static void registerClasses(RegistryEvent.Register<BaseClass> event) {
        BUILTIN_CLASSES.forEach(c -> {
            c.setRegistryName(c.getClassId());
            event.getRegistry().register(c);
        });
    }

    @SuppressWarnings("unused")
    @SubscribeEvent
    public static void registerAbilities(RegistryEvent.Register<BaseAbility> event) {
        BUILTIN_CLASSES.forEach(bc -> bc.getAbilities().forEach(a -> {
            a.setRegistryName(a.getAbilityId());
            event.getRegistry().register(a);
        }));
    }

    @SuppressWarnings("unused")
    @SubscribeEvent
    public static void createRegistries(RegistryEvent.NewRegistry event) {
        REGISTRY_CLASSES = new RegistryBuilder<BaseClass>()
                .setName(new ResourceLocation(MKUltra.MODID, "classes"))
                .setType(BaseClass.class)
                .setIDRange(0, Integer.MAX_VALUE - 1)
                .create();

        REGISTRY_ABILITIES = new RegistryBuilder<BaseAbility>()
                .setName(new ResourceLocation(MKUltra.MODID, "abilities"))
                .setType(BaseAbility.class)
                .setIDRange(0, Integer.MAX_VALUE - 1)
                .create();
    }
}
