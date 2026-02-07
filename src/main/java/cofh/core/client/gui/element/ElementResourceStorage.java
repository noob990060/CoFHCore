package cofh.core.client.gui.element;

import cofh.core.client.gui.IGuiAccess;
import cofh.core.util.helpers.RenderHelper;
import cofh.lib.api.IResourceStorage;
import cofh.lib.util.helpers.MathHelper;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.function.Supplier;

import static cofh.core.CoFHCore.LOG;
import static cofh.lib.util.Constants.FALSE;
import static cofh.lib.util.Constants.TRUE;
import static cofh.lib.util.helpers.StringHelper.format;
import static net.minecraft.client.gui.screens.Screen.hasAltDown;
import static net.minecraft.client.gui.screens.Screen.hasShiftDown;

public abstract class ElementResourceStorage extends ElementBase {

    protected ResourceLocation creativeTexture;
    protected ResourceLocation underlayTexture;
    protected ResourceLocation overlayTexture;

    protected IResourceStorage storage;
    protected int minDisplay = 1;

    protected Supplier<Boolean> drawStorage = TRUE;
    protected Supplier<Boolean> drawUnderlay = TRUE;
    protected Supplier<Boolean> drawOverlay = TRUE;

    protected Supplier<Boolean> claimStorage = FALSE;
    protected Supplier<Boolean> clearStorage = FALSE;
    protected Supplier<Boolean> claimable = FALSE;
    protected Supplier<Boolean> clearable;

    public ElementResourceStorage(IGuiAccess gui, int posX, int posY, IResourceStorage storage) {

        super(gui, posX, posY);
        this.storage = storage;

        this.clearable = () -> storage.getStored() > 0;
    }

    public final ElementResourceStorage setCreativeTexture(String texture) {

        if (texture == null) {
            LOG.warn("Attempted to assign a NULL creative texture.");
            return this;
        }
        this.creativeTexture = ResourceLocation.parse(texture);
        return this;
    }   

    public final ElementResourceStorage setUnderlayTexture(String texture) {

        return setUnderlayTexture(texture, TRUE);
    }

    public final ElementResourceStorage setUnderlayTexture(String texture, Supplier<Boolean> draw) {

        if (texture == null || draw == null) {
            LOG.warn("Attempted to assign a NULL underlay texture.");
            return this;
        }
        if (!texture.contains(":")) {
            texture = "cofh_core:" + texture;
        }
        this.underlayTexture = ResourceLocation.parse(texture);
        this.drawUnderlay = draw;
        return this;
    }

    public final ElementResourceStorage setOverlayTexture(String texture) {

        return setOverlayTexture(texture, TRUE);
    }

    public final ElementResourceStorage setOverlayTexture(String texture, Supplier<Boolean> draw) {

        if (texture == null || draw == null) {
            LOG.warn("Attempted to assign a NULL overlay texture.");
            return this;
        }
        if (!texture.contains(":")) {
            texture = "cofh_core:" + texture;
        }
        this.overlayTexture = ResourceLocation.parse(texture);
        this.drawOverlay = draw;
        return this;
    }

    public final ElementResourceStorage setClaimStorage(Supplier<Boolean> claimStorage) {

        this.claimStorage = claimStorage;
        return this;
    }

    public final ElementResourceStorage setClearStorage(Supplier<Boolean> clearStorage) {

        this.clearStorage = clearStorage;
        return this;
    }

    public ElementResourceStorage setMinDisplay(int minDisplay) {

        this.minDisplay = minDisplay;
        return this;
    }

    @Override
    public void drawBackground(GuiGraphics pGuiGraphics, int mouseX, int mouseY) {

        PoseStack poseStack = pGuiGraphics.pose();
        drawStorage(poseStack);
        drawUnderlayTexture(poseStack);
        drawResource(poseStack);
    }

    @Override
    public void drawForeground(GuiGraphics pGuiGraphics, int mouseX, int mouseY) {

        drawOverlayTexture(pGuiGraphics.pose());
    }

