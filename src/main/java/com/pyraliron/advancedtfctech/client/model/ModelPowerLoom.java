package com.pyraliron.advancedtfctech.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBed;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraftforge.client.model.ModelLoader;

import javax.jws.WebParam;

public class ModelPowerLoom extends ModelBase
{
    public ModelRenderer base;
    public ModelRenderer pirn_loader;
    public ModelRenderer cloth_beam;
    public ModelRenderer cloth_roller;
    public ModelRenderer warp_roller;
    public ModelRenderer warp_beam;
    public ModelRenderer beam_cap;
    public ModelRenderer beam_cap2;
    public ModelRenderer warp_roll;
    public ModelRenderer[] warp_stages = new ModelRenderer[5];
    public ModelRenderer[] cloth_stages = new ModelRenderer[5];
    public ModelRenderer threads;
    public ModelRenderer threads_warp_lower;
    public ModelRenderer threads_cloth_lower;
    public ModelRenderer threads_warp_higher;
    public ModelRenderer shuttle_beam_connectors;
    public ModelRenderer shuttle_beam_super;
    public ModelRenderer split_warp;
    public ModelRenderer pirn_loader_base;
    public ModelRenderer splitter_frame_shuttle;
    public ModelRenderer sudo_warp;
    public ModelRenderer sudo_cloth;
    public ModelRenderer sudo_cloth_unfinished;
    public ModelRenderer pirn_pile;
    public ModelRenderer split_warp_left;
    public ModelRenderer split_warp_right;

    public boolean mirror;
    public boolean isTicking;
    public float ticks = 0;
    public int pirn_rotation = 0;
    public int sub_rotation = 0;
    public boolean has_rotated = false;
    public float empty_pirn_count = 0;

    public ModelPowerLoom(boolean mirror)
    {
        this.textureWidth = 512;
        this.textureHeight = 128;
        this.mirror = mirror;

        refresh();
    }

