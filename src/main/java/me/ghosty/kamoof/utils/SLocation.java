package me.ghosty.kamoof.utils;

import org.bukkit.Location;
import org.bukkit.World;

/**
 * S for Super!!!
 */
public class SLocation extends Location {
	
	public SLocation(Location location) {
		super(location.getWorld(), location.getX(), location.getY(), location.getZ());
	}
	
	public SLocation(World world, double x, double y, double z) {
		super(world, x, y, z);
	}
	
	public SLocation plus(double x, double y, double z) {
		return new SLocation(getWorld(), getX() + x, getY() + y, getZ() + z);
	}
	
}
