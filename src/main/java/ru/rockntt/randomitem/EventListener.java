package ru.rockntt.randomitem;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class EventListener implements Listener {

    @EventHandler
    plublic void onQuit(PlayerQuitEvent e, ){
        Player player = e.getPlayer();

    }
}
