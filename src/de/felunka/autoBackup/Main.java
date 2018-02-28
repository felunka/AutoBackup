package de.felunka.autoBackup;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Main extends JavaPlugin {
	private BukkitRunnable run;
	private long interval;
	private boolean auto;
	private int lastsaves;
	private Lang lang;
	// Fired when plugin is first enabled
	@Override
	public void onEnable() {
		System.out.println("[AutoBackup] AutoBackup by felunka is starting...");
		readConf();
		readLang();
		if(this.auto) {
			run = new SaveRunnable(this, this.lastsaves, this.lang);
			long intertik = this.interval * 20;
			run.runTaskTimer(this, 600L, intertik);
		}
		System.out.println("[AutoBackup] AutoBackup by felunka is started!");
	}

	private void readLang() {
		this.lang = new Lang();
		Path folder = Paths.get("plugins/AutoBackup");
		if(Files.exists(folder)) {
			File conf = new File("plugins/AutoBackup/lang.json");
			try {
				Scanner s = new Scanner(conf, "utf-8");
				String confS = "";
				while(s.hasNextLine()) {
					confS += s.nextLine();
				}
				s.close();
				
				JSONParser parser = new JSONParser();
				JSONObject json = (JSONObject) parser.parse(confS);
				
				if(json.get("tenSecondAnnounce") != null)
					this.lang.TENSECONDANNOUNCE = (String) json.get("tenSecondAnnounce");
				else
					System.out.println("[AutoBackup] lang is broken! Values are missing...");
				
				if(json.get("countingBefore") != null)
					this.lang.COUNTINGBEFORE = (String) json.get("countingBefore");
				else
					System.out.println("[AutoBackup] lang is broken! Values are missing...");
				
				if(json.get("countingAfter") != null)
					this.lang.COUNTINGAFTER = (String) json.get("countingAfter");
				else
					System.out.println("[AutoBackup] lang is broken! Values are missing...");
				
				if(json.get("savingAnnounce") != null)
					this.lang.SAVINGANNOUNCE = (String) json.get("savingAnnounce");
				else
					System.out.println("[AutoBackup] lang is broken! Values are missing...");
				
				if(json.get("savingDoneAnnounce") != null)
					this.lang.SAVINGDONEANNOUNCE = (String) json.get("savingDoneAnnounce");
				else
					System.out.println("[AutoBackup] lang is broken! Values are missing...");
				
				System.out.println("[AutoBackup] found langfile!");
			} catch (Exception e) {
				System.out.println("[AutoBackup] no lang file found. Using default...");
			}
		} else {
			System.out.println("[AutoBackup] no lang file found. Using default...");
		}
	}

	private void readConf() {
		Path folder = Paths.get("plugins/AutoBackup");
		if(Files.exists(folder)) {
			File conf = new File("plugins/AutoBackup/conf.json");
			try {
				Scanner s = new Scanner(conf, "utf-8");
				String confS = "";
				while(s.hasNextLine()) {
					confS += s.nextLine();
				}
				s.close();
				
				JSONParser parser = new JSONParser();
				JSONObject json = (JSONObject) parser.parse(confS);
				
				if(json.get("intervall") != null)
					this.interval = (long) json.get("intervall");
				else
					System.out.println("[AutoBackup] config is broken! Values are missing...");
				if(json.get("intervall") != null)
					this.auto = (boolean) json.get("autobackup");
				else
					System.out.println("[AutoBackup] config is broken! Values are missing...");
				if(json.get("lastsaves") != null)
					this.lastsaves = Integer.parseInt(json.get("lastsaves").toString());
				else
					System.out.println("[AutoBackup] config is broken! Values are missing...");
				
				System.out.println("[AutoBackup] found config!");
			} catch (Exception e) {
				List<String> lines = Arrays.asList("{  ", "   \"autobackup\":false,", "   \"intervall\":21600,", "   \"lastsaves\":12", "}");
				Path file = Paths.get("plugins/AutoBackup/conf.json");
				try {
					Files.write(file, lines, Charset.forName("UTF-8"));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				System.out.println("[AutoBackup] config not found! creating...");
				this.interval = 6000;
				this.auto = false;
			}
		} else {
			System.out.println("[AutoBackup] config not found! creating...");
			new File("plugins/AutoBackup").mkdir();
			List<String> lines = Arrays.asList("{  ", "   \"autobackup\":false,", "   \"intervall\":21600,", "   \"lastsaves\":12", "}");
			Path file = Paths.get("plugins/AutoBackup/conf.json");
			try {
				Files.write(file, lines, Charset.forName("UTF-8"));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			this.interval = 6000;
			this.auto = false;
		}
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("backup")) { 
			new SaveRunnable(this, this.lastsaves, this.lang).runTask(this);
			return true;
		}
		return false; 
	}

	// Fired when plugin is disabled
	@Override
	public void onDisable() {
		if(this.auto)
			run.cancel();
		System.out.println("[AutoBackup] AutoBackup by felunka is disabled.");
	}
}
