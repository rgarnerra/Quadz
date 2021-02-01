package dev.lazurite.fpvracing.client.packet.keybind;

import dev.lazurite.fpvracing.FPVRacing;
import dev.lazurite.fpvracing.common.entity.QuadcopterEntity;
import dev.lazurite.fpvracing.common.item.TransmitterItem;
import dev.lazurite.fpvracing.common.item.container.TransmitterContainer;
import dev.lazurite.rayon.physics.body.EntityRigidBody;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Box;

import java.util.List;

public class NoClipC2S {
    public static final Identifier PACKET_ID = new Identifier(FPVRacing.MODID, "noclip_c2s");

    public static void accept(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender sender) {
        ItemStack hand = player.getMainHandStack();

        server.execute(() -> {
            if (hand.getItem() instanceof TransmitterItem) {
                TransmitterContainer transmitter = FPVRacing.TRANSMITTER_CONTAINER.get(hand);
                List<QuadcopterEntity> quadcopters = player.getEntityWorld().getEntitiesByClass(QuadcopterEntity.class, new Box(player.getBlockPos()).expand(80), null);

                for (QuadcopterEntity quadcopter : quadcopters) {
                    if (quadcopter.getBindId() == transmitter.getBindId()) {
                        EntityRigidBody body = EntityRigidBody.get(quadcopter);
                        body.setNoClip(!body.isNoClipEnabled());

                        if (body.isNoClipEnabled()) {
                            player.sendMessage(new TranslatableText("message.fpvracing.noclip_on"), false);
                        } else {
                            player.sendMessage(new TranslatableText("message.fpvracing.noclip_off"), false);
                        }

                        break;
                    }
                }
            }
        });
    }

    public static void send() {
        ClientPlayNetworking.send(PACKET_ID, PacketByteBufs.create());
    }

    public static void register() {
        ServerPlayNetworking.registerGlobalReceiver(PACKET_ID, NoClipC2S::accept);
    }
}