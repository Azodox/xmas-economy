package fr.olten.xmas.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import fr.olten.jobs.Job;
import fr.olten.jobs.database.JobDatabaseManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.valneas.account.AccountSystemApi;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@CommandAlias("economy")
public class EconomyCommand extends BaseCommand {

    @Default
    @Subcommand("show power")
    public void showPower(Player player){
        var apiProvider = Bukkit.getServicesManager().getRegistration(AccountSystemApi.class);
        if(apiProvider == null){
            return;
        }

        var api = apiProvider.getProvider();
        var currentJobId = api.getAccountManager(player).getAccount().getCurrentJobId();

        var jobApiProvider = Bukkit.getServicesManager().getRegistration(JobDatabaseManager.class);
        if(jobApiProvider == null){
            return;
        }

        var jobApi = jobApiProvider.getProvider();
        var job = jobApi.queryJob(Job.getJob(currentJobId).orElseThrow()).first();

        if(job == null){
            player.sendMessage(Component.text("Impossible de trouver votre job").color(NamedTextColor.RED));
            return;
        }

        var jobPower = jobApi.queryJobPower(job.getPowerId()).first();

        if(jobPower == null){
            player.sendMessage(Component.text("Impossible de récupérer le pouvoir de votre job.").color(NamedTextColor.RED));
            return;
        }

        player.sendMessage(
                Component.text("Economy")
                .color(NamedTextColor.YELLOW)
                .decorate(TextDecoration.BOLD)
                        .append(Component.text(" » ").color(NamedTextColor.DARK_GRAY).decorate(TextDecoration.BOLD))
                        .append(Component.text("Votre pouvoir est ").color(NamedTextColor.YELLOW).decorate(TextDecoration.BOLD))
                        .append(jobPower.name()));
    }
}
