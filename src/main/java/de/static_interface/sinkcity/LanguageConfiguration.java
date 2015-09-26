package de.static_interface.sinkcity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

public class LanguageConfiguration extends YamlConfiguration {

    private static LanguageConfiguration INSTANCE;

    protected static void init(SinkCity sinkCity) {
        INSTANCE = new LanguageConfiguration(sinkCity);
        INSTANCE.addDefaults();
    }

    public static String getLangString(String key) {
        return Utils.format(INSTANCE.get(key, INSTANCE.getDefault(key)).toString());
    }

    private File file;

    private LanguageConfiguration(SinkCity sinkCity) {
        this.file = new File(sinkCity.getDataFolder(), "sinkcity.language.yml");
        try {
            if (!this.file.exists()) {
                this.addDefaults();
                this.file.createNewFile();
                YamlConfiguration defaults = new YamlConfiguration();
                for (String s : this.getDefaults().getKeys(true)) {
                    defaults.set(s, this.getDefault(s));
                }
                defaults.save(this.file);
                defaults = null;
            }
            load();
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    private void load() throws FileNotFoundException, IOException, InvalidConfigurationException {
        try (BufferedReader bufferedReader = Files.newBufferedReader(this.file.toPath(), StandardCharsets.UTF_8)) {
            super.load(bufferedReader);
        }
    }

    private void addDefaults() {
        this.addDefault("Database.UnsupportedType", "&4The requested database type is not supported.");
        this.addDefault("Database.ConnectionFailed", "&4Could not connect to the database. Deactivating.");
        this.addDefault("Database.InternalError", "An internal error occurred with the database. Please ask an administrator for help.");
        this.addDefault("Economy.Registered", "Providing economy services (e.g. taxes).");
        this.addDefault("Economy.Failed", "Can not provide economy services as vault (or an economy plugin) is not available.");
        this.addDefault("Help.Command", "Command");
        this.addDefault("Help.TooFewArguments", "Too few arguments");
        this.addDefault("Help.ConsoleNotAvailable", "This command is not available on the console.");
        this.addDefault("Help.UserNotFound", "{0} is not online.");
        this.addDefault("Help.City", "City");
        this.addDefault("Help.CitySpawn", "City spawn");
        this.addDefault("Help.Mayor", "Mayor");
        this.addDefault("Help.Residents", "Residents");
        this.addDefault("Help.AndMore", "and more");
        this.addDefault("Help.AvailableCommands", "&bAvailable commands:");
        this.addDefault("Help.CommandNotAvailable", "&4This command is not available.");

        this.addDefault("SinkCity.City.Settings.FIRE", "Fire");
        this.addDefault("SinkCity.City.Settings.MOBS", "Mobs");
        this.addDefault("SinkCity.City.Settings.MONSTERS", "Monsters");
        this.addDefault("SinkCity.City.Settings.TNT_BUILD", "TNT building");
        this.addDefault("SinkCity.City.Settings.TNT_EXPLODE", "TNT explosions");
        this.addDefault("SinkCity.City.Settings.EXPLOSION", "Other explosions");
        this.addDefault("SinkCity.City.Settings.OPEN_CITY", "Open city");

        this.addDefault("SinkCity.City.Permissions.CREATE", "Create blocks");
        this.addDefault("SinkCity.City.Permissions.DESTROY", "Destroy blocks");
        this.addDefault("SinkCity.City.Permissions.BLOCKUSE", "Use blocks");
        this.addDefault("SinkCity.City.Permissions.ITEMUSE", "Use items");

        this.addDefault("SinkCity.City.NotFound", "&cCity not found.");
        this.addDefault("SinkCity.City.Help.Create", "Found a city at the chunk the mayor is currently in.");
        this.addDefault("SinkCity.City.Help.Delete", "Delete a city and release all of its chunks to the wilderness.");
        this.addDefault("SinkCity.City.Help.List", "Lists all available cities.");
        this.addDefault("SinkCity.City.Help.Join", "Join a city.");
        this.addDefault("SinkCity.City.Help.Leave", "Leave a city. Mayors can not leave without declaring a new mayor");
        this.addDefault("SinkCity.City.Help.Here", "Tells you which city is situated at that chunk.");
        this.addDefault("SinkCity.City.Help.Info", "Gives you information about the city.");
        this.addDefault("SinkCity.City.Help.Set", "Allows the mayor or assistants to change city settings.");

        this.addDefault("SinkCity.City.JoinSucceed", "&aYou have successfully joined {0}.");
        this.addDefault("SinkCity.City.ResidentJoined", "&a{0} has joined the city {1}!");
        this.addDefault("SinkCity.City.JoinFailedAlreadyResident", "&aYou can not join {0} because you are already a resident.");
        this.addDefault("SinkCity.City.JoinFailed", "&aJoining the city {0} failed. Please ask an administrator for advice");
        this.addDefault("SinkCity.City.LeaveSucceed", "&aYou have successfully left {0}.");
        this.addDefault("SinkCity.City.ResidentLeft", "&a{0} has left the city {1}!");
        this.addDefault("SinkCity.City.LeaveFailed", "&cLeaving the city {0} failed.");
        this.addDefault("SinkCity.City.LeaveFailedNoResident", "&cLeaving the city {0} failed because you aren't a resident.");

        this.addDefault("SinkCity.City.CreationSucceed", "&bSuccessfully founded the city {0}.");
        this.addDefault("SinkCity.City.CreationFailed.NameTaken", "&cThat name is already taken.");
        this.addDefault("SinkCity.City.CreationFailed.CityExistsAtPlace", "&cThere is already a city here.");
        this.addDefault("SinkCity.City.CreationFailed.NameEqualsNull", "&aA city may not be named \"null\"!");

        this.addDefault("SinkCity.City.Modification.NotASetting", "{0} is not a setting.");
        this.addDefault("SinkCity.City.Modification.NotAssistant", "You are not allowed to change settings in {0}.");
        this.addDefault("SinkCity.City.Modification.Succeed", "Successfully updated {0}.");

        this.addDefault("SinkCity.City.DeletionSucceed", "{0} was successfully deleted.");

        this.addDefault("SinkCity.City.List.Start", "Available cities:");
        this.addDefault("SinkCity.City.List.Format", "{0}[{1} residents] ({2})");
        this.addDefault("SinkCity.City.List.NoCities", "There are no cities available.");
        this.addDefault("SinkCity.City.List.NoCitiesPattern", "There are no cities available that match the pattern.");
        this.addDefault("SinkCity.City.NoCityHere", "There is no city here.");
        this.addDefault("SinkCity.City.Here", "The city {0} is here.");

        this.addDefault("SinkCity.City.TeleportImpossible", "Teleporting to the city is not possible.");

        this.addDefault("SinkCity.City.DestroyingDisallowed", "You may not destroy blocks here.");
        this.addDefault("SinkCity.City.BuildingDisallowed", "You may not place blocks here.");
        this.addDefault("SinkCity.City.ItemUseDisallowed", "You may not use this item here.");
    }
}
