package com.deinname.mod.ui;

import com.deinname.mod.modules.Category;
import com.deinname.mod.modules.Module;
import com.deinname.mod.modules.ModuleManager;
import com.deinname.mod.settings.BooleanSetting;
import com.deinname.mod.settings.Setting;
import com.deinname.mod.settings.SliderSetting;
import net.minecraft.client.gui.GuiScreen;

import java.io.IOException;

public class ClickGuiScreen extends GuiScreen {

    private int startX = 100;
    private int startY = 50;
    private int panelWidth = 110;
    private int panelHeight = 220;
    private int itemHeight = 20;

    private Category selectedCategory = Category.COMBAT;
    private Module selectedModule = null;
    private SliderSetting draggingSlider = null;

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();

        int catX = startX;
        drawPanel(catX, startY, panelWidth, panelHeight, "Custom Client");
        int catY = startY + 30;
        for (Category cat : Category.values()) {
            boolean hovered = isHovered(mouseX, mouseY, catX + 5, catY, panelWidth - 10, itemHeight);
            if (cat == selectedCategory || hovered) {
                drawRect(catX + 5, catY, catX + panelWidth - 5, catY + itemHeight, hovered ? 0xFF2A2A2A : 0xFF202020);
            }
            fontRendererObj.drawStringWithShadow(cat.getDisplayName(), catX + 15, catY + 6, cat == selectedCategory ? 0xFFFFFFFF : 0xFFAAAAAA);
            catY += itemHeight + 2;
        }

        int modX = startX + panelWidth + 5;
        drawPanel(modX, startY, panelWidth, panelHeight, selectedCategory.getDisplayName());
        int modY = startY + 30;
        for (Module mod : ModuleManager.modules) {
            if (mod.getCategory() == selectedCategory) {
                boolean hovered = isHovered(mouseX, mouseY, modX + 5, modY, panelWidth - 10, itemHeight);
                drawRect(modX + 5, modY, modX + panelWidth - 5, modY + itemHeight, hovered ? 0xFF2A2A2A : 0xFF1A1A1A);
                
                if (mod.isToggled()) {
                    drawRect(modX + 5, modY, modX + 7, modY + itemHeight, 0xFF00E5FF);
                }
                fontRendererObj.drawStringWithShadow(mod.getName(), modX + 15, modY + 6, mod.isToggled() ? 0xFFFFFFFF : 0xFF888888);
                
                if (mod == selectedModule) {
                    fontRendererObj.drawStringWithShadow(">", modX + panelWidth - 15, modY + 6, 0xFF00E5FF);
                }
                modY += itemHeight + 2;
            }
        }

