package com.danielarevans.br;

import org.bukkit.Location;

public class Map {
	// Two Points that will be used to Generate a SQUARE map
	// Locations Left-Top, Right-Top, Left-Bottom, Right-Bottom
	Location LT, RT, LB, RB, CENTER;
	// Coordinates of quadrants
	double[][] gridX, gridZ;
	// Row and Column Amount in the SQUARE map
	int RCA;

	public Map(Location point1, Location point2, int RowColumnAmount) {

		double halfSide = 0;

		// Find distance to middle of the square

		if (Math.abs(point1.getX() - point2.getX()) >= Math.abs(point1.getZ()
				- point2.getZ())) {
			halfSide = (point1.getX() - point2.getX()) / 2;
		} else {
			halfSide = (point1.getZ() - point2.getZ()) / 2;
		}

		// Establish center location point
		CENTER = new Location(point1.getWorld(), point1.getX() + halfSide,
				point1.getY(), point1.getZ() + halfSide);

		// Locate the four corners

		LT = new Location(CENTER.getWorld(), CENTER.getX() - halfSide,
				CENTER.getY(), CENTER.getZ() - halfSide);
		RT = new Location(CENTER.getWorld(), CENTER.getX() - halfSide * 3,
				CENTER.getY(), CENTER.getZ() - halfSide);
		LB = new Location(CENTER.getWorld(), CENTER.getX() - halfSide,
				CENTER.getY(), CENTER.getZ() - halfSide * 3);
		RB = new Location(CENTER.getWorld(), CENTER.getX() - halfSide * 3,
				CENTER.getY(), CENTER.getZ() - halfSide * 3);

		// Generate Grids
		gridX = new double[RowColumnAmount][RowColumnAmount];
		gridZ = new double[RowColumnAmount][RowColumnAmount];
		RCA = RowColumnAmount;

		// Fill Grid Coordinates
		for (int a = 0; a < RCA; a++) {
			for (int b = 0; b < RCA; b++) {
				// Give X and Z coordinates of every quadrant (0,0) being top
				// left of graph.
				gridX[a][b] = LT.getX() + (((halfSide * 2) / RCA) * b);
				gridZ[a][b] = LT.getZ() + (((halfSide * 2) / RCA) * b);
			}
		}
	}

	public Location getLT() {
		return LT;
	}

	public Location getRB() {
		return RB;
	}

	public Location getLB() {
		return LB;
	}

	public Location getRT() {
		return RT;
	}

}