    @Override
    public void addTooltip(List<Component> tooltipList, int mouseX, int mouseY) {

        if (storage.isCreative()) {
            tooltipList.add(Component.translatable("info.cofh.infinite").withStyle(ChatFormatting.LIGHT_PURPLE).withStyle(ChatFormatting.ITALIC));
        } else {
            tooltipList.add(Component.literal(format(storage.getStored()) + " / " + format(storage.getCapacity()) + " " + storage.getUnit()));
        }
        if (clearable.get() && clearStorage != FALSE && (hasAltDown() || hasShiftDown())) {
            tooltipList.add(Component.translatable("info.cofh.click_to_clear").withStyle(ChatFormatting.GRAY));
        } else if (claimable.get()) {
            tooltipList.add(Component.translatable("info.cofh.click_to_claim").withStyle(ChatFormatting.GRAY));
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {

        if (clearable.get() && hasShiftDown() && hasAltDown()) {
            return clearStorage.get();
        }
        if (claimable.get()) {
            return claimStorage.get();
        }
        return false;
    }

    protected int getScaled(int scale) {

        if (storage.isCreative()) {
            return scale;
        }
        double fraction = (double) storage.getStored() * scale / storage.getCapacity();
        int amount = MathHelper.clamp(MathHelper.round(fraction), 0, scale);
        return fraction > 0 ? Math.max(minDisplay, amount) : amount;
    }

    protected void drawStorage(PoseStack poseStack) {

        // DEBUG: Add element type and position identification for storage drawing
        String elementType = this.getClass().getSimpleName();
        System.out.println("DEBUG: " + elementType + " drawStorage called at pos(" + posX() + "," + posY() + "). Texture: " + texture + ", DrawStorage: " + drawStorage.get());
        
        if (drawStorage.get() && texture != null) {
            System.out.println("DEBUG: Drawing storage texture: " + texture);
            RenderHelper.setPosTexShader();
            RenderHelper.setShaderTexture0(texture);
            drawTexturedModalRect(poseStack, posX(), posY(), 0, 0, width, height);
        } else {
            System.out.println("DEBUG: NOT drawing storage for " + elementType + ". DrawStorage: " + drawStorage.get() + ", Texture: " + texture);
        }
    }

    protected void drawUnderlayTexture(PoseStack poseStack) {

        // DEBUG: Add element type and position identification for underlay drawing
        String elementType = this.getClass().getSimpleName();
        System.out.println("DEBUG: " + elementType + " drawUnderlay called at pos(" + posX() + "," + posY() + "). UnderlayTexture: " + underlayTexture + ", DrawUnderlay: " + drawUnderlay.get());
        
        if (drawUnderlay.get() && underlayTexture != null) {
            System.out.println("DEBUG: Drawing underlay texture: " + underlayTexture);
            RenderHelper.setPosTexShader();
            RenderHelper.setShaderTexture0(underlayTexture);
            drawTexturedModalRect(poseStack, posX(), posY(), 0, 0, width, height);
        } else {
            System.out.println("DEBUG: NOT drawing underlay for " + elementType + ". DrawUnderlay: " + drawUnderlay.get() + ", UnderlayTexture: " + underlayTexture);
        }
    }

    protected abstract void drawResource(PoseStack poseStack);

    protected void drawOverlayTexture(PoseStack poseStack) {

        // DEBUG: Add element type and position identification
        String elementType = this.getClass().getSimpleName();
        System.out.println("DEBUG: " + elementType + " drawOverlay called at pos(" + posX() + "," + posY() + "). DrawOverlay: " + drawOverlay.get() + ", OverlayTexture: " + overlayTexture);
        
        if (drawOverlay.get() && overlayTexture != null) {
            System.out.println("DEBUG: Drawing overlay texture: " + overlayTexture);
            RenderHelper.setPosTexShader();
            RenderHelper.setShaderTexture0(overlayTexture);
            drawTexturedModalRect(poseStack, posX(), posY(), 0, 0, width, height);
        } else {
            System.out.println("DEBUG: NOT drawing overlay for " + elementType + ". DrawOverlay: " + drawOverlay.get() + ", OverlayTexture: " + overlayTexture);
        }
    }

}
