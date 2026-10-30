package com.theabyss.entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
public class StalkerEntity extends Monster {
 public StalkerEntity(EntityType<? extends Monster> type,Level level){super(type,level);}
 public static AttributeSupplier.Builder attributes(){return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH,46).add(Attributes.MOVEMENT_SPEED,0.29).add(Attributes.ATTACK_DAMAGE,7).add(Attributes.FOLLOW_RANGE,56).add(Attributes.KNOCKBACK_RESISTANCE,0.6);}
 @Override protected void registerGoals(){goalSelector.addGoal(0,new FloatGoal(this));goalSelector.addGoal(2,new MeleeAttackGoal(this,1.12,true));goalSelector.addGoal(5,new WaterAvoidingRandomStrollGoal(this,0.72));goalSelector.addGoal(7,new LookAtPlayerGoal(this,Player.class,40));goalSelector.addGoal(8,new RandomLookAroundGoal(this));targetSelector.addGoal(1,new NearestAttackableTargetGoal<>(this,Player.class,true));}
 @Override public boolean isPersistenceRequired(){return true;}
 @Override public boolean removeWhenFarAway(double d){return false;}
}
