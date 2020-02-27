package com.chaosbuffalo.mkultra.client.gui;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.*;
import com.chaosbuffalo.mkultra.event.ClientKeyHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(Side.CLIENT)
public class AbilityBar extends Gui {

    private static final ResourceLocation barTexture = new ResourceLocation(MKUltra.MODID,
            "textures/gui/abilitybar.png");
    private static final int SLOT_WIDTH = 19;
    private static final int SLOT_HEIGHT = 20;
    private static final int MANA_START_U = 21;
    private static final int MANA_START_V = 0;
    private static final int MANA_CELL_WIDTH = 3;
    private static final int MANA_CELL_HEIGHT = 8;

    private static final ResourceLocation COOLDOWN_ICON = new ResourceLocation(MKUltra.MODID,
            "textures/class/abilities/cooldown.png");

    public static final int ABILITY_ICON_SIZE = 16;

    private static final int MIN_BAR_START_Y = 80;

    private Minecraft mc;

    public AbilityBar(Minecraft mc) {
        super();

        // We need this to invoke the render engine.
        this.mc = mc;
    }

    private int getBarStartY(int slotCount) {
        ScaledResolution scaledresolution = new ScaledResolution(this.mc);
        int height = scaledresolution.getScaledHeight();
        int barStart = height / 2 - (slotCount * SLOT_HEIGHT) / 2;
        return Math.max(barStart, MIN_BAR_START_Y);
    }

    private void drawMana(IPlayerData data) {
        ScaledResolution scaledresolution = new ScaledResolution(this.mc);
        int height = scaledresolution.getScaledHeight();

        this.mc.renderEngine.bindTexture(barTexture);
        GlStateManager.disableLighting();

        final int maxManaPerRow = 20;
        final int manaCellWidth = 4;
        final int manaCellRowSize = 9;

        int manaStartY = height - 24 - 10;
        int manaStartX = 24;

        for (int i = 0; i < data.getMana(); i++) {
            int manaX = manaCellWidth * (i % maxManaPerRow);
            int manaY = (i / maxManaPerRow) * manaCellRowSize;
            this.drawTexturedModalRect(manaStartX + manaX,
                    manaStartY + manaY,
                    MANA_START_U, MANA_START_V,
                    MANA_CELL_WIDTH, MANA_CELL_HEIGHT);
        }

    }

    private void drawCastBar(IPlayerData data) {
        if (!data.isCasting()) {
            return;
        }
        PlayerAbilityInfo info = data.getAbilityInfo(data.getCastingAbility());
        if (info == null || !info.isCurrentlyKnown()) {
            return;
        }
        PlayerAbility ability = info.getAbility();
        ScaledResolution scaledresolution = new ScaledResolution(this.mc);
        int height = scaledresolution.getScaledHeight();
        int castStartY = height / 2 + 8;
        int width = 50;
        int barSize = width * data.getCastTicks() / ability.getCastTime(info.getRank());
        int castStartX = scaledresolution.getScaledWidth() / 2 - barSize / 2;
        GlStateManager.pushMatrix();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        this.drawTexturedModalRect(castStartX, castStartY, 26, 21, barSize, 3);
        GlStateManager.popMatrix();
    }

    private void drawBarSlots(int slotCount) {
        this.mc.renderEngine.bindTexture(barTexture);
        GlStateManager.disableLighting();

        int xOffset = 0;
        int yOffset = getBarStartY(slotCount);
        for (int i = 0; i < slotCount; i++) {
            this.drawTexturedModalRect(xOffset, yOffset + i * SLOT_HEIGHT, 0, 0, SLOT_WIDTH, SLOT_HEIGHT);
        }
    }

    private void drawAbilities(IPlayerData data, int slotCount, float partialTicks) {
        GlStateManager.disableLighting();

        final int slotAbilityOffsetX = 1;
        final int slotAbilityOffsetY = 2;

        int barStartY = getBarStartY(slotCount);

        float globalCooldown = ClientKeyHandler.getGlobalCooldown();

        for (int i = 0; i < slotCount; i++) {
            ResourceLocation abilityId = data.getAbilityInSlot(i);
            if (abilityId.equals(MKURegistry.INVALID_ABILITY))
                continue;

            PlayerAbilityInfo info = data.getAbilityInfo(abilityId);
            if (info == null || !info.isCurrentlyKnown())
                continue;

            PlayerAbility ability = info.getAbility();
            if (ability == null)
                continue;

            float manaCost = data.getAbilityManaCost(abilityId);
            if (!data.isCasting() && data.getMana() >= manaCost) {
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            } else {
                GlStateManager.color(0.5f, 0.5f, 0.5f, 1.0F);
            }

            int slotX = slotAbilityOffsetX;
            int slotY = barStartY + slotAbilityOffsetY + (i * SLOT_HEIGHT);

            mc.getTextureManager().bindTexture(ability.getAbilityIcon());
            Gui.drawModalRectWithCustomSizedTexture(slotX, slotY, 0, 0,
                    ABILITY_ICON_SIZE, ABILITY_ICON_SIZE, ABILITY_ICON_SIZE, ABILITY_ICON_SIZE);

            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            float cooldownFactor = data.getCooldownPercent(info, partialTicks);
            if (globalCooldown > 0.0f && cooldownFactor == 0) {
                cooldownFactor = globalCooldown / ClientKeyHandler.getTotalGlobalCooldown();
            }

            // TODO: introduce min cooldown time so there is always a visual indicator that it's on cooldown

            if (cooldownFactor > 0) {
                int coolDownHeight = (int) (cooldownFactor * ABILITY_ICON_SIZE);
                if (coolDownHeight < 1) {
                    coolDownHeight = 1;
                }
                mc.getTextureManager().bindTexture(COOLDOWN_ICON);
                Gui.drawModalRectWithCustomSizedTexture(slotX, slotY, 0, 0,
                        ABILITY_ICON_SIZE, coolDownHeight, ABILITY_ICON_SIZE, coolDownHeight);
            }

            ability.drawAbilityBarEffect(mc, slotX, slotY);
        }
    }

    @SuppressWarnings("unused")
    @SubscribeEvent
    public void onRenderExperienceBar(RenderGameOverlayEvent event) {
        if (event.isCancelable() || event.getType() != RenderGameOverlayEvent.ElementType.EXPERIENCE) {
            return;
        }

        IPlayerData data = MKUPlayerData.get(mc.player);
        if (data == null || !data.hasChosenClass())
            return;

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        int slotCount = data.getActionBarSize();
        drawMana(data);
        drawCastBar(data);
        drawBarSlots(slotCount);
        drawAbilities(data, slotCount, event.getPartialTicks());
    }
}
