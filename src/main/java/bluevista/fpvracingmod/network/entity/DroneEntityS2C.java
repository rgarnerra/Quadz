package bluevista.fpvracingmod.network.entity;

import bluevista.fpvracingmod.network.PacketHelper;
import bluevista.fpvracingmod.server.ServerInitializer;
import bluevista.fpvracingmod.server.entities.DroneEntity;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.fabricmc.fabric.api.network.PacketContext;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.fabricmc.fabric.api.server.PlayerStream;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;
import java.util.UUID;
import java.util.stream.Stream;

public class DroneEntityS2C {
    public static final Identifier PACKET_ID = new Identifier(ServerInitializer.MODID, "drone_entity_s2c");

    public static void accept(PacketContext context, PacketByteBuf buf) {
        PlayerEntity player = context.getPlayer();

        int droneID = buf.readInt();
        UUID playerID = buf.readUuid();

        int band = buf.readInt();
        int channel = buf.readInt();
        int cameraAngle = buf.readInt();
        float fieldOfView = buf.readFloat();

        int godMode = buf.readInt();
        boolean infiniteTracking = buf.readBoolean();

        float throttle = buf.readFloat();
        Vector3f position = PacketHelper.deserializeVector3f(buf);
        Vector3f linearVel = PacketHelper.deserializeVector3f(buf);
        Vector3f angularVel = PacketHelper.deserializeVector3f(buf);
        Quat4f orientation = PacketHelper.deserializeQuaternion(buf);

        context.getTaskQueue().execute(() -> {
            DroneEntity drone = null;

            if(player != null)
                drone = (DroneEntity) player.world.getEntityById(droneID);

            if(drone != null) {
                drone.playerID = playerID;

                drone.setBand(band);
                drone.setChannel(channel);
                drone.setCameraAngle(cameraAngle);
                drone.setFieldOfView(fieldOfView);

                drone.setGodMode(godMode);
                drone.setInfiniteTracking(infiniteTracking);

                if(!drone.playerID.equals(player.getUuid())) { // if any player besides physics-controlling player
                    drone.setThrottle(throttle);
                    drone.setOrientation(orientation);
                    drone.getRigidBody().setLinearVelocity(linearVel);
                    drone.getRigidBody().setAngularVelocity(angularVel);

                    drone.setPos(position.x, position.y, position.z);
                    drone.setRigidBodyPos(position);
                }
            }
        });
    }

    public static void send(DroneEntity drone) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());

        buf.writeInt(drone.getEntityId());
        buf.writeUuid(drone.playerID);

        buf.writeInt(drone.getBand());
        buf.writeInt(drone.getChannel());
        buf.writeInt(drone.getCameraAngle());
        buf.writeFloat(drone.getFieldOfView());

        buf.writeInt(drone.getGodMode());
        buf.writeBoolean(drone.hasInfiniteTracking());

        buf.writeFloat(drone.getThrottle());
        PacketHelper.serializeVector3f(buf, drone.getRigidBodyPos());
        PacketHelper.serializeVector3f(buf, drone.getRigidBody().getLinearVelocity(new Vector3f()));
        PacketHelper.serializeVector3f(buf, drone.getRigidBody().getAngularVelocity(new Vector3f()));
        PacketHelper.serializeQuaternion(buf, drone.getOrientation());

        Stream<PlayerEntity> watchingPlayers = PlayerStream.watching(drone.getEntityWorld(), new BlockPos(drone.getPos()));
        watchingPlayers.forEach(player -> ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, PACKET_ID, buf));
    }

    public static void register() {
        ClientSidePacketRegistry.INSTANCE.register(PACKET_ID, DroneEntityS2C::accept);
    }
}