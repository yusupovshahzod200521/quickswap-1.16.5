package uz.shahzod.quickswap;

import java.util.OptionalDouble;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;

public class ThickLineType extends RenderType {

    // 6.0 = chiziq qalinligi (vanilla taxminan 2.5)
    public static final RenderType THICK = create("quickswap_thick_lines",
            DefaultVertexFormats.POSITION_COLOR, GL11.GL_LINES, 256,
            RenderType.State.builder()
                    .setLineState(new LineState(OptionalDouble.of(6.0D)))
                    .setLayeringState(VIEW_OFFSET_Z_LAYERING)
                    .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                    .setWriteMaskState(COLOR_DEPTH_WRITE)
                    .createCompositeState(false));

    private ThickLineType(String name, VertexFormat format, int mode, int bufferSize,
                          boolean affectsCrumbling, boolean sortOnUpload,
                          Runnable setup, Runnable clear) {
        super(name, format, mode, bufferSize, affectsCrumbling, sortOnUpload, setup, clear);
    }
}
