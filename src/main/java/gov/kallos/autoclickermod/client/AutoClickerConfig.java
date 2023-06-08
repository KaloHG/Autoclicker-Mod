package gov.kallos.autoclickermod.client;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class AutoClickerConfig {

    //The Directory for the Mod (.minecraft/autoclicker)
    private File modDirectory;

    //The ClientConfiguration File (.minecraft/autoclicker/config.json)
    private File clientConfiguration;
    //The backup file for above.
    private File clientBackupConfiguration;

    private boolean autoclicker;
    private int clicksPerSecond;

    public AutoClickerConfig(File modDirectory) {
        this.modDirectory = modDirectory;
        this.clientConfiguration = new File(modDirectory, "config.json");
        this.clientBackupConfiguration = new File(modDirectory, "config.json.old");
        initialize();
    }

    /**
     * Checks that config exists, and if not generates the default one.
     */
    private void initialize() {
        if(!clientConfiguration.exists()) {
            try {
                Autoclicker_ModClient.LOGGER.info("Configuration file did not exist, values won't be saved until client shutdown...");
                clientConfiguration.createNewFile();
                load(true);
            } catch (IOException ex) {
                ex.printStackTrace();
                Autoclicker_ModClient.LOGGER.error("Failed to create configuration file with default values.");
            }
        } else {
            load(false);
        }
    }

    /**
     * Loads all configuration values.
     * @param defaultValues whether the default values should be used or not.
     */
    public void load(boolean defaultValues) {
        try {
            InputStream is = new FileInputStream(clientConfiguration);
            Scanner reader = new Scanner(is);
            //Don't load the array unless we're not default values.
            JSONObject configObject;
            try {
                configObject = new JSONObject(reader.nextLine());
            } catch (NoSuchElementException ex) {
                Autoclicker_ModClient.LOGGER.info("No Contents in JSON File, loading defaults.");
                configObject = null;
                defaultValues = true;
            }
            try {
                this.autoclicker = defaultValues ? DefaultValues.AUTOCLICKER : configObject.getBoolean("autoclicker");
                this.clicksPerSecond = defaultValues ? DefaultValues.CPS : configObject.getInt("cps");
            } catch (JSONException exception) {
                this.autoclicker = DefaultValues.AUTOCLICKER;
                this.clicksPerSecond = DefaultValues.CPS;
            }
            Autoclicker_ModClient.LOGGER.info("Loaded Configuration Successfully.");
            reader.close();
        } catch (FileNotFoundException | SecurityException ex) {
            ex.printStackTrace();
            Autoclicker_ModClient.LOGGER.error("Failed to load client configuration, the mod will not function.");
        }
    }

    /**
     * Saves all configuration values to files.
     */
    public void save() {
        JSONObject configObject = new JSONObject();

        configObject.put("autoclicker", this.autoclicker);
        configObject.put("cps", this.clicksPerSecond);

        try {
            Files.copy(clientConfiguration.toPath(), clientBackupConfiguration.toPath(), StandardCopyOption.REPLACE_EXISTING);
            OutputStream os = new FileOutputStream(clientConfiguration, false);
            PrintWriter writer = new PrintWriter(os);
            writer.print(configObject);
            writer.flush();
            writer.close();
            Autoclicker_ModClient.LOGGER.info("Saved Client Configuration.");
        } catch (FileNotFoundException | SecurityException exception) {
            exception.printStackTrace();
            Autoclicker_ModClient.LOGGER.error("Failed to save configuration, check the stack to find the issue.");
        } catch (IOException e) {
            e.printStackTrace();
            Autoclicker_ModClient.LOGGER.error("Failed to backup current configuration.");
        }
    }

    public int getClicksPerSecond() {
        return clicksPerSecond;
    }

    public void setClicksPerSecond(int clicksPerSecond) {
        this.clicksPerSecond = clicksPerSecond;
    }

    public boolean autoclickerEnabled() {
        return this.autoclicker;
    }

    public boolean toggleAutoclicker() {
        this.autoclicker = !autoclicker;
        return autoclicker;
    }
}
