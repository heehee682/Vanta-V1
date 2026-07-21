package com.deinname.mod.modules.impl;

import com.deinname.mod.modules.Category;
import com.deinname.mod.modules.Module;
import com.deinname.mod.settings.SliderSetting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import org.lwjgl.glfw.GLFW;

import java.util.concurrent.ThreadLocalRandom;

public class AutoClickerModule extends Module {

    private SliderSetting minCps;
    private SliderSetting maxCps;

    private long nextClickTime = 0;
    private boolean wasHolding = false;

    public AutoClickerModule() {
        super("AutoClicker", Category.COMBAT);
        minCps = new SliderSetting("Min CPS", 10, 1, 20);
        maxCps = new SliderSetting("Max CPS", 15, 1, 20);
        addSetting(minCps);
        addSetting(maxCps);
    }

    @Override
    public void onUpdate() {
        MinecraftClient mc = MinecraftClient.getInstance();
        
        if (mc.player != null && mc.currentScreen == null && GLFW.glfwGetMouseButton(mc.getWindow().getHandle(), 0) == 1) {
            if (!wasHolding) {
                nextClickTime = System.currentTimeMillis() + getRandomDelay();
                wasHolding = true;
            }
            
            if (System.currentTimeMillis() >= nextClickTime) {
                // HIER WURDE .getCode() ENTFERNT (passt jetzt für 1.21.10)
                KeyBinding.click(mc.options.attackKey.getDefaultKey());
                nextClickTime = System.currentTimeMillis() + getRandomDelay();
            }
        } else {
            wasHolding = false;
        }
    }

    private long getRandomDelay() {
        double min = Math.min(minCps.getValue(), maxCps.getValue());
        double max = Math.max(minCps.getValue(), maxCps.getValue());

        long minDelay = (long) (1000.0 / max);
        long maxDelay = (long) (1000.0 / min);

        if (minDelay == maxDelay) return minDelay;
        return ThreadLocalRandom.current().nextLong(minDelay, maxDelay + 1);
    }
}
