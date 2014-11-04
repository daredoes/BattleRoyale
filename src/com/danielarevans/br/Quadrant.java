package com.danielarevans.br;
import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.entity.Player;
public class Quadrant {
	double x1,  x2, z1,z2;
	String name = "";
	ArrayList<Player> playersInGrid = new ArrayList<Player>();
	ArrayList<Player> playersInCurrentGrid = new ArrayList<Player>();
	public Quadrant(double x1, double z1, double x2,double z2, String gridName)
	{
		x1 = this.x1;
		x2 = this.x2;
		z1 = this.z1;
		z2 = this.z2;
		
		name = gridName;
	}
	public void getPlayersCurrentlyInGrid(Server serv)
	{
		Player[] tempPlayerArray =serv.getOnlinePlayers();
		for(int a = 0; a < tempPlayerArray.length;a++)
		{
			playersInGrid.add(tempPlayerArray[a]);
		}
		for(int b = 0; b < playersInGrid.size(); b++)
		{
			Location curPlayer = playersInGrid.get(b).getLocation();
			if(curPlayer.getX() >= x1 && curPlayer.getX() <= x2 && curPlayer.getZ() >= z1 && curPlayer.getZ() <= z2)
			{
				playersInCurrentGrid.add(playersInGrid.get(b));
			}
		}
		
	}
	public String getName()
	{
		return name;
	}
}
