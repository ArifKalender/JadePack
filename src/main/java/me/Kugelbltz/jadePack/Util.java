package me.Kugelbltz.jadePack;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Util {
    public static List<Block> getBlocksBelow(double radius, Location location) {
        List<Block> blocks = new ArrayList<>();
        int px = location.getBlockX();
        int py = location.getBlockY() - 1;
        int pz = location.getBlockZ();

        for (int x = px - (int) radius; x <= px + (int) radius; x++) {
            for (int z = pz - (int) radius; z <= pz + (int) radius; z++) {
                Location loc = new Location(location.getWorld(), x, py, z);
                if (loc.distance(location) <= radius) {
                    Block block = loc.getBlock();
                    if (block.getType() != Material.AIR || block.getType() != Material.LAVA) {
                        blocks.add(block);
                    }
                }
            }
        }
        return blocks;
    }
}
