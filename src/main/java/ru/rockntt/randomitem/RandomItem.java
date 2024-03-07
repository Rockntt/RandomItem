package ru.rockntt.randomitem;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
public final class RandomItem extends JavaPlugin {

    private Player[] playerList = new ArrayList<>().toArray(new Player[0]);
    private BukkitTask itemGiveTask;
    
    private boolean isSchedulerRunning = false;
    private int item_give_period = 600;

    @Override
    public void onEnable() {
        Plugin plugin = this;
        itemGiveTask = Bukkit.getScheduler().runTaskTimer(this, () -> {
            MyFunctions.giveRandomItemToPlayers(playerList);
        }, 0L, item_give_period);

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

                    if (playerList.length != 0) {
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
                if(args.length == 1 && args[0].equalsIgnoreCase("stop")){
                    if (isSchedulerRunning) {
                        isSchedulerRunning = false;
                        sender.sendMessage("Игра остановлена");
                    } else {
                        sender.sendMessage("Игра и так не начата");
                    }
                    return true;
                }

                if(args.length == 1 && args[0].equalsIgnoreCase("start")){
                    if (!isSchedulerRunning) {
                        Runnable task = new Runnable() {
                            @Override
                            public void run() {
                                // Ваш код для выдачи предметов игрокам
                                MyFunctions.giveRandomItemToPlayers(playerList);
                            }
                        };


                        itemGiveTask = Bukkit.getScheduler().runTaskTimer(plugin, task, 0, item_give_period);

                        isSchedulerRunning = true;
                        sender.sendMessage("Игра начата!");
                    } else {
                        sender.sendMessage("Игра уже идет!");
                    }
                    return true;
                }
                if(args.length > 2 && (args[0] + " " + args[1]).equalsIgnoreCase("set time")){
                    item_give_period = (int)Math.ceil(Double.parseDouble(args[2])) * 20;
                    sender.sendMessage(String.format("Период выдачи предметов игрокам установлен на %s", item_give_period / 20));
                }
                return true;
            }
            public void addPlayer(Player player) {
                Player[] newArray = Arrays.copyOf(playerList, playerList.length + 1);
                newArray[newArray.length - 1] = player;
                playerList = newArray;
            }
        });
    }

    @Override
    public void onDisable() {
    }
}
