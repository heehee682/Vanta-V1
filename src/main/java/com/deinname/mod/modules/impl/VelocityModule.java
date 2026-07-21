package com.deinname.mod.modules.impl;

import com.deinname.mod.modules.Category;
import com.deinname.mod.modules.Module;
import com.deinname.mod.settings.BooleanSetting;
import com.deinname.mod.settings.SliderSetting;

public class VelocityModule extends Module {
    public VelocityModule() {
        super("Velocity", Category.COMBAT);
        addSetting(new SliderSetting("Horizontal", 90, 0, 100));
        addSetting(new SliderSetting("Vertical", 90, 0, 100));
        addSetting(new BooleanSetting("Only Players", true));
    }
}
