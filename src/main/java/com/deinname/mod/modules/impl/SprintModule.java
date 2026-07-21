package com.deinname.mod.modules.impl;

import com.deinname.mod.modules.Category;
import com.deinname.mod.modules.Module;
import com.deinname.mod.settings.BooleanSetting;
import net.minecraft.client.MinecraftClient;

public class SprintModule extends Module {
    public SprintModule() {
        super("Sprint", Category.MOVEMENT);
        addSetting(new BooleanSetting("Omnidirectional", false));
    }

    @Override
    public void onUpdate() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player != null) {
            BooleanSetting omni = (BooleanSetting) getSettings().get(0);
            if (omni.isEnabled()) {
                if (mc.player.forwardSpeed != 0 || mc.player.sidewaysSpeed != 0) {
                    mc.player.setSprinting(true);
                }
            } else {
                if (mc.player.forwardSpeed > 0) {
                    mc.player.setSprinting(true);
                }
            }
        }
    }
}
