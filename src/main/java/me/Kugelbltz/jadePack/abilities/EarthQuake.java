package me.Kugelbltz.jadePack.abilities;

import com.projectkorra.projectkorra.ability.AddonAbility;
import com.projectkorra.projectkorra.ability.EarthAbility;
import com.projectkorra.projectkorra.attribute.Attribute;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import static com.projectkorra.projectkorra.ProjectKorra.plugin;
import static me.Kugelbltz.jadePack.JadePack.earthQuakeDescription;
import static me.Kugelbltz.jadePack.JadePack.earthQuakeInstructions;

public class EarthQuake extends EarthAbility implements AddonAbility {

    boolean requireSneak, allowDifferentSlots, userMustBeInRadius;
    @Attribute(Attribute.COOLDOWN)
    long cooldown;
    @Attribute(Attribute.DURATION)
    long duration;
    @Attribute(Attribute.RADIUS)
    double radius;
    @Attribute("DamageInterval")
    long damageInterval;
    @Attribute(Attribute.DAMAGE)
    double damage;

    private Location location;
    public EarthQuake(Player player) {
        super(player);
        if (bPlayer.canBend(this) && !hasAbility(player, this.getClass())) {
            location=player.getLocation();
            start();
        }
    }

    private void setFields(){
        cooldown=plugin.getConfig().getLong("Abilities.EarthQuake.Cooldown");
        duration=plugin.getConfig().getLong("Abilities.EarthQuake.Duration");
        radius=plugin.getConfig().getDouble("Abilities.EarthQuake.Radius");
        damageInterval=plugin.getConfig().getLong("Abilities.EarthQuake.DamageInterval");
        damage=plugin.getConfig().getDouble("Abilities.EarthQuake.Damage");
        requireSneak=plugin.getConfig().getBoolean("Abilities.EarthQuake.RequireSneak");
        allowDifferentSlots=plugin.getConfig().getBoolean("Abilities.EarthQuake.AllowDifferentSlots");
        userMustBeInRadius=plugin.getConfig().getBoolean("Abilities.EarthQuake.UserMustBeInRadius");

    }


    @Override
    public boolean isEnabled() {
        return plugin.getConfig().getBoolean("Abilities.EarthQuake.Enabled");
    }

    @Override
    public String getDescription() {
        return earthQuakeDescription;
    }

    @Override
    public String getInstructions() {
        return earthQuakeInstructions;
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
        return cooldown;
    }

    @Override
    public String getName() {
        return "EarthQuake";
    }

    @Override
    public org.bukkit.Location getLocation() {
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
        return "";
    }

    @Override
    public String getVersion() {
        return "";
    }
}
