package me.Kugelbltz.jadePack.listeners;

import com.projectkorra.projectkorra.BendingPlayer;
import com.projectkorra.projectkorra.ability.CoreAbility;
import com.projectkorra.projectkorra.ability.EarthAbility;
import com.projectkorra.projectkorra.earthbending.EarthArmor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import static me.Kugelbltz.jadePack.JadePack.plugin;

public class EarthSlideListener implements Listener {

    float speed = (float) plugin.getConfig().getDouble("Abilities.EarthSlide.Speed");

    @EventHandler
    private void onSneak(PlayerToggleSneakEvent event){
        if(event.isSneaking()){
            Player player = event.getPlayer();
            if(CoreAbility.hasAbility(player, EarthArmor.class)){
                new BukkitRunnable(){
                    @Override
                    public void run() {
                        if(!player.isSneaking() || !CoreAbility.hasAbility(player, EarthArmor.class)){
                            this.cancel();
                        }
                        Location location = new Location(player.getWorld(),player.getLocation().getX(), player.getLocation().getY()-0.5, player.getLocation().getZ());
                        Block block = location.getBlock();
                        if(EarthAbility.isEarthbendable(player,block)){
                            Vector vector = player.getEyeLocation().getDirection();
                            player.setVelocity(vector.multiply(speed));
                        }
                    }
                }.runTaskTimer(plugin,0,1);
            }
        }
    }
}
