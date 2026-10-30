package com.deadair.entity;

import com.deadair.DeadAir;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class DeadAirEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, DeadAir.MODID);
    public static final RegistryObject<EntityType<WatcherEntity>> WATCHER = ENTITY_TYPES.register("watcher", () ->
        EntityType.Builder.of(WatcherEntity::new, MobCategory.MONSTER)
            .sized(0.75F, 3.2F).clientTrackingRange(48).build("watcher"));

    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(WATCHER.get(), WatcherEntity.createAttributes().build());
    }

    private DeadAirEntities() {}
}
