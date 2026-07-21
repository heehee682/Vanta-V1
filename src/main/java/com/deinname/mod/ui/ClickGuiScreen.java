package com.deinname.mod.ui;

import com.deinname.mod.modules.Category;
import com.deinname.mod.modules.Module;
import com.deinname.mod.modules.ModuleManager;
import com.deinname.mod.settings.BooleanSetting;
import com.deinname.mod.settings.Setting;
import com.deinname.mod.settings.SliderSetting;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class ClickGuiScreen extends Screen {

    private int startX = 100;
    private int startY = 50;
    private int panelWidth = 120;
    private int panelHeight = 250;
    private int itemHeight = 22;

    private Category selectedCategory = Category.COMBAT;
    private Module selectedModule = null;
    private SliderSetting draggingSlider = null;

    private boolean dragging = false;
    private int dragX, dragY;

    private int bgColor = 0xE6121212;
    private int panelHeader = 0xFF1A1A1A;
    private int itemColor = 0xFF1E1E1E;
    private int hoverColor = 0xFF2A2A2A;
    private int accentColor = 0xFF6C5CE7;

    public ClickGuiScreen() {
        super(Text.literal("Custom GUI"));
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        context.fill(0, 0, width, height, 0x90000000);

        if (dragging) {
            startX = mouseX - dragX;
            startY = mouseY - dragY;
        }

        int catX = startX;
        drawPanel(context, catX, startY, panelWidth, panelHeight, "Custom Client");
        int catY = startY + 35;
        for (Category cat : Category.values()) {
            boolean hovered = isHovered(mouseX, mouseY, catX + 5, catY, panelWidth - 10, itemHeight);
            context.fill(catX + 5, catY, catX + panelWidth - 5, catY + itemHeight, cat == selectedCategory ? accentColor : (hovered ? hoverColor : itemColor));
            context.drawTextWithShadow(textRenderer, cat.getDisplayName(), catX + 15, catY + 7, cat == selectedCategory ? 0xFFFFFFFF : 0xFFAAAAAA);
            catY += itemHeight + 2;
        }

        int modX = startX + panelWidth + 5;
        drawPanel(context, modX, startY, panelWidth, panelHeight, selectedCategory.getDisplayName());
        int modY = startY + 35;
        for (Module mod : ModuleManager.modules) {
            if (mod.getCategory() == selectedCategory) {
                boolean hovered = isHovered(mouseX, mouseY, modX + 5, modY, panelWidth - 10, itemHeight);
                context.fill(modX + 5, modY, modX + panelWidth - 5, modY + itemHeight, hovered ? hoverColor : itemColor);
                
                if (mod.isToggled()) {
                    context.fill(modX + 5, modY, modX + 7, modY + itemHeight, accentColor);
                }
                context.drawTextWithShadow(textRenderer, mod.getName(), modX + 15, modY + 7, mod.isToggled() ? 0xFFFFFFFF : 0xFF888888);
                
                if (mod == selectedModule) {
                    context.drawTextWithShadow(textRenderer, ">", modX + panelWidth - 15, modY + 7, accentColor);
                }
                modY += itemHeight + 2;
            }
        }

        int setX = startX + (panelWidth * 2) + 10;
        if (selectedModule != null) {
            drawPanel(context, setX, startY, panelWidth + 50, panelHeight, selectedModule.getName());
            int setY = startY + 35;

            boolean toggleHovered = isHovered(mouseX, mouseY, setX + 5, setY, panelWidth + 40, itemHeight);
            context.fill(setX + 5, setY, setX + panelWidth + 45, setY + itemHeight, selectedModule.isToggled() ? accentColor : (toggleHovered ? hoverColor : itemColor));
            context.drawTextWithShadow(textRenderer, "Enabled", setX + 15, setY + 7, selectedModule.isToggled() ? 0xFFFFFFFF : 0xFFCCCCCC);
            setY += itemHeight + 5;

            for (Setting setting : selectedModule.getSettings()) {
                if (setting instanceof BooleanSetting boolSet) {
                    boolean bHovered = isHovered(mouseX, mouseY, setX + 5, setY, panelWidth + 40, itemHeight);
                    context.fill(setX + 5, setY, setX + panelWidth + 45, setY + itemHeight, bHovered ? hoverColor : itemColor);
                    context.drawTextWithShadow(textRenderer, boolSet.getName(), setX + 15, setY + 7, 0xFFCCCCCC);
                    
                    int toggleX = setX + panelWidth + 25;
                    context.fill(toggleX, setY + 5, toggleX + 15, setY + 17, boolSet.isEnabled() ? accentColor : 0xFF444444);
                    setY += itemHeight + 2;
                } else if (setting instanceof SliderSetting sliderSet) {
                    context.drawTextWithShadow(textRenderer, sliderSet.getName(), setX + 15, setY + 2, 0xFFCCCCCC);
                    context.drawTextWithShadow(textRenderer, String.format("%.1f", sliderSet.getValue()), setX + panelWidth + 25, setY + 2, accentColor);
                    
                    int sliderY = setY + 18;
                    context.fill(setX + 10, sliderY, setX + panelWidth + 40, sliderY + 4, 0xFF333333);
                    double percent = (sliderSet.getValue() - sliderSet.getMin()) / (sliderSet.getMax() - sliderSet.getMin());
                    context.fill(setX + 10, sliderY, (int) (setX + 10 + (panelWidth + 30) * percent), sliderY + 4, accentColor);
                    setY += itemHeight + 12;
                }
            }
        }
        super.render(context, mouseX, mouseY, delta);
    }

    private void drawPanel(DrawContext context, int x, int y, int width, int height, String title) {
        context.fill(x, y, x + width, y + height, bgColor);
        context.fill(x, y, x + width, y + 30, panelHeader);
        context.drawTextWithShadow(textRenderer, title, x + 10, y + 11, accentColor);
    }

    private boolean isHovered(int mouseX, int mouseY, int x, int y, int width, int height) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0 && mouseY >= startY && mouseY <= startY + 30) {
            if (mouseX >= startX && mouseX <= startX + (panelWidth * 2) + 10 + (selectedModule != null ? panelWidth + 50 : 0)) {
                dragging = true;
                dragX = (int) mouseX - startX;
                dragY = (int) mouseY - startY;
                return super.mouseClicked(mouseX, mouseY, button);
            }
        }

        int catX = startX;
        int catY = startY + 35;
        for (Category cat : Category.values()) {
            if (isHovered((int)mouseX, (int)mouseY, catX + 5, catY, panelWidth - 10, itemHeight)) { selectedCategory = cat; return super.mouseClicked(mouseX, mouseY, button); }
            catY += itemHeight + 2;
        }

        int modX = startX + panelWidth + 5;
        int modY = startY + 35;
        for (Module mod : ModuleManager.modules) {
            if (mod.getCategory() == selectedCategory) {
                if (isHovered((int)mouseX, (int)mouseY, modX + 5, modY, panelWidth - 10, itemHeight)) {
                    if (button == 0) mod.toggle(); else if (button == 1) selectedModule = mod;
                    return super.mouseClicked(mouseX, mouseY, button);
                }
                modY += itemHeight + 2;
            }
        }

        int setX = startX + (panelWidth * 2) + 10;
        if (selectedModule != null) {
            int setY = startY + 35;
            if (isHovered((int)mouseX, (int)mouseY, setX + 5, setY, panelWidth + 40, itemHeight)) { selectedModule.toggle(); return super.mouseClicked(mouseX, mouseY, button); }
            setY += itemHeight + 5;

            for (Setting setting : selectedModule.getSettings()) {
                if (setting instanceof BooleanSetting) {
                    if (isHovered((int)mouseX, (int)mouseY, setX + 5, setY, panelWidth + 40, itemHeight)) ((BooleanSetting) setting).toggle();
                    setY += itemHeight + 2;
                } else if (setting instanceof SliderSetting) {
                    if (isHovered((int)mouseX, (int)mouseY, setX + 10, setY, panelWidth + 30, itemHeight + 10)) {
                        draggingSlider = (SliderSetting) setting;
                        updateSlider((int)mouseX);
                    }
                    setY += itemHeight + 12;
                }
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (draggingSlider != null) updateSlider((int)mouseX);
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        dragging = false;
        draggingSlider = null;
        return super.mouseReleased(mouseX, mouseY, button);
    }

    private void updateSlider(int mouseX) {
        int setX = startX + (panelWidth * 2) + 10;
        double percent = (double) (mouseX - (setX + 10)) / (panelWidth + 30);
        percent = Math.max(0, Math.min(1, percent));
        draggingSlider.setValue(draggingSlider.getMin() + (draggingSlider.getMax() - draggingSlider.getMin()) * percent);
    }
}
