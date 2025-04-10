package me.Kugelbltz.jadePack.abilities;

import com.projectkorra.projectkorra.ability.*;
import com.projectkorra.projectkorra.earthbending.EarthArmor;
import me.Kugelbltz.jadePack.JadePack;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import static me.Kugelbltz.jadePack.JadePack.plugin;

public class EarthSlide extends EarthAbility implements AddonAbility, PassiveAbility {


    public EarthSlide(Player player) {
        super(player);
    }

    @Override
    public boolean isEnabled() {
        return super.isEnabled();
    }

    @Override
    public String getDescription() {
        return plugin.getConfig().getString("Strings.EarthSlide.Description");
    }

    @Override
    public void progress() {

    }


    @Override
    public boolean isSneakAbility() {
        return true;
    }

    @Override
    public boolean isHarmlessAbility() {
        return false;
    }

    @Override
    public long getCooldown() {
        return 0;
    }

    @Override
    public String getName() {
        return "EarthSlide";
    }

    @Override
    public Location getLocation() {
        return null;
    }

    @Override
    public void load() {

    }

    @Override
    public void stop() {

    }

    @Override
    public String getAuthor() {
        return JadePack.getAuthor();
    }

    @Override
    public String getVersion() {
        return JadePack.version;
    }


    @Override
    public boolean isInstantiable() {
        return false;
    }

    @Override
    public boolean isProgressable() {
        return false;
    }
}
