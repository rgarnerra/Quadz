package dev.lazurite.fpvracing.client.input.keybinds;

import dev.lazurite.fpvracing.FPVRacing;
import dev.lazurite.fpvracing.client.packet.NoClipC2S;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

@Environment(EnvType.CLIENT)
public class NoClipKeybind {
    private static KeyBinding key;

    public static void callback(MinecraftClient client) {
        if (key.wasPressed()) NoClipC2S.send();
    }

    public static void register() {
        key = new KeyBinding(
                "key." + FPVRacing.MODID + ".noclip",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_N,
                "key." + FPVRacing.MODID + ".category"
        );

        KeyBindingHelper.registerKeyBinding(key);
        ClientTickEvents.END_CLIENT_TICK.register(NoClipKeybind::callback);
    }
}
