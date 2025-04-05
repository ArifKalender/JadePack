package me.Kugelbltz.jadePack;

import com.projectkorra.projectkorra.ability.CoreAbility;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class JadePack extends JavaPlugin {
    //EarthQuake, bolinin arabadayken yerden taş söküp art arda attığı yetenek 1. sezon tlok
    public static String earthQuakeDescription, earthQuakeInstructions;
    Plugin plugin;
    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin=this;
        CoreAbility.registerPluginAbilities(this, "me.Kugelbltz.jadePack.abilities");
        saveDefaultConfig();
        registerListeners();
        setFields();
        this.getConfig().options().copyDefaults(true);

    }

    private void registerListeners(){

    }
    private void setFields(){
        earthQuakeDescription=getConfig().getString("Strings.EarthQuake.Description");
        earthQuakeInstructions=getConfig().getString("Strings.EarthQuake.Instructions");
    }

}
