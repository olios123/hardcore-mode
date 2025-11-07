package me.olios.hardcoremode.Database;

import me.olios.hardcoremode.Data;
import me.olios.hardcoremode.Main;
import me.olios.hardcoremode.Managers.UserDataManager;
import me.olios.hardcoremode.Objects.UserData;
import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UpdateData {

    public static void update(int frequency)
    {
        Main.log("The plugin has started the regular data update process.");

        Bukkit.getScheduler().runTaskTimer(Data.plugin, () ->
        {
            if (!Bukkit.getOnlinePlayers().isEmpty())
            {
                for (Player player : Bukkit.getOnlinePlayers())
                {
                    String uuid = player.getUniqueId().toString();

                    UserData userData = UserDataManager.load(uuid);

                    if (MySQL.resultNotNull(MySQL.queryGet
                            ("SELECT * FROM `players` WHERE `uuid`='" + uuid + "'")
                    ))
                    {

                        String sqlQuery = "UPDATE `players` SET " +
                                "`UUID`=?, " +
                                "`name`=?, " +
                                "`displayName`=?, " +
                                "`lastBan`=?, " +
                                "`deathLevel`=?, " +
                                "`deaths`=?, " +
                                "`lives`=?, " +
                                "`livesInfo`=? " +
                                "WHERE `UUID`=?";

                        try (PreparedStatement preparedStatement = MySQL.connection.prepareStatement(sqlQuery))
                        {
                            preparedStatement.setString(1, userData.uuid);
                            preparedStatement.setString(2, player.getName());
                            preparedStatement.setString(3, player.getDisplayName());
                            preparedStatement.setBoolean(4, userData.lastBan);
                            preparedStatement.setInt(5, userData.deathLevel);
                            preparedStatement.setString(6, String.valueOf(player.getStatistic(Statistic.DEATHS)));
                            preparedStatement.setInt(7, userData.lives);
                            preparedStatement.setBoolean(8, userData.livesInfo);
                            preparedStatement.setString(9, userData.uuid);

                            preparedStatement.executeUpdate();
                        }
                        catch (SQLException e)
                        {
                            e.printStackTrace(); // Dodaj odpowiednie obsługi błędów
                        }
                    }
                    else
                    {
                        String sqlQuery = "INSERT INTO `players` " +
                                "(`UUID`, `name`, `displayName`, `lastBan`, `deathLevel`, `deaths`, `lives`, `livesInfo`) " +
                                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

                        try (PreparedStatement preparedStatement = MySQL.connection.prepareStatement(sqlQuery))
                        {
                            preparedStatement.setString(1, userData.uuid);
                            preparedStatement.setString(2, player.getName());
                            preparedStatement.setString(3, player.getDisplayName());
                            preparedStatement.setBoolean(4, userData.lastBan);
                            preparedStatement.setInt(5, userData.deathLevel);
                            preparedStatement.setString(6, String.valueOf(player.getStatistic(Statistic.DEATHS)));
                            preparedStatement.setInt(7, userData.lives);
                            preparedStatement.setBoolean(8, userData.livesInfo);

                            preparedStatement.executeUpdate();
                        }
                        catch (SQLException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }, 0, frequency * 20L);
    }
}
