package com.pyraliron.advancedtfctech.client.model;

import net.minecraft.client.model.PositionTextureVertex;

public class ModelPyramid extends ModelGeneric {
    public ModelPyramid(ModelRendererATT renderer, int texU, int texV, float x, float y, float z, int dx, int dy, int dz, boolean flipface)
    {
        super();
        this.setVertexPositions(makePyramid(renderer,texU,texV,x,y,z,dx,dy,dz,flipface));
    }
    public static PositionTextureVertex[] makePyramid(ModelRendererATT renderer, float texU, float texV, float x, float y, float z, float dx, float dy, float dz, boolean faceup) {
        PositionTextureVertex[] pyramid = new PositionTextureVertex[12];
        for (int i = 0; i < 4; i++) {
            pyramid[3*i] = new PositionTextureVertex((i+1)%4>1?x+dx:x,y,i>1?z+dz:z,(i+1)%4>1?(texU+dx)/renderer.textureWidth:(texU)/renderer.textureWidth,i>1?(texV+dz)/renderer.textureHeight:(texV)/renderer.textureHeight);
            if (faceup) {
                pyramid[3*i+1] = new PositionTextureVertex(x+dx/2,y+dy,z+dz/2,(texU+dx/2)/renderer.textureWidth,(texV+dz/2)/renderer.textureHeight);
                pyramid[3*i+2] = new PositionTextureVertex(i<2?x+dx:x,y,(i+1)%4>1?z+dz:z,i<2?(texU+dx)/renderer.textureWidth:(texU)/renderer.textureWidth,(i+1)%4>1?(texV+dz)/renderer.textureHeight:(texV)/renderer.textureHeight);
            } else {
                pyramid[3*i+1] = new PositionTextureVertex(i<2?x+dx:x,y,(i+1)%4>1?z+dz:z,i<2?(texU+dx)/renderer.textureWidth:(texU)/renderer.textureWidth,(i+1)%4>1?(texV+dz)/renderer.textureHeight:(texV)/renderer.textureHeight);
                pyramid[3*i+2] = new PositionTextureVertex(x+dx/2,y+dy,z+dz/2,(texU+dx/2)/renderer.textureWidth,(texV+dz/2)/renderer.textureHeight);
            }
            //System.out.println(pyramid[3*i]);
            //System.out.println(pyramid[3*i+1]);
            //System.out.println(pyramid[3*i+2]);
        }
        return pyramid;
    }
}
