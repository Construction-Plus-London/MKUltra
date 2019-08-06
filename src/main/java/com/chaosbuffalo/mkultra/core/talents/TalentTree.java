package com.chaosbuffalo.mkultra.core.talents;

import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class TalentTree extends IForgeRegistryEntry.Impl<TalentTree>{

    private HashMap<String, ArrayList<TalentNode>> talentLines;
    private int version;

    public TalentTree(ResourceLocation name, int version){
        setRegistryName(name);
        talentLines = new HashMap<>();
        this.version = version;
    }

    public void addLine(String name, TalentNode... nodes){
        ArrayList<TalentNode> nodesList = new ArrayList<>();
        nodesList.addAll(Arrays.asList(nodes));
        talentLines.put(name, nodesList);
    }

    public HashMap<String, ArrayList<TalentNode>> getLines(){
        return talentLines;
    }

    public int getVersion(){
        return version;
    }

    @SideOnly(Side.CLIENT)
    public String getName() {
        return I18n.format(String.format("%s.%s.name",
                getRegistryName().getNamespace(), getRegistryName().getPath()));
    }
}