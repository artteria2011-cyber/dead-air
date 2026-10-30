package com.deadair;

import com.deadair.entity.DeadAirEntities;
import com.deadair.event.HorrorEvents;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(DeadAir.MODID)
public class DeadAir {
    public static final String MODID = "deadair";

    public DeadAir() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        DeadAirEntities.ENTITY_TYPES.register(bus);
        bus.addListener(DeadAirEntities::registerAttributes);
        MinecraftForge.EVENT_BUS.register(new HorrorEvents());
    }
}
