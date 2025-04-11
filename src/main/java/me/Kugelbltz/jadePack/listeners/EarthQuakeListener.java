package me.Kugelbltz.jadePack.listeners;

import com.projectkorra.projectkorra.BendingPlayer;
import me.Kugelbltz.jadePack.abilities.EarthQuake;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class EarthQuakeListener implements Listener {
    @EventHandler
    private void onRightClick(PlayerInteractEvent event){
        if(event.getAction()== Action.LEFT_CLICK_BLOCK){
            BendingPlayer bPlayer = BendingPlayer.getBendingPlayer(event.getPlayer());
            if(bPlayer.getBoundAbilityName().equalsIgnoreCase("EarthQuake")){
                if(event.getPlayer().isSneaking()){
                    new EarthQuake(event.getPlayer());
                }
            }
        }
    }

}
