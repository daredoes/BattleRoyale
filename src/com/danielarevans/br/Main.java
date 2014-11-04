package com.danielarevans.br;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;



public class Main extends JavaPlugin{
	ArrayList<Quadrant> grids = new ArrayList<Quadrant>();
	Map mainMap;
	static Location point1;
	static Location point2;
	String item = "Rotten Flesh";
	ArrayList<Quadrant> theGrid = new ArrayList<Quadrant>();
	static boolean selectPoint = false;
	public void onEnable(){
		getLogger().info("Let the battle commence!");
		PluginManager pm = getServer().getPluginManager();   
		 
        pm.registerEvents(new BlockInteract(), this);
	}
	public void onDisable(){
		getLogger().info("Did any survive?");
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		if(cmd.getName().equalsIgnoreCase("brmap"))
		{
			mainMap = new Map(point1,point2);
			sender.sendMessage("Map Created");
			return true;
		}
		if(cmd.getName().equalsIgnoreCase("brradar"))
		{
			mainMap.radarPlayers((Player) sender);
			return true;
		}
		if(cmd.getName().equalsIgnoreCase("brMapPoints"))
		{
			if(!selectPoint)
			{
				selectPoint = true;
				sender.sendMessage("Using " + item+", Left-Click a block to set first point, Right-Click a block to set second point");
			}
			else
			{
				selectPoint = false;
				sender.sendMessage("Item back to normal");
			}
			
			return true;
		}
		return false;
	}
	public static boolean setPoint()
	{
		return selectPoint;
	}
	public void radarPlayers(){
		Player[] players = getServer().getOnlinePlayers();
		double distance = 0;
		theGrid = mainMap.getGrid();
		for(int a = 0; a <players.length;a++)
		{
			for(int b = 0; b < players.length;b++)
			{
				//if(b!=a)
				{
					distance = players[a].getLocation().distance(players[b].getLocation());
					for(int c = 0; c < theGrid.size(); c++)
					{
						for(int d = 0; d< theGrid.get(c).playersInCurrentGrid.size();d++)
						{
							if(theGrid.get(c).playersInCurrentGrid.get(d).equals(players[b]))
							{
								//double x = players[a].getLocation().getX() - players[b].getLocation().getX();
								//double z = players[a].getLocation().getZ() - players[b].getLocation().getZ();
								players[a].sendMessage(players[b].getName() + ": Distance("+distance+" blocks), Quadrant("+theGrid.get(c).getName()+")");
							}
						}
						
					}
					
				}
			}
		}
	}
	
}
