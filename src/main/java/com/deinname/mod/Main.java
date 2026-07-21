package com.deinname.mod;

import com.deinname.mod.modules.ModuleManager;
import com.deinname.mod.ui.ClickGuiScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

@Mod(modid = "custommod", name = "Custom Mod", version = "1.0")
public class Main {

    @Mod.Instance
    public static Main instance;

    // Unser benutzerdefinierter Keybind
    public static KeyBinding openClickGui;

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        ModuleManager.init();
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(ModuleManager.class);

        // Keybind erstellen (Standard: Rechts-Shift) und in Minecraft registrieren
        openClickGui = new KeyBinding("Open ClickGUI", Keyboard.KEY_RSHIFT, "Custom Mod");
        ClientRegistry.registerKeyBinding(openClickGui);
    }

    // Wir nutzen den ClientTickEvent, um den Klick abzufangen
    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            // Nur ausführen, wenn man im Spiel ist (nicht im Menü)
            if (Minecraft.getMinecraft().theWorld != null && Minecraft.getMinecraft().currentScreen == null) {
                // isPressed() gibt true zurück, wenn die Taste einmal gedrückt wurde
                if (openClickGui.isPressed()) {
                    Minecraft.getMinecraft().displayGuiScreen(new ClickGuiScreen());
                }
            }
        }
    }
}
