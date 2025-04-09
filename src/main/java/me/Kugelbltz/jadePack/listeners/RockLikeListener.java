package me.Kugelbltz.jadePack.listeners;

import com.projectkorra.projectkorra.BendingPlayer;
import com.projectkorra.projectkorra.ability.CoreAbility;
import com.projectkorra.projectkorra.ability.EarthAbility;
import me.Kugelbltz.jadePack.abilities.RockLike;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityKnockbackByEntityEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.scheduler.BukkitRunnable;

import static me.Kugelbltz.jadePack.JadePack.plugin;

public class RockLikeListener implements Listener {

    @EventHandler
    private void onKb(EntityKnockbackByEntityEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Player) {
            if (((Player) entity).isSneaking()) {
                if (EarthAbility.isEarthbendable((Player) entity, new Location(entity.getWorld(), entity.getLocation().getX(), entity.getLocation().getY() - 1, entity.getLocation().getZ()).getBlock())) {
                    if (entity.hasPermission("bending.ability.rocklike")) {
                        boolean isEnabled = plugin.getConfig().getBoolean("Abilities.RockLike.Enabled");
                        if (isEnabled) {
                            event.setCancelled(true);
                        }
                    }
                }

            }
        }
    }

    @EventHandler
    private void onSneak(PlayerToggleSneakEvent event) {
        boolean isEnabled = plugin.getConfig().getBoolean("Abilities.RockLike.Enabled");
        if (event.isSneaking()) {
            if (isEnabled) {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        Player player = event.getPlayer();
                        if (!player.isSneaking()) {
                            this.cancel();
                            return;
                        }
                        if (!BendingPlayer.getBendingPlayer(player).canBendIgnoreBinds(CoreAbility.getAbility(RockLike.class))) {
                            this.cancel();
                            return;
                        }


                        if (EarthAbility.isEarthbendable(player, new Location(player.getWorld(), player.getLocation().getX(), player.getLocation().getY() - 0.1, player.getLocation().getZ()).getBlock())) {
                            player.getWorld().spawnParticle(Particle.BLOCK_CRUMBLE, player.getLocation(), 9, 0.2, 0.2, 0.2, 0.05, player.getLocation().add(0,-1,0).getBlock().getType().createBlockData());
                        }
                    }
                }.runTaskTimer(plugin, 0, 3);
            }
        }


    }
}