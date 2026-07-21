package com.deinname.mod.modules.impl;

import com.deinname.mod.modules.Category;
import com.deinname.mod.modules.Module;
import com.deinname.mod.settings.SliderSetting;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Mouse;

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
        Minecraft mc = Minecraft.getMinecraft();
        
        // Nur klicken, wenn man im Spiel ist und die linke Maustaste gedrückt hält
        if (mc.thePlayer != null && mc.currentScreen == null && Mouse.isButtonDown(0)) {
            
            if (!wasHolding) {
                nextClickTime = System.currentTimeMillis() + getRandomDelay();
                wasHolding = true;
            }
            
            if (System.currentTimeMillis() >= nextClickTime) {
                // Löst den Angriff aus, indem wir dem Spiel sagen, die Attacke-Taste wurde gedrückt
                mc.gameSettings.keyBindAttack.pressTime = 1; 
                
                // Nächste Klickzeit berechnen
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
