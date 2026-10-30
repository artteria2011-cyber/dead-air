package com.theabyss.registry;
import com.theabyss.TheAbyss;
import com.theabyss.entity.StalkerEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
public class AbyssEntities {
 public static final DeferredRegister<EntityType<?>> TYPES=DeferredRegister.create(ForgeRegistries.ENTITY_TYPES,TheAbyss.MODID);
 public static final RegistryObject<EntityType<StalkerEntity>> STALKER=TYPES.register("stalker",()->EntityType.Builder.of(StalkerEntity::new,MobCategory.MONSTER).sized(0.7F,2.6F).clientTrackingRange(64).build("stalker"));
 public static void attributes(EntityAttributeCreationEvent e){e.put(STALKER.get(),StalkerEntity.attributes().build());}
}
