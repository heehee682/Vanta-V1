package com.deinname.mod.ui;

import com.deinname.mod.modules.Category;
import com.deinname.mod.modules.Module;
import com.deinname.mod.modules.ModuleManager;
import com.deinname.mod.settings.BooleanSetting;
import com.deinname.mod.settings.Setting;
import com.deinname.mod.settings.SliderSetting;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Mouse;

import java.io.IOException;

public class ClickGuiScreen extends GuiScreen {

    private int startX = 100;
    private int startY = 50;
    private int panelWidth = 120;
    private int panelHeight = 250;
    private int itemHeight = 22;

    private Category selectedCategory = Category.COMBAT;
    private Module selectedModule = null;
    private SliderSetting draggingSlider = null;

    // Für das Verschieben des GUIs
    private boolean dragging = false;
    private int dragX, dragY;

    // Farben (Modernes dunkles Design)
    private int bgColor = 0xE6121212;
    private int panelHeader = 0xFF1A1A1A;
    private int itemColor = 0xFF1E1E1E;
    private int hoverColor = 0xFF2A2A2A;
    private int accentColor = 0xFF6C5CE7; // Lila/Blau wie bei Prestige

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();

        // GUI verschieben
        if (dragging) {
            startX = mouseX - dragX;
            startY = mouseY - dragY;
        }

        // 1. Kategorien Panel (Ganz Links)
        int catX = startX;
        drawPanel(catX, startY, panelWidth, panelHeight, "Custom Client");
        int catY = startY + 35;
        for (Category cat : Category.values()) {
            boolean hovered = isHovered(mouseX, mouseY, catX + 5, catY, panelWidth - 10, itemHeight);
            drawRect(catX + 5, catY, catX + panelWidth - 5, catY + itemHeight, cat == selectedCategory ? accentColor : (hovered ? hoverColor : itemColor));
            fontRendererObj.drawStringWithShadow(cat.getDisplayName(), catX + 15, catY + 7, cat == selectedCategory ? 0xFFFFFFFF : 0xFFAAAAAA);
            catY += itemHeight + 2;
        }

        // 2. Module Panel (Mitte)
        int modX = startX + panelWidth + 5;
        drawPanel(modX, startY, panelWidth, panelHeight, selectedCategory.getDisplayName());
        int modY = startY + 35;
        for (Module mod : ModuleManager.modules) {
            if (mod.getCategory() == selectedCategory) {
                boolean hovered = isHovered(mouseX, mouseY, modX + 5, modY, panelWidth - 10, itemHeight);
                drawRect(modX + 5, modY, modX + panelWidth - 5, modY + itemHeight, hovered ? hoverColor : itemColor);
                
                if (mod.isToggled()) {
                    drawRect(modX + 5, modY, modX + 7, modY + itemHeight, accentColor);
                }
                fontRendererObj.drawStringWithShadow(mod.getName(), modX + 15, modY + 7, mod.isToggled() ? 0xFFFFFFFF : 0xFF888888);
                
                if (mod == selectedModule) {
                    fontRendererObj.drawStringWithShadow(">", modX + panelWidth - 15, modY + 7, accentColor);
                }
                modY += itemHeight + 2;
            }
        }

