package com.theabyss.world;
import com.theabyss.registry.AbyssEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
public class HorrorController {
 private final Map<UUID,Integer> dread=new ConcurrentHashMap<>();
 private final String[] whispers={"message.theabyss.w1","message.theabyss.w2","message.theabyss.w3","message.theabyss.w4"};
 @SubscribeEvent public void tick(TickEvent.PlayerTickEvent e){
  if(e.phase!=TickEvent.Phase.END||!(e.player instanceof ServerPlayer p)||p.isSpectator())return;
  if(!(p.level() instanceof ServerLevel level))return;
  int fear=dread.getOrDefault(p.getUUID(),0); boolean alone=level.getEntitiesOfClass(Player.class,new AABB(p.blockPosition()).inflate(32),q->q!=p&&!q.isSpectator()).isEmpty();
  boolean dark=level.getMaxLocalRawBrightness(p.blockPosition())<6; boolean below=!level.canSeeSky(p.blockPosition());
  if(dark&&alone)fear=Math.min(10000,fear+2);else fear=Math.max(0,fear-1);if(below&&alone)fear=Math.min(10000,fear+1);dread.put(p.getUUID(),fear);
  if(p.tickCount%20!=0)return;
  if(fear>180&&p.getRandom().nextFloat()<0.035F)p.displayClientMessage(Component.translatable(whispers[p.getRandom().nextInt(whispers.length)]),true);
  if(fear>650&&p.getRandom().nextFloat()<0.025F)p.displayClientMessage(Component.translatable("message.theabyss.dread"),false);
  if(fear>900&&level.getDayTime()/24000L>=1&&p.tickCount%240==0&&p.getRandom().nextFloat()<0.16F)spawn(level,p);
 }
 private void spawn(ServerLevel level,ServerPlayer p){double a=p.getRandom().nextDouble()*Math.PI*2;int d=26+p.getRandom().nextInt(15);BlockPos b=BlockPos.containing(p.getX()+Math.cos(a)*d,p.getY(),p.getZ()+Math.sin(a)*d);BlockPos pos=level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,b);if(!level.hasChunkAt(pos)||!level.getBlockState(pos.below()).isSolid()||level.getMaxLocalRawBrightness(pos)>5)return;if(!level.getEntitiesOfClass(com.theabyss.entity.StalkerEntity.class,new AABB(pos).inflate(48)).isEmpty())return;var mob=AbyssEntities.STALKER.get().create(level);if(mob==null)return;mob.moveTo(pos.getX()+.5,pos.getY(),pos.getZ()+.5,p.getRandom().nextFloat()*360,0);level.addFreshEntity(mob);}
}
