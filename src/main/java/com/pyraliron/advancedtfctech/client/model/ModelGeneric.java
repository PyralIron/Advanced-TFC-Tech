package com.pyraliron.advancedtfctech.client.model;

import net.minecraft.client.model.PositionTextureVertex;
import net.minecraft.client.model.TexturedQuad;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

public class ModelGeneric {
    private PositionTextureVertex[] vertexPositions;
    public int nVertices;
    /** PTV FORMAT: TRIANGLES*/
    public ModelGeneric(ModelRendererATT renderer, PositionTextureVertex[] vertexPositions)
    {
        this.vertexPositions = vertexPositions;
        for (int i = 0; i < vertexPositions.length; i++) {
            PositionTextureVertex ptv = vertexPositions[i];
            this.vertexPositions[i] = ptv.setTexturePosition(ptv.texturePositionX/renderer.textureWidth,ptv.texturePositionY/renderer.textureHeight);
        }
        this.nVertices = vertexPositions.length;
    }
    public ModelGeneric(ModelRendererATT renderer, TexturedQuad[] quadList) {
        this.vertexPositions = new PositionTextureVertex[quadList.length*6];
        for (int i = 0; i < quadList.length; i++) {
            this.vertexPositions[6*i] = quadList[i].vertexPositions[0];
            this.vertexPositions[6*i+1] = quadList[i].vertexPositions[1];
            this.vertexPositions[6*i+2] = quadList[i].vertexPositions[2];
            this.vertexPositions[6*i+3] = quadList[i].vertexPositions[0];
            this.vertexPositions[6*i+4] = quadList[i].vertexPositions[2];
            this.vertexPositions[6*i+5] = quadList[i].vertexPositions[3];
            for (int j = 0; j < 6; j++) {
                this.vertexPositions[6*i+j] = this.vertexPositions[6*i+j].setTexturePosition(this.vertexPositions[6*i+j].texturePositionX,this.vertexPositions[6*i+j].texturePositionY);
            }
        }
        this.nVertices = vertexPositions.length;
    }
    public ModelGeneric() {
    }
    @SideOnly(Side.CLIENT)
    public void render(BufferBuilder renderer, float scale)
    {
        /** pesky TexturedQuad used numerical values instead of referenced variables for drawType. Use GL11.GL_QUADS, not 7 ! */
        renderer.begin(GL11.GL_TRIANGLES, DefaultVertexFormats.OLDMODEL_POSITION_TEX_NORMAL);
        for (int i = 0; i < nVertices; i+=3)
        {
            Vec3d vec3d = this.vertexPositions[i+1].vector3D.subtractReverse(this.vertexPositions[i].vector3D);
            Vec3d vec3d1 = this.vertexPositions[i+1].vector3D.subtractReverse(this.vertexPositions[i+2].vector3D);
            Vec3d vec3d2 = vec3d1.crossProduct(vec3d).normalize();
            float f = (float)vec3d2.x;
            float f1 = (float)vec3d2.y;
            float f2 = (float)vec3d2.z;
            //System.out.println("f f1 f2 "+f+" "+f1+" "+f2);
            //System.out.println(this.vertexPositions[i]+" "+this.vertexPositions[i+1]+" "+this.vertexPositions[i+2]);
            PositionTextureVertex positiontexturevertex = this.vertexPositions[i];
            renderer.pos(positiontexturevertex.vector3D.x * (double)scale, positiontexturevertex.vector3D.y * (double)scale, positiontexturevertex.vector3D.z * (double)scale).tex((double)positiontexturevertex.texturePositionX, (double)positiontexturevertex.texturePositionY).normal(f, f1, f2).endVertex();
            PositionTextureVertex positiontexturevertex1 = this.vertexPositions[i+1];
            renderer.pos(positiontexturevertex1.vector3D.x * (double)scale, positiontexturevertex1.vector3D.y * (double)scale, positiontexturevertex1.vector3D.z * (double)scale).tex((double)positiontexturevertex1.texturePositionX, (double)positiontexturevertex1.texturePositionY).normal(f, f1, f2).endVertex();
            PositionTextureVertex positiontexturevertex2 = this.vertexPositions[i+2];
            renderer.pos(positiontexturevertex2.vector3D.x * (double)scale, positiontexturevertex2.vector3D.y * (double)scale, positiontexturevertex2.vector3D.z * (double)scale).tex((double)positiontexturevertex2.texturePositionX, (double)positiontexturevertex2.texturePositionY).normal(f, f1, f2).endVertex();


        }
        Tessellator.getInstance().draw();
    }
    public void setVertexPositions(PositionTextureVertex[] vertexPositions) {
        this.vertexPositions = vertexPositions;
        this.nVertices = vertexPositions.length;
    }

}
