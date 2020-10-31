package io.lazurite.fpvracing.client;

import io.lazurite.fpvracing.client.input.keybinds.EMPKeybind;
import io.lazurite.fpvracing.client.input.keybinds.GodModeKeybind;
import io.lazurite.fpvracing.client.input.keybinds.GogglePowerKeybind;
import io.lazurite.fpvracing.client.input.keybinds.NoClipKeybind;
import io.lazurite.fpvracing.client.renderers.FixedWingRenderer;
import io.lazurite.fpvracing.client.renderers.QuadcopterRenderer;
import io.lazurite.fpvracing.client.renderers.QuadcopterItemRenderer;
import io.lazurite.fpvracing.network.tracker.Config;
import io.lazurite.fpvracing.network.packet.*;
import io.lazurite.fpvracing.physics.PhysicsWorld;
import io.lazurite.fpvracing.server.ServerInitializer;
import io.lazurite.fpvracing.server.entities.QuadcopterEntity;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;

/**
 * This class is responsible for loading all of the client-side
 * registries and configurations.
 */
@Environment(EnvType.CLIENT)
public class ClientInitializer implements ClientModInitializer {

    /** The running instance of the minecraft client. */
    public static final MinecraftClient client = MinecraftClient.getInstance();

    /** The physics world used for {@link QuadcopterEntity} objects. */
    public static PhysicsWorld physicsWorld;

    /** The client player's config which is later sent to the server. */
    public static final String CONFIG_NAME = ServerInitializer.MODID + ".properties";
    private static Config config;

    /**
     * Initializes all of the registries and loads the player config.
     */
    @Override
    public void onInitializeClient() {
        GLFW.glfwInit(); // forcefully initializes GLFW

        config = new Config(CONFIG_NAME);
        ClientTick.register();

        GogglePowerKeybind.register();
        GodModeKeybind.register();
        NoClipKeybind.register();
        EMPKeybind.register();

        EntityPhysicsS2C.register();
        SelectedSlotS2C.register();
        ShouldRenderPlayerS2C.register();
        ModdedServerS2C.register();
        ConfigValueS2C.register();
        RevertConfigS2C.register();

        QuadcopterItemRenderer.register();
        QuadcopterRenderer.register();
        FixedWingRenderer.register();
    }

    /**
     * Get the config object.
     * @return the {@link Config} object being stored
     */
    public static Config getConfig() {
        return config;
    }
}