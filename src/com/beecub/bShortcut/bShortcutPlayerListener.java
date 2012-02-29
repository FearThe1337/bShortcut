package com.beecub.bShortcut;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;


public class bShortcutPlayerListener implements Listener {
	private final bShortcut plugin;

	public bShortcutPlayerListener(bShortcut instance) {
		plugin = instance;
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        if (event.isCancelled()) {
            return;
        }
        
        String message = event.getMessage();
        Player player = event.getPlayer();
        
        String pre;
        int i = message.indexOf(' ');
        if(i < 0) { i = message.length(); }
        
        pre = (String) message.subSequence(0, i);
        message = (String) message.subSequence(i, message.length());
        
        if(bConfigManager.handleShortcuts(plugin, player, pre, message)) {
            event.setCancelled(true);
        }
	}
}