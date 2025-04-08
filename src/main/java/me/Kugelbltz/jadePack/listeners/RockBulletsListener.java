package me.Kugelbltz.jadePack.listeners;

import com.projectkorra.projectkorra.BendingPlayer;
import com.projectkorra.projectkorra.ability.EarthAbility;
import me.Kugelbltz.jadePack.abilities.RockBullets;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAnimationEvent;

public class RockBulletsListener implements Listener {

    @EventHandler
    private void onClick(PlayerAnimationEvent event) {
        if(event.getPlayer().isSneaking()){
            BendingPlayer bPlayer = BendingPlayer.getBendingPlayer(event.getPlayer());

            if (bPlayer.getBoundAbilityName().equalsIgnoreCase("RockBullets")) {
                new RockBullets(event.getPlayer());
            }
        }
    }
}
