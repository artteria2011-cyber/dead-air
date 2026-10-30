package com.deadair.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class WatcherEntity extends PathfinderMob {
    public WatcherEntity(EntityType<? extends PathfinderMob> type, Level level) {
        super(type, level);
        this.setPersistenceRequired();
        this.setSilent(true);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return PathfinderMob.createMobAttributes()
            .add(Attributes.MAX_HEALTH, 32.0D)
            .add(Attributes.MOVEMENT_SPEED, 0.0D)
            .add(Attributes.FOLLOW_RANGE, 48.0D);
    }

    @Override protected void registerGoals() {
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 32.0F));
        this.goalSelector.addGoal(9, new RandomLookAroundGoal(this));
    }

    @Override public void aiStep() {
        super.aiStep();
        // The Watcher is intentionally passive in this first release: it observes rather than rushes the player.
        if (!this.level().isClientSide && this.tickCount % 20 == 0) this.setDeltaMovement(0, 0, 0);
    }
}
