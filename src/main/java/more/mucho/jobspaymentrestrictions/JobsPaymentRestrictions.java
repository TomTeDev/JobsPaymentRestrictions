package more.mucho.jobspaymentrestrictions;

import more.mucho.jobspaymentrestrictions.listeners.JobsListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class JobsPaymentRestrictions extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();
        Bukkit.getPluginManager().registerEvents(new JobsListener(this),this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
