package fr.olten.economy.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import fr.olten.economy.Economy;
import fr.olten.economy.bank.Bank;
import fr.olten.economy.bank.account.BankAccount;
import fr.olten.jobs.Job;
import fr.olten.jobs.database.JobDatabaseManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.valneas.account.AccountSystemApi;
import org.bson.types.ObjectId;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;

@CommandAlias("economy")
public class EconomyCommand extends BaseCommand {

    private final Economy economy;

    public EconomyCommand(Economy economy) {
        this.economy = economy;
    }

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

    @Subcommand("purchase")
    public void purchase(Player player, Bank bank, BankAccount bankAccount){
        var bankManager = this.economy.getBankManager();
        var sender = bankManager.getAccount(player.getUniqueId());
        bankManager.manageAccount(bankAccount).purchase().purchase(sender, bankAccount, bank, 150, 20);
    }

    @Subcommand("create bank")
    public void createBank(Player player, String name, String displayName, double balance){
        this.economy.getBankManager().addBank(new Bank(ObjectId.get(), name, displayName, balance, new HashMap<>()));
    }

    @Subcommand("create account")
    public void createAccount(Player player, double minBalance, double maxBalance, double balance){
        var bankManager = this.economy.getBankManager();
        var bank = bankManager.getBank(new ObjectId("62f91dcd6669d734f9dc755c"));
        bankManager.addAccount(bank, new BankAccount(ObjectId.get(), bank.get_id(), player.getUniqueId(), minBalance, maxBalance, balance));
    }
}
