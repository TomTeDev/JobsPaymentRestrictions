package more.mucho.jobspaymentrestrictions;

import more.mucho.jobspaymentrestrictions.listeners.JobsListener;
import net.ess3.api.IEssentials;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class JobsPaymentRestrictions extends JavaPlugin {
    private IEssentials essentials = null;
    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();
        registerEssentials();
        Bukkit.getPluginManager().registerEvents(new JobsListener(this),this);

    }


    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }


    private void registerEssentials(){
        Plugin essentialsPlugin = Bukkit.getPluginManager().getPlugin("Essentials");
        if (essentialsPlugin == null || !essentialsPlugin.isEnabled()) {
            getLogger().severe("EssentialsX not found or not enabled! Disabling plugin...");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        essentials = (IEssentials) essentialsPlugin;
    }

    public IEssentials getEssentials(){
        return this.essentials;
    }
}