        // 3. Settings Panel (Rechts)
        int setX = startX + (panelWidth * 2) + 10;
        if (selectedModule != null) {
            drawPanel(setX, startY, panelWidth + 50, panelHeight, selectedModule.getName());
            int setY = startY + 35;

            boolean toggleHovered = isHovered(mouseX, mouseY, setX + 5, setY, panelWidth + 40, itemHeight);
            drawRect(setX + 5, setY, setX + panelWidth + 45, setY + itemHeight, selectedModule.isToggled() ? accentColor : (toggleHovered ? hoverColor : itemColor));
            fontRendererObj.drawStringWithShadow("Enabled", setX + 15, setY + 7, selectedModule.isToggled() ? 0xFFFFFFFF : 0xFFCCCCCC);
            setY += itemHeight + 5;

            for (Setting setting : selectedModule.getSettings()) {
                if (setting instanceof BooleanSetting) {
                    BooleanSetting boolSet = (BooleanSetting) setting;
                    boolean bHovered = isHovered(mouseX, mouseY, setX + 5, setY, panelWidth + 40, itemHeight);
                    drawRect(setX + 5, setY, setX + panelWidth + 45, setY + itemHeight, bHovered ? hoverColor : itemColor);
                    fontRendererObj.drawStringWithShadow(boolSet.getName(), setX + 15, setY + 7, 0xFFCCCCCC);
                    
                    int toggleX = setX + panelWidth + 25;
                    drawRect(toggleX, setY + 5, toggleX + 15, setY + 17, boolSet.isEnabled() ? accentColor : 0xFF444444);
                    setY += itemHeight + 2;
                } else if (setting instanceof SliderSetting) {
                    SliderSetting sliderSet = (SliderSetting) setting;
                    fontRendererObj.drawStringWithShadow(sliderSet.getName(), setX + 15, setY + 2, 0xFFCCCCCC);
                    fontRendererObj.drawStringWithShadow(String.format("%.1f", sliderSet.getValue()), setX + panelWidth + 25, setY + 2, accentColor);
                    
                    int sliderY = setY + 18;
                    drawRect(setX + 10, sliderY, setX + panelWidth + 40, sliderY + 4, 0xFF333333);
                    double percent = (sliderSet.getValue() - sliderSet.getMin()) / (sliderSet.getMax() - sliderSet.getMin());
                    drawRect(setX + 10, sliderY, (int) (setX + 10 + (panelWidth + 30) * percent), sliderY + 4, accentColor);
                    setY += itemHeight + 12;
                }
            }
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private void drawPanel(int x, int y, int width, int height, String title) {
        drawRect(x, y, x + width, y + height, bgColor);
        drawRect(x, y, x + width, y + 30, panelHeader);
        fontRendererObj.drawStringWithShadow(title, x + 10, y + 11, accentColor);
    }

    private boolean isHovered(int mouseX, int mouseY, int x, int y, int width, int height) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        // Titelleiste zum Verschieben abfragen (Alle 3 Panels oben)
        if (mouseButton == 0 && mouseY >= startY && mouseY <= startY + 30) {
            if (mouseX >= startX && mouseX <= startX + (panelWidth * 2) + 10 + (selectedModule != null ? panelWidth + 50 : 0)) {
                dragging = true;
                dragX = mouseX - startX;
                dragY = mouseY - startY;
                return;
            }
        }

        // Kategorien Klick
        int catX = startX;
        int catY = startY + 35;
        for (Category cat : Category.values()) {
            if (isHovered(mouseX, mouseY, catX + 5, catY, panelWidth - 10, itemHeight)) {
                selectedCategory = cat;
                return;
            }
            catY += itemHeight + 2;
        }

        // Modul Klick
        int modX = startX + panelWidth + 5;
        int modY = startY + 35;
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

        // Settings Klick
        int setX = startX + (panelWidth * 2) + 10;
        if (selectedModule != null) {
            int setY = startY + 35;
            if (isHovered(mouseX, mouseY, setX + 5, setY, panelWidth + 40, itemHeight)) {
                selectedModule.toggle();
                return;
            }
            setY += itemHeight + 5;

            for (Setting setting : selectedModule.getSettings()) {
                if (setting instanceof BooleanSetting) {
                    if (isHovered(mouseX, mouseY, setX + 5, setY, panelWidth + 40, itemHeight)) {
                        ((BooleanSetting) setting).toggle();
                        return;
                    }
                    setY += itemHeight + 2;
                } else if (setting instanceof SliderSetting) {
                    if (isHovered(mouseX, mouseY, setX + 10, setY, panelWidth + 30, itemHeight + 10)) {
                        draggingSlider = (SliderSetting) setting;
                        updateSlider(mouseX);
                        return;
                    }
                    setY += itemHeight + 12;
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
        dragging = false;
        draggingSlider = null;
    }

    private void updateSlider(int mouseX) {
        int setX = startX + (panelWidth * 2) + 10;
        double percent = (double) (mouseX - (setX + 10)) / (panelWidth + 30);
        percent = Math.max(0, Math.min(1, percent));
        double newValue = draggingSlider.getMin() + (draggingSlider.getMax() - draggingSlider.getMin()) * percent;
        draggingSlider.setValue(newValue);
    }
}
