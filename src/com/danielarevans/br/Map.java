package com.danielarevans.br;
import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.entity.Player;
public class Map {
	Location pointA,pointB;
	String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	int gridRowColumnLength = 5;
	int gridNameNum = 0;
	String gridName = "";
	String gridAlphabetLocation = "A";
	ArrayList<Quadrant> grid = new ArrayList<Quadrant>();
	public Map(Location one, Location two)
	{
		pointA = one;
		pointB = two;
		for(double a = pointA.getX(); a <= pointB.getX(); a += pointB.getX()/gridRowColumnLength)
		{
			for(double b = pointA.getZ(); b <= pointB.getZ(); b+= pointB.getZ()/gridRowColumnLength)
			{
				gridNameNum++;
				if(gridNameNum > gridRowColumnLength)
				{
					gridNameNum = 1;
				}
				gridName = gridAlphabetLocation+gridNameNum;
				Quadrant e = new Quadrant(a,b,a+pointB.getX()/gridRowColumnLength,b+pointB.getZ()/gridRowColumnLength,gridName);
				grid.add(e);
			}
			if(alphabet.indexOf(gridAlphabetLocation) <= alphabet.length()-2)
			{
				int test = alphabet.indexOf(gridAlphabetLocation);
				int test2 = alphabet.indexOf(gridAlphabetLocation)+1;
				gridAlphabetLocation = alphabet.substring(test,test2);
			}
		}
	}
	public ArrayList<Quadrant> getGrid()
	{
		return grid;
	}
	public void radarPlayers(Player p)
	{
		for(Quadrant quad : grid)
		{
			quad.getPlayersCurrentlyInGrid(p.getServer());
			double distance = 0;
			p.sendMessage("DICKS");
			for(Player players: quad.playersInCurrentGrid)
			{
				p.sendMessage("DICKBUTT");
				distance = p.getLocation().distance(players.getLocation());
				p.sendMessage(players.getDisplayName()+" is " + distance + "blocks away in grid " + quad.name);
			}
		}
	}

}
