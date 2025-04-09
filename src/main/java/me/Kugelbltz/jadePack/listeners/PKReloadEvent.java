package me.Kugelbltz.jadePack.listeners;

import com.projectkorra.projectkorra.event.BendingReloadEvent;
import me.Kugelbltz.jadePack.JadePack;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import static me.Kugelbltz.jadePack.JadePack.plugin;

public class PKReloadEvent implements Listener {

    @EventHandler
    private void onReload(BendingReloadEvent event) {
        plugin.reloadConfig();
        event.getSender().sendMessage("§x§3§E§A§C§4§CJ§x§4§2§A§7§4§Aa§x§4§5§A§2§4§7d§x§4§9§9§D§4§5e§x§4§C§9§8§4§2p§x§5§0§9§3§4§0a§x§5§4§8§E§3§Dc§x§5§7§8§9§3§Bk §x§5§F§7§F§3§6w§x§6§2§7§A§3§3a§x§6§6§7§5§3§1s §x§6§D§6§B§2§Cr§x§7§1§6§6§2§9e§x§7§4§6§1§2§7l§x§7§8§5§C§2§4o§x§7§C§5§7§2§2a§x§7§F§5§2§1§Fd§x§8§3§4§D§1§De§x§8§6§4§8§1§Ad§x§8§A§4§3§1§8.");

    }
}
