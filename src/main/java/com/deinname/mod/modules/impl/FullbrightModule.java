package com.deinname.mod.modules.impl;

import com.deinname.mod.modules.Category;
import com.deinname.mod.modules.Module;
import net.minecraft.client.Minecraft;

public class FullbrightModule extends Module {
    private float oldGamma;

    public FullbrightModule() {
        super("Fullbright", Category.RENDER);
    }

    @Override
    public void onEnable() {
        oldGamma = Minecraft.getMinecraft().gameSettings.gammaSetting;
        Minecraft.getMinecraft().gameSettings.gammaSetting = 100.0F;
    }

    @Override
    public void onDisable() {
        Minecraft.getMinecraft().gameSettings.gammaSetting = oldGamma;
    }
}
