package me.Kugelbltz.jadePack.listeners;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityKnockbackByEntityEvent;
import org.bukkit.event.entity.EntityKnockbackEvent;

import static me.Kugelbltz.jadePack.JadePack.plugin;

public class RockLikeListener implements Listener {

    @EventHandler
    private void onKb(EntityKnockbackByEntityEvent event){
        Entity entity = event.getEntity();
        if(entity instanceof Player){
            if(((Player) entity).isSneaking()){

                if(entity.hasPermission("bending.ability.rocklike")){
                    boolean isEnabled = plugin.getConfig().getBoolean("Abilities.RockLike.Enabled");
                    if(isEnabled){
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

}
