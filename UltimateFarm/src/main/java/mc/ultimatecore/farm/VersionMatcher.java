package mc.ultimatecore.farm;

import mc.ultimatecore.farm.nms.*;
import org.bukkit.*;
import org.jetbrains.annotations.*;

import java.lang.reflect.*;
import java.util.*;

/**
 * Matches the server's NMS version to its {@link NMS}
 *
 * @author Wesley Smith
 * @since 1.2.1
 */
public class VersionMatcher {
    /**
     * Maps a Minecraft version string to the corresponding revision string
     */
    private static final Map<String, String> VERSION_TO_REVISION = new HashMap<>() {
        {
            this.put("1.8", "1_8_R1");
            this.put("1.8.2", "1_8_R3");
            this.put("1.12", "1_12_R1");
            this.put("1.14", "1_14_R1");
            this.put("1.15", "1_15_R1");
            this.put("1.16", "1_16_R1");
            this.put("1.16.1", "1_16_R2");
            this.put("1.17", "1_17_R1");
            this.put("1.18", "1_18_R1");
            this.put("1.18.1", "1_19_R1");
            this.put("1.19", "1_19_R1");
            this.put("1.20", "1_20_R1");
            this.put("1.20.1", "1_20_R2");
            this.put("1.20.2", "1_20_R3");
            this.put("1.20.3", "1_20_R4");
            this.put("1.20.4", "1_20_R5");
            this.put("1.20.5", "1_20_R6");
            this.put("1.20.6", "1_20_R7");
        }
    };
    /* This needs to be updated to reflect the newest available version wrapper */
    private static final String FALLBACK_REVISION = "1_20_R5";

    /**
     * Matches the server version to it's {@link NMS}
     *
     * @return The {@link NMS} for this server
     * @throws IllegalStateException If the version wrapper failed to be instantiated or is unable to be found
     */
    @Nullable
    public NMS match() {
        String craftBukkitPackage = Bukkit.getServer().getClass().getPackage().getName();

        String rVersion;
        if (!craftBukkitPackage.contains(".v")) { // cb package not relocated (i.e. paper 1.20.5+)
            final String version = Bukkit.getBukkitVersion().split("-")[0];
            rVersion = VERSION_TO_REVISION.getOrDefault(version, FALLBACK_REVISION);
        } else {
            rVersion = craftBukkitPackage.split("\\.")[3].substring(1);
        }

        try {
            return (NMS) Class.forName("mc.ultimatecore.farm.nms." + rVersion)
                    .getDeclaredConstructor()
                    .newInstance();
        } catch (ClassNotFoundException exception) {
            HyperRegions.getInstance().getLogger().warning("Unsupported Version Detected: " + rVersion);
            Bukkit.getPluginManager().disablePlugin(HyperRegions.getInstance());
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
                 InvocationTargetException e) {
            e.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(HyperRegions.getInstance());
        }
        return null;
    }
}
