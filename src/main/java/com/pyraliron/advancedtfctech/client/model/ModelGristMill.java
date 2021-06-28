package com.pyraliron.advancedtfctech.client.model;

import net.dries007.tfc.api.types.Metal;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.PositionTextureVertex;
import net.minecraft.client.model.TexturedQuad;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

import javax.xml.soap.Text;

public class ModelGristMill extends ModelBase {
    ModelRendererATT baseatt;
    ModelRenderer base;
    ModelRendererATT roter_rim_parent;
    ModelRenderer roter_driver;
    ModelRenderer[] roterspokes = new ModelRenderer[8];
    public boolean mirror;
    public ModelGristMill(boolean mirror)
    {
        this.textureWidth = 512;
        this.textureHeight = 128;
        this.mirror = mirror;

        refresh();
    }
    public void refresh() {
        this.base = new ModelRenderer(this, "base");
        this.baseatt = new ModelRendererATT(this);

        if (this.mirror) {
            ModelRenderer base1 = new ModelRenderer(this, 0, 0);
            base1.addBox(0, 0, 0, 16 * 4, 8, 16 * 3, 0);
            base1.setRotationPoint(32 + 64 - 80, 0, 64);
            base1.rotateAngleY = (float) Math.PI / 2;
            base.addChild(base1);
            base.setRotationPoint(-16, 0, -16);

            ModelRenderer grinder = new ModelRenderer(this, 0, 56);
            grinder.addBox(0, 0, 0, 32, 24, 16);
            grinder.setRotationPoint(32 + 64 - 80 + 8, 8, 16);
            base.addChild(grinder);


            ModelRenderer output = new ModelRenderer(this, 96, 71);
            output.addBox(0, 0, 0, 16, 8, 8);
            output.setRotationPoint(32 + 64 - 72, 8, 16);
            output.rotateAngleY = (float) -Math.PI / 2;
            base.addChild(output);

            ModelRenderer power2 = new ModelRenderer(this, 56, 96);
            power2.addBox(0, 0, 0, 16, 12, 16);
            power2.setRotationPoint(32 + 64 - 80 + 16, 8, 48);
            base.addChild(power2);

            ModelRenderer power3 = new ModelRenderer(this, 194, 100);
            power3.addBox(0, 0, 0, 15, 8, 14);
            power3.setRotationPoint(32 + 64 - 80 + 16 + 0.5F, 20, 49);
            base.addChild(power3);

            ModelRenderer power4 = new ModelRenderer(this, 194, 56);
            power4.addBox(0, 0, 0, 6, 6, 1);
            power4.setRotationPoint(32 + 64 - 80 + 21, 21, 63);
            base.addChild(power4);

            ModelRenderer fan = new ModelRenderer(this, 56, 96);
            fan.addBox(0, 0, 0, 16, 12, 16);
            fan.setRotationPoint(32 + 64 - 80 + 16, 8, 0);
            base.addChild(fan);

            ModelRenderer fan2 = new ModelRenderer(this, 194, 56);
            fan2.addBox(0, 0, 0, 15, 8, 14, true);
            fan2.setRotationPoint(32 + 64 - 80 + 16 + 0.5F+15, 20, 15);
            fan2.rotateAngleY = (float)Math.PI;
            base.addChild(fan2);

            ModelRenderer redstone_support = new ModelRenderer(this, 0, 24);
            redstone_support.addBox(0, 0, 0, 4, 10, 2);
            redstone_support.setRotationPoint(28 + 30, 8, 28);
            //redstone_support.rotateAngleY = (float) Math.PI / 2;
            this.base.addChild(redstone_support);

            ModelRenderer redstone_support2 = new ModelRenderer(this, 0, 24);
            redstone_support2.addBox(0, 0, 0, 4, 10, 2);
            redstone_support2.setRotationPoint(28 + 30, 8, 18);
            //redstone_support2.rotateAngleY = (float) Math.PI / 2;
            this.base.addChild(redstone_support2);

            ModelRenderer redstone = new ModelRenderer(this, 0, 0);
            redstone.addBox(0, 0, 0, 16, 16, 8);
            redstone.setRotationPoint(64, 16, 16);
            redstone.rotateAngleY = (float) -Math.PI / 2;
            this.base.addChild(redstone);

            ModelRenderer roter_support = new ModelRenderer(this, 0, 96);
            roter_support.addBox(0, 0, 0, 32, 12, 4);
            roter_support.setRotationPoint(32 + 64 - 80 + 8, 8-0.0625F, 49);
            base.addChild(roter_support);

            ModelRenderer roter_support2 = new ModelRenderer(this, 0, 96);
            roter_support2.addBox(0, 0, 0, 32, 12, 4);
            roter_support2.setRotationPoint(32 + 64 - 80 + 8, 8, 32);
            base.addChild(roter_support2);

            ModelRenderer roter_support3 = new ModelRenderer(this, 0, 112);
            roter_support3.addBox(0, 0, 0, 13, 12, 4);
            roter_support3.setRotationPoint(32 + 64 - 80 + 11.5F, 8, 49);
            roter_support3.rotateAngleY = (float) Math.PI / 2;
            base.addChild(roter_support3);

            ModelRenderer roter_support4 = new ModelRenderer(this, 0, 112);
            roter_support4.addBox(0, 0, 0, 13, 12, 4);
            roter_support4.setRotationPoint(32 + 64 - 80 + 32.5F, 8, 49);
            roter_support4.rotateAngleY = (float) Math.PI / 2;
            base.addChild(roter_support4);


            roter_driver = new ModelRenderer(this, 80, 56);
            roter_driver.addBox(0, -1, -1, 18, 2, 2);
            roter_driver.setRotationPoint(40, 22, 50);
            roter_driver.rotateAngleY = (float) Math.PI / 2;
            base.addChild(roter_driver);

            ModelRenderer roter_driver2 = new ModelRenderer(this, 80, 56);
            roter_driver2.addBox(0, 0, 0, 4, 2, 2);
            roter_driver2.setRotationPoint(39, 21, 18);
            roter_driver2.rotateAngleY = (float) Math.PI / 2;
            base.addChild(roter_driver2);
            for (int i = 0; i < 8; i++) {
                ModelRenderer roter_spoke = new ModelRenderer(this, 80, 56);
                roter_spoke.addBox(-0.5F, -0.5F, -0.5F, 6, 1, 1);
                roter_spoke.setRotationPoint(40, 22F, 40.5F + 1.5F);
                roter_spoke.rotateAngleZ = (float) Math.PI * 2 * i / 8;
                base.addChild(roter_spoke);
                roterspokes[i] = roter_spoke;
            }


            ModelPyramid hopper = new ModelPyramid(baseatt, 382, 93, 16, 48, 0, 16, -12, 16, false);
            baseatt.addModel(hopper);
            ModelPyramid hopper2 = new ModelPyramid(baseatt, 383, 94, 17, 48, 1, 14, -11, 14, true);
            baseatt.addModel(hopper2);

            PositionTextureVertex[] hopper_rim_ptv = {
                    new PositionTextureVertex(16, 48, 0, 424, 85),
                    new PositionTextureVertex(16, 48, 16, 424, 85 + 16),
                    new PositionTextureVertex(32, 48, 0, 424 + 16, 85),
                    new PositionTextureVertex(16, 48, 16, 424, 85 + 16),
                    new PositionTextureVertex(32, 48, 16, 424 + 16, 85 + 16),
                    new PositionTextureVertex(32, 48, 0, 424 + 16, 85),
            };
            ModelGeneric hopper_rim = new ModelGeneric(baseatt, hopper_rim_ptv);
            baseatt.addModel(hopper_rim);

            ModelRenderer hopper_box = new ModelRenderer(this, 406, 101);
            hopper_box.addBox(0, 0, 0, 4, 10, 4);
            hopper_box.setRotationPoint(38, 29, 26);
            hopper_box.rotateAngleY = (float) Math.PI / 2;
            base.addChild(hopper_box);

            ModelRenderer hopper_support = new ModelRenderer(this, 80, 67);
            hopper_support.addBox(0, 0, 0, 16, 2, 2);
            hopper_support.setRotationPoint(34, 32, 30);
            hopper_support.rotateAngleZ = (float) Math.PI / 2;
            base.addChild(hopper_support);

            ModelRenderer hopper_support2 = new ModelRenderer(this, 80, 67);
            hopper_support2.addBox(0, 0, 0, 16, 2, 2);
            hopper_support2.setRotationPoint(48, 32, 30);
            hopper_support2.rotateAngleZ = (float) Math.PI / 2;
            base.addChild(hopper_support2);

            ModelRenderer hopper_support3 = new ModelRenderer(this, 80, 67);
            hopper_support3.addBox(0, 0, 0, 16, 2, 2);
            hopper_support3.setRotationPoint(34, 32, 16);
            hopper_support3.rotateAngleZ = (float) Math.PI / 2;
            base.addChild(hopper_support3);

            ModelRenderer hopper_support4 = new ModelRenderer(this, 80, 67);
            hopper_support4.addBox(0, 0, 0, 16, 2, 2);
            hopper_support4.setRotationPoint(48, 32, 16);
            hopper_support4.rotateAngleZ = (float) Math.PI / 2;
            base.addChild(hopper_support4);

            ModelRenderer hopper_beam = new ModelRenderer(this, 182, 78);
            hopper_beam.addBox(0, 0, 0, 12, 8, 1);
            hopper_beam.setRotationPoint(47, 34, 30);
            hopper_beam.rotateAngleY = (float) Math.PI / 2;
            base.addChild(hopper_beam);

            ModelRenderer hopper_beam2 = new ModelRenderer(this, 182, 78);
            hopper_beam2.addBox(0, 0, 0, 12, 8, 1);
            hopper_beam2.setRotationPoint(32, 34, 30);
            hopper_beam2.rotateAngleY = (float) Math.PI / 2;
            base.addChild(hopper_beam2);

            ModelRenderer hopper_beam3 = new ModelRenderer(this, 182, 78);
            hopper_beam3.addBox(0, 0, 0, 12, 8, 1);
            hopper_beam3.setRotationPoint(34, 34, 16);
            base.addChild(hopper_beam3);

            ModelRenderer hopper_beam4 = new ModelRenderer(this, 182, 78);
            hopper_beam4.addBox(0, 0, 0, 12, 8, 1);
            hopper_beam4.setRotationPoint(34, 34, 31);
            base.addChild(hopper_beam4);

            ModelRendererATT hopper_support_parent = new ModelRendererATT(this);
            hopper_support_parent.setRotationPoint(32, 38, 4);
            //ModelBoxSlanted hopper_support5 = new ModelBoxSlanted(this.baseatt, 80, 56, 0, 0, 0, 3, 1, 2, new Vec3d(3, -2, 0), new Vec3d(0, 1, 0), new Vec3d(0, 0, 2));
            //hopper_support_parent.addModel(hopper_support5);
            hopper_support_parent.rotateAngleZ = (float) Math.PI / 2;
            baseatt.addChild(hopper_support_parent);

            TexturedQuad[] roter_rim = new TexturedQuad[32];
            int radius = 7;
            int radius2 = 6;
            for (int i = 0; i < 8; i++) {
                PositionTextureVertex ptv1 = new PositionTextureVertex((float) (radius * Math.cos(2 * Math.PI * i / 8)), (float) (radius * Math.sin(2 * Math.PI * i / 8)), 0, 80, 56);
                PositionTextureVertex ptv2 = new PositionTextureVertex((float) (radius * Math.cos(2 * Math.PI * (i + 1) / 8)), (float) (radius * Math.sin(2 * Math.PI * (i + 1) / 8)), 0, 87, 56);
                PositionTextureVertex ptv3 = new PositionTextureVertex((float) (radius * Math.cos(2 * Math.PI * i / 8)), (float) (radius * Math.sin(2 * Math.PI * i / 8)), 5, 80, 60);
                PositionTextureVertex ptv4 = new PositionTextureVertex((float) (radius * Math.cos(2 * Math.PI * (i + 1) / 8)), (float) (radius * Math.sin(2 * Math.PI * (i + 1) / 8)), 5, 87, 60);
                PositionTextureVertex ptv5 = new PositionTextureVertex((float) (radius2 * Math.cos(2 * Math.PI * i / 8)), (float) (radius2 * Math.sin(2 * Math.PI * i / 8)), 0, 80, 60);
                PositionTextureVertex ptv6 = new PositionTextureVertex((float) (radius2 * Math.cos(2 * Math.PI * (i + 1) / 8)), (float) (radius2 * Math.sin(2 * Math.PI * (i + 1) / 8)), 0, 80, 100);
                PositionTextureVertex ptv7 = new PositionTextureVertex((float) (radius2 * Math.cos(2 * Math.PI * i / 8)), (float) (radius2 * Math.sin(2 * Math.PI * i / 8)), 5, 100, 100);
                PositionTextureVertex ptv8 = new PositionTextureVertex((float) (radius2 * Math.cos(2 * Math.PI * (i + 1) / 8)), (float) (radius2 * Math.sin(2 * Math.PI * (i + 1) / 8)), 5, 100, 100);
                roter_rim[4 * i] = new TexturedQuad(new PositionTextureVertex[]{ptv4, ptv3, ptv1, ptv2}, 80, 56, 87, 61, baseatt.textureWidth, baseatt.textureHeight);
                roter_rim[4 * i + 1] = new TexturedQuad(new PositionTextureVertex[]{ptv2, ptv1, ptv5, ptv6}, 80, 56, 87, 57, baseatt.textureWidth, baseatt.textureHeight);
                roter_rim[4 * i + 2] = new TexturedQuad(new PositionTextureVertex[]{ptv6, ptv5, ptv7, ptv8}, 80, 56, 87, 61, baseatt.textureWidth, baseatt.textureHeight);
                roter_rim[4 * i + 3] = new TexturedQuad(new PositionTextureVertex[]{ptv8, ptv7, ptv3, ptv4}, 80, 56, 87, 57, baseatt.textureWidth, baseatt.textureHeight);
            }
            roter_rim_parent = new ModelRendererATT(this);
            roter_rim_parent.setRotationPoint(24, 22, 22 + 1.5F);
            ModelGeneric roter_rim_mg = new ModelGeneric(baseatt, roter_rim);
            roter_rim_parent.addModel(roter_rim_mg);
            baseatt.addChild(roter_rim_parent);
        } else {

            ModelRenderer base1 = new ModelRenderer(this, 0, 0);
            base1.addBox(0, 0, 0, 16 * 4, 8, 16 * 3,true);
            base1.setRotationPoint(16, 0, 64);
            base1.rotateAngleY = (float) Math.PI / 2;
            base.addChild(base1);
            base.setRotationPoint(-16, 0, 0);

            ModelRenderer grinder = new ModelRenderer(this, 0, 56);
            grinder.addBox(0, 0, 0, 32, 24, 16);
            grinder.setRotationPoint(24, 8, 32);
            base.addChild(grinder);


            ModelRenderer output = new ModelRenderer(this, 96, 71);
            output.addBox(0, 0, 0, 16, 8, 8,true);
            output.setRotationPoint(24, 8, 32);
            output.rotateAngleY = (float) -Math.PI / 2;
            base.addChild(output);

            ModelRenderer power2 = new ModelRenderer(this, 56, 96);
            power2.addBox(0, 0, 0, 16, 12, 16);
            power2.setRotationPoint(32, 8, 0);
            base.addChild(power2);

            ModelRenderer power3 = new ModelRenderer(this, 194, 100);
            power3.addBox(0, 0, 0, 15, 8, 14);
            power3.setRotationPoint(32 + 0.5F, 20, 1);
            base.addChild(power3);

            ModelRenderer power4 = new ModelRenderer(this, 194, 56);
            power4.addBox(0, 0, 0, 6, 6, 1);
            power4.setRotationPoint(37, 21, 0);
            base.addChild(power4);

            ModelRenderer fan = new ModelRenderer(this, 56, 96);
            fan.addBox(0, 0, 0, 16, 12, 16);
            fan.setRotationPoint(32, 8, 48);
            base.addChild(fan);

            ModelRenderer fan2 = new ModelRenderer(this, 194, 56);
            fan2.addBox(0, 0, 0, 15, 8, 14);
            fan2.setRotationPoint(32 + 0.5F, 20, 49);
            base.addChild(fan2);

            ModelRenderer redstone_support = new ModelRenderer(this, 0, 24);
            redstone_support.addBox(0, 0, 0, 4, 10, 2);
            redstone_support.setRotationPoint(28 + 30, 8, 32+12);
            //redstone_support.rotateAngleY = (float) Math.PI / 2;
            this.base.addChild(redstone_support);

            ModelRenderer redstone_support2 = new ModelRenderer(this, 0, 24);
            redstone_support2.addBox(0, 0, 0, 4, 10, 2);
            redstone_support2.setRotationPoint(28 + 30, 8, 32+2);
            //redstone_support2.rotateAngleY = (float) Math.PI / 2;
            this.base.addChild(redstone_support2);

            ModelRenderer redstone = new ModelRenderer(this, 0, 0);
            redstone.addBox(0, 0, 0, 16, 16, 8);
            redstone.setRotationPoint(64, 16, 32);
            redstone.rotateAngleY = (float) -Math.PI / 2;
            this.base.addChild(redstone);

            ModelRendererATT hopper_parent = new ModelRendererATT(this);
            hopper_parent.setRotationPoint(0,0,32);
            baseatt.addChild(hopper_parent);
            ModelPyramid hopper = new ModelPyramid(baseatt, 382, 93, 16, 48, 0, 16, -12, 16, false);
            hopper_parent.addModel(hopper);
            ModelPyramid hopper2 = new ModelPyramid(baseatt, 383, 94, 17, 48, 1, 14, -11, 14, true);
            hopper_parent.addModel(hopper2);

            PositionTextureVertex[] hopper_rim_ptv = {
                    new PositionTextureVertex(16, 48, 0, 424, 85),
                    new PositionTextureVertex(16, 48, 16, 424, 85 + 16),
                    new PositionTextureVertex(32, 48, 0, 424 + 16, 85),
                    new PositionTextureVertex(16, 48, 16, 424, 85 + 16),
                    new PositionTextureVertex(32, 48, 16, 424 + 16, 85 + 16),
                    new PositionTextureVertex(32, 48, 0, 424 + 16, 85),
            };
            ModelGeneric hopper_rim = new ModelGeneric(baseatt, hopper_rim_ptv);
            hopper_parent.addModel(hopper_rim);

            ModelRenderer hopper_box = new ModelRenderer(this, 406, 101);
            hopper_box.addBox(0, 0, 0, 4, 10, 4);
            hopper_box.setRotationPoint(38, 29, 26+16);
            hopper_box.rotateAngleY = (float) Math.PI / 2;
            base.addChild(hopper_box);

            ModelRenderer hopper_support = new ModelRenderer(this, 80, 67);
            hopper_support.addBox(0, 0, 0, 16, 2, 2);
            hopper_support.setRotationPoint(34, 32, 30+16);
            hopper_support.rotateAngleZ = (float) Math.PI / 2;
            base.addChild(hopper_support);

            ModelRenderer hopper_support2 = new ModelRenderer(this, 80, 67);
            hopper_support2.addBox(0, 0, 0, 16, 2, 2);
            hopper_support2.setRotationPoint(48, 32, 30+16);
            hopper_support2.rotateAngleZ = (float) Math.PI / 2;
            base.addChild(hopper_support2);

            ModelRenderer hopper_support3 = new ModelRenderer(this, 80, 67);
            hopper_support3.addBox(0, 0, 0, 16, 2, 2);
            hopper_support3.setRotationPoint(34, 32, 16+16);
            hopper_support3.rotateAngleZ = (float) Math.PI / 2;
            base.addChild(hopper_support3);

            ModelRenderer hopper_support4 = new ModelRenderer(this, 80, 67);
            hopper_support4.addBox(0, 0, 0, 16, 2, 2);
            hopper_support4.setRotationPoint(48, 32, 16+16);
            hopper_support4.rotateAngleZ = (float) Math.PI / 2;
            base.addChild(hopper_support4);

            ModelRenderer hopper_beam = new ModelRenderer(this, 182, 78);
            hopper_beam.addBox(0, 0, 0, 12, 8, 1);
            hopper_beam.setRotationPoint(47, 34, 30+16);
            hopper_beam.rotateAngleY = (float) Math.PI / 2;
            base.addChild(hopper_beam);

            ModelRenderer hopper_beam2 = new ModelRenderer(this, 182, 78);
            hopper_beam2.addBox(0, 0, 0, 12, 8, 1);
            hopper_beam2.setRotationPoint(32, 34, 30+16);
            hopper_beam2.rotateAngleY = (float) Math.PI / 2;
            base.addChild(hopper_beam2);

            ModelRenderer hopper_beam3 = new ModelRenderer(this, 182, 78);
            hopper_beam3.addBox(0, 0, 0, 12, 8, 1);
            hopper_beam3.setRotationPoint(34, 34, 16+16);
            base.addChild(hopper_beam3);

            ModelRenderer hopper_beam4 = new ModelRenderer(this, 182, 78);
            hopper_beam4.addBox(0, 0, 0, 12, 8, 1);
            hopper_beam4.setRotationPoint(34, 34, 31+16);
            base.addChild(hopper_beam4);

            ModelRenderer roter_support = new ModelRenderer(this, 0, 96);
            roter_support.addBox(0, 0, 0, 32, 12, 4);
            roter_support.setRotationPoint(32 + 64 - 80 + 8, 8, 28);
            base.addChild(roter_support);

            ModelRenderer roter_support2 = new ModelRenderer(this, 0, 96);
            roter_support2.addBox(0, 0, 0, 32, 12, 4);
            roter_support2.setRotationPoint(32 + 64 - 80 + 8, 8-0.0625F, 11);
            base.addChild(roter_support2);

            ModelRenderer roter_support3 = new ModelRenderer(this, 0, 112);
            roter_support3.addBox(0, 0, 0, 13, 12, 4);
            roter_support3.setRotationPoint(32 + 64 - 80 + 11.5F, 8, 28);
            roter_support3.rotateAngleY = (float) Math.PI / 2;
            base.addChild(roter_support3);

            ModelRenderer roter_support4 = new ModelRenderer(this, 0, 112);
            roter_support4.addBox(0, 0, 0, 13, 12, 4);
            roter_support4.setRotationPoint(32 + 64 - 80 + 32.5F, 8, 28);
            roter_support4.rotateAngleY = (float) Math.PI / 2;
            base.addChild(roter_support4);

            roter_driver = new ModelRenderer(this, 80, 56);
            roter_driver.addBox(0, -1, -1, 18, 2, 2);
            roter_driver.setRotationPoint(40, 22, 49-16);
            roter_driver.rotateAngleY = (float) Math.PI / 2;
            base.addChild(roter_driver);

            ModelRenderer roter_driver2 = new ModelRenderer(this, 80, 56);
            roter_driver2.addBox(0, 0, 0, 4, 2, 2);
            roter_driver2.setRotationPoint(39, 21, 18+32);
            roter_driver2.rotateAngleY = (float) Math.PI / 2;
            base.addChild(roter_driver2);
            for (int i = 0; i < 8; i++) {
                ModelRenderer roter_spoke = new ModelRenderer(this, 80, 56);
                roter_spoke.addBox(-0.5F, -0.5F, -0.5F, 6, 1, 1);
                roter_spoke.setRotationPoint(40, 22F, 40.5F + 1.5F-20);
                roter_spoke.rotateAngleZ = (float) Math.PI * 2 * i / 8;
                base.addChild(roter_spoke);
                roterspokes[i] = roter_spoke;
            }

            TexturedQuad[] roter_rim = new TexturedQuad[32];
            int radius = 7;
            int radius2 = 6;
            for (int i = 0; i < 8; i++) {
                PositionTextureVertex ptv1 = new PositionTextureVertex((float) (radius * Math.cos(2 * Math.PI * i / 8)), (float) (radius * Math.sin(2 * Math.PI * i / 8)), 0, 80, 56);
                PositionTextureVertex ptv2 = new PositionTextureVertex((float) (radius * Math.cos(2 * Math.PI * (i + 1) / 8)), (float) (radius * Math.sin(2 * Math.PI * (i + 1) / 8)), 0, 87, 56);
                PositionTextureVertex ptv3 = new PositionTextureVertex((float) (radius * Math.cos(2 * Math.PI * i / 8)), (float) (radius * Math.sin(2 * Math.PI * i / 8)), 5, 80, 60);
                PositionTextureVertex ptv4 = new PositionTextureVertex((float) (radius * Math.cos(2 * Math.PI * (i + 1) / 8)), (float) (radius * Math.sin(2 * Math.PI * (i + 1) / 8)), 5, 87, 60);
                PositionTextureVertex ptv5 = new PositionTextureVertex((float) (radius2 * Math.cos(2 * Math.PI * i / 8)), (float) (radius2 * Math.sin(2 * Math.PI * i / 8)), 0, 80, 60);
                PositionTextureVertex ptv6 = new PositionTextureVertex((float) (radius2 * Math.cos(2 * Math.PI * (i + 1) / 8)), (float) (radius2 * Math.sin(2 * Math.PI * (i + 1) / 8)), 0, 80, 100);
                PositionTextureVertex ptv7 = new PositionTextureVertex((float) (radius2 * Math.cos(2 * Math.PI * i / 8)), (float) (radius2 * Math.sin(2 * Math.PI * i / 8)), 5, 100, 100);
                PositionTextureVertex ptv8 = new PositionTextureVertex((float) (radius2 * Math.cos(2 * Math.PI * (i + 1) / 8)), (float) (radius2 * Math.sin(2 * Math.PI * (i + 1) / 8)), 5, 100, 100);
                roter_rim[4 * i] = new TexturedQuad(new PositionTextureVertex[]{ptv4, ptv3, ptv1, ptv2}, 80, 56, 87, 61, baseatt.textureWidth, baseatt.textureHeight);
                roter_rim[4 * i + 1] = new TexturedQuad(new PositionTextureVertex[]{ptv2, ptv1, ptv5, ptv6}, 80, 56, 87, 57, baseatt.textureWidth, baseatt.textureHeight);
                roter_rim[4 * i + 2] = new TexturedQuad(new PositionTextureVertex[]{ptv6, ptv5, ptv7, ptv8}, 80, 56, 87, 61, baseatt.textureWidth, baseatt.textureHeight);
                roter_rim[4 * i + 3] = new TexturedQuad(new PositionTextureVertex[]{ptv8, ptv7, ptv3, ptv4}, 80, 56, 87, 57, baseatt.textureWidth, baseatt.textureHeight);
            }
            roter_rim_parent = new ModelRendererATT(this);
            roter_rim_parent.setRotationPoint(24, 22, 22 + 1.5F-4);
            ModelGeneric roter_rim_mg = new ModelGeneric(baseatt, roter_rim);
            roter_rim_parent.addModel(roter_rim_mg);
            baseatt.addChild(roter_rim_parent);
        }

    }
    @Override
    public void render(Entity entity, float f0, float f1, float f2, float f3, float f4, float f5)
    {
        // @ ModelRenderer.compileDisplayList :: unlocking the secrets of rendering
        roter_rim_parent.rotateAngleZ = (float)(f4*2*Math.PI*2);
        for (int i = 0; i < 8; i++) {
            roterspokes[i].rotateAngleZ = (float)Math.PI*2*i/8 + (float)(f4*2*Math.PI*2);
        }
        roter_driver.rotateAngleZ = (float)(f4*2*Math.PI*2);

        this.baseatt.render(f5);
        this.base.render(f5);

    }
}
