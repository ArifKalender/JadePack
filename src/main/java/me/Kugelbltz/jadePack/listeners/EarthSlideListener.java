package me.Kugelbltz.jadePack.listeners;

import com.projectkorra.projectkorra.BendingPlayer;
import com.projectkorra.projectkorra.ability.CoreAbility;
import com.projectkorra.projectkorra.ability.EarthAbility;
import com.projectkorra.projectkorra.earthbending.EarthArmor;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import static me.Kugelbltz.jadePack.JadePack.plugin;

public class EarthSlideListener implements Listener {


    @EventHandler
    private void onSneak(PlayerToggleSneakEvent event){
        if(event.isSneaking()){
            Player player = event.getPlayer();
            if(CoreAbility.hasAbility(player, EarthArmor.class)){
                float speed = (float) plugin.getConfig().getDouble("Abilities.EarthSlide.Speed");
                double step_height = player.getAttribute(Attribute.STEP_HEIGHT).getBaseValue();
                new BukkitRunnable(){
                    @Override
                    public void run() {
                        player.getAttribute(Attribute.STEP_HEIGHT).setBaseValue(1);


                        if(!player.isOnline() || player.isDead()){
                            player.getAttribute(Attribute.STEP_HEIGHT).setBaseValue(step_height);
                            this.cancel();
                        }
                        if(!player.isSneaking() || !CoreAbility.hasAbility(player, EarthArmor.class) ){
                            player.getAttribute(Attribute.STEP_HEIGHT).setBaseValue(step_height);
                            this.cancel();
                        }

                        if(returnBendable(player) && BendingPlayer.getBendingPlayer(player).getBoundAbilityName().equalsIgnoreCase("EarthArmor")){
                            Vector vector = player.getEyeLocation().getDirection();
                            vector.setY(-1);
                            player.setVelocity(vector.normalize().multiply(speed));
                        }
                    }
                }.runTaskTimer(plugin,0,1);
            }
        }
    }



    private boolean returnBendable(Player player){
        Block below;
        Block behind;
        Block front;

        below = new Location(player.getWorld(),player.getLocation().getX(),player.getLocation().getY()-0.1,player.getLocation().getZ()).getBlock();
        behind = below.getLocation().add(player.getLocation().getDirection().setY(0).normalize().multiply(-1)).getBlock();
        front = below.getLocation().add(player.getLocation().getDirection().setY(0).normalize()).getBlock();

        if(EarthAbility.isEarthbendable(player,below) || EarthAbility.isEarthbendable(player,front) || EarthAbility.isEarthbendable(player,behind)){
            return true;
        }

        return false;
    }
}
