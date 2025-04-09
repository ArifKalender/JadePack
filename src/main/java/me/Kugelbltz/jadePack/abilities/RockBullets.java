package me.Kugelbltz.jadePack.abilities;

import com.projectkorra.projectkorra.GeneralMethods;
import com.projectkorra.projectkorra.ability.AddonAbility;
import com.projectkorra.projectkorra.ability.CoreAbility;
import com.projectkorra.projectkorra.ability.EarthAbility;
import com.projectkorra.projectkorra.attribute.Attribute;
import com.projectkorra.projectkorra.region.RegionProtection;
import com.projectkorra.projectkorra.util.DamageHandler;
import com.projectkorra.projectkorra.util.TempBlock;
import com.projectkorra.projectkorra.util.TempFallingBlock;
import me.Kugelbltz.jadePack.JadePack;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.UUID;

import static me.Kugelbltz.jadePack.JadePack.plugin;

public class RockBullets extends EarthAbility implements AddonAbility {

    @Attribute(Attribute.DAMAGE)
    private double damage;
    @Attribute(Attribute.RANGE)
    private double range;
    @Attribute(Attribute.COOLDOWN)
    private long cooldown;
    @Attribute("Interval")
    private long throwInterval;
    @Attribute(Attribute.DURATION)
    private long duration;
    boolean allowDifferentSlots;
    long throwInternalChangeable;
    Location target;

    public RockBullets(Player player) {
        super(player);
        if (bPlayer.canBend(this) && !hasAbility(player, this.getClass())) {
            if (player.isOnGround()) {
                setFields();
                start();
                throwInternalChangeable=throwInterval;
            }
        }
    }

    private void setFields() {
        damage = plugin.getConfig().getDouble("Abilities.RockBullets.Damage");
        range = plugin.getConfig().getDouble("Abilities.RockBullets.Range");
        cooldown = plugin.getConfig().getLong("Abilities.RockBullets.Cooldown");
        throwInterval = plugin.getConfig().getLong("Abilities.RockBullets.Interval");
        duration = plugin.getConfig().getLong("Abilities.RockBullets.Duration");
        allowDifferentSlots = plugin.getConfig().getBoolean("Abilities.RockBullets.RemovalPolicy.AllowDifferentSlots");
    }

    private void throwRocks() {
        Location rockStart = player.getLocation();
        rockStart = GeneralMethods.getRightSide(rockStart, 2);
        rockStart.add(rockStart.getDirection().normalize().multiply(2));

        rockStart.setY(player.getLocation().getY()-1);
        Location target = player.getEyeLocation();

        for(int i=0;i<=range;i++){
            for(Entity entity : GeneralMethods.getEntitiesAroundPoint(target,1)){
                if(entity instanceof LivingEntity && entity != player){
                    break;
                }
            }
            if(target.getBlock().isPassable()){
                target.add(target.getDirection().normalize());
            }else{
                break;
            }
        }


        if (this.isEarthbendable(rockStart.getBlock())) {
            if (!RegionProtection.isRegionProtected(this, rockStart)) {
                Material mat = rockStart.getBlock().getType();
                new TempBlock(rockStart.getBlock(), Material.AIR.createBlockData(), 3000, this);
                FallingBlock fB = GeneralMethods.spawnFallingBlock(rockStart, mat, mat.createBlockData());
                fB.getWorld().spawnParticle(Particle.BLOCK_CRUMBLE,fB.getLocation(),10,1,1,1,0,mat.createBlockData());
                fB.setVelocity(new Vector(0, 0.66, 0));
                fB.setDropItem(false);
                fB.setCancelDrop(true);
                fB.getWorld().playSound(fB.getLocation(), Sound.BLOCK_ANCIENT_DEBRIS_BREAK,2,0);

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if(fB.getLocation().distance(target)<3){
                            fB.remove();
                            this.cancel();
                        }
                        fB.getWorld().spawnParticle(Particle.BLOCK_CRUMBLE,fB.getLocation(),5,0.5,0.5,0.5,0,mat.createBlockData());
                        for(Entity entity : GeneralMethods.getEntitiesAroundPoint(fB.getLocation(),1)){
                            if(entity instanceof LivingEntity && entity != player){
                                entity.getWorld().playSound(entity.getLocation(), Sound.BLOCK_ANCIENT_DEBRIS_BREAK, (float) (2*(range/10)),0);
                                DamageHandler.damageEntity(entity,damage, CoreAbility.getAbility(RockBullets.class));
                                ((LivingEntity) entity).setNoDamageTicks(0);
                            }
                        }
                        fB.setVelocity(target.toVector().subtract(fB.getLocation().toVector()).normalize().multiply(2));
                    }
                }.runTaskTimer(plugin, 10,1);
            }
        }
    }

    @Override
    public void progress() {
        if (!player.isOnline() || player.isDead() || !bPlayer.canBendIgnoreBinds(this) || this.getStartTime() + duration < System.currentTimeMillis()) {
            remove();
            bPlayer.addCooldown(this);
            return;
        }

        if(!allowDifferentSlots){
            if(!bPlayer.getBoundAbilityName().equalsIgnoreCase("RockBullets")){
                remove();
                bPlayer.addCooldown(this);
                return;
            }
        }


        Location target = player.getEyeLocation();

        for(int i=0;i<=range;i++){
            for(Entity entity : GeneralMethods.getEntitiesAroundPoint(target,1)){
                if(entity instanceof LivingEntity && entity != player){
                    break;
                }
            }
            if(target.getBlock().isPassable()){
                target.add(target.getDirection().normalize());
            }else{
                break;
            }
        }

        if(throwInternalChangeable<50){
            throwRocks();
            throwInternalChangeable=throwInterval;
        }else{
            throwInternalChangeable-=50;
        }
        //player.setVelocity(player.getEyeLocation().getDirection().setY(-1).multiply(0.6));

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
        return cooldown;
    }

    @Override
    public String getName() {
        return "RockBullets";
    }

    @Override
    public Location getLocation() {
        return target;
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
}
