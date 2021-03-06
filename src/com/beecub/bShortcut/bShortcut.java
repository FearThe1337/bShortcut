package com.beecub.bShortcut;

import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import util.bChat;


public class bShortcut extends JavaPlugin {
	private bShortcutPlayerListener playerListener;
	private final Logger log;
	private bChat bChat;
	private bConfigManager configManager;

	public bShortcut(){		
		this.log = Logger.getLogger("Minecraft");
	}
	
	public void onEnable() {
		PluginDescriptionFile pdfFile = this.getDescription();
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(playerListener, this);
		
		
		log.info("[" +  pdfFile.getName() + "]" + " version " + pdfFile.getVersion() + " is enabled!");
		
		bChat = new bChat(this.getServer());
		configManager = new bConfigManager(this);
		configManager.load(this);	
		playerListener = new bShortcutPlayerListener(this,configManager);
	}
	
	public void onDisable() {
		PluginDescriptionFile pdfFile = this.getDescription();
		log.info("[" + pdfFile.getName() + "]" + " version " + pdfFile.getVersion() + " disabled!");
	}
	
	@SuppressWarnings("static-access")
    public boolean onCommand(CommandSender sender, Command c, String commandLabel, String[] args) {
		if(c.getName().equalsIgnoreCase("bShortcut")) {
			if(sender.hasPermission("bShortcut.reload")){
				PluginDescriptionFile pdfFile = this.getDescription();
				configManager.reload(this);
	            bChat.sendMessageToCommandSender(sender, "&6[" + pdfFile.getName() + "]" + " config reloaded");
	            return true;
			}else{
				sender.sendMessage(ChatColor.RED + "You do not have permission to access this command.");
				return true;				
			}
		}
        return false;
	}
}
