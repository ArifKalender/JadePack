package me.Kugelbltz.jadePack;

import com.projectkorra.projectkorra.ability.CoreAbility;
import me.Kugelbltz.jadePack.listeners.EarthQuakeListener;
import me.Kugelbltz.jadePack.listeners.PKReloadEvent;
import me.Kugelbltz.jadePack.listeners.RockBulletsListener;
import me.Kugelbltz.jadePack.listeners.RockLikeListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

public final class JadePack extends JavaPlugin {
    public static Plugin plugin;
    public static String version = "§x§3§E§A§C§4§C‧§x§4§5§A§2§4§7⁺§x§4§C§9§9§4§3˚§x§5§3§8§F§3§E༓§x§5§A§8§6§3§9J§x§6§1§7§C§3§4a§x§6§7§7§3§3§0d§x§6§E§6§9§2§Be§x§7§5§6§0§2§6P§x§7§C§5§6§2§1a§x§8§3§4§D§1§Dc§x§8§A§4§3§1§8k";
    @Override
    public void onEnable() {
        plugin=this;
        CoreAbility.registerPluginAbilities(this, "me.Kugelbltz.jadePack.abilities");
        saveDefaultConfig();
        registerListeners();
        this.getConfig().options().copyDefaults(true);

    }

    public static String getAuthor(){
        if(plugin.getServer().getOnlineMode()){
            return Bukkit.getPlayer(UUID.fromString("606e8422-e4b9-4921-a673-ca85ffb35be6")).getName();
        }else{
            return "Kugelbltz";
        }
    }
    private void registerListeners(){
        plugin.getServer().getPluginManager().registerEvents(new EarthQuakeListener(), this);
        plugin.getServer().getPluginManager().registerEvents(new RockBulletsListener(), this);
        plugin.getServer().getPluginManager().registerEvents(new PKReloadEvent(), this);
        plugin.getServer().getPluginManager().registerEvents(new RockLikeListener(), this);
    }


}
