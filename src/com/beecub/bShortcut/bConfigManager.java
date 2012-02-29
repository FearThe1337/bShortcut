package com.beecub.bShortcut;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import util.bChat;

public class bConfigManager {
	protected static bShortcut bShortcut;
    protected File confFile;
    static List<String> shortcuts = new LinkedList<String>();
    static List<String> shortcutsImport = new LinkedList<String>();
	
	@SuppressWarnings("static-access")
	public bConfigManager(bShortcut bShortcut) {
    	this.bShortcut = bShortcut;	
    }
    
	static void load(bShortcut plugin) {	    
    	shortcuts.clear();
    	FileConfiguration fc = plugin.getConfig();
    	ConfigurationSection cs = fc.getConfigurationSection("shortcuts.commands");
    	shortcuts = new LinkedList<String>(cs.getKeys(false));
    }
	
	static void reload(bShortcut plugin) {
		load(plugin);
	}
		
	static boolean handleShortcuts(JavaPlugin plugin, Player player, String pre, String message) {
	    List<String> perform = new LinkedList<String>();
        if(shortcuts.contains(pre)) {      
        	FileConfiguration fc = plugin.getConfig();
        	perform = fc.getStringList("shortcuts.commands." + pre);
            if(performCommand(plugin, player, perform, pre, message)) {
                return true;
            }
        }
        return false;
	}
	
	static boolean performCommand(JavaPlugin plugin, Player player, List<String> perform, String pre, String message) {
	    String performMessage;
        if(perform != null && perform.size() > 1) {
            for(int i = 0; i < perform.size(); i++) {
                performMessage = perform.get(i);
                performMessage = handleVariables(player, performMessage, message);
                if(performMessage.startsWith("&system")) {
                    performMessage = performMessage.replaceAll("&system", "");
                    bChat.broadcastMessage(performMessage);
                }
                else if(performMessage.contains("&onlineplayers")) {
                    Player[] players = bShortcut.getServer().getOnlinePlayers();
                    for(int j = 0; j < players.length; j++) {
                        player.chat(performMessage.replaceAll("&onlineplayers", players[j].getName()));
                    }
                }
                else {
                    player.chat(performMessage);
                }
            }
            return true;
        }
        else if(perform != null && perform.size() == 0) {
        	FileConfiguration fc = plugin.getConfig();
            performMessage = fc.getString("shortcuts.commands." + pre, null);
            performMessage = handleVariables(player, performMessage, message);
            if(performMessage.startsWith("&system")) {
                performMessage = performMessage.replaceAll("&system", "");
                bChat.broadcastMessage(performMessage);
            }
            else if(performMessage.contains("&onlineplayers")) {
                Player[] players = bShortcut.getServer().getOnlinePlayers();
                for(int j = 0; j < players.length; j++) {
                    player.chat(performMessage.replaceAll("&onlineplayers", players[j].getName()));
                }
            }
            else {
                player.chat(performMessage);
            }
            return true;
        }
        return false;
	}
	
	static String handleVariables(Player player, String performMessage, String message) {
	    String[] args = null;
	    message = message.replaceFirst(" ", "");
	    args = message.split(" ");
	    for(int k = 0; k < args.length; k++) {
	        performMessage = performMessage.replaceAll("&" + (k + 1), args[k]);
	    }
	    performMessage = performMessage.replaceAll("&player", player.getName());
	    performMessage = performMessage.replaceAll("&args", message);
	    return performMessage;
    }
}