    public void refresh()
    {
        this.base = new ModelRenderer(this, 0, 0);
        this.base.addBox(32, 0, 0, 16 * 3, 4, 16 * 3);

        ModelRenderer base2 = new ModelRenderer(this,16,54+16);
        base2.addBox(0,0,0,16*2,4,16);
        base2.setRotationPoint(16,0,32);
        base2.rotateAngleY = (float)Math.PI/2;
        this.base.addChild(base2);

        ModelRenderer base3 = new ModelRenderer(this,99,81);
        base3.addBox(35,0,11,42,16,28);
        this.base.addChild(base3);

        ModelRenderer base4 = new ModelRenderer(this,414,95);
        base4.addBox(0,0,0,32,12,17);
        base4.setRotationPoint(16,4,32);
        base4.rotateAngleY = (float)Math.PI/2;
        this.base.addChild(base4);

        ModelRenderer powerInput = new ModelRenderer(this,414,0);
        powerInput.addBox(54+23+3,4,16,16,28,16);
        this.base.addChild(powerInput);

        ModelRenderer redstoneToggle = new ModelRenderer(this,349,104);
        redstoneToggle.addBox(0,0,0,16,16,8);
        redstoneToggle.setRotationPoint(16,16,16);
        redstoneToggle.rotateAngleY = (float)Math.PI/2;
        //this.base.addChild(redstoneToggle);

        ModelRenderer powerBase = new ModelRenderer(this,414,44);
        powerBase.addBox(54+23+3,0,16,16,4,16);
        this.base.addChild(powerBase);

        this.split_warp_left = new ModelRenderer(this,"split_warp_left");
        this.split_warp_right = new ModelRenderer(this,"split_warp_right");

        ModelRenderer splitter_frame = new ModelRenderer(this,228,0);
        splitter_frame.addBox(74,4,10.5F,6,43,14);
        ModelRenderer splitter_frame2 = new ModelRenderer(this,268,0);
        splitter_frame2.addBox(32,4,10.5F,6,43,14);
        splitter_frame.addChild(splitter_frame2);
        ModelRenderer splitter_frame_top = new ModelRenderer(this,308,0);
        splitter_frame_top.addBox(41,-24-32-16-4-1.5F+4-0.5F,16-5.5F,6,36,14);
        splitter_frame_top.rotateAngleZ = (float)Math.toRadians(90);
        splitter_frame.addChild(splitter_frame_top);
        this.base.addChild(splitter_frame);
        ModelRenderer splitter_board = new ModelRenderer(this,0,92);
        splitter_board.addBox(37,38,18,38,3,1);
        ModelRenderer splitter_board2 = new ModelRenderer(this,0,92);
        splitter_board2.addBox(37,38,16,38,3,1);
        splitter_frame.addChild(splitter_board);
        splitter_frame.addChild(splitter_board2);
        this.split_warp_left.addChild(splitter_board);
        this.split_warp_right.addChild(splitter_board2);

        warp_roller = new ModelRenderer(this,8,0);
        warp_roller.addBox(-1.5F,0,-1.5F,3,40,3);
        warp_roller.setRotationPoint(24+32+16+4,10,5);
        warp_roller.rotateAngleZ = (float)Math.toRadians(90);
        this.base.addChild(warp_roller);

        warp_beam = new ModelRenderer(this,8,0);
        warp_beam.addBox(-1.5F,0,-1.5F,3,40,3);
        warp_beam.setRotationPoint(24+32+16+4,24,6F);
        warp_beam.rotateAngleZ = (float)Math.toRadians(90);
        this.base.addChild(warp_beam);

        cloth_roller = new ModelRenderer(this,8,0);
        cloth_roller.addBox(-1.5F,0,-1.5F,3,40,3);
        cloth_roller.rotateAngleZ = (float)Math.toRadians(90);
        cloth_roller.setRotationPoint(24+32+16+4,10,43);
        this.base.addChild(cloth_roller);

        cloth_beam = new ModelRenderer(this,8,0);
        cloth_beam.addBox(-1.5F,0,-1.5F,3,40,3);
        cloth_beam.rotateAngleZ = (float)Math.toRadians(90);
        cloth_beam.setRotationPoint(24+32+16+4,24,43);
        this.base.addChild(cloth_beam);

        beam_cap = new ModelRenderer(this,0,54);
        beam_cap.addBox(-5F,-5F,0,10,10,1);
        beam_cap2 = new ModelRenderer(this,0,54);
        beam_cap2.addBox(-5F,-5F,0,10,10,1);
        beam_cap.rotateAngleX = (float)Math.toRadians(90);
        beam_cap2.rotateAngleX = (float)Math.toRadians(90);
        beam_cap.rotateAngleZ = (float)Math.toRadians(90);
        beam_cap2.rotateAngleZ = (float)Math.toRadians(90);
        beam_cap.setRotationPoint(24+32+16+4,11.5F-1.5F,6.5F-1.5F);
        beam_cap2.setRotationPoint(24+32+16+4,11.5F-1.5F,6.5F+27+16-5-1.5F);
        //his.base.addChild(beam_cap);
        //this.base.addChild(beam_cap2);

        this.warp_roll = new ModelRenderer(this,192,0);
        warp_roll.addBox(-1F,0,-1F,2,34,3);
        warp_roll.setRotationPoint(24+32+16+4,24,32);
        warp_roll.rotateAngleZ = (float)Math.toRadians(90);

        ModelRenderer shuttle_beam = new ModelRenderer(this,211,79);
        shuttle_beam.addBox(0,0,-1,62,5,5);
        shuttle_beam.setRotationPoint(19,30,25);
        shuttle_beam_connectors = new ModelRenderer(this,"shuttle_beam_connectors");

        ModelRenderer shuttle_beam_connector = new ModelRenderer(this, 0, 108);
        shuttle_beam_connector.addBox(0,0,0,3,8,3);
        ModelRenderer shuttle_beam_connector2 = new ModelRenderer(this, 0, 108);
        shuttle_beam_connector2.addBox(0,0,0,3,8,3);
        shuttle_beam_connector.setRotationPoint(38-4.5F-14,23,25);
        shuttle_beam_connector2.setRotationPoint(54+23,23,25);
        shuttle_beam_connectors.addChild(shuttle_beam_connector);
        shuttle_beam_connectors.addChild(shuttle_beam_connector2);
        shuttle_beam_super = new ModelRenderer(this,"shuttle_beam_super");
        shuttle_beam_super.addChild(shuttle_beam_connector);
        shuttle_beam_super.addChild(shuttle_beam_connector2);

        ModelRenderer shuttle_bus = new ModelRenderer(this,211,89);
        shuttle_bus.addBox(0,0,-1,62,3,5);
        shuttle_bus.setRotationPoint(19,20,25);
        this.base.addChild(shuttle_beam);
        this.base.addChild(shuttle_beam_connectors);
        this.base.addChild(shuttle_bus);
        this.shuttle_beam_super.addChild(shuttle_beam);
        this.shuttle_beam_super.addChild(shuttle_bus);
        for (int i = 0; i < 5; i++) {
            ModelRenderer warp_stage = new ModelRenderer(this,192,0);
            warp_stage.addBox(-2F-i/2.0F,0,-2F-i/2.0F,4+i,32,4+i);
            warp_stage.setRotationPoint(24+32+16,10,5);
            warp_stage.rotateAngleZ = (float)Math.toRadians(90);
            warp_stage.isHidden = true;
            this.base.addChild(warp_stage);
            this.warp_stages[i] = warp_stage;

            ModelRenderer cloth_stage = new ModelRenderer(this,348, 36);
            cloth_stage.addBox(-2F-i/2.0F,0,-2F-i/2.0F,4+i,32,4+i);
            cloth_stage.setRotationPoint(24+32+16,10,43);
            cloth_stage.rotateAngleZ = (float)Math.toRadians(90);
            cloth_stage.isHidden = true;
            this.base.addChild(cloth_stage);
            this.cloth_stages[i] = cloth_stage;
        }

        ModelRenderer shuttle_frame_driver = new ModelRenderer(this,222,57);
        shuttle_frame_driver.addBox(0,0,0,16,16,6);
        shuttle_frame_driver.setRotationPoint(38-6F,4,40);
        shuttle_frame_driver.rotateAngleY = (float)Math.PI/2;
        this.base.addChild(shuttle_frame_driver);
        ModelRenderer shuttle_frame_driver2 = new ModelRenderer(this,222,57);
        shuttle_frame_driver2.addBox(0,0,0,16,16,6);
        shuttle_frame_driver2.setRotationPoint(54+23-3,4,40);
        shuttle_frame_driver2.rotateAngleY = (float)Math.PI/2;
        this.base.addChild(shuttle_frame_driver2);

        ModelRenderer roller_supporter = new ModelRenderer(this,310,50);
        roller_supporter.addBox(0,0,0,10,24,4);
        roller_supporter.setRotationPoint(38-5F,4,48F);
        roller_supporter.rotateAngleY = (float)Math.PI/2;
        this.base.addChild(roller_supporter);

        ModelRenderer roller_supporter2 = new ModelRenderer(this,310,50);
        roller_supporter2.addBox(0,0,0,10,24,4);
        roller_supporter2.setRotationPoint(54+23-2,4,48F);
        roller_supporter2.rotateAngleY = (float)Math.PI/2;
        this.base.addChild(roller_supporter2);

        ModelRenderer roller_supporter3 = new ModelRenderer(this,310,50);
        roller_supporter3.addBox(0,0,0,10,24,4);
        roller_supporter3.setRotationPoint(38-5F,4,10.5F);
        roller_supporter3.rotateAngleY = (float)Math.PI/2;
        this.base.addChild(roller_supporter3);

        ModelRenderer roller_supporter4 = new ModelRenderer(this,310,50);
        roller_supporter4.addBox(0,0,0,10,24,4);
        roller_supporter4.setRotationPoint(54+23-2,4,10.5F);
        roller_supporter4.rotateAngleY = (float)Math.PI/2;
        this.base.addChild(roller_supporter4);

        ModelRenderer sudo_warp_roll = new ModelRenderer(this,192,0);
        sudo_warp_roll.addBox(-2F,0,-2F,4,32,4);
        sudo_warp_roll.setRotationPoint(24+32+16,24,6F);
        sudo_warp_roll.rotateAngleZ = (float)Math.toRadians(90);
        this.base.addChild(sudo_warp_roll);
        this.sudo_warp = sudo_warp_roll;

        ModelRenderer sudo_cloth_roll = new ModelRenderer(this,348, 36);
        sudo_cloth_roll.addBox(-2F,0,-2F,4,32,4);
        sudo_cloth_roll.setRotationPoint(24+32+16,24,43);
        sudo_cloth_roll.rotateAngleZ = (float)Math.toRadians(90);
        this.base.addChild(sudo_cloth_roll);
        this.sudo_cloth = sudo_cloth_roll;

        sudo_cloth_unfinished = new ModelRenderer(this,348, 0);
        sudo_cloth_unfinished.addBox(-2F,0,-2F,4,32,4);
        sudo_cloth_unfinished.setRotationPoint(24+32+16,24,43);
        sudo_cloth_unfinished.rotateAngleZ = (float)Math.toRadians(90);
        this.base.addChild(sudo_cloth_unfinished);

        this.pirn_loader = new ModelRenderer(this,0,0);
        this.splitter_frame_shuttle = new ModelRenderer(this,"splitter_frame_shuttle");
        this.base.addChild(splitter_frame_shuttle);
        for (int i = 0; i < 32; i++) {
            if (i % 2 == 0) {
                ModelRenderer splitterwire = new ModelRenderer(this,20,0);
                splitterwire.addBox(i+20+15+5,20,18,1,18,1);
                splitter_frame.addChild(splitterwire);
                this.split_warp_left.addChild(splitterwire);

                ModelRenderer splitterwireshuttle = new ModelRenderer(this,20,0);
                splitterwireshuttle.addBox(0,0,0,1,9,1);
                splitterwireshuttle.setRotationPoint(i+20+15+5+0.5F,21,18+7.5F);
                splitter_frame_shuttle.addChild(splitterwireshuttle);
            } else {
                ModelRenderer splitterwire = new ModelRenderer(this,20,0);
                splitterwire.addBox(i+20+15+5,20,16,1,18,1);
                splitter_frame.addChild(splitterwire);
                this.split_warp_right.addChild(splitterwire);
            }
        }
        this.pirn_pile = new ModelRenderer(this,"pirn_pile");
        for (int i = 0; i < 4; i++) {
            for (int j = i; j < 4; j++) {
                ModelRenderer empty_pirn = new ModelRenderer(this, 0, 10);
                empty_pirn.addBox(0,0,0,2,9,2);
                empty_pirn.rotateAngleZ = (float)Math.PI/2;
                empty_pirn.setRotationPoint(28, 16+i*2,4-i*1.5F+j*3);
                this.pirn_pile.addChild(empty_pirn);
            }
        }
        this.base.addChild(this.pirn_pile);
        //this.pirn_loader.addBox(32,0,0,2,9,2);
        //this.base.addChild(pirn_loader);
        //this.pirn_loader.rotateAngleZ = (float)Math.toRadians(90);
        for (int i = 0; i < 8; i++) {
            ModelRenderer pirn = new ModelRenderer(this,0,0);
            pirn.addBox(0,0,0,2,9,2);
            pirn.rotateAngleZ = (float)Math.PI/2;
            pirn.setRotationPoint(32,25+(float)Math.round(Math.cos(Math.toRadians(i*360/8))*4),(float)Math.round(Math.sin(Math.toRadians(i*360/8))*4)+19);
            this.base.addChild(pirn);
            this.pirn_loader.addChild(pirn);
        }
        pirn_loader_base = new ModelRenderer(this,20,19);
        pirn_loader_base.addBox(-3,0,-3,6,10,6);
        pirn_loader_base.setRotationPoint(32,26,20);
        pirn_loader_base.rotateAngleZ = (float)Math.toRadians(90);
        this.base.addChild(pirn_loader_base);

        ModelRenderer pirn_loader_cap = new ModelRenderer(this,0,54);
        pirn_loader_cap.addBox(-5F,-5F,0,10,10,1);
        pirn_loader_cap.rotateAngleX = (float)Math.toRadians(90);
        pirn_loader_cap.rotateAngleZ = (float)Math.toRadians(90);
        pirn_loader_cap.setRotationPoint(21.5F,11.5F-1.5F+21,6.5F-1.5F+14);
        //this.base.addChild(pirn_loader_cap);

        this.threads = new ModelRenderer(this,"threads");
        this.threads_warp_higher = new ModelRenderer(this,"threads_warp_higher");
        this.threads_warp_lower = new ModelRenderer(this,"threads_warp_lower");
        this.threads_cloth_lower = new ModelRenderer(this,"threads_cloth_lower");
        int warpthickness = 3;
        /*ModelRenderer thread = new ModelRenderer(this, 348, 0);
        thread.addBox(0, 0, 0, 32, 18, 1);
        thread.setRotationPoint(20 + 20, 23, 18);
        thread.rotateAngleX = (float)Math.PI/2+(float) Math.atan2(24 - 23-4, -18);
        //thread.rotateAngleX = (float) Math.toRadians(-90);
        //thread.rotateAngleY = (float) Math.atan2(24 - 23-4, -18);
        //thread.rotateAngleZ = (float) Math.toRadians(90);
        this.threads.addChild(thread);
        this.threads_warp_lower.addChild(thread);*/

        /*ModelRenderer thread3 = new ModelRenderer(this, 348, 0);
        thread3.addBox(0, 0, 0, 32, 17, 1);
        thread3.setRotationPoint(20 + 20, 23, 17);
        thread3.rotateAngleX = (float)Math.PI/2+(float) Math.atan2(-2, 18+8);
        //thread3.rotateAngleX = (float) Math.toRadians(-90);
        //thread3.rotateAngleY = (float) Math.atan2(-2, 18);
        //thread3.rotateAngleZ = (float) Math.toRadians(90);
        this.threads.addChild(thread3);
        this.threads_warp_higher.addChild(thread3);*/

        ModelRenderer thread2 = new ModelRenderer(this, 348, 0);
        thread2.addBox(0, 0, 0, 32, 16, 1);
        thread2.setRotationPoint(20 + 20,24-0.5F,-1.5F+6+0.5F);
        thread2.rotateAngleX = (float)Math.PI/2*3-(float) Math.atan2(24-10, -5+warpthickness/2.0);
        //thread2.rotateAngleX = (float) Math.toRadians(-90);
        //thread2.rotateAngleY = (float) Math.atan2(24-15, -5+warpthickness/2.0);
        //thread2.rotateAngleZ = (float) Math.toRadians(90);
        this.threads.addChild(thread2);

        ModelRenderer thread4 = new ModelRenderer(this, 348, 35);
        thread4.addBox(0, 0, 0, 32, 15, 1);
        thread4.setRotationPoint(20 + 20, 24-1F, 38+3F+4);
        thread4.rotateAngleX = (float)Math.PI/2*3-(float) Math.atan2(24-10, 4-warpthickness/2.0);

        //this.threads.addChild(thread4);
        this.threads_warp_lower.addChild(thread2);

        this.threads_warp_higher.addChild(thread2);
        //this.threads_warp_higher.addChild(thread4);

        for (int i = 0; i < 20; i++) {
            ModelRenderer thread_cloth_animated = new ModelRenderer(this, 348, 84-(i*15)/20);
            thread_cloth_animated.addBox(0, 0, 0, 32, 15, 1);
            thread_cloth_animated.setRotationPoint(20 + 20, 24-1F, 38+3F+4);
            thread_cloth_animated.rotateAngleX = (float)Math.PI/2*3-(float) Math.atan2(24-10, 4-warpthickness/2.0);
            this.threads_cloth_lower.addChild(thread_cloth_animated);
            this.threads.addChild(thread_cloth_animated);
        }



        this.split_warp = new ModelRenderer(this,"split_warp");
        for (int i = 0; i < 32; i++) {
            if (i % 2 == 0) {

                ModelRenderer split_thread = new ModelRenderer(this, 348+i, 0);
                split_thread.addBox(0, 0, 0, 1, 27, 1);
                split_thread.setRotationPoint(20 + 20+i, 23, 17);
                split_thread.rotateAngleX = (float)Math.PI/2+(float) Math.atan2(-3, 18+8);

                ModelRenderer split_thread2 = new ModelRenderer(this, 348+i, 0);
                split_thread2.addBox(0, 0, 0, 1, 13, 1);
                split_thread2.setRotationPoint(20 + 20+i, 22, 18);
                split_thread2.rotateAngleX = (float)Math.PI/2-(float) Math.atan2(3, -18);
                //thread3.rotateAngleX = (float) Math.toRadians(-90);
                //thread3.rotateAngleY = (float) Math.atan2(-2, 18);
                //thread3.rotateAngleZ = (float) Math.toRadians(90);
                this.threads.addChild(split_thread);
                this.split_warp.addChild(split_thread);
                this.threads.addChild(split_thread2);
                this.split_warp.addChild(split_thread2);


            } else {
                ModelRenderer split_thread = new ModelRenderer(this, 348+i, 0);
                split_thread.addBox(0, 0, 0, 1, 27, 1);
                split_thread.setRotationPoint(20 + 20+i, 30, 17);
                split_thread.rotateAngleX = (float)Math.PI/2+(float) Math.atan2(4, 18+8);

                ModelRenderer split_thread2 = new ModelRenderer(this, 348+i, 0);
                split_thread2.addBox(0, 0, 0, 1, 13, 1);
                split_thread2.setRotationPoint(20 + 20+i, 29, 18);
                split_thread2.rotateAngleX = (float)Math.PI/2-(float) Math.atan2(-4, -18);
                //thread3.rotateAngleX = (float) Math.toRadians(-90);
                //thread3.rotateAngleY = (float) Math.atan2(-2, 18);
                //thread3.rotateAngleZ = (float) Math.toRadians(90);
                this.threads.addChild(split_thread);
                this.split_warp.addChild(split_thread);
                this.threads.addChild(split_thread2);
                this.split_warp.addChild(split_thread2);
                /*ModelRenderer thread = new ModelRenderer(this,192,4);
                thread.addBox(0,0,0,0,18,1);
                thread.setRotationPoint(i+20+17,23,18);
                thread.rotateAngleX = (float)Math.toRadians(-90);
                thread.rotateAngleY = (float) Math.atan2(-3, -18);
                thread.rotateAngleZ = (float)Math.toRadians(90);
                this.threads.addChild(thread);
                this.threads_warp_lower.addChild(thread);*/

                /*ModelRenderer thread3 = new ModelRenderer(this, 192, 4);
                thread3.addBox(0, 0, 0, 0, 18, 1);
                thread3.setRotationPoint(i + 20 + 17, 23, 17);
                thread3.rotateAngleX = (float) Math.toRadians(-90);
                thread3.rotateAngleY = (float) Math.atan2(-2, 18);
                thread3.rotateAngleZ = (float) Math.toRadians(90);
                this.threads.addChild(thread3);
                this.threads_warp_higher.addChild(thread3);*/

                /*ModelRenderer thread2 = new ModelRenderer(this, 192, 4);
                thread2.addBox(0, 0, 0, 0, 16, 1);
                thread2.setRotationPoint(24+32+16+1-i,24-1.5F,-1.5F);
                thread2.rotateAngleX = (float) Math.toRadians(-90);
                thread2.rotateAngleY = (float) Math.atan2(24-15, -5+warpthickness/2.0);
                thread2.rotateAngleZ = (float) Math.toRadians(90);
                this.threads.addChild(thread2);*/
            }
        }


        //box.addBox(16, 8F, 20, 16, 28, 8);
        //this.base.addChild(box);

        /*this.base = new ModelRenderer(this, 0, 0);
        this.base.addBox(0, 0, 0, 16 * 6, 8, 16 * 3);

        ModelRenderer box = new ModelRenderer(this, 128, 56);
        box.addBox(16, 8F, 20, 16, 28, 8);
        this.base.addChild(box);


        arm = new ModelRenderer(this, 0, 92);
        arm.addBox(-24 - 16, 0, -4, 70, 10, 8);
        arm.setRotationPoint(56, 48, 24);
        this.base.addChild(arm);

        ModelRenderer head = new ModelRenderer(this, 272, 0);
        head.addBox(30, -15, -5, 12, 30, 10);
        this.arm.addChild(head);

        ModelRenderer barBack = new ModelRenderer(this, 260, 40);
        barBack.addBox(-35F, 3F, -11F, 4, 4, 22);
        this.arm.addChild(barBack);


        ModelRenderer leg1 = new ModelRenderer(this, 176, 56);
        leg1.addBox(-2F, -2F, -2F, 4, 43, 4);
        leg1.setRotationPoint(56 - 13.6F, 8F, 12F);
        leg1.rotateAngleX = (float) Math.toRadians(10);
        leg1.rotateAngleZ = (float) Math.toRadians(-15);
        leg1.rotateAngleY = (float) Math.toRadians(20);
        this.base.addChild(leg1);

        ModelRenderer leg2 = new ModelRenderer(this, 176, 56);
        leg2.addBox(-2F, -2F, -2F, 4, 43, 4);
        leg2.setRotationPoint(56 + 13.6F, 8F, 12F);
        leg2.rotateAngleX = (float) Math.toRadians(10);
        leg2.rotateAngleZ = (float) Math.toRadians(15);
        leg2.rotateAngleY = (float) Math.toRadians(-20);
        this.base.addChild(leg2);

        ModelRenderer leg3 = new ModelRenderer(this, 176, 56);
        leg3.addBox(-2F, -2F, -1F, 4, 43, 4);
        leg3.setRotationPoint(56 - 13.6F, 8F, 36F);
        leg3.rotateAngleX = (float) Math.toRadians(-10);
        leg3.rotateAngleZ = (float) Math.toRadians(-15);
        leg3.rotateAngleY = (float) Math.toRadians(-20);
        this.base.addChild(leg3);

        ModelRenderer leg4 = new ModelRenderer(this, 176, 56);
        leg4.addBox(-2F, -2F, -2F, 4, 43, 4);
        leg4.setRotationPoint(56 + 13.6F, 8F, 36F);
        leg4.rotateAngleX = (float) Math.toRadians(-10);
        leg4.rotateAngleZ = (float) Math.toRadians(15);
        leg4.rotateAngleY = (float) Math.toRadians(20);
        this.base.addChild(leg4);

        ModelRenderer bar = new ModelRenderer(this, 238, 40);
        bar.addBox(54, 46F, 15F, 4, 4, 18);
        this.base.addChild(bar);


        swingy = new ModelRenderer(this, 290, 40);
        swingy.addBox(-4F, -2F, -14F, 8, 10, 4);
        swingy.setRotationPoint(24, 30, 30);
        this.base.addChild(swingy);

        ModelRenderer counter = new ModelRenderer(this, 200, 62);
        counter.addBox(-12F, 8F, -14F, 24, 10, 4);
        this.swingy.addChild(counter);

        ModelRenderer swingy2 = new ModelRenderer(this, 290, 40);
        swingy2.addBox(-4F, -2F, -2F, 8, 10, 4);
        this.swingy.addChild(swingy2);

        ModelRenderer counter2 = new ModelRenderer(this, 200, 62);
        counter2.addBox(-12F, 8F, -2F, 24, 10, 4);
        this.swingy.addChild(counter2);

        connector = new ModelRenderer(this, 192, 56);
        connector.addBox(-1F, -1F, -12F, 2, 24, 2);
        this.base.addChild(connector);

        ModelRenderer connector2 = new ModelRenderer(this, 192, 56);
        connector2.addBox(-1F, -1F, 6F, 2, 24, 2);
        this.connector.addChild(connector2);

        ModelRenderer out1 = new ModelRenderer(this, 0, 56);
        out1.addBox(48F, 0.01F, -0.01F, 16, 16, 16);
        this.base.addChild(out1);

        ModelRenderer out2 = new ModelRenderer(this, 64, 56);
        out2.addBox(48F, 0.01F, 32.01F, 16, 16, 16);
        this.base.addChild(out2);

        ModelRenderer well = new ModelRenderer(this, 192, 89);
        well.addBox(83F, 0F, 19F, 10, 16, 10);
        this.base.addChild(well);

        ModelRenderer pipe1 = new ModelRenderer(this, 194, 77);
        pipe1.addBox(59F, 8F, 21F, 24, 6, 6);
        this.base.addChild(pipe1);

        ModelRenderer pipe2 = new ModelRenderer(this, 140, 94);
        pipe2.addBox(53F, 8F, 16F, 6, 6, 16);
        this.base.addChild(pipe2);

        ModelRenderer redControlsF2 = new ModelRenderer(this, 0, 0);
        redControlsF2.addBox(2, 8, mirror ? 34 : 2, 4, 8, 2);
        this.base.addChild(redControlsF2);

        ModelRenderer redControlsF1 = new ModelRenderer(this, 0, 0);
        redControlsF1.addBox(2, 8, mirror ? 44 : 12, 4, 8, 2);
        base.addChild(redControlsF1);

        ModelRenderer redControls = new ModelRenderer(this, 0, 0);
        redControls.addBox(0, 16, mirror ? 32 : 0, 8, 16, 16);
        base.addChild(redControls);

        ModelRenderer powerThing = new ModelRenderer(this, 208, 0);
        powerThing.addBox(0, 8, mirror ? 0 : 32, 16, 24, 16);
        base.addChild(powerThing);

        wellConnector = new ModelRenderer(this, 256, 66);
        wellConnector.addBox(-1F, 0F, -1F, 2, 30, 2);

        wellConnector2 = new ModelRenderer(this, 256, 66);
        wellConnector2.addBox(-1F, 0F, -1F, 2, 16, 2);

        this.base.addChild(wellConnector);
        this.base.addChild(wellConnector2);
        */
    }

