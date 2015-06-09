package it.bestapp.paganino.adapter.bustapaga;

import it.bestapp.paganino.R;
import it.bestapp.paganino.fragment.Lista;
import it.bestapp.paganino.utility.SingletonParametersBridge;
import it.bestapp.paganino.utility.connessione.HRConnect;
import it.bestapp.paganino.utility.setting.SettingsManager;
import it.bestapp.paganino.utility.thread.ThreadAnaPDF;
import it.bestapp.paganino.utility.thread.ThreadPDF;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

public class BustaAdapter extends BaseAdapter implements Filterable {

    private Activity act;
    private Lista frag;
    private HRConnect conn;
    private SettingsManager settings;
    private static List<Busta> list;
    private List<Integer> hiddenItem;
	//private static PageDownloadedInterface callBackMain;

	public BustaAdapter(Lista f, HRConnect c) {
		act = f.getActivity();
        frag = f;
        conn = c;
	//	callBackMain = (PageDownloadedInterface) a;
		list = new ArrayList<Busta>();
		hiddenItem = new ArrayList<Integer>();


        SingletonParametersBridge singleton = SingletonParametersBridge.getInstance();
        settings = (SettingsManager) singleton.getParameter("settings", act);


	}

	@Override 
	public View getView(int position, View v, ViewGroup parent) {
		ViewHolder vHolder;

		for (Integer hiddenIndex : hiddenItem)
			if (hiddenIndex <= position)
				position++;

		if (v == null) {
			v = act.getLayoutInflater().inflate(R.layout.row_swp_busta, null);
			v.setLongClickable(true);
			vHolder = new ViewHolder(v, act);
			v.setTag(vHolder);
		} else {
			vHolder = (ViewHolder) v.getTag();
		}

		Busta bPaga = (Busta) getItem(position);
		vHolder.populate(bPaga, frag, conn);
        if (!settings.isDrive()){
            vHolder.nascondiDrive();
        }

		return v;
	}

	private static class ViewHolder {
		private TextView  meseTextView;
		private TextView  annoTextView;
		private ImageView scarica;
		private ImageView grafico;
		private ImageView excel;
        private ImageView drive;
		private Activity act;

		public ViewHolder(View convertView,Activity a) {
			act = a;
			meseTextView = (TextView) convertView.findViewById(R.id.listMese);
			annoTextView = (TextView) convertView.findViewById(R.id.listAnno);


            scarica = (ImageView) convertView.findViewById(R.id.imageScarica);
            drive   = (ImageView) convertView.findViewById(R.id.imageDrive);
            excel   = (ImageView) convertView.findViewById(R.id.imageExcel);
            grafico = (ImageView) convertView.findViewById(R.id.imageGrafico);
		}

        public void nascondiDrive(){
            drive.setVisibility(View.GONE);
        }


		public void populate(final Busta bPaga, final Lista frag, final HRConnect conn) {
			meseTextView.setText(bPaga.getMese(act));
			annoTextView.setText(bPaga.getAnno());


            scarica.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    (new ThreadPDF(frag, conn, bPaga, 'S')).execute();
                }
            });

            excel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    (new ThreadAnaPDF(frag, conn, bPaga, 'E')).execute();
                }
            });

            grafico.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    (new ThreadAnaPDF(frag, conn, bPaga, 'G')).execute();
                }
            });

			drive.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
                    (new ThreadPDF(frag, conn, bPaga, 'D')).execute();
				}
			});

		}
	}

	@Override
	public int getCount() {
		return list.size() - hiddenItem.size();

	}

	@Override
	public Busta getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		for (Integer hiddenIndex : hiddenItem)
			if (hiddenIndex <= position)
				position++;

		return (long) position;
	}

	public List<Busta> getList() {
		return list;
	}
	public void setList(List<Busta> l) {
		list = l;
	}


	public void add(Busta bP) {
		list.add(bP);
	}

	@Override
	public Filter getFilter() {
		Filter filter = new Filter() {

			@Override
			protected FilterResults performFiltering(CharSequence constraint) {
				FilterResults results = new FilterResults();
				List<Busta> lBP = new ArrayList<Busta>();

				if (constraint == null || constraint.length() == 0) {
					// No filter implemented we return all the list
					results.values = list;
					results.count = list.size();
				} else {
					for (Busta bP : list) {
						if (bP.getMese(act)
								.toUpperCase()
								.startsWith(constraint.toString().toUpperCase()))
							lBP.add(bP);
					}
				}
				results.values = lBP;
				results.count = lBP.size();
				return results;
			}

			@Override
			protected void publishResults(CharSequence constraint,
					FilterResults results) {
				List<Busta> listaVisibili = null;
				Busta p;
				hiddenItem.clear();
				// Now we have to inform the adapter about the new list filtered
				listaVisibili = (List<Busta>) results.values;
				if (results.count == 0) {
					hiddenItem.clear();
					for (Busta bP : list) {
						bP.setNascosto(false);
					}
					notifyDataSetInvalidated();
				} else {
					for (Busta bP : list) {
						if (!listaVisibili.contains(bP)) {
							bP.setNascosto(true);
							hiddenItem.add(list.indexOf(bP));
						} else
							bP.setNascosto(false);
					}
				}
				notifyDataSetChanged();
			}
		};
		return filter;
	}


	public void updateReceiptsList() {
		List<Busta> newlist = new ArrayList(list);
		list.clear();
		list.addAll(newlist);
		this.notifyDataSetChanged();
	}



}