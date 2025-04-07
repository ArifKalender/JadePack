package me.Kugelbltz.jadePack.abilities;

import com.projectkorra.projectkorra.ability.AddonAbility;
import com.projectkorra.projectkorra.ability.EarthAbility;
import com.projectkorra.projectkorra.attribute.Attribute;
import me.Kugelbltz.jadePack.JadePack;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import static me.Kugelbltz.jadePack.JadePack.plugin;

public class RockBullets extends EarthAbility  implements AddonAbility {

    @Attribute(Attribute.DAMAGE)
    private double damage;
    @Attribute(Attribute.RANGE)
    private double range;
    @Attribute(Attribute.COOLDOWN)
    private long cooldown;
    @Attribute("Interval")
    long damageInterval;

    public RockBullets(Player player) {
        super(player);
        if(bPlayer.canBend(this) && !hasAbility(player, this.getClass())){
            if (player.isOnGround()) {
                setFields();
                start();
            }
        }
    }

    private void setFields(){
        damage = plugin.getConfig().getDouble("Abilities.RockBullets.Damage");
        range = plugin.getConfig().getDouble("Abilities.RockBullets.Range");
        cooldown = plugin.getConfig().getLong("Abilities.RockBullets.Cooldown");
        damageInterval = plugin.getConfig().getLong("Abilities.RockBullets.DamageInterval");
    }

    @Override
    public void progress() {
        if(!player.isOnline() || player.isDead() || !bPlayer.canBend(this)){
            remove();
            bPlayer.addCooldown(this);
            return;
        }
    }

    @Override
    public boolean isEnabled() {
        return plugin.getConfig().getBoolean("Abilities.RockBullets.Enabled");
    }

    @Override
    public String getInstructions() {
        return plugin.getConfig().getString("Strings.RockBullets.Instructions");
    }

    @Override
    public String getDescription() {
        return plugin.getConfig().getString("Strings.RockBullets.Description");
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
        return "";
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
        return "";
    }

    @Override
    public String getVersion() {
        return "";
    }
}