        int setX = startX + (panelWidth * 2) + 10;
        if (selectedModule != null) {
            drawPanel(setX, startY, panelWidth + 40, panelHeight, selectedModule.getName());
            int setY = startY + 30;

            boolean toggleHovered = isHovered(mouseX, mouseY, setX + 5, setY, panelWidth + 30, itemHeight);
            drawRect(setX + 5, setY, setX + panelWidth + 35, setY + itemHeight, selectedModule.isToggled() ? 0xFF00E5FF : 0xFF333333);
            fontRendererObj.drawStringWithShadow("Enabled", setX + 15, setY + 6, selectedModule.isToggled() ? 0xFF000000 : 0xFFFFFFFF);
            setY += itemHeight + 5;

            for (Setting setting : selectedModule.getSettings()) {
                if (setting instanceof BooleanSetting) {
                    BooleanSetting boolSet = (BooleanSetting) setting;
                    boolean bHovered = isHovered(mouseX, mouseY, setX + 5, setY, panelWidth + 30, itemHeight);
                    drawRect(setX + 5, setY, setX + panelWidth + 35, setY + itemHeight, bHovered ? 0xFF2A2A2A : 0xFF1A1A1A);
                    fontRendererObj.drawStringWithShadow(boolSet.getName(), setX + 15, setY + 6, 0xFFCCCCCC);
                    
                    int toggleX = setX + panelWidth + 20;
                    drawRect(toggleX, setY + 4, toggleX + 12, setY + 16, boolSet.isEnabled() ? 0xFF00E5FF : 0xFF555555);
                    setY += itemHeight + 2;
                } else if (setting instanceof SliderSetting) {
                    SliderSetting sliderSet = (SliderSetting) setting;
                    fontRendererObj.drawStringWithShadow(sliderSet.getName(), setX + 15, setY + 2, 0xFFCCCCCC);
                    fontRendererObj.drawStringWithShadow(String.format("%.1f", sliderSet.getValue()), setX + panelWidth + 15, setY + 2, 0xFF00E5FF);
                    
                    int sliderY = setY + 15;
                    drawRect(setX + 10, sliderY, setX + panelWidth + 30, sliderY + 4, 0xFF333333);
                    double percent = (sliderSet.getValue() - sliderSet.getMin()) / (sliderSet.getMax() - sliderSet.getMin());
                    drawRect(setX + 10, sliderY, (int) (setX + 10 + (panelWidth + 20) * percent), sliderY + 4, 0xFF00E5FF);
                    setY += itemHeight + 10;
                }
            }
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private void drawPanel(int x, int y, int width, int height, String title) {
        drawRect(x, y, x + width, y + height, 0xDD151515);
        drawRect(x, y, x + width, y + 25, 0xFF101010);
        drawRect(x, y + 25, x + width, y + 26, 0xFF000000);
        fontRendererObj.drawStringWithShadow(title, x + 10, y + 8, 0xFF00E5FF);
    }

    private boolean isHovered(int mouseX, int mouseY, int x, int y, int width, int height) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        int catX = startX;
        int catY = startY + 30;
        for (Category cat : Category.values()) {
            if (isHovered(mouseX, mouseY, catX + 5, catY, panelWidth - 10, itemHeight)) {
                selectedCategory = cat;
                return;
            }
            catY += itemHeight + 2;
        }

        int modX = startX + panelWidth + 5;
        int modY = startY + 30;
        for (Module mod : ModuleManager.modules) {
            if (mod.getCategory() == selectedCategory) {
                if (isHovered(mouseX, mouseY, modX + 5, modY, panelWidth - 10, itemHeight)) {
                    if (mouseButton == 0) mod.toggle();
                    else if (mouseButton == 1) selectedModule = mod;
                    return;
                }
                modY += itemHeight + 2;
            }
        }

        int setX = startX + (panelWidth * 2) + 10;
        if (selectedModule != null) {
            int setY = startY + 30;
            if (isHovered(mouseX, mouseY, setX + 5, setY, panelWidth + 30, itemHeight)) {
                selectedModule.toggle();
                return;
            }
            setY += itemHeight + 5;

            for (Setting setting : selectedModule.getSettings()) {
                if (setting instanceof BooleanSetting) {
                    if (isHovered(mouseX, mouseY, setX + 5, setY, panelWidth + 30, itemHeight)) {
                        ((BooleanSetting) setting).toggle();
                        return;
                    }
                    setY += itemHeight + 2;
                } else if (setting instanceof SliderSetting) {
                    int sliderY = setY + 15;
                    if (isHovered(mouseX, mouseY, setX + 10, setY, panelWidth + 20, itemHeight + 5)) {
                        draggingSlider = (SliderSetting) setting;
                        updateSlider(mouseX);
                        return;
                    }
                    setY += itemHeight + 10;
                }
            }
        }
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        if (draggingSlider != null) updateSlider(mouseX);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        draggingSlider = null;
    }

    private void updateSlider(int mouseX) {
        int setX = startX + (panelWidth * 2) + 10;
        double percent = (double) (mouseX - (setX + 10)) / (panelWidth + 20);
        percent = Math.max(0, Math.min(1, percent));
        double newValue = draggingSlider.getMin() + (draggingSlider.getMax() - draggingSlider.getMin()) * percent;
        draggingSlider.setValue(newValue);
    }
}
