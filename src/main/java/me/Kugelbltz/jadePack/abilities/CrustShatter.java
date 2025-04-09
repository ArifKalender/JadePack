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
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
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
    private long charge;
    private boolean fallingBlocks;
    private boolean arrowVisual;
    Vector dir;

    private boolean canStart;
    private float increasingWidth;
    private Location location;


    public CrustShatter(Player player) {
        super(player);
        if (bPlayer.canBendIgnoreBinds(this) && !hasAbility(player, CrustShatter.class)) {
            setFields();
            dir = player.getEyeLocation().getDirection().setY(0).normalize();
            start();
        }
    }

    private void setFields() {
        cooldown = plugin.getConfig().getLong("Abilities.CrustShatter.Cooldown");
        damage = plugin.getConfig().getDouble("Abilities.CrustShatter.Damage");
        range = plugin.getConfig().getDouble("Abilities.CrustShatter.Range");
        widthIncrement = (float) plugin.getConfig().getDouble("Abilities.CrustShatter.WidthIncrement");
        fallingBlocks = plugin.getConfig().getBoolean("Abilities.CrustShatter.FallingBlocks");
        arrowVisual = plugin.getConfig().getBoolean("Abilities.CrustShatter.ArrowVisual");
        charge = plugin.getConfig().getLong("Abilities.CrustShatter.Charge");

        canStart = false;
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

    private void drawLine(Location location1, Location location2, int slowness) {
        new BukkitRunnable() {
            private final double distance = location1.distance(location2);
            private final int points = (int) (distance * 10);
            private final Location current = location1.clone();
            private final double stepX = (location2.getX() - location1.getX()) / points;
            private final double stepY = (location2.getY() - location1.getY()) / points;
            private final double stepZ = (location2.getZ() - location1.getZ()) / points;
            private int count = 0;

            @Override
            public void run() {

                if (count >= points) {
                    this.cancel();
                    return;
                }

                current.add(stepX, stepY, stepZ);
                current.getWorld().spawnParticle(Particle.CRIT, current, 1, 0, 0, 0, 0);
                count++;
            }
        }.runTaskTimer(plugin, 0, slowness);
    }

    private void drawArrow() {
        Location arrowTop, arrowBottom, arrowLeft, arrowRight;

        arrowTop = player.getEyeLocation().add(dir.clone().multiply(3)).add(0, 1, 0);
        arrowBottom = player.getLocation().add(dir.clone().multiply(3));
        arrowLeft = GeneralMethods.getLeftSide(arrowTop.clone().add(0, -1.2, 0), 0.6);
        arrowRight = GeneralMethods.getRightSide(arrowTop.clone().add(0, -1.2, 0), 0.6);

        drawLine(arrowTop, arrowBottom, 1);
        drawLine(arrowLeft, arrowBottom, 2);
        drawLine(arrowRight, arrowBottom, 2);

        player.setVelocity(new Vector(0, 0, 0));
    }

    List<Block> toRemove = new ArrayList<>();

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
            }
            if (this.isEarthbendable(block.getLocation().add(0, 2, 0).getBlock())) {
                BlockData data = block.getLocation().add(0, 2, 0).getBlock().getBlockData();
                new TempBlock(block.getLocation().add(0, 2, 0).getBlock(), Material.AIR.createBlockData(), 6000, this);
                if (fallingBlocks) {
                    new TempFallingBlock(block.getLocation().add(0, 2, 0), data, new Vector(new Random().nextDouble(), Math.abs(new Random().nextDouble()), new Random().nextDouble()).multiply(0.6), this);
                }

            }

            if (this.isEarthbendable(block)) {
                BlockData data = block.getBlockData();

                new TempBlock(block, Material.AIR.createBlockData(), 6000, this);
                if (fallingBlocks) {
                    new TempFallingBlock(block.getLocation(), data, new Vector(new Random().nextDouble(), Math.abs(new Random().nextDouble()), new Random().nextDouble()).multiply(0.6), this);
                }

                for (Entity entity : GeneralMethods.getEntitiesAroundPoint(block.getLocation(), 1)) {
                    player.sendMessage("entity");
                    entity.setVelocity(new Vector(new Random().nextDouble(), Math.abs(new Random().nextDouble()), new Random().nextDouble()));
                    DamageHandler.damageEntity(entity, damage, this);
                }
            }
        }

    }

    @Override
    public void progress() {

        if (getStartTime() + 1400 > System.currentTimeMillis()) {
            player.setVelocity(new Vector(0, 0, 0));
            Location loc = player.getLocation().add(dir.clone().multiply(3));
            loc.getWorld().spawnParticle(Particle.ENCHANTED_HIT, loc, 1, 0, 0, 0, 0);

            if (arrowVisual) {
                drawArrow();
                arrowVisual = false;
            }
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
