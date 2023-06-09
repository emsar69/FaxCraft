package com.emsar.faxcraft.gui.screen;

import com.emsar.faxcraft.ModMain;
import com.emsar.faxcraft.gui.menu.CompressorMenu;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class CompressorScreen extends AbstractContainerScreen<CompressorMenu> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(ModMain.MODID, "textures/gui/compressor.png");
    public CompressorScreen(CompressorMenu menu, Inventory inventory, Component component) {
        super(menu, inventory, component);
    }

    @Override
    protected void renderBg(PoseStack p_97787_, float p_97788_, int p_97789_, int p_97790_) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1,1,1,1);
        RenderSystem.setShaderTexture(0, TEXTURE);

        int x = (width-imageWidth)/2;
        int y = (height-imageHeight)/2;

        this.blit(p_97787_, x, y, 0, 0, imageWidth, imageHeight);

        System.out.println(menu.data.get(1));

        if(menu.isCrafting()){
            this.blit(p_97787_, x+84, y+32, 176, 45, menu.getScaled(), 17);
        }
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float delta) {
        renderBackground(poseStack);
        super.render(poseStack, mouseX, mouseY, delta);
        renderTooltip(poseStack, mouseX, mouseY);
    }
}
