package ru.rockntt.randomitem;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

import ru.rockntt.randomitem.MyFunctions;

public final class RandomItem extends JavaPlugin {

    private List<Player> playerList = new ArrayList<>();
    private BukkitTask itemGiveTask;
    private boolean isSchedulerRunning = false;

    @Override
    public void onEnable() {
        Bukkit.getScheduler().runTaskTimer(this, MyFunctions.giveRandomItemToPlayers, 0, 600); // 20 тиков = 1 секунда
        getCommand("randomitem").setExecutor(new CommandExecutor() {
            @Override
            public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
                if(args.length == 0){
                    sender.sendMessage("Reload plugin: /randomitem reload");
                    return true;
                }

                if (args.length > 1) {
                    if (args[0].equalsIgnoreCase("addplayer")) {
                        Player player = (Player) sender;
                        Player targetPlayer = getServer().getPlayerExact(args[1]); // Получаем игрока по нику
                        if (targetPlayer != null) {
                            addPlayer(targetPlayer);
                            sender.sendMessage("Игрок " + targetPlayer.getName() + " добавлен в список участвующих.");
                        } else {
                            sender.sendMessage("Игрок не найден.");
                        }
                        return true;

                    }
                }
                if(args[0].equalsIgnoreCase("reload")){
                    reloadConfig();
                    sender.sendMessage("Plugin reloaded");
                    return true;
                }
                if(args[0].equalsIgnoreCase("players")){

                    if (playerList.size() != 0) {
                        s = new String();
                        s += "Участвующие игроки: ";
                        for (Player k : playerList) {
                            s += k.getName() + " ";
                        }
                        sender.sendMessage(s);
                        return true;
                    } else {
                        sender.sendMessage("Пока что никто не участвует в вашей игре. Добавьте игроков: /randomitem addplayer <nick>");
                    }
                    return true;
                }

                return true;
            }
            public void addPlayer(Player player) {
                playerList.add(player);
            }
        });
        getCommand("start").setExecutor(new CommandExecutor() {
            @Override
            public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
                if (!isSchedulerRunning) {
                    itemGiveTask = Bukkit.getScheduler().runTaskTimer(RandomItem.this, MyFunctions.giveRandomItemToPlayers, 0, 600);
                    isSchedulerRunning = true;
                    sender.sendMessage("Scheduler started");
                } else {
                    sender.sendMessage("Scheduler is already running");
                }
                return true;
            }
        });

        getCommand("stop").setExecutor(new CommandExecutor() {
            @Override
            public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
                if (isSchedulerRunning) {
                    itemGiveTask.cancel();
                    isSchedulerRunning = false;
                    sender.sendMessage("Scheduler stopped");
                } else {
                    sender.sendMessage("Scheduler is not running");
                }
                return true;
            }
        });
    }

    @Override
    public void onDisable() {
        stopSchedulerTask();
    }
}
