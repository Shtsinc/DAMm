package daam.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;

public class DrawUtils {

    @OnlyIn(Dist.CLIENT)
    public static void open(Screen screen) {
        Minecraft.getInstance().setScreen(screen);
    }

    public static void grid(AABB axisAlignedBB, float red, float green, float blue, float alpha) {
        drawBoundingBox(
            axisAlignedBB.minX - 0.01, axisAlignedBB.minY - 0.01, axisAlignedBB.minZ - 0.01, 
            axisAlignedBB.maxX - 0.01, axisAlignedBB.maxY - 0.01, axisAlignedBB.maxZ - 0.01, 
            red, green, blue, alpha
        );
    }

    public static void drawBoundingBox(double x1, double y1, double z1, double x2, double y2, double z2, 
                                     float red, float green, float blue, float alpha) {
        Tesselator instance = Tesselator.getInstance();
        BufferBuilder buffer = instance.getBuilder();

        // Set up rendering state
        RenderSystem.setShader(() -> GameRenderer.getPositionColorShader());

        // Draw bottom face
        buffer.begin(VertexFormat.Mode.LINE_STRIP, DefaultVertexFormat.POSITION_COLOR);
        buffer.vertex(x1, y1, z1).color(red, green, blue, alpha).endVertex();
        buffer.vertex(x2, y1, z1).color(red, green, blue, alpha).endVertex();
        buffer.vertex(x2, y1, z2).color(red, green, blue, alpha).endVertex();
        buffer.vertex(x1, y1, z2).color(red, green, blue, alpha).endVertex();
        instance.end();

        // Draw top face
        buffer.begin(VertexFormat.Mode.LINE_STRIP, DefaultVertexFormat.POSITION_COLOR);
        buffer.vertex(x1, y2, z1).color(red, green, blue, alpha).endVertex();
        buffer.vertex(x2, y2, z1).color(red, green, blue, alpha).endVertex();
        buffer.vertex(x2, y2, z2).color(red, green, blue, alpha).endVertex();
        buffer.vertex(x1, y2, z2).color(red, green, blue, alpha).endVertex();
        instance.end();

        // Draw vertical edges
        buffer.begin(VertexFormat.Mode.LINES, DefaultVertexFormat.POSITION_COLOR);
        
        // Vertical lines connecting bottom and top
        buffer.vertex(x1, y1, z1).color(red, green, blue, alpha).endVertex();
        buffer.vertex(x1, y2, z1).color(red, green, blue, alpha).endVertex();

        buffer.vertex(x2, y1, z1).color(red, green, blue, alpha).endVertex();
        buffer.vertex(x2, y2, z1).color(red, green, blue, alpha).endVertex();

        buffer.vertex(x2, y1, z2).color(red, green, blue, alpha).endVertex();
        buffer.vertex(x2, y2, z2).color(red, green, blue, alpha).endVertex();
        
        buffer.vertex(x1, y1, z2).color(red, green, blue, alpha).endVertex();
        buffer.vertex(x1, y2, z2).color(red, green, blue, alpha).endVertex();
        
        instance.end();

        // Draw grid lines (simplified version)
        buffer.begin(VertexFormat.Mode.LINES, DefaultVertexFormat.POSITION_COLOR);

        double offsetSize = 1.0;
        int maxLines = 64; // Limit grid density for performance
        
        // Grid on Z faces
        double stepY = (y2 - y1) / Math.min(maxLines, (int)((y2 - y1) / offsetSize));
        double stepX = (x2 - x1) / Math.min(maxLines, (int)((x2 - x1) / offsetSize));
        
        if (stepY > 0) {
            for (double y = y1; y <= y2; y += stepY) {
                // Back face (z2)
                buffer.vertex(x1, y, z2).color(red, green, blue, alpha * 0.5f).endVertex();
                buffer.vertex(x2, y, z2).color(red, green, blue, alpha * 0.5f).endVertex();
                
                // Front face (z1)
                buffer.vertex(x1, y, z1).color(red, green, blue, alpha * 0.5f).endVertex();
                buffer.vertex(x2, y, z1).color(red, green, blue, alpha * 0.5f).endVertex();
            }
        }

        if (stepX > 0) {
            for (double x = x1; x <= x2; x += stepX) {
                // Back face (z2)
                buffer.vertex(x, y1, z2).color(red, green, blue, alpha * 0.5f).endVertex();
                buffer.vertex(x, y2, z2).color(red, green, blue, alpha * 0.5f).endVertex();
                
                // Front face (z1)
                buffer.vertex(x, y1, z1).color(red, green, blue, alpha * 0.5f).endVertex();
                buffer.vertex(x, y2, z1).color(red, green, blue, alpha * 0.5f).endVertex();
            }
        }

        instance.end();
    }
}