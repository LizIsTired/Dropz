package dev.lazurite.dropz.client.render;

import dev.lazurite.api.physics.client.PhysicsWorld;
import dev.lazurite.api.physics.client.handler.ClientPhysicsHandler;
import dev.lazurite.api.physics.util.math.QuaternionHelper;
import dev.lazurite.dropz.client.ClientInitializer;
import dev.lazurite.dropz.server.ServerInitializer;
import dev.lazurite.dropz.server.entity.PhysicsItemEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import shadow.com.bulletphysics.dynamics.RigidBody;
import shadow.com.bulletphysics.linearmath.Transform;
import shadow.javax.vecmath.Vector3f;

@Environment(EnvType.CLIENT)
public class PhysicsItemRenderer extends EntityRenderer<PhysicsItemEntity> {
    public PhysicsItemRenderer(EntityRenderDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void render(PhysicsItemEntity entity, float yaw, float delta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        MinecraftClient client = MinecraftClient.getInstance();
        ClientPhysicsHandler physics = (ClientPhysicsHandler) entity.getPhysics();

//        PhysicsWorld world = PhysicsWorld.getInstance();
//        world.getDynamicsWorld().setDebugDrawer(ClientInitializer.debugRenderer);
//        RigidBody body = physics.getRigidBody();
//        world.getDynamicsWorld().debugDrawWorld();
//        world.getDynamicsWorld().debugDrawObject(body.getWorldTransform(new Transform()), body.getCollisionShape(), new Vector3f(1.0f, 0, 1.0f));

        matrixStack.push();

        ItemStack itemStack = entity.getStack();
        BakedModel bakedModel = client.getItemRenderer().getHeldItemModel(itemStack, entity.world, null);
        matrixStack.peek().getModel().multiply(QuaternionHelper.quat4fToQuaternion(entity.getPhysics().getOrientation()));
        client.getItemRenderer().renderItem(itemStack, ModelTransformation.Mode.GROUND, false, matrixStack, vertexConsumerProvider, i, OverlayTexture.DEFAULT_UV, bakedModel);

        matrixStack.pop();
    }

    @Override
    public Identifier getTexture(PhysicsItemEntity entity) {
        return null;
    }

    public static void register() {
        EntityRendererRegistry.INSTANCE.register(ServerInitializer.PHYSICS_ITEM_ENTITY, (entityRenderDispatcher, context) -> new PhysicsItemRenderer(entityRenderDispatcher));
    }
}