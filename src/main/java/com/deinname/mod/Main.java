package com.deinname.mod;

import com.deinname.mod.modules.ModuleManager;
import com.deinname.mod.ui.ClickGuiScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class Main implements ClientModInitializer {

    public static KeyBinding openClickGui;

    @Override
    public void onInitializeClient() {
        ModuleManager.init();

        openClickGui = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "Open ClickGUI",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_RIGHT_SHIFT,
            "Custom Mod"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openClickGui.wasPressed()) {
                client.setScreen(new ClickGuiScreen());
            }
            
            for (com.deinname.mod.modules.Module module : ModuleManager.modules) {
                if (module.isToggled()) {
                    module.onUpdate();
                }
            }
        });
    }
}
