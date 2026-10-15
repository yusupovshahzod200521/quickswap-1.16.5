package uz.shahzod.quickswap;

import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.DrawHighlightEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "quickswap", value = Dist.CLIENT)
public class OutlineHandler {

    // Chiziq rangi (0.0 - 1.0): qizil, yashil, ko'k, shaffoflik
    private static final float R = 0.0F;
    private static final float G = 0.0F;
    private static final float B = 0.0F;
    private static final float A = 0.8F;

    @SubscribeEvent
    public static void onHighlight(DrawHighlightEvent.HighlightBlock event) {
        Minecraft mc = Minecraft.getInstance();
        ClientWorld world = mc.level;
        if (world == null) return;

        BlockRayTraceResult target = event.getTarget();
        BlockPos pos = target.getBlockPos();
        BlockState state = world.getBlockState(pos);
        if (state.isAir(world, pos)) return;

        event.setCanceled(true); // vanilla chiziqni o'chiramiz

        VoxelShape shape = state.getShape(world, pos,
                ISelectionContext.of(event.getInfo().getEntity()));
        Vector3d cam = event.getInfo().getPosition();
        Matrix4f matrix = event.getMatrix().last().pose();
        IRenderTypeBuffer buffers = event.getBuffers();
        IVertexBuilder builder = buffers.getBuffer(ThickLineType.THICK);

        double ox = pos.getX() - cam.x;
        double oy = pos.getY() - cam.y;
        double oz = pos.getZ() - cam.z;

        shape.forAllEdges((x1, y1, z1, x2, y2, z2) -> {
            builder.vertex(matrix, (float) (x1 + ox), (float) (y1 + oy), (float) (z1 + oz))
                    .color(R, G, B, A).endVertex();
            builder.vertex(matrix, (float) (x2 + ox), (float) (y2 + oy), (float) (z2 + oz))
                    .color(R, G, B, A).endVertex();
        });

        if (buffers instanceof IRenderTypeBuffer.Impl) {
            ((IRenderTypeBuffer.Impl) buffers).endBatch(ThickLineType.THICK);
        }
    }
}
