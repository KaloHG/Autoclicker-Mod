package gov.kallos.autoclickermod.client;

import gov.kallos.autoclickermod.client.gui.ConfigGUI;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class Autoclicker_ModClient implements ClientModInitializer {

    private static Autoclicker_ModClient INSTANCE;
    public static final Logger LOGGER = LoggerFactory.getLogger("autoclickermod");

    public static final File DIRECTORY = new File(MinecraftClient.getInstance().runDirectory, "autoclicker");

    public static AutoClickerConfig CONFIG;

    @Override
    public void onInitializeClient() {
        LOGGER.info("Initializing Autoclicker + config.");
        INSTANCE = this;
        DIRECTORY.mkdir(); //Makes dir if exists.
        CONFIG = new AutoClickerConfig(DIRECTORY);
        Autoclicker clicker = new Autoclicker();

        registerBinds();
        CONFIG.save();
    }

    public static Autoclicker_ModClient getINSTANCE() {
        return INSTANCE;
    }

    public static AutoClickerConfig getCONFIG() {
        return CONFIG;
    }

    private void registerBinds() {
        KeyBinding modConfigScreenBind = KeyBindingHelper.registerKeyBinding(new KeyBinding("Config GUI", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_COMMA, "Ramiel"));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while(modConfigScreenBind.wasPressed()) {
                MinecraftClient.getInstance().setScreen(new ConfigGUI(MinecraftClient.getInstance().currentScreen));
            }
        });
    }
}
