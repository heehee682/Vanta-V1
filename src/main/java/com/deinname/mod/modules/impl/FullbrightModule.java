package com.deinname.mod.modules.impl;

import com.deinname.mod.modules.Category;
import com.deinname.mod.modules.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.SimpleOption;

public class FullbrightModule extends Module {
    private double oldGamma;

    public FullbrightModule() {
        super("Fullbright", Category.RENDER);
    }

    @Override
    public void onEnable() {
        SimpleOption<Double> gamma = MinecraftClient.getInstance().options.getGamma();
        oldGamma = gamma.getValue();
        gamma.setValue(10.0); // Sehr heller Gamma-Wert
    }

    @Override
    public void onDisable() {
        MinecraftClient.getInstance().options.getGamma().setValue(oldGamma);
    }
}
