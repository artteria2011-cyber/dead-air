package com.theabyss;
import com.theabyss.registry.AbyssEntities;
import com.theabyss.world.HorrorController;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
@Mod(TheAbyss.MODID) public class TheAbyss {
 public static final String MODID="theabyss";
 public TheAbyss(){ IEventBus bus=FMLJavaModLoadingContext.get().getModEventBus(); AbyssEntities.TYPES.register(bus); bus.addListener(AbyssEntities::attributes); MinecraftForge.EVENT_BUS.register(new HorrorController()); }
}
