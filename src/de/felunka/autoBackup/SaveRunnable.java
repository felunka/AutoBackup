package de.felunka.autoBackup;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandException;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class SaveRunnable extends BukkitRunnable {

	private final JavaPlugin plugin;
	private ZipIt zip;
	private int lastsave;
	private Lang lang;

	public SaveRunnable(JavaPlugin plugin, int lastsave, Lang lang) {
		this.plugin = plugin;
		this.lastsave = lastsave;
		this.lang = lang;
		this.zip = new ZipIt();
	}

	@Override
	public void run() {
		try {
			this.plugin.getServer().broadcastMessage(this.lang.getTenSecond());
			TimeUnit.SECONDS.sleep(5);
			this.plugin.getServer().broadcastMessage(this.lang.getCount(5));
			TimeUnit.SECONDS.sleep(1);
			this.plugin.getServer().broadcastMessage(this.lang.getCount(4));
			TimeUnit.SECONDS.sleep(1);
			this.plugin.getServer().broadcastMessage(this.lang.getCount(3));
			TimeUnit.SECONDS.sleep(1);
			this.plugin.getServer().broadcastMessage(this.lang.getCount(2));
			TimeUnit.SECONDS.sleep(1);
			this.plugin.getServer().broadcastMessage(this.lang.getCount(1));
			TimeUnit.SECONDS.sleep(1);
			this.plugin.getServer().broadcastMessage(this.lang.getSavingAnn());
			if (save()) {
				System.out.println("[AutoBackup] World saved!");
			} else {
				System.out.println("[AutoBackup] Save failed!");
				return;
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Path folder = Paths.get("backup");
		if (!Files.exists(folder)) {
			new File("backup").mkdir();
		}
		String timeStamp = new SimpleDateFormat("_dd.MM.yyyy_HH:mm").format(Calendar.getInstance().getTime());
		List<World> worlds = Bukkit.getWorlds();
		for (World w : worlds) {
			System.out.println("[AutoBackup] Backing up: " + w.getName());
			Path worldFolder = Paths.get("backup/" + w.getName());
			if (!Files.exists(worldFolder)) {
				new File("backup/" + w.getName()).mkdir();
			}
			this.zip.zipIt(w.getName(), "backup/" + w.getName() + "/" + w.getName() + timeStamp + ".zip");
			cleanup("backup/" + w.getName() + "/");
		}
		this.plugin.getServer().broadcastMessage(this.lang.getSavingDone());
	}

	private void cleanup(String directory) {
		try {
			if (Files.list(Paths.get(directory)).count() > this.lastsave) {
				Path dir = Paths.get(directory);

				Optional<Path> lastFilePath = Files.list(dir).filter(f -> !Files.isDirectory(f))
						.min(Comparator.comparingLong(f -> f.toFile().lastModified()));

				if (lastFilePath.isPresent()) {
					new File(lastFilePath.get().toString()).delete();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private boolean save() {
		try {
			this.plugin.getServer().dispatchCommand(this.plugin.getServer().getConsoleSender(), "save-all");
			return true;
		} catch (CommandException e) {
			e.printStackTrace();
			return false;
		}
	}
}
