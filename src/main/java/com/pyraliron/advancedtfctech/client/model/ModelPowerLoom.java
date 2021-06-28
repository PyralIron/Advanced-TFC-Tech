package com.pyraliron.advancedtfctech.client.model;

import com.pyraliron.advancedtfctech.crafting.PowerLoomRecipe;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBed;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraftforge.client.model.ModelLoader;

import javax.jws.WebParam;

import static com.pyraliron.advancedtfctech.crafting.PowerLoomRecipe.EnumPowerLoomProcessType.BURLAP;

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
    public ModelRenderer[] warp_stages2 = new ModelRenderer[5];
    public ModelRenderer[] cloth_stages2 = new ModelRenderer[5];
    public ModelRenderer threads;
    public ModelRenderer warp_lower_stages;
    public ModelRenderer warp_lower_stages2;
    public ModelRenderer threads_warp_lower;
    public ModelRenderer threads_cloth_lower;
    public ModelRenderer threads_cloth_lower2;
    public ModelRenderer threads_warp_higher;
    public ModelRenderer shuttle_beam_connectors;
    public ModelRenderer shuttle_beam_super;
    public ModelRenderer split_warp;
    public ModelRenderer split_warp2;
    public ModelRenderer pirn_loader_base;
    public ModelRenderer splitter_frame_shuttle;
    public ModelRenderer sudo_warp;
    public ModelRenderer sudo_cloth;
    public ModelRenderer sudo_cloth_unfinished;
    public ModelRenderer sudo_warp2;
    public ModelRenderer sudo_cloth2;
    public ModelRenderer sudo_cloth_unfinished2;
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
        if (this.mirror) {
            this.base = new ModelRenderer(this, 0, 0);
            this.base.addBox(32, 0, 0, 16 * 3, 4, 16 * 3);

            ModelRenderer base2 = new ModelRenderer(this, 16, 54 + 16);
            base2.addBox(0, 0, 0, 16 * 2, 4, 16);
            base2.setRotationPoint(16, 0, 48);
            base2.rotateAngleY = (float) Math.PI / 2;
            this.base.addChild(base2);

            ModelRenderer base3 = new ModelRenderer(this, 99, 81);
            base3.addBox(0, 0, 0, 42, 16, 28);
            base3.setRotationPoint(35,0,9);
            this.base.addChild(base3);

            ModelRenderer base4 = new ModelRenderer(this, 239, 97);
            base4.addBox(0, 0, 0, 32, 12, 17);
            base4.setRotationPoint(33, 4, 16);
            base4.rotateAngleY = (float) (3*Math.PI / 2);

            this.base.addChild(base4);

            ModelRenderer powerInput = new ModelRenderer(this, 144, 0);
            powerInput.addBox(54 + 23 + 3, 4, 16, 16, 28, 16);
            this.base.addChild(powerInput);

            ModelRenderer powerBase = new ModelRenderer(this, 128, 54);
            powerBase.addBox(54 + 23 + 3, 0, 16, 16, 4, 16);
            this.base.addChild(powerBase);

            this.split_warp_left = new ModelRenderer(this, "split_warp_left");
            this.split_warp_right = new ModelRenderer(this, "split_warp_right");

            ModelRenderer splitter_frame = new ModelRenderer(this, 228, 0);
            splitter_frame.addBox(74, 4, 48-14-10.5F, 6, 43, 14);
            ModelRenderer splitter_frame2 = new ModelRenderer(this, 268, 0);
            splitter_frame2.addBox(32, 4, 48-14-10.5F, 6, 43, 14);
            splitter_frame.addChild(splitter_frame2);
            ModelRenderer splitter_frame_top = new ModelRenderer(this, 308, 0);
            splitter_frame_top.addBox(41, -24 - 32 - 16 - 4 - 1.5F + 4 - 0.5F, 48-14-10.5F, 6, 36, 14);
            splitter_frame_top.rotateAngleZ = (float) Math.toRadians(90);
            splitter_frame.addChild(splitter_frame_top);
            this.base.addChild(splitter_frame);

            ModelRenderer splitter_board = new ModelRenderer(this, 0, 92);
            splitter_board.addBox(37, 38, 29, 38, 3, 1);
            ModelRenderer splitter_board2 = new ModelRenderer(this, 0, 92);
            splitter_board2.addBox(37, 38, 31, 38, 3, 1);
            splitter_frame.addChild(splitter_board);
            splitter_frame.addChild(splitter_board2);
            this.split_warp_left.addChild(splitter_board);
            this.split_warp_right.addChild(splitter_board2);

            warp_roller = new ModelRenderer(this, 8, 0);
            warp_roller.addBox(-1.5F, 0, -1.5F, 3, 40, 3);
            warp_roller.setRotationPoint(24 + 32 + 16 + 4, 10, 43);
            warp_roller.rotateAngleZ = (float) Math.toRadians(90);
            this.base.addChild(warp_roller);

            warp_beam = new ModelRenderer(this, 8, 0);
            warp_beam.addBox(-1.5F, 0, -1.5F, 3, 40, 3);
            warp_beam.setRotationPoint(24 + 32 + 16 + 4, 24, 42);
            warp_beam.rotateAngleZ = (float) Math.toRadians(90);
            this.base.addChild(warp_beam);

            cloth_roller = new ModelRenderer(this, 8, 0);
            cloth_roller.addBox(-1.5F, 0, -1.5F, 3, 40, 3);
            cloth_roller.rotateAngleZ = (float) Math.toRadians(90);
            cloth_roller.setRotationPoint(24 + 32 + 16 + 4, 10, 5);
            this.base.addChild(cloth_roller);

            cloth_beam = new ModelRenderer(this, 8, 0);
            cloth_beam.addBox(-1.5F, 0, -1.5F, 3, 40, 3);
            cloth_beam.rotateAngleZ = (float) Math.toRadians(90);
            cloth_beam.setRotationPoint(24 + 32 + 16 + 4, 24, 5);
            this.base.addChild(cloth_beam);

            ModelRenderer shuttle_beam = new ModelRenderer(this, 211, 79);
            shuttle_beam.addBox(0, 0, -1, 62, 5, 5);
            shuttle_beam.setRotationPoint(19, 30, 20);
            shuttle_beam_connectors = new ModelRenderer(this, "shuttle_beam_connectors");

            ModelRenderer shuttle_beam_connector = new ModelRenderer(this, 0, 108);
            shuttle_beam_connector.addBox(0, 0, 0, 3, 8, 3);
            ModelRenderer shuttle_beam_connector2 = new ModelRenderer(this, 0, 108);
            shuttle_beam_connector2.addBox(0, 0, 0, 3, 8, 3);
            shuttle_beam_connector.setRotationPoint(38 - 4.5F - 14, 23, 20);
            shuttle_beam_connector2.setRotationPoint(54 + 23, 23, 20);
            shuttle_beam_connectors.addChild(shuttle_beam_connector);
            shuttle_beam_connectors.addChild(shuttle_beam_connector2);
            shuttle_beam_super = new ModelRenderer(this, "shuttle_beam_super");
            shuttle_beam_super.addChild(shuttle_beam_connector);
            shuttle_beam_super.addChild(shuttle_beam_connector2);

            ModelRenderer shuttle_bus = new ModelRenderer(this, 211, 89);
            shuttle_bus.addBox(0, 0, -1, 62, 3, 5);
            shuttle_bus.setRotationPoint(19, 20, 20);
            this.base.addChild(shuttle_beam);
            this.base.addChild(shuttle_beam_connectors);
            this.base.addChild(shuttle_bus);
            this.shuttle_beam_super.addChild(shuttle_beam);
            this.shuttle_beam_super.addChild(shuttle_bus);

            for (int i = 0; i < 5; i++) {
                ModelRenderer warp_stage = new ModelRenderer(this, 348, 0);
                warp_stage.addBox(-2F - i / 2.0F, 0, -2F - i / 2.0F, 4 + i, 32, 4 + i);
                warp_stage.setRotationPoint(24 + 32 + 16, 10, 43);
                warp_stage.rotateAngleZ = (float) Math.toRadians(90);
                warp_stage.isHidden = true;
                this.base.addChild(warp_stage);
                this.warp_stages[i] = warp_stage;

                ModelRenderer cloth_stage = new ModelRenderer(this, 348, 36);
                cloth_stage.addBox(-2F - i / 2.0F, 0, -2F - i / 2.0F, 4 + i, 32, 4 + i);
                cloth_stage.setRotationPoint(24 + 32 + 16, 10, 5);
                cloth_stage.rotateAngleZ = (float) Math.toRadians(90);
                cloth_stage.isHidden = true;
                this.base.addChild(cloth_stage);
                this.cloth_stages[i] = cloth_stage;

                ModelRenderer warp_stage2 = new ModelRenderer(this, 414, 0);
                warp_stage2.addBox(-2F - i / 2.0F, 0, -2F - i / 2.0F, 4 + i, 32, 4 + i);
                warp_stage2.setRotationPoint(24 + 32 + 16, 10, 43);
                warp_stage2.rotateAngleZ = (float) Math.toRadians(90);
                warp_stage2.isHidden = true;
                this.base.addChild(warp_stage2);
                this.warp_stages2[i] = warp_stage2;

                ModelRenderer cloth_stage2 = new ModelRenderer(this, 414, 36);
                cloth_stage2.addBox(-2F - i / 2.0F, 0, -2F - i / 2.0F, 4 + i, 32, 4 + i);
                cloth_stage2.setRotationPoint(24 + 32 + 16, 10, 5);
                cloth_stage2.rotateAngleZ = (float) Math.toRadians(90);
                cloth_stage2.isHidden = true;
                this.base.addChild(cloth_stage2);
                this.cloth_stages2[i] = cloth_stage2;
            }

            ModelRenderer shuttle_frame_driver = new ModelRenderer(this, 222, 57);
            shuttle_frame_driver.addBox(0, 0, 0, 16, 16, 6);
            shuttle_frame_driver.setRotationPoint(38 - 6F, 4, 24);
            shuttle_frame_driver.rotateAngleY = (float) Math.PI / 2;
            this.base.addChild(shuttle_frame_driver);
            ModelRenderer shuttle_frame_driver2 = new ModelRenderer(this, 222, 57);
            shuttle_frame_driver2.addBox(0, 0, 0, 16, 16, 6);
            shuttle_frame_driver2.setRotationPoint(54 + 23 - 3, 4, 24);
            shuttle_frame_driver2.rotateAngleY = (float) Math.PI / 2;
            this.base.addChild(shuttle_frame_driver2);

            ModelRenderer roller_supporter = new ModelRenderer(this, 12, 98);
            roller_supporter.addBox(0, 0, 0, 10, 24, 4);
            roller_supporter.setRotationPoint(38 - 5F, 4, 10);
            roller_supporter.rotateAngleY = (float) Math.PI / 2;
            this.base.addChild(roller_supporter);

            ModelRenderer roller_supporter2 = new ModelRenderer(this, 40, 98);
            roller_supporter2.addBox(0, 0, 0, 10, 24, 4);
            roller_supporter2.setRotationPoint(54 + 23 - 2, 4, 10);
            roller_supporter2.rotateAngleY = (float) Math.PI / 2;
            this.base.addChild(roller_supporter2);

            ModelRenderer roller_supporter3 = new ModelRenderer(this, 68, 98);
            roller_supporter3.addBox(0, 0, 0, 10, 24, 4);
            roller_supporter3.setRotationPoint(38 - 5F, 4, 47.5F);
            roller_supporter3.rotateAngleY = (float) Math.PI / 2;
            this.base.addChild(roller_supporter3);

            ModelRenderer roller_supporter4 = new ModelRenderer(this, 310, 50);
            roller_supporter4.addBox(0, 0, 0, 10, 24, 4);
            roller_supporter4.setRotationPoint(54 + 23 - 2, 4, 47.5F);
            roller_supporter4.rotateAngleY = (float) Math.PI / 2;
            this.base.addChild(roller_supporter4);

            ModelRenderer sudo_warp_roll = new ModelRenderer(this, 348, 0);
            sudo_warp_roll.addBox(-2F, 0, -2F, 4, 32, 4);
            sudo_warp_roll.setRotationPoint(24 + 32 + 16, 24, 42);
            sudo_warp_roll.rotateAngleZ = (float) Math.toRadians(90);
            this.base.addChild(sudo_warp_roll);
            this.sudo_warp = sudo_warp_roll;

            ModelRenderer sudo_cloth_roll = new ModelRenderer(this, 348, 36);
            sudo_cloth_roll.addBox(-2F, 0, -2F, 4, 32, 4);
            sudo_cloth_roll.setRotationPoint(24 + 32 + 16, 24, 5);
            sudo_cloth_roll.rotateAngleZ = (float) Math.toRadians(90);
            this.base.addChild(sudo_cloth_roll);
            this.sudo_cloth = sudo_cloth_roll;

            sudo_cloth_unfinished = new ModelRenderer(this, 348, 0);
            sudo_cloth_unfinished.addBox(-2F, 0, -2F, 4, 32, 4);
            sudo_cloth_unfinished.setRotationPoint(24 + 32 + 16, 24, 5);
            sudo_cloth_unfinished.rotateAngleZ = (float) Math.toRadians(90);
            this.base.addChild(sudo_cloth_unfinished);

            ModelRenderer sudo_warp_roll2 = new ModelRenderer(this, 414, 0);
            sudo_warp_roll2.addBox(-2F, 0, -2F, 4, 32, 4);
            sudo_warp_roll2.setRotationPoint(24 + 32 + 16, 24, 42);
            sudo_warp_roll2.rotateAngleZ = (float) Math.toRadians(90);
            this.base.addChild(sudo_warp_roll2);
            this.sudo_warp2 = sudo_warp_roll2;

            ModelRenderer sudo_cloth_roll2 = new ModelRenderer(this, 414, 36);
            sudo_cloth_roll2.addBox(-2F, 0, -2F, 4, 32, 4);
            sudo_cloth_roll2.setRotationPoint(24 + 32 + 16, 24, 5);
            sudo_cloth_roll2.rotateAngleZ = (float) Math.toRadians(90);
            this.base.addChild(sudo_cloth_roll2);
            this.sudo_cloth2 = sudo_cloth_roll2;

            sudo_cloth_unfinished2 = new ModelRenderer(this, 414, 0);
            sudo_cloth_unfinished2.addBox(-2F, 0, -2F, 4, 32, 4);
            sudo_cloth_unfinished2.setRotationPoint(24 + 32 + 16, 24, 5);
            sudo_cloth_unfinished2.rotateAngleZ = (float) Math.toRadians(90);
            this.base.addChild(sudo_cloth_unfinished2);

            this.pirn_loader = new ModelRenderer(this, 0, 0);
            this.splitter_frame_shuttle = new ModelRenderer(this, "splitter_frame_shuttle");
            this.base.addChild(splitter_frame_shuttle);
            for (int i = 0; i < 32; i++) {
                if (i % 2 == 0) {
                    ModelRenderer splitterwire = new ModelRenderer(this, 20, 0);
                    splitterwire.addBox(i + 20 + 15 + 5, 20, 47-18, 1, 18, 1);
                    splitter_frame.addChild(splitterwire);
                    this.split_warp_left.addChild(splitterwire);

                    ModelRenderer splitterwireshuttle = new ModelRenderer(this, 20, 0);
                    splitterwireshuttle.addBox(0, 0, 0, 1, 9, 1);
                    splitterwireshuttle.setRotationPoint(i + 20 + 15 + 5 + 0.5F, 21, 47-(18 + 7.5F));
                    splitter_frame_shuttle.addChild(splitterwireshuttle);
                } else {
                    ModelRenderer splitterwire = new ModelRenderer(this, 20, 0);
                    splitterwire.addBox(i + 20 + 15 + 5, 20, 47-16, 1, 18, 1);
                    splitter_frame.addChild(splitterwire);
                    this.split_warp_right.addChild(splitterwire);
                }
            }
            this.pirn_pile = new ModelRenderer(this, "pirn_pile");
            for (int i = 0; i < 4; i++) {
                for (int j = i; j < 4; j++) {
                    ModelRenderer empty_pirn = new ModelRenderer(this, 0, 11);
                    empty_pirn.addBox(0, 0, 0, 2, 9, 2);
                    empty_pirn.rotateAngleZ = (float) Math.PI / 2;
                    empty_pirn.setRotationPoint(28, (16 + i * 2), 46-(4 - i * 1.5F + j * 3));
                    this.pirn_pile.addChild(empty_pirn);
                }
            }
            this.base.addChild(this.pirn_pile);

            for (int i = 0; i < 8; i++) {
                ModelRenderer pirn = new ModelRenderer(this, 0, 0);
                pirn.addBox(0, 0, 0, 2, 9, 2);
                pirn.rotateAngleZ = (float) Math.PI / 2;
                pirn.setRotationPoint(32, 25 + (float) Math.round(Math.cos(Math.toRadians(i * 360 / 8)) * 4), 46-((float) Math.round(Math.sin(Math.toRadians(i * 360 / 8)) * 4) + 19));
                this.base.addChild(pirn);
                this.pirn_loader.addChild(pirn);
            }
            for (int i = 0; i < 8; i++) {
                ModelRenderer pirn2 = new ModelRenderer(this, 0, 22);
                pirn2.addBox(0, 0, 0, 2, 9, 2);
                pirn2.rotateAngleZ = (float) Math.PI / 2;
                pirn2.setRotationPoint(32, 25 + (float) Math.round(Math.cos(Math.toRadians(i * 360 / 8)) * 4), 46-((float) Math.round(Math.sin(Math.toRadians(i * 360 / 8)) * 4) + 19));
                this.base.addChild(pirn2);
                this.pirn_loader.addChild(pirn2);
            }
            pirn_loader_base = new ModelRenderer(this, 20, 19);
            pirn_loader_base.addBox(-3, 0, -3, 6, 10, 6);
            pirn_loader_base.setRotationPoint(32, 26, 28);
            pirn_loader_base.rotateAngleZ = (float) Math.toRadians(90);
            this.base.addChild(pirn_loader_base);

            this.threads = new ModelRenderer(this, "threads");
            this.threads_warp_higher = new ModelRenderer(this, "threads_warp_higher");
            this.threads_warp_lower = new ModelRenderer(this, "threads_warp_lower");
            this.warp_lower_stages = new ModelRenderer(this, "warp_lower_stages");
            this.threads_cloth_lower = new ModelRenderer(this, "threads_cloth_lower");
            this.warp_lower_stages2 = new ModelRenderer(this, "warp_lower_stages2");
            this.threads_cloth_lower2 = new ModelRenderer(this, "threads_cloth_lower2");
            int warpthickness = 3;

            ModelRenderer thread2 = new ModelRenderer(this, 348, 0);
            thread2.addBox(0, 0, 0, 32, 16, 1);
            thread2.setRotationPoint(20 + 20, 24 - 0.5F, 49-(-1.5F + 6 + 0.5F));
            thread2.rotateAngleX =(float) Math.PI*2- ((float) Math.PI / 2 * 3 - (float) Math.atan2(24 - 10, -5 + warpthickness / 2.0));
            //thread2.rotateAngleX = (float) Math.toRadians(-90);
            //thread2.rotateAngleY = (float) Math.atan2(24-15, -5+warpthickness/2.0);
            //thread2.rotateAngleZ = (float) Math.toRadians(90);
            //this.threads.addChild(thread2);

            ModelRenderer thread4 = new ModelRenderer(this, 348, 35);
            thread4.addBox(0, 0, 0, 32, 15, 1);
            thread4.setRotationPoint(20 + 20, 24 - 1F, 49-(38 + 3F + 4));
            thread4.rotateAngleX = (float) Math.PI*2 - ((float) Math.PI / 2 * 3 - (float) Math.atan2(24 - 10, 4 - warpthickness / 2.0));

            //this.threads.addChild(thread4);
            this.threads_warp_lower.addChild(thread2);

            this.threads_warp_higher.addChild(thread2);

            for (int i = 0; i < 40; i++) {
                ModelRenderer thread_cloth_animated = new ModelRenderer(this, 348, 84 - i);
                thread_cloth_animated.addBox(0, 0, 0, 32, 15, 1);
                thread_cloth_animated.setRotationPoint(20 + 20, 24 - 1F, 49-(38 + 3F + 4));
                thread_cloth_animated.rotateAngleX = (float)Math.PI*2-((float) Math.PI / 2 * 3 - (float) Math.atan2(24 - 10, 4 - warpthickness / 2.0));
                this.threads_cloth_lower.addChild(thread_cloth_animated);
                this.threads.addChild(thread_cloth_animated);

                ModelRenderer thread_cloth_animated2 = new ModelRenderer(this, 414, 84 - i);
                thread_cloth_animated2.addBox(0, 0, 0, 32, 15, 1);
                thread_cloth_animated2.setRotationPoint(20 + 20, 24 - 1F, 49-(38 + 3F + 4));
                thread_cloth_animated2.rotateAngleX = (float)Math.PI*2-((float) Math.PI / 2 * 3 - (float) Math.atan2(24 - 10, 4 - warpthickness / 2.0));
                this.threads_cloth_lower2.addChild(thread_cloth_animated2);
                this.threads.addChild(thread_cloth_animated2);
            }
            for (int i = 0; i < 20; i++) {
                ModelRenderer thread_warp_animated = new ModelRenderer(this, 348, 2+i);
                thread_warp_animated.addBox(0, 0, 0, 32, 15, 1);
                thread_warp_animated.setRotationPoint(20 + 20, 24 - 0.5F, 49-(-1.5F + 6 + 0.5F));
                thread_warp_animated.rotateAngleX =(float) Math.PI*2- ((float) Math.PI / 2 * 3 - (float) Math.atan2(24 - 10, -5 + warpthickness / 2.0));
                this.warp_lower_stages.addChild(thread_warp_animated);
                this.threads.addChild(thread_warp_animated);

                ModelRenderer thread_warp_animated2 = new ModelRenderer(this, 414, 2+i);
                thread_warp_animated2.addBox(0, 0, 0, 32, 15, 1);
                thread_warp_animated2.setRotationPoint(20 + 20, 24 - 0.5F, 49-(-1.5F + 6 + 0.5F));
                thread_warp_animated2.rotateAngleX =(float) Math.PI*2- ((float) Math.PI / 2 * 3 - (float) Math.atan2(24 - 10, -5 + warpthickness / 2.0));
                this.warp_lower_stages2.addChild(thread_warp_animated2);
                this.threads.addChild(thread_warp_animated2);
            }


            this.split_warp = new ModelRenderer(this, "split_warp");
            this.split_warp2 = new ModelRenderer(this, "split_warp2");
            for (int i = 0; i < 32; i++) {
                if (i % 2 == 0) {

                    ModelRenderer split_thread = new ModelRenderer(this, 348 + i, 0);
                    split_thread.addBox(0, 0, 0, 1, 27, 1);
                    split_thread.setRotationPoint(20 + 20 + i, 21, 48-17);
                    split_thread.rotateAngleX = (float) Math.PI*2-((float) Math.PI / 2 + (float) Math.atan2(-3, 18 + 8));

                    ModelRenderer split_thread2 = new ModelRenderer(this, 348 + i, 0);
                    split_thread2.addBox(0, 0, 0, 1, 13, 1);
                    split_thread2.setRotationPoint(20 + 20 + i, 22, 48-18);
                    split_thread2.rotateAngleX = (float)Math.PI*2-((float) Math.PI / 2 - (float) Math.atan2(3, -18));

                    this.threads.addChild(split_thread);
                    this.split_warp.addChild(split_thread);
                    this.threads.addChild(split_thread2);
                    this.split_warp.addChild(split_thread2);

                    ModelRenderer split_thread3 = new ModelRenderer(this, 414 + i, 0);
                    split_thread3.addBox(0, 0, 0, 1, 27, 1);
                    split_thread3.setRotationPoint(20 + 20 + i, 21, 48-17);
                    split_thread3.rotateAngleX = (float) Math.PI*2-((float) Math.PI / 2 + (float) Math.atan2(-3, 18 + 8));

                    ModelRenderer split_thread4 = new ModelRenderer(this, 414 + i, 0);
                    split_thread4.addBox(0, 0, 0, 1, 13, 1);
                    split_thread4.setRotationPoint(20 + 20 + i, 22, 48-18);
                    split_thread4.rotateAngleX = (float)Math.PI*2-((float) Math.PI / 2 - (float) Math.atan2(3, -18));

                    this.threads.addChild(split_thread3);
                    this.split_warp2.addChild(split_thread3);
                    this.threads.addChild(split_thread4);
                    this.split_warp2.addChild(split_thread4);


                } else {
                    ModelRenderer split_thread = new ModelRenderer(this, 348 + i, 0);
                    split_thread.addBox(0, 0, 0, 1, 27, 1);
                    split_thread.setRotationPoint(20 + 20 + i, 28, 48-17);
                    split_thread.rotateAngleX = -((float) Math.PI / 2 + (float) Math.atan2(4, 18 + 8));

                    ModelRenderer split_thread2 = new ModelRenderer(this, 348 + i, 0);
                    split_thread2.addBox(0, 0, 0, 1, 13, 1);
                    split_thread2.setRotationPoint(20 + 20 + i, 29, 48-18);
                    split_thread2.rotateAngleX = -((float) Math.PI / 2 - (float) Math.atan2(-4, -18));

                    this.threads.addChild(split_thread);
                    this.split_warp.addChild(split_thread);
                    this.threads.addChild(split_thread2);
                    this.split_warp.addChild(split_thread2);

                    ModelRenderer split_thread3 = new ModelRenderer(this, 414 + i, 0);
                    split_thread3.addBox(0, 0, 0, 1, 27, 1);
                    split_thread3.setRotationPoint(20 + 20 + i, 28, 48-17);
                    split_thread3.rotateAngleX = -((float) Math.PI / 2 + (float) Math.atan2(4, 18 + 8));

                    ModelRenderer split_thread4 = new ModelRenderer(this, 414 + i, 0);
                    split_thread4.addBox(0, 0, 0, 1, 13, 1);
                    split_thread4.setRotationPoint(20 + 20 + i, 29, 48-18);
                    split_thread4.rotateAngleX = -((float) Math.PI / 2 - (float) Math.atan2(-4, -18));

                    this.threads.addChild(split_thread3);
                    this.split_warp2.addChild(split_thread3);
                    this.threads.addChild(split_thread4);
                    this.split_warp2.addChild(split_thread4);

                }
            }
        } else {
            this.base = new ModelRenderer(this, 0, 0);
            this.base.addBox(32, 0, 0, 16 * 3, 4, 16 * 3);

            ModelRenderer base2 = new ModelRenderer(this, 16, 54 + 16);
            base2.addBox(0, 0, 0, 16 * 2, 4, 16);
            base2.setRotationPoint(16, 0, 32);
            base2.rotateAngleY = (float) Math.PI / 2;
            this.base.addChild(base2);

            ModelRenderer base3 = new ModelRenderer(this, 99, 81);
            base3.addBox(0, 0, 0, 42, 16, 28);
            base3.setRotationPoint(35+42,0,11+28);
            base3.rotateAngleY = (float)Math.PI;
            this.base.addChild(base3);

            ModelRenderer base4 = new ModelRenderer(this, 239, 97);
            base4.addBox(0, 0, 0, 32, 12, 17);
            base4.setRotationPoint(16, 4, 32);
            base4.rotateAngleY = (float) Math.PI / 2;
            this.base.addChild(base4);

            ModelRenderer powerInput = new ModelRenderer(this, 144, 0);
            powerInput.addBox(54 + 23 + 3, 4, 16, 16, 28, 16);
            this.base.addChild(powerInput);

            ModelRenderer redstoneToggle = new ModelRenderer(this, 349, 104);
            redstoneToggle.addBox(0, 0, 0, 16, 16, 8);
            redstoneToggle.setRotationPoint(16, 16, 16);
            redstoneToggle.rotateAngleY = (float) Math.PI / 2;
            //this.base.addChild(redstoneToggle);

            ModelRenderer powerBase = new ModelRenderer(this, 128, 54);
            powerBase.addBox(54 + 23 + 3, 0, 16, 16, 4, 16);
            this.base.addChild(powerBase);

            this.split_warp_left = new ModelRenderer(this, "split_warp_left");
            this.split_warp_right = new ModelRenderer(this, "split_warp_right");

            ModelRenderer splitter_frame = new ModelRenderer(this, 228, 0);
            splitter_frame.addBox(74, 4, 10.5F, 6, 43, 14);
            ModelRenderer splitter_frame2 = new ModelRenderer(this, 268, 0);
            splitter_frame2.addBox(32, 4, 10.5F, 6, 43, 14);
            splitter_frame.addChild(splitter_frame2);
            ModelRenderer splitter_frame_top = new ModelRenderer(this, 308, 0);
            splitter_frame_top.addBox(41, -24 - 32 - 16 - 4 - 1.5F + 4 - 0.5F, 16 - 5.5F, 6, 36, 14);
            splitter_frame_top.rotateAngleZ = (float) Math.toRadians(90);
            splitter_frame.addChild(splitter_frame_top);
            this.base.addChild(splitter_frame);
            ModelRenderer splitter_board = new ModelRenderer(this, 0, 92);
            splitter_board.addBox(37, 38, 18, 38, 3, 1);
            ModelRenderer splitter_board2 = new ModelRenderer(this, 0, 92);
            splitter_board2.addBox(37, 38, 16, 38, 3, 1);
            splitter_frame.addChild(splitter_board);
            splitter_frame.addChild(splitter_board2);
            this.split_warp_left.addChild(splitter_board);
            this.split_warp_right.addChild(splitter_board2);

            warp_roller = new ModelRenderer(this, 8, 0);
            warp_roller.addBox(-1.5F, 0, -1.5F, 3, 40, 3);
            warp_roller.setRotationPoint(24 + 32 + 16 + 4, 10, 5);
            warp_roller.rotateAngleZ = (float) Math.toRadians(90);
            this.base.addChild(warp_roller);

            warp_beam = new ModelRenderer(this, 8, 0);
            warp_beam.addBox(-1.5F, 0, -1.5F, 3, 40, 3);
            warp_beam.setRotationPoint(24 + 32 + 16 + 4, 24, 6F);
            warp_beam.rotateAngleZ = (float) Math.toRadians(90);
            this.base.addChild(warp_beam);

            cloth_roller = new ModelRenderer(this, 8, 0);
            cloth_roller.addBox(-1.5F, 0, -1.5F, 3, 40, 3);
            cloth_roller.rotateAngleZ = (float) Math.toRadians(90);
            cloth_roller.setRotationPoint(24 + 32 + 16 + 4, 10, 43);
            this.base.addChild(cloth_roller);

            cloth_beam = new ModelRenderer(this, 8, 0);
            cloth_beam.addBox(-1.5F, 0, -1.5F, 3, 40, 3);
            cloth_beam.rotateAngleZ = (float) Math.toRadians(90);
            cloth_beam.setRotationPoint(24 + 32 + 16 + 4, 24, 43);
            this.base.addChild(cloth_beam);

            beam_cap = new ModelRenderer(this, 0, 54);
            beam_cap.addBox(-5F, -5F, 0, 10, 10, 1);
            beam_cap2 = new ModelRenderer(this, 0, 54);
            beam_cap2.addBox(-5F, -5F, 0, 10, 10, 1);
            beam_cap.rotateAngleX = (float) Math.toRadians(90);
            beam_cap2.rotateAngleX = (float) Math.toRadians(90);
            beam_cap.rotateAngleZ = (float) Math.toRadians(90);
            beam_cap2.rotateAngleZ = (float) Math.toRadians(90);
            beam_cap.setRotationPoint(24 + 32 + 16 + 4, 11.5F - 1.5F, 6.5F - 1.5F);
            beam_cap2.setRotationPoint(24 + 32 + 16 + 4, 11.5F - 1.5F, 6.5F + 27 + 16 - 5 - 1.5F);
            //his.base.addChild(beam_cap);
            //this.base.addChild(beam_cap2);

            this.warp_roll = new ModelRenderer(this, 348, 0);
            warp_roll.addBox(-1F, 0, -1F, 2, 34, 3);
            warp_roll.setRotationPoint(24 + 32 + 16 + 4, 24, 32);
            warp_roll.rotateAngleZ = (float) Math.toRadians(90);

            ModelRenderer shuttle_beam = new ModelRenderer(this, 211, 79);
            shuttle_beam.addBox(0, 0, -1, 62, 5, 5);
            shuttle_beam.setRotationPoint(19, 30, 25);
            shuttle_beam_connectors = new ModelRenderer(this, "shuttle_beam_connectors");

            ModelRenderer shuttle_beam_connector = new ModelRenderer(this, 0, 108);
            shuttle_beam_connector.addBox(0, 0, 0, 3, 8, 3);
            ModelRenderer shuttle_beam_connector2 = new ModelRenderer(this, 0, 108);
            shuttle_beam_connector2.addBox(0, 0, 0, 3, 8, 3);
            shuttle_beam_connector.setRotationPoint(38 - 4.5F - 14, 23, 25);
            shuttle_beam_connector2.setRotationPoint(54 + 23, 23, 25);
            shuttle_beam_connectors.addChild(shuttle_beam_connector);
            shuttle_beam_connectors.addChild(shuttle_beam_connector2);
            shuttle_beam_super = new ModelRenderer(this, "shuttle_beam_super");
            shuttle_beam_super.addChild(shuttle_beam_connector);
            shuttle_beam_super.addChild(shuttle_beam_connector2);

            ModelRenderer shuttle_bus = new ModelRenderer(this, 211, 89);
            shuttle_bus.addBox(0, 0, -1, 62, 3, 5);
            shuttle_bus.setRotationPoint(19, 20, 25);
            this.base.addChild(shuttle_beam);
            this.base.addChild(shuttle_beam_connectors);
            this.base.addChild(shuttle_bus);
            this.shuttle_beam_super.addChild(shuttle_beam);
            this.shuttle_beam_super.addChild(shuttle_bus);
            for (int i = 0; i < 5; i++) {
                ModelRenderer warp_stage = new ModelRenderer(this, 348, 0);
                warp_stage.addBox(-2F - i / 2.0F, 0, -2F - i / 2.0F, 4 + i, 32, 4 + i);
                warp_stage.setRotationPoint(24 + 32 + 16, 10, 5);
                warp_stage.rotateAngleZ = (float) Math.toRadians(90);
                warp_stage.isHidden = true;
                this.base.addChild(warp_stage);
                this.warp_stages[i] = warp_stage;

                ModelRenderer cloth_stage = new ModelRenderer(this, 348, 36);
                cloth_stage.addBox(-2F - i / 2.0F, 0, -2F - i / 2.0F, 4 + i, 32, 4 + i);
                cloth_stage.setRotationPoint(24 + 32 + 16, 10, 43);
                cloth_stage.rotateAngleZ = (float) Math.toRadians(90);
                cloth_stage.isHidden = true;
                this.base.addChild(cloth_stage);
                this.cloth_stages[i] = cloth_stage;

                ModelRenderer warp_stage2 = new ModelRenderer(this, 414, 0);
                warp_stage2.addBox(-2F - i / 2.0F, 0, -2F - i / 2.0F, 4 + i, 32, 4 + i);
                warp_stage2.setRotationPoint(24 + 32 + 16, 10, 5);
                warp_stage2.rotateAngleZ = (float) Math.toRadians(90);
                warp_stage2.isHidden = true;
                this.base.addChild(warp_stage2);
                this.warp_stages2[i] = warp_stage2;

                ModelRenderer cloth_stage2 = new ModelRenderer(this, 414, 36);
                cloth_stage2.addBox(-2F - i / 2.0F, 0, -2F - i / 2.0F, 4 + i, 32, 4 + i);
                cloth_stage2.setRotationPoint(24 + 32 + 16, 10, 43);
                cloth_stage2.rotateAngleZ = (float) Math.toRadians(90);
                cloth_stage2.isHidden = true;
                this.base.addChild(cloth_stage2);
                this.cloth_stages2[i] = cloth_stage2;
            }

            ModelRenderer shuttle_frame_driver = new ModelRenderer(this, 222, 57);
            shuttle_frame_driver.addBox(0, 0, 0, 16, 16, 6);
            shuttle_frame_driver.setRotationPoint(38 - 6F, 4, 40);
            shuttle_frame_driver.rotateAngleY = (float) Math.PI / 2;
            this.base.addChild(shuttle_frame_driver);
            ModelRenderer shuttle_frame_driver2 = new ModelRenderer(this, 222, 57);
            shuttle_frame_driver2.addBox(0, 0, 0, 16, 16, 6);
            shuttle_frame_driver2.setRotationPoint(54 + 23 - 3, 4, 40);
            shuttle_frame_driver2.rotateAngleY = (float) Math.PI / 2;
            this.base.addChild(shuttle_frame_driver2);

            ModelRenderer roller_supporter = new ModelRenderer(this, 12, 98);
            roller_supporter.addBox(0, 0, 0, 10, 24, 4);
            roller_supporter.setRotationPoint(38 - 5F, 4, 48F);
            roller_supporter.rotateAngleY = (float) Math.PI / 2;
            this.base.addChild(roller_supporter);

            ModelRenderer roller_supporter2 = new ModelRenderer(this, 40, 98);
            roller_supporter2.addBox(0, 0, 0, 10, 24, 4);
            roller_supporter2.setRotationPoint(54 + 23 - 2, 4, 48F);
            roller_supporter2.rotateAngleY = (float) Math.PI / 2;
            this.base.addChild(roller_supporter2);

            ModelRenderer roller_supporter3 = new ModelRenderer(this, 68, 98);
            roller_supporter3.addBox(0, 0, 0, 10, 24, 4);
            roller_supporter3.setRotationPoint(38 - 5F, 4, 10.5F);
            roller_supporter3.rotateAngleY = (float) Math.PI / 2;
            this.base.addChild(roller_supporter3);

            ModelRenderer roller_supporter4 = new ModelRenderer(this, 310, 50);
            roller_supporter4.addBox(0, 0, 0, 10, 24, 4);
            roller_supporter4.setRotationPoint(54 + 23 - 2, 4, 10.5F);
            roller_supporter4.rotateAngleY = (float) Math.PI / 2;
            this.base.addChild(roller_supporter4);

            ModelRenderer sudo_warp_roll = new ModelRenderer(this, 348, 0);
            sudo_warp_roll.addBox(-2F, 0, -2F, 4, 32, 4);
            sudo_warp_roll.setRotationPoint(24 + 32 + 16, 24, 6F);
            sudo_warp_roll.rotateAngleZ = (float) Math.toRadians(90);
            this.base.addChild(sudo_warp_roll);
            this.sudo_warp = sudo_warp_roll;

            ModelRenderer sudo_cloth_roll = new ModelRenderer(this, 348, 36);
            sudo_cloth_roll.addBox(-2F, 0, -2F, 4, 32, 4);
            sudo_cloth_roll.setRotationPoint(24 + 32 + 16, 24, 43);
            sudo_cloth_roll.rotateAngleZ = (float) Math.toRadians(90);
            this.base.addChild(sudo_cloth_roll);
            this.sudo_cloth = sudo_cloth_roll;

            sudo_cloth_unfinished = new ModelRenderer(this, 348, 0);
            sudo_cloth_unfinished.addBox(-2F, 0, -2F, 4, 32, 4);
            sudo_cloth_unfinished.setRotationPoint(24 + 32 + 16, 24, 43);
            sudo_cloth_unfinished.rotateAngleZ = (float) Math.toRadians(90);
            this.base.addChild(sudo_cloth_unfinished);

            ModelRenderer sudo_warp_roll2 = new ModelRenderer(this, 414, 0);
            sudo_warp_roll2.addBox(-2F, 0, -2F, 4, 32, 4);
            sudo_warp_roll2.setRotationPoint(24 + 32 + 16, 24, 6F);
            sudo_warp_roll2.rotateAngleZ = (float) Math.toRadians(90);
            this.base.addChild(sudo_warp_roll2);
            this.sudo_warp2 = sudo_warp_roll2;

            ModelRenderer sudo_cloth_roll2 = new ModelRenderer(this, 414, 36);
            sudo_cloth_roll2.addBox(-2F, 0, -2F, 4, 32, 4);
            sudo_cloth_roll2.setRotationPoint(24 + 32 + 16, 24, 43);
            sudo_cloth_roll2.rotateAngleZ = (float) Math.toRadians(90);
            this.base.addChild(sudo_cloth_roll2);
            this.sudo_cloth2 = sudo_cloth_roll2;

            sudo_cloth_unfinished2 = new ModelRenderer(this, 414, 0);
            sudo_cloth_unfinished2.addBox(-2F, 0, -2F, 4, 32, 4);
            sudo_cloth_unfinished2.setRotationPoint(24 + 32 + 16, 24, 43);
            sudo_cloth_unfinished2.rotateAngleZ = (float) Math.toRadians(90);
            this.base.addChild(sudo_cloth_unfinished2);

            this.pirn_loader = new ModelRenderer(this, 0, 0);
            this.splitter_frame_shuttle = new ModelRenderer(this, "splitter_frame_shuttle");
            this.base.addChild(splitter_frame_shuttle);
            for (int i = 0; i < 32; i++) {
                if (i % 2 == 0) {
                    ModelRenderer splitterwire = new ModelRenderer(this, 20, 0);
                    splitterwire.addBox(i + 20 + 15 + 5, 20, 18, 1, 18, 1);
                    splitter_frame.addChild(splitterwire);
                    this.split_warp_left.addChild(splitterwire);

                    ModelRenderer splitterwireshuttle = new ModelRenderer(this, 20, 0);
                    splitterwireshuttle.addBox(0, 0, 0, 1, 9, 1);
                    splitterwireshuttle.setRotationPoint(i + 20 + 15 + 5 + 0.5F, 21, 18 + 7.5F);
                    splitter_frame_shuttle.addChild(splitterwireshuttle);
                } else {
                    ModelRenderer splitterwire = new ModelRenderer(this, 20, 0);
                    splitterwire.addBox(i + 20 + 15 + 5, 20, 16, 1, 18, 1);
                    splitter_frame.addChild(splitterwire);
                    this.split_warp_right.addChild(splitterwire);
                }
            }
            this.pirn_pile = new ModelRenderer(this, "pirn_pile");
            for (int i = 0; i < 4; i++) {
                for (int j = i; j < 4; j++) {
                    ModelRenderer empty_pirn = new ModelRenderer(this, 0, 11);
                    empty_pirn.addBox(0, 0, 0, 2, 9, 2);
                    empty_pirn.rotateAngleZ = (float) Math.PI / 2;
                    empty_pirn.setRotationPoint(28, 16 + i * 2, 4 - i * 1.5F + j * 3);
                    this.pirn_pile.addChild(empty_pirn);
                }
            }
            this.base.addChild(this.pirn_pile);
            //this.pirn_loader.addBox(32,0,0,2,9,2);
            //this.base.addChild(pirn_loader);
            //this.pirn_loader.rotateAngleZ = (float)Math.toRadians(90);
            for (int i = 0; i < 8; i++) {
                ModelRenderer pirn = new ModelRenderer(this, 0, 0);
                pirn.addBox(0, 0, 0, 2, 9, 2);
                pirn.rotateAngleZ = (float) Math.PI / 2;
                pirn.setRotationPoint(32, 25 + (float) Math.round(Math.cos(Math.toRadians(i * 360 / 8)) * 4), (float) Math.round(Math.sin(Math.toRadians(i * 360 / 8)) * 4) + 19);
                this.base.addChild(pirn);
                this.pirn_loader.addChild(pirn);
            }
            for (int i = 0; i < 8; i++) {
                ModelRenderer pirn2 = new ModelRenderer(this, 0, 22);
                pirn2.addBox(0, 0, 0, 2, 9, 2);
                pirn2.rotateAngleZ = (float) Math.PI / 2;
                pirn2.setRotationPoint(32, 25 + (float) Math.round(Math.cos(Math.toRadians(i * 360 / 8)) * 4), (float) Math.round(Math.sin(Math.toRadians(i * 360 / 8)) * 4) + 19);
                this.base.addChild(pirn2);
                this.pirn_loader.addChild(pirn2);
            }
            pirn_loader_base = new ModelRenderer(this, 20, 19);
            pirn_loader_base.addBox(-3, 0, -3, 6, 10, 6);
            pirn_loader_base.setRotationPoint(32, 26, 20);
            pirn_loader_base.rotateAngleZ = (float) Math.toRadians(90);
            this.base.addChild(pirn_loader_base);

            ModelRenderer pirn_loader_cap = new ModelRenderer(this, 0, 54);
            pirn_loader_cap.addBox(-5F, -5F, 0, 10, 10, 1);
            pirn_loader_cap.rotateAngleX = (float) Math.toRadians(90);
            pirn_loader_cap.rotateAngleZ = (float) Math.toRadians(90);
            pirn_loader_cap.setRotationPoint(21.5F, 11.5F - 1.5F + 21, 6.5F - 1.5F + 14);
            //this.base.addChild(pirn_loader_cap);

            this.threads = new ModelRenderer(this, "threads");
            this.threads_warp_higher = new ModelRenderer(this, "threads_warp_higher");
            this.threads_warp_lower = new ModelRenderer(this, "threads_warp_lower");
            this.warp_lower_stages = new ModelRenderer(this, "warp_lower_stages");
            this.threads_cloth_lower = new ModelRenderer(this, "threads_cloth_lower");
            this.warp_lower_stages2 = new ModelRenderer(this, "warp_lower_stages2");
            this.threads_cloth_lower2 = new ModelRenderer(this, "threads_cloth_lower2");
            int warpthickness = 3;

            ModelRenderer thread2 = new ModelRenderer(this, 348, 0);
            thread2.addBox(0, 0, 0, 32, 16, 1);
            thread2.setRotationPoint(20 + 20, 24 - 0.5F, -1.5F + 6 + 0.5F);
            thread2.rotateAngleX = (float) Math.PI / 2 * 3 - (float) Math.atan2(24 - 10, -5 + warpthickness / 2.0);
            //thread2.rotateAngleX = (float) Math.toRadians(-90);
            //thread2.rotateAngleY = (float) Math.atan2(24-15, -5+warpthickness/2.0);
            //thread2.rotateAngleZ = (float) Math.toRadians(90);
            //this.threads.addChild(thread2);

            ModelRenderer thread4 = new ModelRenderer(this, 348, 35);
            thread4.addBox(0, 0, 0, 32, 15, 1);
            thread4.setRotationPoint(20 + 20, 24 - 1F, 38 + 3F + 4);
            thread4.rotateAngleX = (float) Math.PI / 2 * 3 - (float) Math.atan2(24 - 10, 4 - warpthickness / 2.0);

            //this.threads.addChild(thread4);
            this.threads_warp_lower.addChild(thread2);

            this.threads_warp_higher.addChild(thread2);
            //this.threads_warp_higher.addChild(thread4);

            for (int i = 0; i < 40; i++) {
                ModelRenderer thread_cloth_animated = new ModelRenderer(this, 348, 84 - i);
                thread_cloth_animated.addBox(0, 0, 0, 32, 15, 1);
                thread_cloth_animated.setRotationPoint(20 + 20, 24 - 1F, 38 + 3F + 4);
                thread_cloth_animated.rotateAngleX = (float) Math.PI / 2 * 3 - (float) Math.atan2(24 - 10, 4 - warpthickness / 2.0);
                this.threads_cloth_lower.addChild(thread_cloth_animated);
                this.threads.addChild(thread_cloth_animated);

                ModelRenderer thread_cloth_animated2 = new ModelRenderer(this, 414, 84 - i);
                thread_cloth_animated2.addBox(0, 0, 0, 32, 15, 1);
                thread_cloth_animated2.setRotationPoint(20 + 20, 24 - 1F, 38 + 3F + 4);
                thread_cloth_animated2.rotateAngleX = (float) Math.PI / 2 * 3 - (float) Math.atan2(24 - 10, 4 - warpthickness / 2.0);
                this.threads_cloth_lower2.addChild(thread_cloth_animated2);
                this.threads.addChild(thread_cloth_animated2);
            }
            for (int i = 0; i < 20; i++) {
                ModelRenderer thread_warp_animated = new ModelRenderer(this, 348, 2+i);
                thread_warp_animated.addBox(0, 0, 0, 32, 15, 1);
                thread_warp_animated.setRotationPoint(20 + 20, 24 - 0.5F, -1.5F + 6 + 0.5F);
                thread_warp_animated.rotateAngleX = (float) Math.PI / 2 * 3 - (float) Math.atan2(24 - 10, -5 + warpthickness / 2.0);
                this.warp_lower_stages.addChild(thread_warp_animated);
                this.threads.addChild(thread_warp_animated);

                ModelRenderer thread_warp_animated2 = new ModelRenderer(this, 414, 2+i);
                thread_warp_animated2.addBox(0, 0, 0, 32, 15, 1);
                thread_warp_animated2.setRotationPoint(20 + 20, 24 - 0.5F, -1.5F + 6 + 0.5F);
                thread_warp_animated2.rotateAngleX = (float) Math.PI / 2 * 3 - (float) Math.atan2(24 - 10, -5 + warpthickness / 2.0);
                this.warp_lower_stages2.addChild(thread_warp_animated2);
                this.threads.addChild(thread_warp_animated2);
            }


            this.split_warp = new ModelRenderer(this, "split_warp");
            this.split_warp2 = new ModelRenderer(this, "split_warp2");
            for (int i = 0; i < 32; i++) {
                if (i % 2 == 0) {

                    ModelRenderer split_thread = new ModelRenderer(this, 348 + i, 0);
                    split_thread.addBox(0, 0, 0, 1, 27, 1);
                    split_thread.setRotationPoint(20 + 20 + i, 23, 17);
                    split_thread.rotateAngleX = (float) Math.PI / 2 + (float) Math.atan2(-3, 18 + 8);

                    ModelRenderer split_thread2 = new ModelRenderer(this, 348 + i, 0);
                    split_thread2.addBox(0, 0, 0, 1, 13, 1);
                    split_thread2.setRotationPoint(20 + 20 + i, 22, 18);
                    split_thread2.rotateAngleX = (float) Math.PI / 2 - (float) Math.atan2(3, -18);

                    this.threads.addChild(split_thread);
                    this.split_warp.addChild(split_thread);
                    this.threads.addChild(split_thread2);
                    this.split_warp.addChild(split_thread2);

                    ModelRenderer split_thread3 = new ModelRenderer(this, 414 + i, 0);
                    split_thread3.addBox(0, 0, 0, 1, 27, 1);
                    split_thread3.setRotationPoint(20 + 20 + i, 23, 17);
                    split_thread3.rotateAngleX = (float) Math.PI / 2 + (float) Math.atan2(-3, 18 + 8);

                    ModelRenderer split_thread4 = new ModelRenderer(this, 414 + i, 0);
                    split_thread4.addBox(0, 0, 0, 1, 13, 1);
                    split_thread4.setRotationPoint(20 + 20 + i, 22, 18);
                    split_thread4.rotateAngleX = (float) Math.PI / 2 - (float) Math.atan2(3, -18);

                    this.threads.addChild(split_thread3);
                    this.split_warp2.addChild(split_thread3);
                    this.threads.addChild(split_thread4);
                    this.split_warp2.addChild(split_thread4);


                } else {
                    ModelRenderer split_thread = new ModelRenderer(this, 348 + i, 0);
                    split_thread.addBox(0, 0, 0, 1, 27, 1);
                    split_thread.setRotationPoint(20 + 20 + i, 30, 17);
                    split_thread.rotateAngleX = (float) Math.PI / 2 + (float) Math.atan2(4, 18 + 8);

                    ModelRenderer split_thread2 = new ModelRenderer(this, 348 + i, 0);
                    split_thread2.addBox(0, 0, 0, 1, 13, 1);
                    split_thread2.setRotationPoint(20 + 20 + i, 29, 18);
                    split_thread2.rotateAngleX = (float) Math.PI / 2 - (float) Math.atan2(-4, -18);

                    this.threads.addChild(split_thread);
                    this.split_warp.addChild(split_thread);
                    this.threads.addChild(split_thread2);
                    this.split_warp.addChild(split_thread2);

                    ModelRenderer split_thread3 = new ModelRenderer(this, 414 + i, 0);
                    split_thread3.addBox(0, 0, 0, 1, 27, 1);
                    split_thread3.setRotationPoint(20 + 20 + i, 30, 17);
                    split_thread3.rotateAngleX = (float) Math.PI / 2 + (float) Math.atan2(4, 18 + 8);

                    ModelRenderer split_thread4 = new ModelRenderer(this, 414 + i, 0);
                    split_thread4.addBox(0, 0, 0, 1, 13, 1);
                    split_thread4.setRotationPoint(20 + 20 + i, 29, 18);
                    split_thread4.rotateAngleX = (float) Math.PI / 2 - (float) Math.atan2(-4, -18);

                    this.threads.addChild(split_thread3);
                    this.split_warp2.addChild(split_thread3);
                    this.threads.addChild(split_thread4);
                    this.split_warp2.addChild(split_thread4);

                }
            }
        }
    }

    @Override
    public void render(Entity entity, float processType, float amountInput, float amountOutput, float amountPirns, float primerCount, float f5)
    {
        //System.out.println("processType "+processType);
        if (!this.mirror) {
            int tickAmplified = Math.round(this.ticks * 500);
            //System.out.println("TICKAMPLIFIED "+tickAmplified+" "+(this.ticks*500)+" "+this.ticks);
            if (tickAmplified == 50 && !this.has_rotated) {
                this.has_rotated = true;
            }
            if (tickAmplified == 0 && this.has_rotated) {
                this.has_rotated = false;
            }
            this.sub_rotation = tickAmplified < 50 ? tickAmplified : 0;
            //System.out.println(this.sub_rotation);
            //this.cloth_beam.rotateAngleY += 0.01;
            if (tickAmplified < 50) {
                this.cloth_roller.rotateAngleY = (float) Math.PI * 2 * 0 / 225;
                this.warp_roller.rotateAngleY = (float) Math.PI * 2 * 0 / 225;
            } else {
                this.cloth_roller.rotateAngleY = (float) Math.PI * 2 * (tickAmplified-50) / 225;
                this.warp_roller.rotateAngleY = (float) Math.PI * 2 * (tickAmplified-50) / 225;
            }
            //this.warp_beam.rotateAngleY += 0.01;
            //this.warp_roller.rotateAngleY += 0.01;
            //this.beam_cap.rotateAngleY += 0.01;
            //this.beam_cap2.rotateAngleY += 0.01;
            //this.warp_roll.rotateAngleY += 0.01;

            this.sudo_cloth.isHidden = !(Math.round(primerCount) > 15) || (tickAmplified < 20 && (Math.round(amountOutput) <= 0)) || (Math.round(processType) == BURLAP.ordinal());
            this.sudo_warp.isHidden = !(Math.round(primerCount) > 15) || (Math.round(processType) == BURLAP.ordinal());
            this.sudo_cloth2.isHidden = !(Math.round(primerCount) > 15) || (tickAmplified < 20 && (Math.round(amountOutput) <= 0)) || (Math.round(processType) != BURLAP.ordinal());
            this.sudo_warp2.isHidden = !(Math.round(primerCount) > 15) || (Math.round(processType) != BURLAP.ordinal());
            this.threads.isHidden = !(Math.round(primerCount) > 15);
            this.threads_warp_lower.isHidden = !(Math.round(amountInput) > 0);
            this.warp_lower_stages.isHidden = !(Math.round(amountInput) > 0);
            this.warp_lower_stages2.isHidden = !(Math.round(amountInput) > 0);
            this.sudo_cloth_unfinished.isHidden = !(Math.round(primerCount) > 15) || (tickAmplified >= 20 || (Math.round(amountOutput) > 0)) || (Math.round(processType) == BURLAP.ordinal());
            this.sudo_cloth_unfinished2.isHidden = !(Math.round(primerCount) > 15) || (tickAmplified >= 20 || (Math.round(amountOutput) > 0)) || (Math.round(processType) != BURLAP.ordinal());
            //System.out.println("amplified "+(tickAmplified*20)/500);
            if (Math.round(amountOutput) == 0) {
                for (int i = 0; i < 40; i++) {
                    if (tickAmplified < 50) {
                        this.threads_cloth_lower.childModels.get(i).isHidden = i != 0 || (Math.round(processType) == BURLAP.ordinal());
                        this.threads_cloth_lower2.childModels.get(i).isHidden = i != 0 || (Math.round(processType) != BURLAP.ordinal());
                    } else {
                        this.threads_cloth_lower.childModels.get(i).isHidden = i != Math.min(39, ((tickAmplified - 37) * 20) / 500) || (Math.round(processType) == BURLAP.ordinal());
                        this.threads_cloth_lower2.childModels.get(i).isHidden = i != Math.min(39, ((tickAmplified - 37) * 20) / 500) || (Math.round(processType) != BURLAP.ordinal());
                    }
                    if (i < 20) {
                        if (tickAmplified < 50) {
                            this.warp_lower_stages.childModels.get(i).isHidden = i != 0 || (Math.round(processType) == BURLAP.ordinal());
                            this.warp_lower_stages2.childModels.get(i).isHidden = i != 0 || (Math.round(processType) != BURLAP.ordinal());
                        } else {
                            this.warp_lower_stages.childModels.get(i).isHidden = i != Math.min(19, ((tickAmplified - 37) * 20) / 500) || (Math.round(processType) == BURLAP.ordinal());
                            this.warp_lower_stages2.childModels.get(i).isHidden = i != Math.min(19, ((tickAmplified - 37) * 20) / 500) || (Math.round(processType) != BURLAP.ordinal());
                        }
                    }
                }
            } else {
                for (int i = 0; i < 40; i++) {
                    if (tickAmplified < 50) {
                        this.threads_cloth_lower.childModels.get(i).isHidden = i != 20 && (Math.round(processType) == BURLAP.ordinal());
                        this.threads_cloth_lower2.childModels.get(i).isHidden = i != 20 && (Math.round(processType) != BURLAP.ordinal());
                    } else {
                        this.threads_cloth_lower.childModels.get(i).isHidden = i != Math.min(39, 17+((tickAmplified - 37) * 20) / 500) || (Math.round(processType) == BURLAP.ordinal());
                        this.threads_cloth_lower2.childModels.get(i).isHidden = i != Math.min(39, 17+((tickAmplified - 37) * 20) / 500) || (Math.round(processType) != BURLAP.ordinal());
                    }
                    if (i < 20) {
                        if (tickAmplified < 50) {
                            this.warp_lower_stages.childModels.get(i).isHidden = i != 0 && (Math.round(processType) == BURLAP.ordinal());
                            this.warp_lower_stages2.childModels.get(i).isHidden = i != 0 && (Math.round(processType) != BURLAP.ordinal());
                        } else {
                            this.warp_lower_stages.childModels.get(i).isHidden = i != Math.min(19, ((tickAmplified - 37) * 20) / 500) || (Math.round(processType) == BURLAP.ordinal());
                            this.warp_lower_stages2.childModels.get(i).isHidden = i != Math.min(19, ((tickAmplified - 37) * 20) / 500) || (Math.round(processType) != BURLAP.ordinal());
                        }
                    }
                }
            }
            //this.threads_cloth_lower.setTextureOffset(348, (tickAmplified*20)/500);

            //System.out.println("amount input "+amountInput+" "+Math.round(amountInput/192.0F*5.0F));
            for (int i = 0; i < 5; i++) {
                this.warp_stages[i].isHidden = true;
                this.warp_stages2[i].isHidden = true;
                if (tickAmplified < 50) {
                    this.warp_stages[i].rotateAngleY = (float) (((float) 0) * 2 * Math.PI / 225);
                    this.cloth_stages[i].rotateAngleY = (float) (((float) 0) * 2 * Math.PI / 225);
                    this.warp_stages2[i].rotateAngleY = (float) (((float) 0) * 2 * Math.PI / 225);
                    this.cloth_stages2[i].rotateAngleY = (float) (((float) 0) * 2 * Math.PI / 225);
                }
                else {
                    this.warp_stages[i].rotateAngleY = (float) (((float) tickAmplified - 50) * 2 * Math.PI / 225);
                    this.cloth_stages[i].rotateAngleY = (float) (((float) tickAmplified - 50) * 2 * Math.PI / 225);
                    this.warp_stages2[i].rotateAngleY = (float) (((float) tickAmplified - 50) * 2 * Math.PI / 225);
                    this.cloth_stages2[i].rotateAngleY = (float) (((float) tickAmplified - 50) * 2 * Math.PI / 225);
                }
                if (i == Math.floor((amountInput - 1) / 192.0F * 5.0F)) {
                    this.warp_stages[i].isHidden = (Math.round(processType) == BURLAP.ordinal());
                    this.warp_stages2[i].isHidden = (Math.round(processType) != BURLAP.ordinal());
                }

                this.cloth_stages[i].isHidden = true;
                this.cloth_stages2[i].isHidden = true;
                if (i == Math.floor((amountOutput - 1) / 192.0F * 5.0F)) {
                    this.cloth_stages[i].isHidden = (Math.round(processType) == BURLAP.ordinal());
                    this.cloth_stages2[i].isHidden = (Math.round(processType) != BURLAP.ordinal());
                }
            }
            //System.out.println("empty pirn "+Math.round(this.empty_pirn_count));
            for (int i = 0; i < 10; i++) {
                this.pirn_pile.childModels.get(i).isHidden = !(Math.round(this.empty_pirn_count) > ((float) i) * 128.0F / 20.0F);
            }

            float shuttle_beam_displacement = 0;
            float shuttle_launch1 = 0;
            float shuttle_launch2 = 0;
            float warp_split = 1F;

            float pirn_starty = 25 + (float) (Math.cos(Math.toRadians((1 + (float) this.sub_rotation / 50) * 360 / 8)) * 4);
            float pirn_startz = 19 + (float) (Math.sin(Math.toRadians((1 + ((float) this.sub_rotation) / 50) * 360 / 8)) * 4);
            float pirn_endy = 21.2F;
            float pirn_endz = 25.5F;
            float pirn_speedy = (pirn_endy - pirn_starty) / 50;
            float pirn_speedz = (pirn_endz - pirn_startz) / 50;
            this.pirn_loader.childModels.get(0).rotationPointX = 32;
            this.pirn_loader.childModels.get(0).rotationPointZ = pirn_endz;
            this.pirn_loader.childModels.get(8).rotationPointX = 32;
            this.pirn_loader.childModels.get(8).rotationPointZ = pirn_endz;
            if (tickAmplified < 50) {
                warp_split = 1F;
                shuttle_beam_displacement = 0;
            } else if (tickAmplified % 50 < 20) {
                warp_split = 1F;
                shuttle_beam_displacement = ((float) (Math.cos((tickAmplified % 50) * 2 * Math.PI / 20 + Math.PI)) + 1) / 2;
                //System.out.println("shuttle x "+32 + ((tickAmplified%50)-5.0F)/5.0F*(54+23-(38-4.5F-14)));
                if (tickAmplified % 50 >= 5 && tickAmplified % 50 < 10) {
                    shuttle_launch1 = ((float) (Math.cos((tickAmplified % 50 - 5) * 2 * Math.PI / 5 + Math.PI)) + 1) / 2;
                    this.pirn_loader.childModels.get(0).rotationPointX = 32 + ((tickAmplified % 50) - 4.0F) / 5.0F * 48;
                    this.pirn_loader.childModels.get(8).rotationPointX = 32 + ((tickAmplified % 50) - 4.0F) / 5.0F * 48;
                    //this.pirn_loader.childModels.get(0).rotationPointX = 32 + (54+23-(38-4.5F-14))-11;
                }
                if (tickAmplified % 50 >= 10 && tickAmplified % 50 < 15) {
                    shuttle_launch2 = ((float) (Math.cos((tickAmplified % 50 - 5) * 2 * Math.PI / 5 + Math.PI)) + 1) / 2;
                    this.pirn_loader.childModels.get(0).rotationPointX = 32 + (5 + (-(tickAmplified % 50) + 9.0F)) / 5.0F * 48;
                    this.pirn_loader.childModels.get(8).rotationPointX = 32 + (5 + (-(tickAmplified % 50) + 9.0F)) / 5.0F * 48;
                    //this.pirn_loader.childModels.get(0).rotationPointX = 32 + (54+23-(38-4.5F-14))-11;
                }

                this.pirn_loader.childModels.get(0).rotationPointZ = pirn_endz + ((float) (Math.cos((tickAmplified % 50) * 2 * Math.PI / 20 + Math.PI)) + 1) / 2 * 10;
                this.pirn_loader.childModels.get(8).rotationPointZ = pirn_endz + ((float) (Math.cos((tickAmplified % 50) * 2 * Math.PI / 20 + Math.PI)) + 1) / 2 * 10;
            } else if (tickAmplified % 50 < 25) {
                warp_split = (float) (Math.cos((tickAmplified % 50 - 20) * Math.PI / 5));
                shuttle_beam_displacement = 0;
                //this.pirn_loader.childModels.get(0).rotationPointX = 32 + (54+23-(38-4.5F-14))-11;
            } else if (tickAmplified % 50 < 45) {
                warp_split = -1F;
                shuttle_beam_displacement = ((float) (Math.cos((tickAmplified % 50 - 5) * 2 * Math.PI / 20 + Math.PI)) + 1) / 2;
                this.pirn_loader.childModels.get(0).rotationPointZ = pirn_endz + ((float) (Math.cos((tickAmplified % 50 - 5) * 2 * Math.PI / 20 + Math.PI)) + 1) / 2 * 10;
                this.pirn_loader.childModels.get(8).rotationPointZ = pirn_endz + ((float) (Math.cos((tickAmplified % 50 - 5) * 2 * Math.PI / 20 + Math.PI)) + 1) / 2 * 10;
            } else if (tickAmplified % 50 < 50) {
                warp_split = (float) (Math.cos((tickAmplified % 50 - 20) * Math.PI / 5));
                shuttle_beam_displacement = 0;
            }
            for (int i = 0; i < 4; i++) {
                this.shuttle_beam_super.childModels.get(i).rotationPointZ = 25 + shuttle_beam_displacement * 10;
            }
            this.shuttle_beam_connectors.childModels.get(0).rotationPointX = 38 - 4.5F - 14 + shuttle_launch1 * 5;
            this.shuttle_beam_connectors.childModels.get(1).rotationPointX = 54 + 23 - shuttle_launch2 * 5;
            for (ModelRenderer model : this.split_warp_left.childModels) {
                model.setRotationPoint(0, (float) (warp_split * 3.5 + 1.5), 0);
            }
            for (ModelRenderer model : this.split_warp_right.childModels) {
                model.setRotationPoint(0, (float) (-warp_split * 3.5 + 1.5), 0);
            }
            for (int i = 0; i < 64; i++) {
                ModelRenderer model = this.split_warp.childModels.get(i);
                ModelRenderer model2 = this.split_warp2.childModels.get(i);
                model.isHidden = Math.round(processType) == BURLAP.ordinal();
                model2.isHidden = Math.round(processType) != BURLAP.ordinal();
                if (i % 4 == 0) {
                    model.setRotationPoint(20 + 20 + Math.floorDiv(i, 2), 30 - 3.5F - warp_split * 3.5F, 17);
                    model.rotateAngleX = (float) Math.PI / 2 + (float) Math.atan2(0.5 - warp_split * 3.5, 18 + 8 + 3);
                    model2.setRotationPoint(20 + 20 + Math.floorDiv(i, 2), 30 - 3.5F - warp_split * 3.5F, 17);
                    model2.rotateAngleX = (float) Math.PI / 2 + (float) Math.atan2(0.5 - warp_split * 3.5, 18 + 8 + 3);
                    this.splitter_frame_shuttle.childModels.get(i / 4).rotationPointZ = 18 + 7.5F + shuttle_beam_displacement * 10;
                } else if (i % 4 == 1) {
                    model.setRotationPoint(20 + 20 + Math.floorDiv(i, 2), 29 - 3.5F - warp_split * 3.5F, 18);
                    model.rotateAngleX = (float) Math.PI / 2 - (float) Math.atan2(-0.5 + warp_split * 3.5 - 0.5, -12 - 2);
                    model2.setRotationPoint(20 + 20 + Math.floorDiv(i, 2), 29 - 3.5F - warp_split * 3.5F, 18);
                    model2.rotateAngleX = (float) Math.PI / 2 - (float) Math.atan2(-0.5 + warp_split * 3.5 - 0.5, -12 - 2);
                } else if (i % 4 == 2) {
                    model.setRotationPoint(20 + 20 + Math.floorDiv(i, 2), 23 + 3.5F + warp_split * 3.5F, 17);
                    model.rotateAngleX = (float) Math.PI / 2 + (float) Math.atan2(0.5 + warp_split * 3.5, 18 + 8 + 3);
                    model2.setRotationPoint(20 + 20 + Math.floorDiv(i, 2), 23 + 3.5F + warp_split * 3.5F, 17);
                    model2.rotateAngleX = (float) Math.PI / 2 + (float) Math.atan2(0.5 + warp_split * 3.5, 18 + 8 + 3);
                    //this.splitter_frame_shuttle.childModels.get(i/4).rotationPointZ = 18+7.5F+shuttle_beam_displacement*10;
                } else {
                    model.setRotationPoint(20 + 20 + Math.floorDiv(i, 2), 22 + 3.5F + warp_split * 3.5F, 18);
                    model.rotateAngleX = (float) Math.PI / 2 - (float) Math.atan2(-0.5 - warp_split * 3.5 - 0.5, -12 - 2);
                    model2.setRotationPoint(20 + 20 + Math.floorDiv(i, 2), 22 + 3.5F + warp_split * 3.5F, 18);
                    model2.rotateAngleX = (float) Math.PI / 2 - (float) Math.atan2(-0.5 - warp_split * 3.5 - 0.5, -12 - 2);
                }
            }
            for (int i = 0; i < 8; i++) {
                if (i == 0) {
                    if (tickAmplified < 50) {
                        this.pirn_loader.childModels.get(i).setRotationPoint(32, pirn_starty + ((float) this.sub_rotation * pirn_speedy), pirn_startz + ((float) this.sub_rotation * pirn_speedz));
                        this.pirn_loader.childModels.get(i+8).setRotationPoint(32, pirn_starty + ((float) this.sub_rotation * pirn_speedy), pirn_startz + ((float) this.sub_rotation * pirn_speedz));

                    } else {
                        this.pirn_loader.childModels.get(i).rotationPointY = pirn_endy;
                        this.pirn_loader.childModels.get(i+8).rotationPointY = pirn_endy;
                    }
                } else {
                    if (tickAmplified < 50) {
                        this.pirn_loader.childModels.get(i).setRotationPoint(32, 25 + (float) (Math.cos(Math.toRadians((-i + 1 + (float) this.sub_rotation / 50) * 360 / 8)) * 4), (float) (Math.sin(Math.toRadians((-i + 1 + (float) this.sub_rotation / 50) * 360 / 8)) * 4) + 19);
                        this.pirn_loader.childModels.get(i+8).setRotationPoint(32, 25 + (float) (Math.cos(Math.toRadians((-i + 1 + (float) this.sub_rotation / 50) * 360 / 8)) * 4), (float) (Math.sin(Math.toRadians((-i + 1 + (float) this.sub_rotation / 50) * 360 / 8)) * 4) + 19);


                    } else {
                        this.pirn_loader.childModels.get(i).setRotationPoint(32, 25 + (float) (Math.cos(Math.toRadians((-i + 2 + (float) this.sub_rotation / 50) * 360 / 8)) * 4), (float) (Math.sin(Math.toRadians((-i + 2 + (float) this.sub_rotation / 50) * 360 / 8)) * 4) + 19);
                        this.pirn_loader.childModels.get(i+8).setRotationPoint(32, 25 + (float) (Math.cos(Math.toRadians((-i + 2 + (float) this.sub_rotation / 50) * 360 / 8)) * 4), (float) (Math.sin(Math.toRadians((-i + 2 + (float) this.sub_rotation / 50) * 360 / 8)) * 4) + 19);

                    }
                }
                if (i < Math.round(amountPirns)) {
                    this.pirn_loader.childModels.get(i).isHidden = Math.round(processType) == BURLAP.ordinal();
                    this.pirn_loader.childModels.get(i+8).isHidden = Math.round(processType) != BURLAP.ordinal();
                } else {
                    this.pirn_loader.childModels.get(i).isHidden = true;
                    this.pirn_loader.childModels.get(i+8).isHidden = true;
                }
            }
            this.pirn_loader_base.rotateAngleY = -(float) Math.toRadians(360 / 8 * this.pirn_rotation + ((float) this.sub_rotation) / 50 * 360 / 8);


            this.base.render(f5);
            //GlStateManager.scale(1,1,0.5);
            this.threads.render(f5);
        } else {
            int tickAmplified = Math.round(this.ticks * 500);
            //System.out.println("TICKAMPLIFIED "+tickAmplified+" "+(this.ticks*500)+" "+this.ticks);
            if (tickAmplified == 50 && !this.has_rotated) {
                this.has_rotated = true;
            }
            if (tickAmplified == 0 && this.has_rotated) {
                this.has_rotated = false;
            }
            this.sub_rotation = tickAmplified < 50 ? tickAmplified : 0;
            //System.out.println(this.sub_rotation);
            //this.cloth_beam.rotateAngleY += 0.01;
            if (tickAmplified < 50) {
                this.cloth_roller.rotateAngleY = (float) -Math.PI * 2 * 0 / 225;
                this.warp_roller.rotateAngleY = (float) -Math.PI * 2 * 0 / 225;
            } else {
                this.cloth_roller.rotateAngleY = (float) -Math.PI * 2 * (tickAmplified-50) / 225;
                this.warp_roller.rotateAngleY = (float) -Math.PI * 2 * (tickAmplified-50) / 225;
            }
            //this.warp_beam.rotateAngleY += 0.01;
            //this.warp_roller.rotateAngleY += 0.01;
            //this.beam_cap.rotateAngleY += 0.01;
            //this.beam_cap2.rotateAngleY += 0.01;
            //this.warp_roll.rotateAngleY += 0.01;

            this.sudo_cloth.isHidden = !(Math.round(primerCount) > 15) || (tickAmplified < 20 && (Math.round(amountOutput) <= 0)) || (Math.round(processType) == BURLAP.ordinal());
            this.sudo_warp.isHidden = !(Math.round(primerCount) > 15) || (Math.round(processType) == BURLAP.ordinal());
            this.sudo_cloth2.isHidden = !(Math.round(primerCount) > 15) || (tickAmplified < 20 && (Math.round(amountOutput) <= 0)) || (Math.round(processType) != BURLAP.ordinal());
            this.sudo_warp2.isHidden = !(Math.round(primerCount) > 15) || (Math.round(processType) != BURLAP.ordinal());
            this.threads.isHidden = !(Math.round(primerCount) > 15);
            this.threads_warp_lower.isHidden = !(Math.round(amountInput) > 0);
            this.warp_lower_stages.isHidden = !(Math.round(amountInput) > 0);
            this.warp_lower_stages2.isHidden = !(Math.round(amountInput) > 0);
            this.sudo_cloth_unfinished.isHidden = !(Math.round(primerCount) > 15) || (tickAmplified >= 20 || (Math.round(amountOutput) > 0)) || (Math.round(processType) == BURLAP.ordinal());
            this.sudo_cloth_unfinished2.isHidden = !(Math.round(primerCount) > 15) || (tickAmplified >= 20 || (Math.round(amountOutput) > 0)) || (Math.round(processType) != BURLAP.ordinal());
            //System.out.println("amplified "+(tickAmplified*20)/500);
            if (Math.round(amountOutput) == 0) {
                for (int i = 0; i < 40; i++) {
                    if (tickAmplified < 50) {
                        this.threads_cloth_lower.childModels.get(i).isHidden = i != 0 || (Math.round(processType) == BURLAP.ordinal());
                        this.threads_cloth_lower2.childModels.get(i).isHidden = i != 0 || (Math.round(processType) != BURLAP.ordinal());
                    } else {
                        this.threads_cloth_lower.childModels.get(i).isHidden = i != Math.min(39, ((tickAmplified - 37) * 20) / 500) || (Math.round(processType) == BURLAP.ordinal());
                        this.threads_cloth_lower2.childModels.get(i).isHidden = i != Math.min(39, ((tickAmplified - 37) * 20) / 500) || (Math.round(processType) != BURLAP.ordinal());
                    }
                    if (i < 20) {
                        if (tickAmplified < 50) {
                            this.warp_lower_stages.childModels.get(i).isHidden = i != 0 || (Math.round(processType) == BURLAP.ordinal());
                            this.warp_lower_stages2.childModels.get(i).isHidden = i != 0 || (Math.round(processType) != BURLAP.ordinal());
                        } else {
                            this.warp_lower_stages.childModels.get(i).isHidden = i != Math.min(19, ((tickAmplified - 37) * 20) / 500) || (Math.round(processType) == BURLAP.ordinal());
                            this.warp_lower_stages2.childModels.get(i).isHidden = i != Math.min(19, ((tickAmplified - 37) * 20) / 500) || (Math.round(processType) != BURLAP.ordinal());
                        }
                    }
                }
            } else {
                for (int i = 0; i < 40; i++) {
                    if (tickAmplified < 50) {
                        this.threads_cloth_lower.childModels.get(i).isHidden = i != 20 && (Math.round(processType) == BURLAP.ordinal());
                        this.threads_cloth_lower2.childModels.get(i).isHidden = i != 20 && (Math.round(processType) != BURLAP.ordinal());
                    } else {
                        this.threads_cloth_lower.childModels.get(i).isHidden = i != Math.min(39, 17+((tickAmplified - 37) * 20) / 500) || (Math.round(processType) == BURLAP.ordinal());
                        this.threads_cloth_lower2.childModels.get(i).isHidden = i != Math.min(39, 17+((tickAmplified - 37) * 20) / 500) || (Math.round(processType) != BURLAP.ordinal());
                    }
                    if (i < 20) {
                        if (tickAmplified < 50) {
                            this.warp_lower_stages.childModels.get(i).isHidden = i != 0 && (Math.round(processType) == BURLAP.ordinal());
                            this.warp_lower_stages2.childModels.get(i).isHidden = i != 0 && (Math.round(processType) != BURLAP.ordinal());
                        } else {
                            this.warp_lower_stages.childModels.get(i).isHidden = i != Math.min(19, ((tickAmplified - 37) * 20) / 500) || (Math.round(processType) == BURLAP.ordinal());
                            this.warp_lower_stages2.childModels.get(i).isHidden = i != Math.min(19, ((tickAmplified - 37) * 20) / 500) || (Math.round(processType) != BURLAP.ordinal());
                        }
                    }
                }
            }
            //this.threads_cloth_lower.setTextureOffset(348, (tickAmplified*20)/500);

            //System.out.println("amount input "+amountInput+" "+Math.round(amountInput/192.0F*5.0F));
            for (int i = 0; i < 5; i++) {
                this.warp_stages[i].isHidden = true;
                this.warp_stages2[i].isHidden = true;
                if (tickAmplified < 50) {
                    this.warp_stages[i].rotateAngleY = (float) -(((float) 0) * 2 * Math.PI / 225);
                    this.warp_stages2[i].rotateAngleY = (float) -(((float) 0) * 2 * Math.PI / 225);
                    this.cloth_stages[i].rotateAngleY = (float) -(((float) 0) * 2 * Math.PI / 225);
                    this.cloth_stages2[i].rotateAngleY = (float) -(((float) 0) * 2 * Math.PI / 225);
                } else {
                    this.warp_stages[i].rotateAngleY = (float) -(((float) tickAmplified - 50) * 2 * Math.PI / 225);
                    this.cloth_stages[i].rotateAngleY = (float) -(((float) tickAmplified - 50) * 2 * Math.PI / 225);
                    this.warp_stages2[i].rotateAngleY = (float) -(((float) tickAmplified - 50) * 2 * Math.PI / 225);
                    this.cloth_stages2[i].rotateAngleY = (float) -(((float) tickAmplified - 50) * 2 * Math.PI / 225);
                }
                if (i == Math.floor((amountInput - 1) / 192.0F * 5.0F)) {
                    this.warp_stages[i].isHidden = (Math.round(processType) == BURLAP.ordinal());
                    this.warp_stages2[i].isHidden = (Math.round(processType) != BURLAP.ordinal());
                }

                this.cloth_stages[i].isHidden = true;
                this.cloth_stages2[i].isHidden = true;
                if (i == Math.floor((amountOutput - 1) / 192.0F * 5.0F)) {
                    this.cloth_stages[i].isHidden = (Math.round(processType) == BURLAP.ordinal());
                    this.cloth_stages2[i].isHidden = (Math.round(processType) != BURLAP.ordinal());
                }
            }
            for (int i = 0; i < 10; i++) {
                this.pirn_pile.childModels.get(i).isHidden = !(Math.round(this.empty_pirn_count) > ((float) i) * 128.0F / 20.0F);
            }

            float shuttle_beam_displacement = 0;
            float shuttle_launch1 = 0;
            float shuttle_launch2 = 0;
            float warp_split = 1F;

            float pirn_starty = 25 + (float) (Math.cos(Math.toRadians((-1 - (float) this.sub_rotation / 50) * 360 / 8)) * 4);
            float pirn_startz = 46-19 + (float) (Math.sin(Math.toRadians(-(1 - ((float) this.sub_rotation) / 50) * 360 / 8)) * 4);
            float pirn_endy = 21.2F;
            float pirn_endz = 46-25.5F;
            float pirn_speedy = (pirn_endy - pirn_starty) / 50;
            float pirn_speedz = (pirn_endz - pirn_startz) / 50;
            this.pirn_loader.childModels.get(0).rotationPointX = 32;
            this.pirn_loader.childModels.get(0).rotationPointZ = pirn_endz;
            this.pirn_loader.childModels.get(8).rotationPointX = 32;
            this.pirn_loader.childModels.get(8).rotationPointZ = pirn_endz;
            if (tickAmplified < 50) {
                warp_split = 1F;
                shuttle_beam_displacement = 0;
            } else if (tickAmplified % 50 < 20) {
                warp_split = 1F;
                shuttle_beam_displacement = ((float) (Math.cos((tickAmplified % 50) * 2 * Math.PI / 20 + Math.PI)) + 1) / 2;
                //System.out.println("shuttle x "+32 + ((tickAmplified%50)-5.0F)/5.0F*(54+23-(38-4.5F-14)));
                if (tickAmplified % 50 >= 5 && tickAmplified % 50 < 10) {
                    shuttle_launch1 = ((float) (Math.cos((tickAmplified % 50 - 5) * 2 * Math.PI / 5 + Math.PI)) + 1) / 2;
                    this.pirn_loader.childModels.get(0).rotationPointX = 32 + ((tickAmplified % 50) - 4.0F) / 5.0F * 48;
                    this.pirn_loader.childModels.get(8).rotationPointX = 32 + ((tickAmplified % 50) - 4.0F) / 5.0F * 48;
                    //this.pirn_loader.childModels.get(0).rotationPointX = 32 + (54+23-(38-4.5F-14))-11;
                }
                if (tickAmplified % 50 >= 10 && tickAmplified % 50 < 15) {
                    shuttle_launch2 = ((float) (Math.cos((tickAmplified % 50 - 5) * 2 * Math.PI / 5 + Math.PI)) + 1) / 2;
                    this.pirn_loader.childModels.get(0).rotationPointX = 32 + (5 + (-(tickAmplified % 50) + 9.0F)) / 5.0F * 48;
                    this.pirn_loader.childModels.get(8).rotationPointX = 32 + (5 + (-(tickAmplified % 50) + 9.0F)) / 5.0F * 48;
                    //this.pirn_loader.childModels.get(0).rotationPointX = 32 + (54+23-(38-4.5F-14))-11;
                }

                this.pirn_loader.childModels.get(0).rotationPointZ = pirn_endz - ((float) (Math.cos(-(tickAmplified % 50) * 2 * Math.PI / 20 + Math.PI)) + 1) / 2 * 10;
                this.pirn_loader.childModels.get(8).rotationPointZ = pirn_endz - ((float) (Math.cos(-(tickAmplified % 50) * 2 * Math.PI / 20 + Math.PI)) + 1) / 2 * 10;
            } else if (tickAmplified % 50 < 25) {
                warp_split = (float) (Math.cos((tickAmplified % 50 - 20) * Math.PI / 5));
                shuttle_beam_displacement = 0;
                //this.pirn_loader.childModels.get(0).rotationPointX = 32 + (54+23-(38-4.5F-14))-11;
            } else if (tickAmplified % 50 < 45) {
                warp_split = -1F;
                shuttle_beam_displacement = ((float) (Math.cos((tickAmplified % 50 - 5) * 2 * Math.PI / 20 + Math.PI)) + 1) / 2;
                this.pirn_loader.childModels.get(0).rotationPointZ = pirn_endz - ((float) (Math.cos(-(tickAmplified % 50 - 5) * 2 * Math.PI / 20 + Math.PI)) + 1) / 2 * 10;
                this.pirn_loader.childModels.get(8).rotationPointZ = pirn_endz - ((float) (Math.cos(-(tickAmplified % 50 - 5) * 2 * Math.PI / 20 + Math.PI)) + 1) / 2 * 10;
            } else if (tickAmplified % 50 < 50) {
                warp_split = (float) (Math.cos((tickAmplified % 50 - 20) * Math.PI / 5));
                shuttle_beam_displacement = 0;
            }
            for (int i = 0; i < 4; i++) {
                this.shuttle_beam_super.childModels.get(i).rotationPointZ = 20 - shuttle_beam_displacement * 10;
            }
            this.shuttle_beam_connectors.childModels.get(0).rotationPointX = 38 - 4.5F - 14 + shuttle_launch1 * 5;
            this.shuttle_beam_connectors.childModels.get(1).rotationPointX = 54 + 23 - shuttle_launch2 * 5;

            for (ModelRenderer model : this.split_warp_left.childModels) {
                model.setRotationPoint(0, (float) (warp_split * 3.5 + 1.5), 0);
            }
            for (ModelRenderer model : this.split_warp_right.childModels) {
                model.setRotationPoint(0, (float) (-warp_split * 3.5 + 1.5), 0);
            }
            for (int i = 0; i < 64; i++) {
                ModelRenderer model = this.split_warp.childModels.get(i);
                ModelRenderer model2 = this.split_warp2.childModels.get(i);
                model.isHidden = Math.round(processType) == BURLAP.ordinal();
                model2.isHidden = Math.round(processType) != BURLAP.ordinal();
                if (i % 4 == 0) {
                    model.setRotationPoint(20 + 20 + Math.floorDiv(i, 2), 29 - 3.5F - warp_split * 3.5F, 48-17);
                    model.rotateAngleX = (float) Math.PI / 2 + (float) Math.atan2(0.5 - warp_split * 3.5, -(18 + 8 + 3));
                    model2.setRotationPoint(20 + 20 + Math.floorDiv(i, 2), 29 - 3.5F - warp_split * 3.5F, 48-17);
                    model2.rotateAngleX = (float) Math.PI / 2 + (float) Math.atan2(0.5 - warp_split * 3.5, -(18 + 8 + 3));
                    this.splitter_frame_shuttle.childModels.get(i / 4).rotationPointZ = 47-(18 + 7.5F) - shuttle_beam_displacement * 10;
                } else if (i % 4 == 1) {
                    model.setRotationPoint(20 + 20 + Math.floorDiv(i, 2), 30 - 3.5F - warp_split * 3.5F, 48-18);
                    model.rotateAngleX = (float) Math.PI / 2 - (float) Math.atan2(-0.5 + warp_split * 3.5 - 0.5, -(-12 - 2));
                    model2.setRotationPoint(20 + 20 + Math.floorDiv(i, 2), 30 - 3.5F - warp_split * 3.5F, 48-18);
                    model2.rotateAngleX = (float) Math.PI / 2 - (float) Math.atan2(-0.5 + warp_split * 3.5 - 0.5, -(-12 - 2));
                } else if (i % 4 == 2) {
                    model.setRotationPoint(20 + 20 + Math.floorDiv(i, 2), 22 + 3.5F + warp_split * 3.5F, 48-17);
                    model.rotateAngleX = (float) Math.PI / 2 + (float) Math.atan2(0.5 + warp_split * 3.5, -(18 + 8 + 3));
                    model2.setRotationPoint(20 + 20 + Math.floorDiv(i, 2), 22 + 3.5F + warp_split * 3.5F, 48-17);
                    model2.rotateAngleX = (float) Math.PI / 2 + (float) Math.atan2(0.5 + warp_split * 3.5, -(18 + 8 + 3));
                    //this.splitter_frame_shuttle.childModels.get(i/4).rotationPointZ = 18+7.5F+shuttle_beam_displacement*10;
                } else {
                    model.setRotationPoint(20 + 20 + Math.floorDiv(i, 2), 23 + 3.5F + warp_split * 3.5F, 48-18);
                    model.rotateAngleX = (float) Math.PI / 2 - (float) Math.atan2(-0.5 - warp_split * 3.5 - 0.5, -(-12 - 2));
                    model2.setRotationPoint(20 + 20 + Math.floorDiv(i, 2), 23 + 3.5F + warp_split * 3.5F, 48-18);
                    model2.rotateAngleX = (float) Math.PI / 2 - (float) Math.atan2(-0.5 - warp_split * 3.5 - 0.5, -(-12 - 2));
                }
            }
            for (int i = 0; i < 8; i++) {
                if (i == 0) {
                    if (tickAmplified < 50) {
                        this.pirn_loader.childModels.get(i).setRotationPoint(32, pirn_starty + ((float) this.sub_rotation * pirn_speedy), pirn_startz + ((float) this.sub_rotation * pirn_speedz));
                        this.pirn_loader.childModels.get(i+8).setRotationPoint(32, pirn_starty + ((float) this.sub_rotation * pirn_speedy), pirn_startz + ((float) this.sub_rotation * pirn_speedz));

                    } else {
                        this.pirn_loader.childModels.get(i).rotationPointY = pirn_endy;
                        this.pirn_loader.childModels.get(i+8).rotationPointY = pirn_endy;
                    }
                } else {
                    if (tickAmplified < 50) {
                        this.pirn_loader.childModels.get(i).setRotationPoint(32, 25 + (float) (Math.cos(Math.toRadians(-(-i + 1 + (float) this.sub_rotation / 50) * 360 / 8)) * 4), (float) (Math.sin(Math.toRadians(-(-i + 1 + (float) this.sub_rotation / 50) * 360 / 8)) * 4) + 46-19);
                        this.pirn_loader.childModels.get(i+8).setRotationPoint(32, 25 + (float) (Math.cos(Math.toRadians(-(-i + 1 + (float) this.sub_rotation / 50) * 360 / 8)) * 4), (float) (Math.sin(Math.toRadians(-(-i + 1 + (float) this.sub_rotation / 50) * 360 / 8)) * 4) + 46-19);

                    } else {
                        this.pirn_loader.childModels.get(i).setRotationPoint(32, 25 + (float) (Math.cos(Math.toRadians(-(-i + 2 + (float) this.sub_rotation / 50) * 360 / 8)) * 4), (float) (Math.sin(Math.toRadians(-(-i + 2 + (float) this.sub_rotation / 50) * 360 / 8)) * 4) + 46-19);
                        this.pirn_loader.childModels.get(i+8).setRotationPoint(32, 25 + (float) (Math.cos(Math.toRadians(-(-i + 2 + (float) this.sub_rotation / 50) * 360 / 8)) * 4), (float) (Math.sin(Math.toRadians(-(-i + 2 + (float) this.sub_rotation / 50) * 360 / 8)) * 4) + 46-19);

                    }
                }
                if (i < Math.round(amountPirns)) {
                    this.pirn_loader.childModels.get(i).isHidden = Math.round(processType) == BURLAP.ordinal();
                    this.pirn_loader.childModels.get(i+8).isHidden = Math.round(processType) != BURLAP.ordinal();
                } else {
                    this.pirn_loader.childModels.get(i).isHidden = true;
                    this.pirn_loader.childModels.get(i+8).isHidden = true;
                }
            }
            this.pirn_loader_base.rotateAngleY = (float) Math.toRadians(360 / 8 * this.pirn_rotation + ((float) this.sub_rotation) / 50 * 360 / 8);
            //System.out.println("empty pirn "+Math.round(this.empty_pirn_count));
            this.base.render(f5);
            this.threads.render(f5);
        }
    }
}
