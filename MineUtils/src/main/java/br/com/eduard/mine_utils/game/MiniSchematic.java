package br.com.eduard.mine_utils.game;

import br.com.eduard.java_utils.Copyable;
import br.com.eduard.mine_utils.Mine;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.data.BlockData;
import org.bukkit.util.Vector;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * API de capturar os blocos de um Terreno de um Ponto ao outro<br>
 * (Schematic do WorldEdit versão Compacta)
 * <br>
 * Antigo nome Schematic
 * @author Eduard
 * @version 1.0
 *
 */
@SuppressWarnings("unused")
final public class MiniSchematic {

	private Vector relative, low, high;
	private transient int count;
	private short width;
	private short height;
	private short length;
	private transient List<Chest> chests = new ArrayList<>();
	private transient String[] blocksInfo;


	 public short getWidth() {
		return width;
	}

	public void setWidth(short width) {
		this.width = width;
	}

	public short getHeight() {
		return height;
	}

	public void setHeight(short height) {
		this.height = height;
	}

	public short getLength() {
		return length;
	}

	public void setLength(short length) {
		this.length = length;
	}

	public static int getIndex(int x, int y, int z, int width, int length) {
		return y * width * length + z * width + x;
	}

	public MiniSchematic() {
	}


	public boolean hasFirstLocation() {
		return high != null;
	}

	public boolean hasSecondLocation() {
		return low != null;
	}
	public void copy(World world) {
		copy(relative.toLocation(world), low.toLocation(world), high.toLocation(world));
	}

	public void copy(Location relativeLocation) {
		World world = relativeLocation.getWorld();
		copy(relativeLocation, low.toLocation(world), high.toLocation(world));
	}

	public MiniSchematic copy() {
		return Copyable.copyObject(this);
	}

	@SuppressWarnings("deprecation")
	public void copy(Location relativeLocation, Location firstLocation, Location secondLocation) {
		setCount(0);
		Location highLoc = Mine.getHighLocation(firstLocation, secondLocation);
		Location lowLoc = Mine.getLowLocation(firstLocation, secondLocation);
		setHigh(highLoc.toVector());
		setLow(lowLoc.toVector());
		setRelative(relativeLocation.toVector());
		chests.clear();

		width = (short) (highLoc.getBlockX() - lowLoc.getBlockX());
		height = (short) (highLoc.getBlockY() - lowLoc.getBlockY());
		length = (short) (highLoc.getBlockZ() - lowLoc.getBlockZ());
		int size = width * height * length;
		this.blocksInfo = new String[size];
		World worldUsed = relativeLocation.getWorld();
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				for (int z = 0; z < length; z++) {
					count++;
					int index = getIndex(x, y, z, width, length);
					Block block = worldUsed.getBlockAt(lowLoc.getBlockX() + x, lowLoc.getBlockY() + y,
							lowLoc.getBlockZ() + z);
					if (block.getState() instanceof Chest) {
						Chest chest = (Chest) block.getState();
						chests.add(chest);
					}
					blocksInfo[index] =block.getBlockData().getAsString();
				}
			}
		}
	}

	public void paste(Location newRelative) {
		paste(newRelative, false);
	}

	@SuppressWarnings("deprecation")
	public void paste(Location newRelative, boolean minusLag) {
		World worldUsed = newRelative.getWorld();
		this.chests.clear();
		int difX = newRelative.getBlockX() - relative.getBlockX();
		int difY = newRelative.getBlockY() - relative.getBlockY();
		int difZ = newRelative.getBlockZ() - relative.getBlockZ();
		setCount(0);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				for (int z = 0; z < length; z++) {
					count++;
					int index = getIndex(x, y, z, width, length);
					Block block = worldUsed.getBlockAt(difX + low.getBlockX() + x, difY + low.getBlockY() + y,
							difZ + low.getBlockZ() + z);
					
					String blockInfo = blocksInfo[index];
					block.setBlockData(Bukkit.createBlockData(blockInfo));

					if (block.getState() instanceof Chest) {
						Chest chest = (Chest) block.getState();
						chests.add(chest);
						
					}
				}
			}
		}

	}

	public void setType(BlockData blockType ) {
		for (int index = 0; index < blocksInfo.length; index++) {
			blocksInfo[index] = blockType.getAsString();
		}
	}

	/**
	 * Precisa usar Sistema de Paleta de Armazenamento para gastar menos tempo processando
	 * @param file
	 */
	public void save(File file) {
		try {
			file.getParentFile().mkdirs();
			FileOutputStream fileWriting = new FileOutputStream(file);
			DataOutputStream dataWriting = new DataOutputStream(new GZIPOutputStream(fileWriting));
			dataWriting.writeShort(width);
			dataWriting.writeShort(height);
			dataWriting.writeShort(length);
			dataWriting.writeInt(blocksInfo.length);
			for (var blockDataString : blocksInfo){
				dataWriting.writeUTF(blockDataString);
			}
			dataWriting.writeUTF(Mine.serializeVector(low));
			dataWriting.writeUTF(Mine.serializeVector(high));
			dataWriting.writeUTF(Mine.serializeVector(relative));
			dataWriting.flush();
			dataWriting.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public MiniSchematic reload(File file) {
		try {
			FileInputStream s = new FileInputStream(file);
			DataInputStream d = new DataInputStream(new GZIPInputStream(s));
			this.width = d.readShort();
			this.height = d.readShort();
			this.length = d.readShort();
			int size = d.readInt();

			this.blocksInfo = new String[size];
			for(int index = 0; index < size;index++){
				this.blocksInfo[index] = d.readUTF();
			}
			low = Mine.deserializeVector(d.readUTF());
			high = Mine.deserializeVector(d.readUTF());
			relative = Mine.deserializeVector(d.readUTF());

			d.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;

	}


	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public static MiniSchematic load(File subfile) {
		return new MiniSchematic().reload(subfile);
	}

	public Vector getRelative() {
		return relative;
	}

	public void setRelative(Vector relative) {
		this.relative = relative;
	}

	public Vector getLow() {
		return low;
	}

	public void setLow(Vector low) {
		this.low = low;
	}

	public Vector getHigh() {
		return high;
	}

	public void setHigh(Vector high) {
		this.high = high;
	}

	public List<Chest> getChests() {
		return chests;
	}

	public void setChests(List<Chest> chests) {
		this.chests = chests;
	}


}
