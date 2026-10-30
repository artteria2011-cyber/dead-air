package com.deadair.event;

import com.deadair.entity.DeadAirEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class HorrorEvents {
    private final Map<UUID, Integer> paranoia = new ConcurrentHashMap<>();
    private final Map<UUID, Integer> lastWhisper = new ConcurrentHashMap<>();
    private final Map<UUID, Boolean> hasDied = new ConcurrentHashMap<>();

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || !(event.player instanceof ServerPlayer player)) return;
        if (player.level().isClientSide || player.isSpectator()) return;
        UUID id = player.getUUID();
        int ticks = paranoia.getOrDefault(id, 0);
        boolean dark = player.level().getMaxLocalRawBrightness(player.blockPosition()) < 7;
        boolean alone = player.level().getEntitiesOfClass(Player.class, new AABB(player.blockPosition()).inflate(24), p -> p != player && !p.isSpectator()).isEmpty();
        if (dark && alone) ticks = Math.min(12000, ticks + 2);
        else ticks = Math.max(0, ticks - 1);
        paranoia.put(id, ticks);

        if (player.tickCount % 20 == 0) {
            long dayTime = player.level().getDayTime() % 24000L;
            boolean night = dayTime >= 13000 && dayTime <= 23000;
            boolean fullMoon = player.level() instanceof ServerLevel sl && sl.getMoonPhase() == 0;
            if (night && fullMoon && player.tickCount % 200 == 0 && player.getRandom().nextFloat() < 0.35F) {
                player.displayClientMessage(Component.translatable("message.deadair.full_moon"), true);
            }
            if (ticks > 500 && player.tickCount % 180 == 0 && player.getRandom().nextFloat() < 0.28F) {
                player.displayClientMessage(Component.translatable("message.deadair.whisper"), true);
            }
            if (ticks > 1100 && night && player.tickCount % 400 == 0 && player.getRandom().nextFloat() < 0.18F) {
                player.displayClientMessage(Component.translatable("message.deadair.static"), true);
            }
            // Rare distant sightings after the first death or after the third in-game night.
            long day = player.level().getDayTime() / 24000L;
            boolean unlocked = hasDied.getOrDefault(id, false) || day >= 3;
            if (unlocked && night && player.tickCount % 240 == 0 && player.getRandom().nextFloat() < 0.12F && player.level() instanceof ServerLevel level) {
                spawnDistantWatcher(level, player);
            }
        }
    }

    private void spawnDistantWatcher(ServerLevel level, ServerPlayer player) {
        double angle = player.getRandom().nextDouble() * Math.PI * 2;
        int distance = 22 + player.getRandom().nextInt(13);
        BlockPos base = BlockPos.containing(player.getX() + Math.cos(angle) * distance, player.getY(), player.getZ() + Math.sin(angle) * distance);
        BlockPos safe = level.getHeightmapPos(net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, base);
        if (!level.hasChunkAt(safe) || !level.getBlockState(safe.below()).isSolid()) return;
        if (!level.getEntitiesOfClass(Entity.class, new AABB(safe).inflate(5), e -> e instanceof com.deadair.entity.WatcherEntity).isEmpty()) return;
        var watcher = DeadAirEntities.WATCHER.get().create(level);
        if (watcher == null) return;
        watcher.moveTo(safe.getX() + 0.5, safe.getY(), safe.getZ() + 0.5, player.getRandom().nextFloat() * 360, 0);
        level.addFreshEntity(watcher);
        // A sighting is brief: the figure vanishes before the player can approach it.
        level.getServer().tell(new net.minecraft.server.TickTask(level.getServer().getTickCount() + 100, () -> {
            if (watcher.isAlive()) watcher.discard();
        }));
    }

    @SubscribeEvent
    public void onDeath(LivingDeathEvent event) {
        if (event.getEntity() instanceof Player player) hasDied.put(player.getUUID(), true);
    }

    @SubscribeEvent
    public void onWake(PlayerWakeUpEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player) || player.level().isClientSide) return;
        int ticks = paranoia.getOrDefault(player.getUUID(), 0);
        if (ticks > 1600 && player.level().isNight()) {
            player.displayClientMessage(Component.literal("§8Ты проснулся. В комнате пахнет гарью."), false);
            player.hurt(player.damageSources().magic(), 1.0F);
        }
    }
}