    @Override
    public void render(Entity entity, float processType, float amountInput, float amountOutput, float amountPirns, float primerCount, float f5)
    {
        int tickAmplified = Math.round(this.ticks*500);
        //System.out.println("TICKAMPLIFIED "+tickAmplified+" "+(this.ticks*500)+" "+this.ticks);
        if (tickAmplified == 50 && !this.has_rotated) {this.pirn_rotation = (this.pirn_rotation+1)%8;this.has_rotated = true;}
        if (tickAmplified == 0 && this.has_rotated) {this.has_rotated = false;}
        this.sub_rotation = tickAmplified < 50 ? tickAmplified : 0;
        //System.out.println(this.sub_rotation);
        //this.cloth_beam.rotateAngleY += 0.01;
        this.cloth_roller.rotateAngleY = (float)Math.PI*2*tickAmplified/250F;
        this.warp_roller.rotateAngleY = (float)Math.PI*2*tickAmplified/250F;
        //this.warp_beam.rotateAngleY += 0.01;
        //this.warp_roller.rotateAngleY += 0.01;
        //this.beam_cap.rotateAngleY += 0.01;
        //this.beam_cap2.rotateAngleY += 0.01;
        //this.warp_roll.rotateAngleY += 0.01;

        this.sudo_cloth.isHidden = !(Math.round(primerCount) > 15) || (tickAmplified < 20 && (Math.round(amountOutput) <= 0));
        this.sudo_warp.isHidden = !(Math.round(primerCount) > 15);
        this.threads.isHidden = !(Math.round(primerCount) > 15);
        this.threads_warp_lower.isHidden = !(Math.round(amountInput) > 0);
        this.sudo_cloth_unfinished.isHidden = !(Math.round(primerCount) > 15) || (tickAmplified >= 20 || (Math.round(amountOutput) > 0));
        //System.out.println("amplified "+(tickAmplified*20)/500);
        if (Math.round(amountOutput) == 0) {
            for (int i = 0; i < 20; i++) {
                this.threads_cloth_lower.childModels.get(i).isHidden = i != ((tickAmplified+5) * 20) / 500;
            }
        } else {
            for (int i = 0; i < 20; i++) {
                this.threads_cloth_lower.childModels.get(i).isHidden = i != 19;
            }
        }
        //this.threads_cloth_lower.setTextureOffset(348, (tickAmplified*20)/500);

        //System.out.println("amount input "+amountInput+" "+Math.round(amountInput/192.0F*5.0F));
        for (int i = 0; i < 5; i++) {
            this.warp_stages[i].isHidden = true;
            this.warp_stages[i].rotateAngleY = (float)(((float)tickAmplified)*2*Math.PI/250.0F);
            if (i == Math.floor((amountInput-1)/192.0F*5.0F)) {this.warp_stages[i].isHidden = false;}

            this.cloth_stages[i].isHidden = true;
            this.cloth_stages[i].rotateAngleY = (float)(((float)tickAmplified)*2*Math.PI/250.0F);
            if (i == Math.floor((amountOutput-1)/192.0F*5.0F)) {this.cloth_stages[i].isHidden = false;}
        }
        //System.out.println("empty pirn "+Math.round(this.empty_pirn_count));
        for (int i = 0; i < 10; i++) {
            this.pirn_pile.childModels.get(i).isHidden = !(Math.round(this.empty_pirn_count) > ((float) i)*128.0F/20.0F);
        }

        float shuttle_beam_displacement = 0;
        float shuttle_launch1 = 0;
        float shuttle_launch2 = 0;
        float warp_split = 1F;

        float pirn_starty = 25+(float)(Math.cos(Math.toRadians((1+(float)this.sub_rotation/50)*360/8))*4);
        float pirn_startz = 19+(float)(Math.sin(Math.toRadians((1+((float)this.sub_rotation)/50)*360/8))*4);
        float pirn_endy = 21.2F;
        float pirn_endz = 25.5F;
        float pirn_speedy = (pirn_endy-pirn_starty)/50;
        float pirn_speedz = (pirn_endz-pirn_startz)/50;
        this.pirn_loader.childModels.get(0).rotationPointX = 32;
        this.pirn_loader.childModels.get(0).rotationPointZ = pirn_endz;
        if (tickAmplified < 50) {
            warp_split = 1F;
            shuttle_beam_displacement = 0;
        } else if (tickAmplified%50 < 20) {
            warp_split = 1F;
            shuttle_beam_displacement = ((float) (Math.cos((tickAmplified%50)*2*Math.PI/20+Math.PI))+1)/2;
            //System.out.println("shuttle x "+32 + ((tickAmplified%50)-5.0F)/5.0F*(54+23-(38-4.5F-14)));
            if (tickAmplified%50 >= 5 && tickAmplified%50 < 10) {
                shuttle_launch1 = ((float) (Math.cos((tickAmplified%50-5)*2*Math.PI/5+Math.PI))+1)/2;
                this.pirn_loader.childModels.get(0).rotationPointX = 32 + ((tickAmplified%50)-4.0F)/4.0F*48;
                //this.pirn_loader.childModels.get(0).rotationPointX = 32 + (54+23-(38-4.5F-14))-11;
            }
            if (tickAmplified%50 >= 10 && tickAmplified%50 < 15) {
                shuttle_launch2 = ((float) (Math.cos((tickAmplified%50-5)*2*Math.PI/5+Math.PI))+1)/2;
                this.pirn_loader.childModels.get(0).rotationPointX = 32 + (5+(-(tickAmplified%50)+9.0F))/4.0F*48;
                //this.pirn_loader.childModels.get(0).rotationPointX = 32 + (54+23-(38-4.5F-14))-11;
            }

            this.pirn_loader.childModels.get(0).rotationPointZ = pirn_endz + ((float) (Math.cos((tickAmplified%50)*2*Math.PI/20+Math.PI))+1)/2*10;
        } else if (tickAmplified%50 < 25) {
            warp_split = (float) (Math.cos((tickAmplified%50-20) * Math.PI / 5));
            shuttle_beam_displacement = 0;
            //this.pirn_loader.childModels.get(0).rotationPointX = 32 + (54+23-(38-4.5F-14))-11;
        } else if (tickAmplified%50 < 45) {
            warp_split = -1F;
            shuttle_beam_displacement = ((float) (Math.cos((tickAmplified%50-5)*2*Math.PI/20+Math.PI))+1)/2;
            this.pirn_loader.childModels.get(0).rotationPointZ = pirn_endz + ((float) (Math.cos((tickAmplified%50-5)*2*Math.PI/20+Math.PI))+1)/2*10;
        } else if (tickAmplified%50 < 50) {
            warp_split = (float) (Math.cos((tickAmplified%50-20) * Math.PI / 5));
            shuttle_beam_displacement = 0;
        }
        for (int i = 0; i < 4; i++) {
            this.shuttle_beam_super.childModels.get(i).rotationPointZ = 25+shuttle_beam_displacement*10;
        }
        this.shuttle_beam_connectors.childModels.get(0).rotationPointX = 38-4.5F-14+shuttle_launch1*5;
        this.shuttle_beam_connectors.childModels.get(1).rotationPointX = 54+23-shuttle_launch2*5;
        for (ModelRenderer model : this.split_warp_left.childModels) {
            model.setRotationPoint(0,(float)(warp_split*3.5+2.5),0);
        }
        for (ModelRenderer model : this.split_warp_right.childModels) {
            model.setRotationPoint(0,(float)(-warp_split*3.5+2.5),0);
        }
        for (int i = 0; i < 64; i++) {
            ModelRenderer model = this.split_warp.childModels.get(i);
            if (i % 4 == 0) {
                model.setRotationPoint(20 + 20+Math.floorDiv(i,2), 30-3.5F-warp_split*3.5F, 17);
                model.rotateAngleX = (float)Math.PI/2+(float) Math.atan2(0.5-warp_split*3.5, 18+8+3);
                this.splitter_frame_shuttle.childModels.get(i/4).rotationPointZ = 18+7.5F+shuttle_beam_displacement*10;
            } else if (i % 4 == 1) {
                model.setRotationPoint(20 + 20+Math.floorDiv(i,2), 29-3.5F-warp_split*3.5F, 18);
                model.rotateAngleX = (float)Math.PI/2-(float) Math.atan2(-0.5+warp_split*3.5-0.5, -10-2);
            } else if (i % 4 == 2) {
                model.setRotationPoint(20 + 20+Math.floorDiv(i,2), 23+3.5F+warp_split*3.5F, 17);
                model.rotateAngleX = (float)Math.PI/2+(float) Math.atan2(0.5+warp_split*3.5, 18+8+3);
                //this.splitter_frame_shuttle.childModels.get(i/4).rotationPointZ = 18+7.5F+shuttle_beam_displacement*10;
            } else {
                model.setRotationPoint(20 + 20+Math.floorDiv(i,2), 22+3.5F+warp_split*3.5F, 18);
                model.rotateAngleX = (float)Math.PI/2-(float) Math.atan2(-0.5-warp_split*3.5-0.5, -10-2);
            }
        }
        for (int i = 0; i < 8; i++) {
            if (i == 0) {
                if (tickAmplified < 50) {
                    this.pirn_loader.childModels.get(i).setRotationPoint(32,pirn_starty+((float)this.sub_rotation*pirn_speedy),pirn_startz+((float)this.sub_rotation*pirn_speedz));
                } else {
                    this.pirn_loader.childModels.get(i).rotationPointY = pirn_endy;
                }
            }
            else {
                if (tickAmplified < 50) {
                    this.pirn_loader.childModels.get(i).setRotationPoint(32,25+(float)(Math.cos(Math.toRadians((-i+1+(float)this.sub_rotation/50)*360/8))*4),(float)(Math.sin(Math.toRadians((-i+1+(float)this.sub_rotation/50)*360/8))*4)+19);
                } else {
                    this.pirn_loader.childModels.get(i).setRotationPoint(32,25+(float)(Math.cos(Math.toRadians((-i+2+(float)this.sub_rotation/50)*360/8))*4),(float)(Math.sin(Math.toRadians((-i+2+(float)this.sub_rotation/50)*360/8))*4)+19);
                }
            }
            if (i < Math.round(amountPirns)) {
                this.pirn_loader.childModels.get((i)%8).isHidden = false;
            } else {
                this.pirn_loader.childModels.get((i)%8).isHidden = true;
            }
        }
        this.pirn_loader_base.rotateAngleY = -(float)Math.toRadians(360/8*this.pirn_rotation+((float)this.sub_rotation)/50*360/8);


        /*arm.rotateAngleZ = (float) Math.toRadians(15 * Math.sin(ticks / 25D));
        swingy.rotateAngleZ = (float) (2 * (Math.PI / 4) + (ticks / 25D));
        arm.

        float dist = 8.5F;

        float sin = (float) Math.sin(swingy.rotateAngleZ);
        float cos = (float) Math.cos(swingy.rotateAngleZ);
        connector.setRotationPoint(24 - dist * sin, 30 + dist * cos, 26);
        if (sin < 0)
        {
            connector.rotateAngleZ = (float) (1F * (Math.PI / 2) + Math.atan(25F / (dist * sin)));
        }
        else if (sin > 0)
        {
            connector.rotateAngleZ = (float) (3F * (Math.PI / 2) + Math.atan(25F / (dist * sin)));
        }

        float sin2 = (float) Math.sin(arm.rotateAngleZ);
        float cos2 = (float) Math.cos(arm.rotateAngleZ);


        float x = 24 - dist * sin;
        float y = 30 + dist * cos;

        float w = 33F;
        float h = 4F;

        float tx = 56 + w * -cos2 - h * sin2;
        float ty = 48 + w * -sin2 + h * cos2;

        connector.setRotationPoint(x, y, 26);
        connector.rotateAngleZ = (float) (3F * (Math.PI / 2) + Math.atan2(ty - y, tx - x));

        wellConnector.setRotationPoint(88F, 16F, 24F);
        wellConnector2.setRotationPoint(88F, 16F, 24F);

        float w2 = -34F;
        float h2 = -13F;

        float x2 = w2 * -cos2 - h2 * sin2;
        float y2 = w2 * -sin2 + h2 * cos2;

        float tx2 = 32F;
        float ty2 = -32F;
        wellConnector.setRotationPoint(56 + x2, 48 + y2, 24);
        wellConnector.rotateAngleZ = (float) (3F * (Math.PI / 2) + Math.atan2(ty2 - y2, tx2 - x2));

        wellConnector2.setRotationPoint(56 + x2, 48 + y2, 24);
        wellConnector2.rotateAngleZ = (float) (3F * (Math.PI / 2) + Math.atan2(ty2 - y2, tx2 - x2));

        if (Math.sqrt((tx2 - x2) * (tx2 - x2) + (ty2 - y2) * (ty2 - y2)) <= 16)
        {
            wellConnector.isHidden = true;
            wellConnector2.isHidden = false;
        }
        else
        {
            wellConnector2.isHidden = true;
            wellConnector.isHidden = false;
        }
        */
        this.base.render(f5);
        //GlStateManager.scale(1,1,0.5);
        this.threads.render(f5);
    }
}
