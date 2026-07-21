package com.deinname.mod.modules.impl;

import com.deinname.mod.modules.Category;
import com.deinname.mod.modules.Module;
import com.deinname.mod.settings.BooleanSetting;
import net.minecraft.client.Minecraft;

public class SprintModule extends Module {
    public SprintModule() {
        super("Sprint", Category.MOVEMENT);
        addSetting(new BooleanSetting("Omnidirectional", false));
    }

    @Override
    public void onUpdate() {
        if (Minecraft.getMinecraft().thePlayer != null) {
            if (Minecraft.getMinecraft().thePlayer.moveForward > 0) {
                Minecraft.getMinecraft().thePlayer.setSprinting(true);
            }
        }
    }
}
