package io.lazurite.fpvracing.client.renderers;

import io.lazurite.fpvracing.client.ClientInitializer;
import io.lazurite.fpvracing.client.models.QuadcopterModel;
import io.lazurite.fpvracing.physics.entity.PhysicsEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;

public class QuadcopterItemRenderer {
    private static QuadcopterModel model;

    public static void register() {
        int size = PhysicsEntity.SIZE.getDataType().fromConfig(ClientInitializer.getConfig(), PhysicsEntity.SIZE.getName());
        model = new QuadcopterModel(size);
    }

    public static void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, int size) {
        matrices.push();
        matrices.translate(0.5, 0.45, 0.5);
        matrices.scale(2.0f, 2.0f, 2.0f);
        model.setSize(size);
        model.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntitySolid(QuadcopterRenderer.quadTexture)), light, overlay, 1f, 1f, 1f, 1f);
        matrices.pop();
    }

}