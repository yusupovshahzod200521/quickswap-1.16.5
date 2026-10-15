package com.shahzod.whiteoutline;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.DrawHighlightEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod("whiteoutline")
@Mod.EventBusSubscriber(modid = "whiteoutline", value = Dist.CLIENT)
public class WhiteOutline {

    @SubscribeEvent
    public static void onHighlight(DrawHighlightEvent.HighlightBlock event) {
        ClientWorld world = Minecraft.getInstance().world;
        Entity entity = event.getInfo().getRenderViewEntity();
        if (world == null || entity == null) return;

        BlockPos pos = event.getTarget().getPos();
        BlockState state = world.getBlockState(pos);
        VoxelShape shape = state.getShape(world, pos, ISelectionContext.forEntity(entity));

        Vector3d cam = event.getInfo().getProjectedView();
        MatrixStack ms = event.getMatrix();
        IRenderTypeBuffer buffers = event.getBuffers();
        IVertexBuilder vb = buffers.getBuffer(RenderType.getLines());

        double ox = pos.getX() - cam.x;
        double oy = pos.getY() - cam.y;
        double oz = pos.getZ() - cam.z;

        MatrixStack.Entry entry = ms.getLast();
        shape.forEachEdge((x1, y1, z1, x2, y2, z2) -> {
            float dx = (float) (x2 - x1);
            float dy = (float) (y2 - y1);
            float dz = (float) (z2 - z1);
            float len = MathHelper.sqrt(dx * dx + dy * dy + dz * dz);
            dx /= len; dy /= len; dz /= len;
            vb.pos(entry.getMatrix(), (float) (x1 + ox), (float) (y1 + oy), (float) (z1 + oz))
              .color(1f, 1f, 1f, 1f).normal(entry.getNormal(), dx, dy, dz).endVertex();
            vb.pos(entry.getMatrix(), (float) (x2 + ox), (float) (y2 + oy), (float) (z2 + oz))
              .color(1f, 1f, 1f, 1f).normal(entry.getNormal(), dx, dy, dz).endVertex();
        });

        event.setCanceled(true);
    }
}
