package br.diemes.main;

import java.util.ArrayList;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

public class CalendariosActivity extends Activity {
	private ListView lvCalendarios;
	private Button btTeste;
	private ArrayList<Calendario> calendarios;
	ArrayList<String> IDCalSelecionados = new ArrayList<String>();
	LayoutInflater inflater;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.calendarios);
		inflater = this.getLayoutInflater();
		
		this.lvCalendarios = (ListView) findViewById(R.id.lvCalendarios);
		this.btTeste = (Button) findViewById(R.id.btTeste);
		lvCalendarios.setItemsCanFocus(false);
		
		btTeste.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Toast.makeText(CalendariosActivity.this, "Posição: "+lvCalendarios.getCheckedItemPosition(), Toast.LENGTH_SHORT).show();
				
			}
		});

		calendarios = obterCalendarios();
		for(Calendario cal : calendarios){
			IDCalSelecionados.add(cal.toString());
		}
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, IDCalSelecionados);
		
		lvCalendarios.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		lvCalendarios.setAdapter(adapter);
		lvCalendarios.setItemChecked(1,true);
		
		
		lvCalendarios.setOnItemClickListener(new OnItemClickListener() {  
  
            public void onItemClick(AdapterView parent, View view,int position, long id) {  
  
  
            }  
        });  
		
	}

	public ArrayList<Calendario> obterCalendarios() {
		String[] nomeColunas = new String[] { "_id", "displayName" };
		Uri uriCalendarios;
		calendarios = new ArrayList<Calendario>();
		if (Build.VERSION.SDK_INT >= 8) {
			uriCalendarios = Uri
					.parse("content://com.android.calendar/calendars");
		} else {
			uriCalendarios = Uri.parse("content://calendar/calendars");
		}
		Cursor cursor = this.managedQuery(uriCalendarios, nomeColunas, null,
				null, null); // all calendars
		// Cursor l_managedCursor = this.managedQuery(l_calendars, l_projection,
		// "selected=1", null, null); //active calendars
		if (cursor.moveToFirst()) {

			String l_calName;
			String l_calId;

			int l_nameCol = cursor.getColumnIndex(nomeColunas[1]);
			int l_idCol = cursor.getColumnIndex(nomeColunas[0]);
			do {
				l_calName = cursor.getString(l_nameCol);
				l_calId = cursor.getString(l_idCol);
				calendarios.add(new Calendario(l_calName, l_calId));
			} while (cursor.moveToNext());

		}
		return calendarios;
	}

	class Calendario {
		public String name;
		public String id;

		public Calendario(String _name, String _id) {
			name = _name;
			id = _id;
		}

		@Override
		public String toString() {
			return id + ": " + name;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

	}
	
	public class CaledarioAdapter extends BaseAdapter{
		
		
		@Override
		public int getCount() {
			return calendarios.size();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			
			if(convertView == null){
				convertView = inflater.inflate(R.layout.linha_calendario, null);
				holder = new ViewHolder();
				holder.cbCalendario = (CheckBox) convertView.findViewById(R.id.cbCalendario);
				convertView.setTag(holder);			
			}else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.cbCalendario.setText(calendarios.get(position).toString());
			
			return convertView;
		}
		
		private class ViewHolder{
			CheckBox cbCalendario;
		}
		
	}

}
