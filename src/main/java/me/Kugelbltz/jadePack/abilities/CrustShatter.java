package me.Kugelbltz.jadePack.abilities;

import com.projectkorra.projectkorra.GeneralMethods;
import com.projectkorra.projectkorra.ability.AddonAbility;
import com.projectkorra.projectkorra.ability.ComboAbility;
import com.projectkorra.projectkorra.ability.EarthAbility;
import com.projectkorra.projectkorra.ability.util.ComboManager;
import com.projectkorra.projectkorra.attribute.Attribute;
import com.projectkorra.projectkorra.util.ClickType;
import com.projectkorra.projectkorra.util.DamageHandler;
import com.projectkorra.projectkorra.util.TempBlock;
import com.projectkorra.projectkorra.util.TempFallingBlock;
import me.Kugelbltz.jadePack.JadePack;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static me.Kugelbltz.jadePack.JadePack.plugin;

public class CrustShatter extends EarthAbility implements AddonAbility, ComboAbility {

    @Attribute(Attribute.COOLDOWN)
    private long cooldown;
    @Attribute(Attribute.DAMAGE)
    private double damage;
    @Attribute(Attribute.RANGE)
    private double range;
    @Attribute("WidthIncrement")
    private float widthIncrement;
    @Attribute(Attribute.CHARGE_DURATION)
    private boolean fallingBlocks;
    Vector dir;
    private float increasingWidth;
    Location loc;
    long charge;

    public CrustShatter(Player player) {
        super(player);
        if (bPlayer.canBendIgnoreBinds(this) && !hasAbility(player, CrustShatter.class)) {
            if (this.isEarthbendable(player.getLocation().add(0, -0.1, 0).getBlock())) {
                setFields();
                dir = player.getEyeLocation().getDirection().setY(0).normalize();
                loc = player.getLocation().add(player.getLocation().getDirection().normalize());
                loc.add(0,0.5,0);
                start();
            }
        }
    }

    private void setFields() {
        cooldown = plugin.getConfig().getLong("Abilities.CrustShatter.Cooldown");
        damage = plugin.getConfig().getDouble("Abilities.CrustShatter.Damage");
        range = plugin.getConfig().getDouble("Abilities.CrustShatter.Range");
        widthIncrement = (float) plugin.getConfig().getDouble("Abilities.CrustShatter.WidthIncrement");
        fallingBlocks = plugin.getConfig().getBoolean("Abilities.CrustShatter.FallingBlocks");
        charge = plugin.getConfig().getLong("Abilities.CrustShatter.Charge");

    }

    @Override
    public boolean isEnabled() {
        return plugin.getConfig().getBoolean("Abilities.CrustShatter.Enabled");
    }

    @Override
    public String getInstructions() {
        return plugin.getConfig().getString("Strings.CrustShatter.Instructions");
    }

    @Override
    public String getDescription() {
        return plugin.getConfig().getString("Strings.CrustShatter.Description");
    }

    List<Block> toRemove = new ArrayList<>();
    List<Entity> damaged = new ArrayList<>();

    private void shatterGround() {
        player.getWorld().playSound(player.getLocation(), Sound.ITEM_MACE_SMASH_GROUND_HEAVY, 2, 0);
        Location temp = player.getLocation().clone().add(0, -0.5, 0);
        for (int i = 0; i <= range; i++) {
            temp.add(dir.clone().normalize());
            toRemove.add(temp.getBlock());
            increasingWidth += widthIncrement;
            for (float j = 0; j <= increasingWidth; j += 0.1f) {
                toRemove.add(GeneralMethods.getLeftSide(temp, j).getBlock());
                toRemove.add(GeneralMethods.getRightSide(temp, j).getBlock());
            }
        }
        for (Block block : toRemove) {
            if (this.isEarthbendable(block.getLocation().add(0, 1, 0).getBlock())) {
                BlockData data = block.getLocation().add(0, 1, 0).getBlock().getBlockData();
                new TempBlock(block.getLocation().add(0, 1, 0).getBlock(), Material.AIR.createBlockData(), 6000, this);
                if (fallingBlocks) {
                    new TempFallingBlock(block.getLocation().add(0, 1, 0), data, new Vector(new Random().nextDouble(), Math.abs(new Random().nextDouble()), new Random().nextDouble()).multiply(0.6), this);
                }
                block.getWorld().spawnParticle(Particle.EXPLOSION,block.getLocation().add(0,1,0),1,1,1,1,0);
            }
            if (this.isEarthbendable(block.getLocation().add(0, 2, 0).getBlock())) {
                BlockData data = block.getLocation().add(0, 2, 0).getBlock().getBlockData();
                new TempBlock(block.getLocation().add(0, 2, 0).getBlock(), Material.AIR.createBlockData(), 6000, this);
                if (fallingBlocks) {
                    new TempFallingBlock(block.getLocation().add(0, 2, 0), data, new Vector(new Random().nextDouble(), Math.abs(new Random().nextDouble()), new Random().nextDouble()).multiply(0.6), this);
                }
                block.getWorld().spawnParticle(Particle.EXPLOSION,block.getLocation().add(0,2,0),1,1,1,1,0);

            }

            if (this.isEarthbendable(block)) {
                BlockData data = block.getBlockData();

                block.getWorld().spawnParticle(Particle.EXPLOSION,block.getLocation(),1,1,1,1,0);
                new TempBlock(block, Material.AIR.createBlockData(), 6000, this);
                if (fallingBlocks) {
                    new TempFallingBlock(block.getLocation(), data, new Vector(new Random().nextDouble(), Math.abs(new Random().nextDouble()), new Random().nextDouble()).multiply(0.6), this);
                }

                for (Entity entity : GeneralMethods.getEntitiesAroundPoint(block.getLocation(), 1.5)) {
                    if (entity instanceof LivingEntity && entity != player && !damaged.contains(entity)) {
                        DamageHandler.damageEntity(entity, damage, this);
                        entity.setVelocity(new Vector(new Random().nextDouble(), 1, new Random().nextDouble()));
                        damaged.add(entity);
                    }

                }
            }
        }

    }

    @Override
    public void progress() {

        if (getStartTime() + charge > System.currentTimeMillis()) {
            player.setVelocity(new Vector(0, 0, 0));
            loc.getWorld().spawnParticle(Particle.SMOKE,loc,4,0,0.1,0,0);
        } else {
            shatterGround();
            remove();
            bPlayer.addCooldown(this);
            return;
        }

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
        return "CrustShatter";
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
    public Object createNewComboInstance(Player player) {
        return new CrustShatter(player);
    }

    @Override
    public ArrayList<ComboManager.AbilityInformation> getCombination() {
        ArrayList<ComboManager.AbilityInformation> combination = new ArrayList<>();
        combination.add(new ComboManager.AbilityInformation("EarthQuake", ClickType.SHIFT_DOWN));
        combination.add(new ComboManager.AbilityInformation("Shockwave", ClickType.LEFT_CLICK));
        combination.add(new ComboManager.AbilityInformation("Shockwave", ClickType.SHIFT_UP));
        combination.add(new ComboManager.AbilityInformation("EarthSmash", ClickType.SHIFT_DOWN));
        combination.add(new ComboManager.AbilityInformation("EarthSmash", ClickType.SHIFT_UP));

        return combination;
    }
}
