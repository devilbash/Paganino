package it.bestapp.paganino.adapter.drawer;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.gc.materialdesign.views.ButtonFlat;

import it.bestapp.paganino.R;


public class MenuAdapter extends ArrayAdapter<MenuItem> {

    private int resource;
    private LayoutInflater inflater;

    public MenuAdapter(Context context, int resourceId, List<MenuItem> objects) {
        super(context, resourceId, objects);
        resource = resourceId;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View v, ViewGroup parent) {
        MenuItem item = getItem(position);
        ViewHolder holder;
        if (v == null) {
            v = inflater.inflate(resource, parent, false);
            holder = new ViewHolder();
            holder.nome= (ButtonFlat) v.findViewById(R.id.menuNome);
            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }
        holder.nome.setText(item.getNome());
        return v;
    }

    private static class ViewHolder {
        ButtonFlat nome;
    }
}