package com.pyraliron.advancedtfctech.client.model;

import net.minecraft.client.model.PositionTextureVertex;
import net.minecraft.client.model.TexturedQuad;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModelBoxSlanted extends ModelGeneric {
    private TexturedQuad[] quadList;
    public ModelBoxSlanted(ModelRendererATT renderer, int texU, int texV, int x, int y, int z, int dx, int dy, int dz, Vec3d sx, Vec3d sy, Vec3d sz)
    {
        super();
        makeSlantedBox(renderer,texU,texV,x,y,z,dx,dy,dz,sx,sy,sz);
    }
    public void makeSlantedBox(ModelRendererATT renderer, int texU, int texV, int x, int y, int z, int dx, int dy, int dz, Vec3d sx, Vec3d sy, Vec3d sz) {
        this.quadList = new TexturedQuad[6];
        PositionTextureVertex positiontexturevertex7 = new PositionTextureVertex(x, y, z, 0.0F, 0.0F);
        PositionTextureVertex positiontexturevertex = new PositionTextureVertex((float)(x+sx.x), (float)(y+sx.y), (float)(z+sx.z), 0.0F, 8.0F);
        PositionTextureVertex positiontexturevertex1 = new PositionTextureVertex((float)(x+sx.x+sy.x), (float)(y+sx.y+sy.y), (float)(z+sx.z+sy.z), 8.0F, 8.0F);
        PositionTextureVertex positiontexturevertex2 = new PositionTextureVertex((float)(x+sy.x), (float)(y+sy.y), (float)(z+sy.z), 8.0F, 0.0F);
        PositionTextureVertex positiontexturevertex3 = new PositionTextureVertex((float)(x+sz.x), (float)(y+sz.y), (float)(z+sz.z), 0.0F, 0.0F);
        PositionTextureVertex positiontexturevertex4 = new PositionTextureVertex((float)(x+sx.x+sz.x), (float)(y+sx.y+sz.y), (float)(z+sx.z+sz.z), 0.0F, 8.0F);
        PositionTextureVertex positiontexturevertex5 = new PositionTextureVertex((float)(x+sx.x+sy.x+sz.x), (float)(y+sx.y+sy.y+sz.y), (float)(z+sx.z+sy.z+sz.z), 8.0F, 8.0F);
        PositionTextureVertex positiontexturevertex6 = new PositionTextureVertex((float)(x+sy.x+sz.x), (float)(y+sy.y+sz.y), (float)(z+sy.z+sz.z), 8.0F, 0.0F);

        this.quadList[0] = new TexturedQuad(new PositionTextureVertex[] {positiontexturevertex4, positiontexturevertex, positiontexturevertex1, positiontexturevertex5}, texU + dz + dx, texV + dz, texU + dz + dx + dz, texV + dz + dy, renderer.textureWidth, renderer.textureHeight);
        this.quadList[1] = new TexturedQuad(new PositionTextureVertex[] {positiontexturevertex7, positiontexturevertex3, positiontexturevertex6, positiontexturevertex2}, texU, texV + dz, texU + dz, texV + dz + dy, renderer.textureWidth, renderer.textureHeight);
        this.quadList[2] = new TexturedQuad(new PositionTextureVertex[] {positiontexturevertex4, positiontexturevertex3, positiontexturevertex7, positiontexturevertex}, texU + dz, texV, texU + dz + dx, texV + dz, renderer.textureWidth, renderer.textureHeight);
        this.quadList[3] = new TexturedQuad(new PositionTextureVertex[] {positiontexturevertex1, positiontexturevertex2, positiontexturevertex6, positiontexturevertex5}, texU + dz + dx, texV + dz, texU + dz + dx + dx, texV, renderer.textureWidth, renderer.textureHeight);
        this.quadList[4] = new TexturedQuad(new PositionTextureVertex[] {positiontexturevertex, positiontexturevertex7, positiontexturevertex2, positiontexturevertex1}, texU + dz, texV + dz, texU + dz + dx, texV + dz + dy, renderer.textureWidth, renderer.textureHeight);
        this.quadList[5] = new TexturedQuad(new PositionTextureVertex[] {positiontexturevertex3, positiontexturevertex4, positiontexturevertex5, positiontexturevertex6}, texU + dz + dx + dz, texV + dz, texU + dz + dx + dz + dx, texV + dz + dy, renderer.textureWidth, renderer.textureHeight);

    }

    @SideOnly(Side.CLIENT)
    public void render(BufferBuilder renderer, float scale)
    {
        for (TexturedQuad texturedquad : this.quadList)
        {
            texturedquad.draw(renderer, scale);
        }
    }
}
