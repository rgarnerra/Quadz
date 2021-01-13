package dev.lazurite.fpvracing.client;

import dev.lazurite.fpvracing.client.renderer.QuadcopterItemRenderer;
import dev.lazurite.fpvracing.client.renderer.QuadcopterRenderer;
import dev.lazurite.fpvracing.network.packet.*;
import dev.lazurite.fpvracing.client.input.keybinds.EMPKeybind;
import dev.lazurite.fpvracing.client.input.keybinds.GodModeKeybind;
import dev.lazurite.fpvracing.client.input.keybinds.GogglePowerKeybind;
import dev.lazurite.fpvracing.client.input.keybinds.NoClipKeybind;
import dev.lazurite.fpvracing.server.ServerInitializer;
import dev.lazurite.fpvracing.util.VersionChecker;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.lwjgl.glfw.GLFW;

@Environment(EnvType.CLIENT)
public class ClientInitializer implements ClientModInitializer {
    private static VersionChecker versionChecker;

    @Override
    public void onInitializeClient() {
        GLFW.glfwInit(); // forcefully initializes GLFW

        ClientTick.register();

        GogglePowerKeybind.register();
        GodModeKeybind.register();
        NoClipKeybind.register();
        EMPKeybind.register();

        SelectedSlotS2C.register();
        ShouldRenderPlayerS2C.register();

        QuadcopterItemRenderer.register();
        QuadcopterRenderer.register();

        versionChecker = VersionChecker.getVersion(ServerInitializer.MODID, ServerInitializer.VERSION, ServerInitializer.URL);
    }

    public static VersionChecker getVersionChecker() {
        return versionChecker;
    }
}
