package it.bestapp.paganino.dialog;

import android.app.Activity;
import android.os.Environment;
import android.view.View;
import android.widget.ArrayAdapter;
import com.afollestad.materialdialogs.MaterialDialog;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import it.bestapp.paganino.R;
import it.bestapp.paganino.utility.SingletonParametersBridge;
import it.bestapp.paganino.utility.setting.SettingsManager;

/**
 * Created by marco.compostella on 04/04/2015.
 */
public class OpenDir {

    private Activity act;
    private String root;
    private List<String> item = null;
    private List<String> path = null;

    private SingletonParametersBridge singleton;
    private SettingsManager settings;

    public OpenDir(Activity a) {
        act = a;
        singleton = SingletonParametersBridge.getInstance();
        settings = (SettingsManager) singleton.getParameter("settings");

        root = Environment.getExternalStorageDirectory().toString();
        getDir(root);
    }

    private void getDir(String dirPath) {
        item = new ArrayList<String>();
        path = new ArrayList<String>();

        File f = new File(dirPath);
        File[] files = f.listFiles();

        if(!dirPath.equals(root)){
            item.add(root);
            path.add(root);

            item.add("../");
            path.add(f.getParent());
        }

        for(int i=0; i < files.length; i++) {
            File file = files[i];
            path.add(file.getPath());
            if(file.isDirectory())
                item.add(file.getName() + "/");
            else
                item.add(file.getName());
        }
    }

    public void show(){
        new MaterialDialog.Builder(act)
                .title("Scegli la cartella di salvataggio")
                .adapter(new ArrayAdapter<String>(act, R.layout.row_dia_dir, item),
                        new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                                settings.setPath(Environment.getExternalStorageDirectory().toString()+ "/" + text.toString());
                                dialog.dismiss();
                            }
                        })
                .show();
    }
}
