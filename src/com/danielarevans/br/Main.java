package com.danielarevans.br;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {
	// The ArrayList of all who will be involved in combat.
	ArrayList<Player> Fighters = new ArrayList<Player>();
	// Allows the program to know whether or not fighting is currently underway.
	boolean GameInProgress = false;
	// Declare location points to be instantiated later.
	// waitPoint currently not being used.
	Location point1, point2, waitPoint, spawnPoint;
	// Declare map variable
	Map mainMap;
	// Random variable used for Weapon Assignment
	int rand = 0;
	// Variables that control whether points are being set or not
	boolean setSpawn = false;
	boolean setPoint = false;
	boolean setWait = false;
	// Used in the setMetadata Compass Value
	int index = 0;

	// Survival Kits Consist Of Food and a Tool or Weapon
	ArrayList<ItemStack> Tool = new ArrayList<ItemStack>();

	public void onEnable() {
		// Fun Message to tell the server
		getLogger().info("Let the battle commence!");
		// Enable Listener for class
		getServer().getPluginManager().registerEvents(this, this);

		// Make List of Weapons
		Tool.add(new ItemStack(Material.STONE_SWORD, 1));
		Tool.add(new ItemStack(Material.DIAMOND_SWORD, 1));
		Tool.add(new ItemStack(Material.DIAMOND_CHESTPLATE, 1));
		Tool.add(new ItemStack(Material.TNT, 16));
		Tool.add(new ItemStack(Material.COMPASS, 1));

	}

	public void onDisable() {
		// Fun message to tell the server
		getLogger().info("Did any survive?");
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		// Join command adds players to Fighters array if no game is already in
		// progress
		// Also assigns base compass Metadata
		if (cmd.getName().equalsIgnoreCase("join")) {
			if (GameInProgress) {
				sender.sendMessage("IT IS TOO LATE TO JOIN COMBAT");
			} else {
				// Get the online players and sort through them
				for (Player a : getServer().getOnlinePlayers()) {
					// Upon match add player to list of fighters.
					if (a.getName().equalsIgnoreCase(sender.getName())) {
						if (!Fighters.contains(a)) {
							Fighters.add(a);
							a.setMetadata("compassIndex",
									new FixedMetadataValue(this, 0));
							a.sendMessage("YOU FIGHT FOR GLORY");
						}

					}
				}
			}

			return true;
		}
		// Leave command removes players from Fighters array if no game is
		// already in progress
		if (cmd.getName().equalsIgnoreCase("leave")) {
			// Get the online players and sort through them
			if (!GameInProgress) {
				for (Player a : getServer().getOnlinePlayers()) {
					// Upon match add player to list of fighters.
					if (a.getName().equalsIgnoreCase(sender.getName())) {
						if (Fighters.contains(a)) {
							Fighters.remove(a);
							a.sendMessage("HANG YOUR HEAD IN SHAME");
						} else {
							a.sendMessage("YOU ARE NOT A FIGHTER TO BEGIN WITH");
						}

					}
				}
			} else {
				sender.sendMessage("YOU CANNOT LEAVE WHAT HAS ALREADY BEGUN");
			}
			return true;
		}
		// Start command begins the game by changing the appropriate variables
		if (cmd.getName().equalsIgnoreCase("start")) {
			GameInProgress = true;
			// Deletes and replaces all players in the ArrayList Fighters
			// inventories
			// Does not store them
			newInventories(Fighters);
			for (Player p : getServer().getOnlinePlayers()) {
				// Game Begins Message
				p.sendMessage("LET THE GAMES BEGIN");
			}
			// Moves all fighters to set combat spawn point and turns all of the
			// invisible to each other
			// Gives them 30 seconds to move to new areas
			for (Player p : Fighters) {
				p.teleport(spawnPoint);
				p.sendMessage("You have been teleported to combat. Run!");
				p.sendMessage("YOU HAVE 30 SECONDS");
				hidePlayers(p, Fighters);
			}
			getServer().getScheduler().scheduleSyncDelayedTask(this,
					new Runnable() {

						public void run() {
							for (Player p : Fighters) {
								showPlayers(p, Fighters);
							}
						}
					}, 600L);

			/*
			 * getServer().getScheduler().scheduleSyncDelayedTask(this, new
			 * Runnable() {
			 * 
			 * public void run() { getLogger().info("IT HAS RUN"); } }, 180L);
			 */
			return true;
		}
		// End command will end the game early if used by an admin and reset all
		// neccessary variables to start another game
		if (cmd.getName().equalsIgnoreCase("end")) {
			if (GameInProgress) {
				for (Player p : Fighters) {
					Fighters.remove(p);
				}
				for (Player p : getServer().getOnlinePlayers()) {
					// Message to tell all players the games have been ended
					// early
					p.sendMessage("The games have been ended early.");
				}
				GameInProgress = false;
			} else {
				// Message to let sender know that no game is active
				sender.sendMessage("There is currently no game being played");
			}
			return true;
		}
		// Allows admin player to set the corners of a map
		// If the map isn't a perfect square the longer side will become the
		// length of all sides from point 1
		if (cmd.getName().equalsIgnoreCase("setMap")) {

			if (setPoint) {
				setPoint = false;
				sender.sendMessage("Rotten Flesh has returned to normal");

			} else {
				setPoint = true;
				sender.sendMessage("Please use Rotten Flesh to set opposite corners of the map.");
				sender.sendMessage("Left-Click to set the first corner and Right-Click to set the second corner.");
				for (Player p : getServer().getOnlinePlayers()) {
					if (p.getName().equalsIgnoreCase(sender.getName())) {
						p.getInventory().addItem(
								new ItemStack(Material.ROTTEN_FLESH, 1));
					}
				}
			}
			return true;
		}
		// Allows admin player to set a waiting spawn point for all players
		// NOT CURRENTLY BEING USED IN GAME
		if (cmd.getName().equalsIgnoreCase("setWait")) {
			if (setWait) {
				setWait = false;
				sender.sendMessage("Bone has returned to normal");
			} else {
				setWait = true;
				sender.sendMessage("Please use Bone to set a point where players will wait while being sent into combat.");
				sender.sendMessage("Right-Click the block you would like them to spawn on.");
				for (Player p : getServer().getOnlinePlayers()) {
					if (p.getName().equalsIgnoreCase(sender.getName())) {
						p.getInventory().addItem(
								new ItemStack(Material.BONE, 1));
					}
				}
			}
			return true;
		}
		// Allows admin player to set a spawn point for all fighting players
		if (cmd.getName().equalsIgnoreCase("setSpawn")) {
			if (setSpawn) {
				setSpawn = false;
				sender.sendMessage("Flint has returned to normal");
			} else {
				setSpawn = true;
				sender.sendMessage("Please use Flint to set a point where players spawn to begin combat.");
				sender.sendMessage("Right-Click the block you would like them to spawn on.");
				for (Player p : getServer().getOnlinePlayers()) {
					if (p.getName().equalsIgnoreCase(sender.getName())) {
						p.getInventory().addItem(
								new ItemStack(Material.FLINT, 1));
					}
				}
			}
			return true;
		}
		// Uses information from setMap to generate a Map Object
		// Resets appropriate variables
		if (cmd.getName().equalsIgnoreCase("genMap")) {
			if (args.length == 1) {
				mainMap = new Map(point1, point2, Integer.parseInt(args[0]));
				sender.sendMessage("MAP CREATED");
				setPoint = false;
				sender.sendMessage("Rotten Flesh has been reverted to normal");
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	// On player respawn this will check to remove them from the fighters and
	// see if the games are over yet
	@EventHandler
	public void respawn(PlayerRespawnEvent e) {
		getLogger().info("Size: " + Fighters.size());
		if (Fighters.contains(e.getPlayer())) {
			Fighters.remove(e.getPlayer());
			e.getPlayer().sendMessage("You have been removed from combat!");
		}
		if (Fighters.size() <= 1 && GameInProgress) {
			GameInProgress = false;
			for (Player p : getServer().getOnlinePlayers()) {
				p.sendMessage("THE GAMES ARE OVER");

				if (Fighters.size() == 0) {
					p.sendMessage("THERE IS NO VICTOR. EVERYONE LOSES.");
				} else {
					p.sendMessage("THE VICTOR IS "
							+ Fighters.get(0).getDisplayName() + ".");
					Fighters.remove(0);
				}
			}
		}
	}

	// Hides players from sight
	public void hidePlayers(Player p, ArrayList<Player> a) {
		for (Player b : a) {
			if (!p.equals(b))
				p.hidePlayer(b);
		}
	}

	// Reveals players to player
	public void showPlayers(Player p, ArrayList<Player> a) {
		for (Player b : a) {
			if (!p.equals(b))
				p.showPlayer(b);
		}
	}

	// On player quit checks if player was a combatant and removes them if so
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		if (Fighters.contains(e.getPlayer())) {
			Fighters.remove(e.getPlayer());
		}
	}

	// Used for setting all points
	@EventHandler
	public void onPointSelect(PlayerInteractEvent e) {
		if (setWait) {
			if (e.hasItem()) {
				// sets waiting point
				if (e.getItem().getType().equals(Material.BONE)) {
					if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
						waitPoint = e.getClickedBlock().getLocation();
						waitPoint.setY(waitPoint.getY() + 1);
						e.getPlayer().sendMessage("Waiting point set.");
					}
				}
			}
		}
		if (setSpawn) {
			if (e.hasItem()) {
				// sets spawn point
				if (e.getItem().getType().equals(Material.FLINT)) {
					if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
						spawnPoint = e.getClickedBlock().getLocation();
						spawnPoint.setY(spawnPoint.getY() + 1);
						e.getPlayer().sendMessage("Spawn point set");
					}
				}
			}
		}
		if (setPoint) {
			if (e.hasItem()) {
				// sets map opposite corners
				if (e.getItem().getType().equals(Material.ROTTEN_FLESH)) {
					if (e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
						point1 = e.getClickedBlock().getLocation();
						e.getPlayer().sendMessage("Point 1 set");
					}
					if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
						point2 = e.getClickedBlock().getLocation();
						e.getPlayer().sendMessage("Point 2 set");
					}
				}
			}
			// Modifies fighter compass to locate players
			if (Fighters.contains(e.getPlayer()))
				if (GameInProgress)
					if (e.getItem().getType().equals(Material.COMPASS)) {
						if (e.getAction().equals(Action.LEFT_CLICK_AIR)
								|| e.getAction()
										.equals(Action.LEFT_CLICK_BLOCK)) {
							// Change player in list of who compass targets
							index = e.getPlayer().getMetadata("compassIndex")
									.get(0).asInt() - 1;
							if (Fighters.get(index).equals(e.getPlayer())) {
								index--;
							}
							if (index < 0) {
								index = Fighters.size() - 1;
							}

							e.getPlayer().setMetadata("compassIndex",
									new FixedMetadataValue(this, index));
						}
						if (e.getAction().equals(Action.RIGHT_CLICK_AIR)
								|| e.getAction().equals(
										Action.RIGHT_CLICK_BLOCK)) {
							index = e.getPlayer().getMetadata("compassIndex")
									.get(0).asInt() + 1;
							if (Fighters.get(index).equals(e.getPlayer())) {
								index++;
							}
							if (index >= Fighters.size()) {
								index = 0;
							}

							e.getPlayer()
									.getItemInHand()
									.getItemMeta()
									.setDisplayName(
											Fighters.get(index)
													.getDisplayName());

							e.getPlayer().setMetadata("compassIndex",
									new FixedMetadataValue(this, index));
						}

					}
		}
	}
	//Sets Player Compass target as well as prevents fighters from leaving map
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e) {
		if (GameInProgress) {
			if (Fighters.contains(e.getPlayer())) {
				if (e.getPlayer().getInventory().contains(Material.COMPASS)) {
					e.getPlayer().setCompassTarget(
							Fighters.get(
									e.getPlayer().getMetadata("compassIndex")
											.get(0).asInt()).getLocation());
				}

				Location from = e.getFrom();
				getLogger().info(e.getFrom().toString());
				double greaterX, greaterZ, lowerX, lowerZ;
				if (mainMap.getLT().getX() > mainMap.getRB().getX()) {
					greaterX = mainMap.getLT().getX();
					lowerX = mainMap.getRB().getX();
				} else {
					lowerX = mainMap.getLT().getX();
					greaterX = mainMap.getRB().getX();
				}
				if (mainMap.getLT().getZ() > mainMap.getRB().getZ()) {
					greaterZ = mainMap.getLT().getZ();
					lowerZ = mainMap.getRB().getZ();
				} else {
					lowerZ = mainMap.getLT().getZ();
					greaterZ = mainMap.getRB().getZ();
				}
				if (from.getX() < lowerX || from.getX() > greaterX) {
					e.getPlayer().setHealth(0.0);
					Fighters.remove(e.getPlayer());
				}
				if (from.getZ() > greaterZ || from.getZ() < lowerZ) {
					e.getPlayer().setHealth(0.0);
					Fighters.remove(e.getPlayer());
				}

				// getLogger().info("LT X: " + mainMap.getLT().getX());
				// getLogger().info("LT Z: " + mainMap.getLT().getZ());

				// getLogger().info("RT X: " + mainMap.getRT().getX());
				// getLogger().info("RT Z: " + mainMap.getRT().getZ());

				// getLogger().info("LB X: " + mainMap.getLB().getX());
				// getLogger().info("LB Z: " + mainMap.getLB().getZ());

				// getLogger().info("RB X: " + mainMap.getRB().getX());
				// getLogger().info("RB Z: " + mainMap.getRB().getZ());

			}
		}
	}
	//Gives all Players in the array a Tool, food, and a bonus item depending on their tool
	public void newInventories(ArrayList<Player> people) {
		for (Player a : people) {
			a.getInventory().clear();
			rand = (int) (Math.random() * Tool.size());
			a.getInventory().addItem(Tool.get(rand));
			a.getInventory().addItem(new ItemStack(Material.COOKED_BEEF, 8));
			if (Tool.get(rand).getType().equals(Material.BOW)) {
				a.getInventory().addItem(
						new ItemStack(Material.ARROW,
								(int) Math.random() * 48 + 16));
			}
			if (Tool.get(rand).getType().equals(Material.TNT)) {
				a.getInventory().addItem(
						new ItemStack(Material.FLINT_AND_STEEL, 1));
			}
			//Removes compass if it is given out so each match may only have one compass
			if (Tool.get(rand).getType().equals(Material.COMPASS)) {
				Tool.remove(rand);
			}
		}
	}


}
