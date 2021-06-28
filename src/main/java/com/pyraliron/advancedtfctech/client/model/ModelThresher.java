package com.pyraliron.advancedtfctech.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.PositionTextureVertex;
import net.minecraft.client.model.TexturedQuad;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

import javax.swing.text.Position;

public class ModelThresher extends ModelBase {
    ModelRenderer base;
    ModelRendererATT baseatt;
    ModelRendererATT barrel_parent;
    ModelRendererATT test;
    public boolean mirror;
    public ModelThresher(boolean mirror)
    {
        this.textureWidth = 512;
        this.textureHeight = 128;
        this.mirror = mirror;

        refresh();
    }
    public void refresh() {
        this.base = new ModelRenderer(this, "base");
        this.baseatt = new ModelRendererATT(this);

        base.setRotationPoint(64,0,-16);
        baseatt.setRotationPoint(64,0,-16);
        base.rotateAngleY = (float) -Math.PI/2;
        baseatt.rotateAngleY = (float) -Math.PI/2;

        ModelRenderer base1 = new ModelRenderer(this,0,0);
        base1.addBox(0, 0, 0, 16 * 3, 8, 16 * 3);
        base1.setRotationPoint(16, 0, 48);
        base1.rotateAngleY = (float) Math.PI / 2;
        base.addChild(base1);

        /*ModelRenderer base2 = new ModelRenderer(this, 168, 56);
        base2.addBox(0, 0, 0, 16 * 2, 24, 16 * 2);
        base2.setRotationPoint(-8+32, 16, 0);
        //base2.rotateAngleY = (float) Math.PI / 2;
        this.base.addChild(base2);*/

        /*ModelRenderer base3 = new ModelRenderer(this, 264, 48);
        base3.addBox(0, 0, 0, 16 * 2, 24, 16);
        base3.setRotationPoint(-8+32, 16, 32);
        this.base.addChild(base3);*/

        /*ModelRenderer base4 = new ModelRenderer(this, 316, 0);
        base4.addBox(0, 0, 0, 43, 19, 22);
        base4.setRotationPoint(-3+32, 8, 43-10);
        base4.rotateAngleY = (float) Math.PI / 2;
        this.base.addChild(base4);*/

//        ModelRenderer power = new ModelRenderer(this, 360, 41);
//        power.addBox(0, 0, 0, 16, 24, 16);
//        power.setRotationPoint(48, 8, 48);
//        //base4.rotateAngleY = (float) Math.PI / 2;
//        this.base.addChild(power);

        /*ModelRenderer power2 = new ModelRenderer(this, 56, 96);
        power2.addBox(0, 0, 0, 16, 8, 16);
        power2.setRotationPoint(48, 8, 48);
        //base4.rotateAngleY = (float) Math.PI / 2;
        this.base.addChild(power2);*/

        if (this.mirror) {
            ModelRenderer redstone_support = new ModelRenderer(this, 0, 24);
            redstone_support.addBox(0, 0, 0, 4, 10, 2);
            redstone_support.setRotationPoint(18, 8, 62 - 64 + 8 + 10 + 12);
            //redstone_support.rotateAngleY = (float) Math.PI / 2;
            this.base.addChild(redstone_support);

            ModelRenderer redstone_support2 = new ModelRenderer(this, 0, 24);
            redstone_support2.addBox(0, 0, 0, 4, 10, 2);
            redstone_support2.setRotationPoint(18, 8, 62 - 64 + 8 + 12);
            //redstone_support2.rotateAngleY = (float) Math.PI / 2;
            this.base.addChild(redstone_support2);

            ModelRenderer redstone = new ModelRenderer(this, 0, 0);
            redstone.addBox(0, 0, 0, 16, 16, 8);
            redstone.setRotationPoint(16, 16, 32);
            redstone.rotateAngleY = (float) Math.PI / 2;
            this.base.addChild(redstone);

            ModelRenderer fan = new ModelRenderer(this, 296, 88);
            fan.addBox(0, 0, 0, 16, 16, 16);
            fan.setRotationPoint(48, 16, 16);
            //fan.rotateAngleY = (float) Math.PI / 2;
            this.base.addChild(fan);
        } else {
            ModelRenderer redstone_support = new ModelRenderer(this, 0, 24);
            redstone_support.addBox(0, 0, 0, 4, 10, 2);
            redstone_support.setRotationPoint(18+48-8, 8, 62 - 64 + 8 + 10 + 12);
            //redstone_support.rotateAngleY = (float) Math.PI / 2;
            this.base.addChild(redstone_support);

            ModelRenderer redstone_support2 = new ModelRenderer(this, 0, 24);
            redstone_support2.addBox(0, 0, 0, 4, 10, 2);
            redstone_support2.setRotationPoint(18+48-8, 8, 62 - 64 + 8 + 12);
            //redstone_support2.rotateAngleY = (float) Math.PI / 2;
            this.base.addChild(redstone_support2);

            ModelRenderer redstone = new ModelRenderer(this, 0, 0);
            redstone.addBox(0, 0, 0, 16, 16, 8);
            redstone.setRotationPoint(64, 16, 16);
            redstone.rotateAngleY = (float) -Math.PI / 2;
            this.base.addChild(redstone);

            ModelRenderer fan = new ModelRenderer(this, 296, 88);
            fan.addBox(0, 0, 0, 16, 16, 16);
            fan.setRotationPoint(32, 16, 32);
            fan.rotateAngleY = (float) Math.PI;
            this.base.addChild(fan);
        }


        ModelRenderer input_support = new ModelRenderer(this, 424, 41);
        input_support.addBox(0, 0, 0, 6, 21, 3);
        input_support.setRotationPoint(21+4, 8, -3+10);
        this.base.addChild(input_support);

        ModelRenderer input_support2 = new ModelRenderer(this, 424, 41);
        input_support2.addBox(0, 0, 0, 6, 21, 3);
        input_support2.setRotationPoint(56-7, 8, -3+10);
        this.base.addChild(input_support2);

        ModelRenderer input_support3 = new ModelRenderer(this, 424, 41);
        input_support3.addBox(0, 0, 0, 6, 21, 3);
        input_support3.setRotationPoint(21+4, 8, -2+10+30);
        this.base.addChild(input_support3);

        ModelRenderer input_support4 = new ModelRenderer(this, 424, 41);
        input_support4.addBox(0, 0, 0, 6, 21, 3);
        input_support4.setRotationPoint(56-7, 8, -2+10+30);
        this.base.addChild(input_support4);

//        ModelRenderer input_support3 = new ModelRenderer(this, 424, 41);
//        input_support3.addBox(0, 0, 0, 10, 21, 3);
//        input_support3.setRotationPoint(31, 8, 48);
//        input_support3.rotateAngleY = (float) Math.PI / 2;
//        this.base.addChild(input_support3);
//
//        ModelRenderer input_support4 = new ModelRenderer(this, 424, 41);
//        input_support4.addBox(0, 0, 0, 10, 21, 3);
//        input_support4.setRotationPoint(46, 8, 48);
//        input_support4.rotateAngleY = (float) Math.PI / 2;
//        this.base.addChild(input_support4);

        /*ModelRenderer input2 = new ModelRenderer(this, 296, 88);
        input2.addBox(0, 0, 0, 32, 15, 15);
        input2.setRotationPoint(48-0.5F, 19, 19);
        input2.rotateAngleY = (float) Math.PI / 3;
        input2.rotateAngleZ = (float) Math.PI / 2;
        this.base.addChild(input2);*/

        ModelRenderer chimney_support = new ModelRenderer(this, 128, 56);
        chimney_support.addBox(0, 0, 0, 6, 43-16, 14);
        chimney_support.setRotationPoint(46, 4, 33);
        //chimney_support.rotateAngleY = (float) Math.PI / 2;
        //this.base.addChild(chimney_support);

        ModelRenderer chimney_support2 = new ModelRenderer(this, 128, 56);
        chimney_support2.addBox(0, 0, 0, 6, 43-16, 14);
        chimney_support2.setRotationPoint(28, 4, 33);
        //this.base.addChild(chimney_support2);

        ModelRenderer chimney = new ModelRenderer(this, 0, 56);
        chimney.addBox(0, 0, 0, 16, 12, 12);
        chimney.setRotationPoint(64-11.5F-16-2.5F, 34-16, 64-16);
        chimney.rotateAngleY = (float) Math.PI / 2;
        this.base.addChild(chimney);



        ModelRenderer output = new ModelRenderer(this, 80, 104);
        output.addBox(0, 0, 0, 16, 8, 16);
        output.setRotationPoint(48, 8, 0);
        output.rotateAngleY = (float) -Math.PI / 2;
        this.base.addChild(output);

//        ModelRenderer conveyor = new ModelRenderer(this, 456,0);
//        conveyor.addBox(0, 0, 0, 16, 16, 1);
//        conveyor.setRotationPoint(32, 24+9, 0);
//        conveyor.rotateAngleX = (float) Math.PI / 2;
//        this.base.addChild(conveyor);


        int[] hp_rel = {32,48,16};

        ModelPyramid hopper = new ModelPyramid(baseatt,382,93,hp_rel[0],hp_rel[1],hp_rel[2],16,-12,16,false);
        baseatt.addModel(hopper);
        ModelPyramid hopper2 = new ModelPyramid(baseatt,383,94,hp_rel[0]+1,hp_rel[1],hp_rel[2]+1,14,-11,14,true);
        baseatt.addModel(hopper2);

        PositionTextureVertex[] hopper_rim_ptv = {
                new PositionTextureVertex(hp_rel[0],hp_rel[1],hp_rel[2],424,85),
                new PositionTextureVertex(hp_rel[0],hp_rel[1],hp_rel[2]+16,424,85+16),
                new PositionTextureVertex(hp_rel[0]+16,hp_rel[1],hp_rel[2],424+16,85),
                new PositionTextureVertex(hp_rel[0],hp_rel[1],hp_rel[2]+16,424,85+16),
                new PositionTextureVertex(hp_rel[0]+16,hp_rel[1],hp_rel[2]+16,424+16,85+16),
                new PositionTextureVertex(hp_rel[0]+16,hp_rel[1],hp_rel[2],424+16,85),
        };
        ModelGeneric hopper_rim = new ModelGeneric(baseatt,hopper_rim_ptv);
        baseatt.addModel(hopper_rim);

        ModelRendererATT hopper_support_parent = new ModelRendererATT(this);
        hopper_support_parent.setRotationPoint(32,38,4);

//        ModelRenderer hopper_box = new ModelRenderer(this,406,101);
//        hopper_box.addBox(0, 0, 0, 4, 10, 4);
//        hopper_box.setRotationPoint(38-32+hp_rel[0], 29-48+hp_rel[1], 26-16+hp_rel[2]);
//        hopper_box.rotateAngleY = (float) Math.PI/2;
//        base.addChild(hopper_box);
        int[] hps = {422,105};
        int[] hpsdim = {16,2,2};
        ModelRenderer hopper_support = new ModelRenderer(this,hps[0],hps[1]);
        hopper_support.addBox(0, 0, 0, hpsdim[0], hpsdim[1], hpsdim[2]);
        hopper_support.setRotationPoint(34, 32, 30);
        hopper_support.rotateAngleZ = (float) Math.PI/2;
        base.addChild(hopper_support);

        ModelRenderer hopper_support2 = new ModelRenderer(this,hps[0],hps[1]);
        hopper_support2.addBox(0, 0, 0, hpsdim[0], hpsdim[1], hpsdim[2]);
        hopper_support2.setRotationPoint(48, 32, 30);
        hopper_support2.rotateAngleZ = (float) Math.PI/2;
        base.addChild(hopper_support2);

        ModelRenderer hopper_support3 = new ModelRenderer(this,hps[0],hps[1]);
        hopper_support3.addBox(0, 0, 0, hpsdim[0], hpsdim[1], hpsdim[2]);
        hopper_support3.setRotationPoint(34, 32, 16);
        hopper_support3.rotateAngleZ = (float) Math.PI/2;
        base.addChild(hopper_support3);

        ModelRenderer hopper_support4 = new ModelRenderer(this,hps[0],hps[1]);
        hopper_support4.addBox(0, 0, 0, hpsdim[0], hpsdim[1], hpsdim[2]);
        hopper_support4.setRotationPoint(48, 32, 16);
        hopper_support4.rotateAngleZ = (float) Math.PI/2;
        base.addChild(hopper_support4);

        int[] hpb = {374,117};
        ModelRenderer hopper_beam = new ModelRenderer(this,hpb[0],hpb[1]);
        hopper_beam.addBox(0, 0, 0, 12, 8, 1);
        hopper_beam.setRotationPoint(47, 34, 30);
        hopper_beam.rotateAngleY = (float) Math.PI/2;
        base.addChild(hopper_beam);

        ModelRenderer hopper_beam2 = new ModelRenderer(this,hpb[0],hpb[1]);
        hopper_beam2.addBox(0, 0, 0, 12, 8, 1);
        hopper_beam2.setRotationPoint(32, 34, 30);
        hopper_beam2.rotateAngleY = (float) Math.PI/2;
        base.addChild(hopper_beam2);

        ModelRenderer hopper_beam3 = new ModelRenderer(this,hpb[0],hpb[1]);
        hopper_beam3.addBox(0, 0, 0, 12, 8, 1);
        hopper_beam3.setRotationPoint(34, 34, 16);
        base.addChild(hopper_beam3);

        ModelRenderer hopper_beam4 = new ModelRenderer(this,hpb[0],hpb[1]);
        hopper_beam4.addBox(0, 0, 0, 12, 8, 1);
        hopper_beam4.setRotationPoint(34, 34, 31);
        base.addChild(hopper_beam4);


        baseatt.addChild(hopper_support_parent);



        /*ModelRenderer chimney2 = new ModelRenderer(this, 208, 10);
        chimney2.addBox(0, 0, 0, 32, 11, 11);
        chimney2.setRotationPoint(64-16-2.5F, 30, 30);
        chimney2.rotateAngleY = (float) 0;
        chimney2.rotateAngleZ = (float) Math.PI / 2;
        this.base.addChild(chimney2);*/

//        ModelRenderer pipe = new ModelRenderer(this, 266, 0);
//        pipe.addBox(0, 0, 0, 24, 5, 5);
//        pipe.setRotationPoint(52-14.5F, 43, 49.5F-8);
//        pipe.rotateAngleY = (float) Math.PI / 2;
//        this.base.addChild(pipe);
//
//        ModelRenderer pipe2 = new ModelRenderer(this, 266, 0);
//        pipe2.addBox(0, 0, 0, 10, 5, 5);
//        pipe2.setRotationPoint(52-9.5F, 33, 28-10.5F);
//        pipe2.rotateAngleZ = (float) Math.PI/2;
//        this.base.addChild(pipe2);
//
//        ModelRenderer pipe3 = new ModelRenderer(this, 266, 0);
//        pipe3.addBox(0, 0, 0, 20, 5, 5);
//        pipe3.setRotationPoint(52-9.5F, 43, 24-2.5F);
//        this.base.addChild(pipe3);
//
//        ModelRenderer pipe4 = new ModelRenderer(this, 266, 0);
//        pipe4.addBox(0, 0, 0, 19, 5, 5);
//        pipe4.setRotationPoint(62.5F, 24, 24-2.5F);
//        pipe4.rotateAngleZ = (float) Math.PI/2;
//        this.base.addChild(pipe4);



        TexturedQuad[] barrel = new TexturedQuad[8];
        float radius = 15/(float)Math.cos(Math.PI/8);
        //System.out.println(baseatt.textureWidth);
        for (int i = 0; i < 8; i++) {
            float a = (float)(radius*Math.cos(2*Math.PI*((double)i+0.5)/8));
            float b = (float)(radius*Math.cos(2*Math.PI*((double)i+1.5)/8));
            float a2 = (float)(radius*Math.sin(2*Math.PI*((double)i+0.5)/8));
            float b2 = (float)(radius*Math.sin(2*Math.PI*((double)i+1.5)/8));
            PositionTextureVertex ptv1 = new PositionTextureVertex(a,a2,0,279,64);
            PositionTextureVertex ptv2 = new PositionTextureVertex(b,b2,0,279,64+32);
            PositionTextureVertex ptv3 = new PositionTextureVertex(a,a2,32,279+32,64);
            PositionTextureVertex ptv4 = new PositionTextureVertex(b,b2,32,279+32,64+32);
            barrel[i] = new TexturedQuad(new PositionTextureVertex[] {ptv4,ptv3,ptv1,ptv2},279,64,279+32,87,baseatt.textureWidth, baseatt.textureHeight);

        }
        PositionTextureVertex[] barrel_side = new PositionTextureVertex[48];
        int[] bc = {474,61};
        for (int i = 0; i < 8; i++) {
            float a = (float)(radius*Math.cos(2*Math.PI*((double)i+0.5)/8));
            float b = (float)(radius*Math.cos(2*Math.PI*((double)i+1.5)/8));
            float a2 = (float)(radius*Math.sin(2*Math.PI*((double)i+0.5)/8));
            float b2 = (float)(radius*Math.sin(2*Math.PI*((double)i+1.5)/8));
            PositionTextureVertex ptv1 = new PositionTextureVertex(a,a2,0,(bc[0]+a),(bc[1]+a2));
            PositionTextureVertex ptv2 = new PositionTextureVertex(b,b2,0,(bc[0]+b),(bc[1]+b2));
            PositionTextureVertex ptv3 = new PositionTextureVertex(0,0,0,bc[0],bc[1]);
            barrel_side[6*i] = ptv1;
            barrel_side[6*i+1] = ptv3;
            barrel_side[6*i+2] = ptv2;
            PositionTextureVertex ptv4 = new PositionTextureVertex(a,a2,32,(bc[0]+a),(bc[1]+a2));
            PositionTextureVertex ptv5 = new PositionTextureVertex(b,b2,32,(bc[0]+b),(bc[1]+b2));
            PositionTextureVertex ptv6 = new PositionTextureVertex(0,0,32,bc[0],bc[1]);
            barrel_side[6*i+3] = ptv4;
            barrel_side[6*i+4] = ptv5;
            barrel_side[6*i+5] = ptv6;

        }

        barrel_parent = new ModelRendererATT(this);
        barrel_parent.setRotationPoint(24,24,24);
        barrel_parent.rotateAngleY = (float)Math.PI/2;
        //System.out.println("barrel_mg start");
        ModelGeneric barrel_mg = new ModelGeneric(baseatt,barrel);
        ModelGeneric barrel_side_mg = new ModelGeneric(baseatt,barrel_side);
        barrel_parent.addModel(barrel_mg);
        barrel_parent.addModel(barrel_side_mg);
        baseatt.addChild(barrel_parent);
        //System.out.println("barrel_mg end");

        this.test = new ModelRendererATT(this);
        PositionTextureVertex[] test3 = {
                new PositionTextureVertex(0,80,0,0,0),
                new PositionTextureVertex(0,80,80,0,100),
                new PositionTextureVertex(80,80,0,100,0),
                new PositionTextureVertex(0,80,80,0,100),
                new PositionTextureVertex(80,80,0,100,0),
                new PositionTextureVertex(80,80,80,100,100),

                /*new PositionTextureVertex(0,80,0,374,85),
                new PositionTextureVertex(16,72,16,374+16,85+16),
                new PositionTextureVertex(32,80,0,374+32,85),

                new PositionTextureVertex(32,80,0,374+32,85),
                new PositionTextureVertex(16,72,16,374+16,85+16),
                new PositionTextureVertex(32,80,32,374+32,85+32),

                new PositionTextureVertex(32,80,32,374+32,85+32),
                new PositionTextureVertex(16,72,16,374+16,85+16),
                new PositionTextureVertex(0,80,32,374,85+32),

                new PositionTextureVertex(0,80,32,374,85+32),
                new PositionTextureVertex(16,72,16,374+16,85+16),
                new PositionTextureVertex(0,80,0,374,85),*/
        };
        ModelGeneric test2 = new ModelGeneric(test,test3);
        ModelPyramid test4 = new ModelPyramid(test,374,85,0,0,0,32,8,32,true);
        test.addModel(test2);
        test.addModel(test4);


    }
    @Override
    public void render(Entity entity, float f0, float f1, float f2, float f3, float f4, float f5)
    {
        // @ ModelRenderer.compileDisplayList :: unlocking the secrets of rendering

        this.base.render(f5);
        this.baseatt.render(f5);
        //this.test.render(f5);

    }
}
