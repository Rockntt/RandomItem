package ru.rockntt.randomitem;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.bukkit.entity.Player;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MyFunctions {
//    public static BukkitRunnable giveRandomItemToPlayers;
    private static Random random = new Random();

    public static void giveRandomItemToPlayers(Player[] playerList) {
        for (Player player : playerList) {
            Material[] materials = Material.values(); // Получаем все возможные материалы
            int randomMaterialIndex = random.nextInt(materials.length); // Генерируем случайный индекс
            Material randomMaterial = materials[randomMaterialIndex]; // Получаем случайный материал
            ItemStack item = new ItemStack(randomMaterial, 1); // Создаем предмет с случайным материалом
            player.getInventory().addItem(item); // Выдаем предмет игроку
        }
    }
}
