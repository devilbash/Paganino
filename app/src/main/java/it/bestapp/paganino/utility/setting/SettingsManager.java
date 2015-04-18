package it.bestapp.paganino.utility.setting;

import android.content.Context;
import android.os.Environment;

public class SettingsManager extends PreferencesManager  {
    static SettingsManager settingsManager;

    private String path;
    private boolean sync;
    private boolean ricordami;
    private int giorno;
    private String ora;
    private String user;
    private String paswd;
    private boolean notifica;
    private boolean drive;

    private final int SETTING_NOTIFICA     = 0;     //notifica stipendio
    private final int SETTING_TIMER_T      = 1;     //timer notifica ora
    private final int SETTING_TIMER_D      = 2;     //timer notifica giorno
    private final int SETTING_RICORDAMI    = 3;     //ricorda login
    private final int SETTING_USER         = 4;     //ricorda: user
    private final int SETTING_PSWD         = 5;     //ricorda: user
    private final int SETTING_PATH         = 6;     //path salvataggio file
    private final int SETTING_SYNC         = 7;     //Abilita autoupdate
    private final int SETTING_DRIVE        = 8;     //Abilita google Drive

    private final String PATH =  Environment.getExternalStorageDirectory() + "/paganino";

    public SettingsManager(Context c) {
        super(c);
        path      = getString(SETTING_PATH);
        notifica  = getBoolean(SETTING_NOTIFICA);
        sync      = getBoolean(SETTING_SYNC);
        ricordami = getBoolean(SETTING_RICORDAMI);
        giorno    = getInt(SETTING_TIMER_D);
        ora       = getString(SETTING_TIMER_T);
        user      = getString(SETTING_USER);
        paswd     = getString(SETTING_PSWD);
        drive     = getBoolean(SETTING_DRIVE);
    }


    public static SettingsManager getSettingsManager(Context c){
        if ( settingsManager == null )
            settingsManager = new SettingsManager(c);
        return settingsManager;
    }

    public String getPath() {
        if ( path == null || path == "") path = PATH;
        return path;
    }

    public void setPath(String path) {
        this.path = path;
        setString(SETTING_PATH, path);
    }

    public boolean isDrive() {
        return drive;
    }

    public void setDrive(boolean drive) {
        this.drive = drive;
        setBoolean(SETTING_DRIVE, drive);
    }

    public boolean isNotifica() {
        return notifica;
    }

    public void setNotifica(boolean notifica) {
        this.notifica = notifica;
        setBoolean(SETTING_NOTIFICA, notifica);
    }

    public boolean isSync() {
        return sync;
    }

    public void setSync(boolean sync) {
        this.sync = sync;
        setBoolean(SETTING_SYNC, sync);
    }

    public int getGiorno() {
        if (giorno == 0 ) giorno = 27;
        return giorno;
    }

    public void setGiorno(int giorno) {
        this.giorno = giorno;
        setInt(SETTING_TIMER_D, giorno);
    }

    public String getOra() {
        if (ora.equalsIgnoreCase("")) ora = "09:00";
        return ora;
    }

    public void setOra(String ora) {
        this.ora = ora;
        setString(SETTING_TIMER_T, ora);
    }

    public boolean isRicordami() {
        return ricordami;
    }

    public void setRicordami(boolean ricordami) {
        this.ricordami = ricordami;
        setBoolean(SETTING_RICORDAMI,ricordami);
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
        setString(SETTING_USER, user);
    }

    public String getPaswd() {
        return paswd;
    }

    public void setPaswd(String paswd) {
        this.paswd = paswd;
        setString(SETTING_PSWD, paswd);
    }
}
