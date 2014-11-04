package com.danielarevans.br;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;


public class BlockInteract implements Listener{
	static Location point;
	@EventHandler
	public void onPointSelect(PlayerInteractEvent e)
	{
		if(Main.setPoint())
		{
			if(e.getItem().getType().equals(Material.ROTTEN_FLESH))
			{
				if(e.getAction().equals(Action.LEFT_CLICK_BLOCK))
				{
					Main.point1 = e.getClickedBlock().getLocation();
					e.getPlayer().sendMessage("Point 1 set");
				}
				if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK))
				{
					Main.point2 = e.getClickedBlock().getLocation();
					e.getPlayer().sendMessage("Point 2 set");
				}
			}
		}
	}
	public static Location getPoint()
	{
		return point;
	}

}
