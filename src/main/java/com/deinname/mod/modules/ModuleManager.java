package com.deinname.mod.modules;

import com.deinname.mod.modules.impl.AutoClickerModule;
import com.deinname.mod.modules.impl.FullbrightModule;
import com.deinname.mod.modules.impl.SprintModule;

import java.util.ArrayList;
import java.util.List;

public class ModuleManager {
    public static List<Module> modules = new ArrayList<>();

    public static void init() {
        modules.add(new SprintModule());
        modules.add(new FullbrightModule());
        modules.add(new AutoClickerModule());
    }
}
