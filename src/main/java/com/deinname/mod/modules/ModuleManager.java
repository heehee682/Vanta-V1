package com.deinname.mod.modules;

import com.deinname.mod.modules.impl.AutoClickerModule;
import com.deinname.mod.modules.impl.FullbrightModule;
import com.deinname.mod.modules.impl.SprintModule;
import com.deinname.mod.modules.impl.VelocityModule;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

public class ModuleManager {
    public static List<Module> modules = new ArrayList<>();

    public static void init() {
        modules.add(new SprintModule());
        modules.add(new FullbrightModule());
        modules.add(new VelocityModule());
        modules.add(new AutoClickerModule());
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            for (Module module : modules) {
                if (module.isToggled()) {
                    module.onUpdate();
                }
            }
        }
    }
}
