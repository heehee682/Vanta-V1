package com.deinname.mod;

import com.deinname.mod.modules.ModuleManager;
import com.deinname.mod.ui.ClickGuiScreen;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

@Mod(modid = "custommod", name = "Custom Mod", version = "1.0")
public class Main {

    @Mod.Instance
    public static Main instance;

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        ModuleManager.init();
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(ModuleManager.class);
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (Keyboard.isCreated() && Keyboard.getEventKeyState()) {
            int keyCode = Keyboard.getEventKey();
            if (keyCode == Keyboard.KEY_RSHIFT) {
                net.minecraft.client.Minecraft.getMinecraft().displayGuiScreen(new ClickGuiScreen());
            }
        }
    }
}
