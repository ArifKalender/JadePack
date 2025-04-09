package me.Kugelbltz.jadePack.abilities;

import com.projectkorra.projectkorra.ability.AddonAbility;
import com.projectkorra.projectkorra.ability.EarthAbility;
import com.projectkorra.projectkorra.ability.PassiveAbility;
import me.Kugelbltz.jadePack.JadePack;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class RockLike extends EarthAbility implements AddonAbility, PassiveAbility {

    public RockLike(Player player) {
        super(player);
    }

    @Override
    public void progress() {

    }

    @Override
    public boolean isSneakAbility() {
        return false;
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
        return "RockLike";
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
        return JadePack.getAuthor() + " | Idea by LucidWeaver";
    }

    @Override
    public boolean isEnabled() {
        return JadePack.plugin.getConfig().getBoolean("Abilities.RockLike.Enabled");
    }

    @Override
    public String getDescription() {
        return JadePack.plugin.getConfig().getString("Strings.RockLike.Description");
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
