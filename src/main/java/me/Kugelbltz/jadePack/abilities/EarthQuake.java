package me.Kugelbltz.jadePack.abilities;

import com.projectkorra.projectkorra.GeneralMethods;
import com.projectkorra.projectkorra.ability.AddonAbility;
import com.projectkorra.projectkorra.ability.EarthAbility;
import com.projectkorra.projectkorra.attribute.Attribute;
import com.projectkorra.projectkorra.util.DamageHandler;
import com.projectkorra.projectkorra.util.TempBlock;
import com.projectkorra.projectkorra.util.TempFallingBlock;
import me.Kugelbltz.jadePack.JadePack;
import me.Kugelbltz.jadePack.Util;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.util.Vector;

import java.util.Random;

import static me.Kugelbltz.jadePack.JadePack.plugin;
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
    @Attribute("Interval")
    long damageInterval;
    @Attribute(Attribute.DAMAGE)
    double damage;

    private Location location;
    int i = 0;

    public EarthQuake(Player player) {
        super(player);
        setFields();
        if (bPlayer.canBend(this) && !hasAbility(player, this.getClass())) {
            if (player.isOnGround()) {
                location = player.getLocation();
                location.getWorld().playSound(location, Sound.ITEM_MACE_SMASH_GROUND_HEAVY, 3, 0);
                start();
            }
        }
    }

    private void setFields() {
        cooldown = plugin.getConfig().getLong("Abilities.EarthQuake.Cooldown");
        duration = plugin.getConfig().getLong("Abilities.EarthQuake.Duration");
        radius = plugin.getConfig().getDouble("Abilities.EarthQuake.Radius");
        damageInterval = plugin.getConfig().getLong("Abilities.EarthQuake.DamageInterval");
        damage = plugin.getConfig().getDouble("Abilities.EarthQuake.Damage");
        requireSneak = plugin.getConfig().getBoolean("Abilities.EarthQuake.RequireSneak");
        allowDifferentSlots = plugin.getConfig().getBoolean("Abilities.EarthQuake.AllowDifferentSlots");
        userMustBeInRadius = plugin.getConfig().getBoolean("Abilities.EarthQuake.UserMustBeInRadius");

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

    private void removeAbility() {
        remove();
        bPlayer.addCooldown(this);
    }


    @Override
    public void progress() {
        i++;
        if (!bPlayer.canBendIgnoreBinds(this) || player.isDead() || !player.isOnline() || getStartTime() + duration < System.currentTimeMillis()) {
            removeAbility();
            return;
        }
        if (requireSneak && !player.isSneaking()) {
            removeAbility();
            return;
        }
       if (!allowDifferentSlots) {
            if (!bPlayer.getBoundAbilityName().equalsIgnoreCase(getName())) {
                removeAbility();
                return;
            }
        }
        if (userMustBeInRadius) {
            if (player.getLocation().distance(location) > radius) {
                removeAbility();
                return;
            }
        }

        for (Entity entity : GeneralMethods.getEntitiesAroundPoint(location, radius)) {
            if (entity instanceof LivingEntity && entity != player) {
                if (Math.round((float) damageInterval / 50) == 0) {
                    DamageHandler.damageEntity(entity, damage, this);
                    ((LivingEntity) entity).setNoDamageTicks(0);
                } else if (i % (Math.round((float) (damageInterval / 50))) == 0) {
                    DamageHandler.damageEntity(entity, damage, this);
                    ((LivingEntity) entity).setNoDamageTicks(0);
                }
            }
        }

        for (Block block : Util.getBlocksBelow(radius, location)) {
            if (this.isEarthbendable(block)) {
                if (new Random().nextDouble(0, 1) < 0.005) {
                    new TempFallingBlock(new Location(block.getWorld(), block.getX(), block.getY() + 1, block.getZ()), block.getType().createBlockData(), new Vector(0, 0.3, 0), this);
                    if(block.getLocation().distance(player.getLocation())>1.5){
                        new TempBlock(block, Material.AIR.createBlockData(),500, this);
                    }
                    location.getWorld().playSound(location, Sound.BLOCK_ANCIENT_DEBRIS_BREAK, 1, 0);
                } else if (new Random().nextDouble(0, 1) < 0.5) {
                    block.getWorld().spawnParticle(Particle.BLOCK_CRUMBLE, block.getLocation(), 15, 0.5, 0.5, 0.5, 0, block.getBlockData());
                }

            }
        }



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
        return cooldown;
    }

    @Override
    public String getName() {
        return "EarthQuake";
    }

    @Override
    public Location getLocation() {
        return location;
    }

    @Override
    public void load() {

    }

    @Override
    public void stop() {

    }

    @Override
    public String getAuthor() {
        return "Kugelbltz";
    }

    @Override
    public String getVersion() {
        return JadePack.version;
    }
}
