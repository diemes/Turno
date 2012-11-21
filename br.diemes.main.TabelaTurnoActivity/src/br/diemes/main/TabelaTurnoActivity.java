package br.diemes.main;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class TabelaTurnoActivity extends Activity {

	public static final int COLUMNS_CYCLE = 35;
	public static final int LINES_MOUNTHS = 12;
	public static final int INFO = 0;
	public static final int ADDEVENTO = 1;
	public static final int SELECIONACALENDARIOS = 2;
	public static boolean destacado = false; 

	private ArrayList<Event> eventos;
	private Intent it = new Intent();

	int lin = 0, col = 0, id = 0, month = 0, day = 0, aux = 0, bckColor = 0,
			colorText = 0;
	ColorStateList textColor = null;

	TabelaTurno tblTurno = new TabelaTurno();
	GregorianCalendar date = new GregorianCalendar();
	int tableYear = date.get(Calendar.YEAR);

	TextView[][] tblTurnoView = new TextView[LINES_MOUNTHS][COLUMNS_CYCLE];
	TextView tvYear = null;
	Button btnPrevius = null;
	Button btnNext = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.table);

		tvYear = (TextView) findViewById(R.id.tvAno);
		tvYear.setText(String.valueOf(tableYear));

		id = R.id.cellDay00_00;

		for (lin = 0; lin < tblTurnoView.length; lin++) {
			for (col = 0; col < tblTurnoView[lin].length; col++) {
				tblTurnoView[lin][col] = (TextView) findViewById(id++);
			}
		}

		updateTabela(tableYear);

		btnPrevius = (Button) findViewById(R.id.btnPrevius);
		btnPrevius.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				tableYear--;

				updateTabela(tableYear);

			}
		});

		btnNext = (Button) findViewById(R.id.btnNext);
		btnNext.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				tableYear++;

				updateTabela(tableYear);

			}
		});

		// Log.i("Eventos", eventos.toString());

	}

	public void updateTabela(int year) {
		tblTurno.setYear(year);
		tvYear.setText(String.valueOf(year));
		atualizaTextoCalendario();
		destacaEventos(year);



	}

	void destacaEventos(int year){

		if(eventos!=null){
			removeDestaqueEventos();
		}

		eventos = getEvents(year);	


		for (Event event : eventos) {
			day = tblTurno.getPositionDay((GregorianCalendar) event
					.getDtStart());
			month = event.getDtStart().get(Calendar.MONTH);
			if (((day + 1) % 7 == 0 || (day + 1) % 7 == 6)) {
				tblTurnoView[month][day]
						.setBackgroundResource(R.drawable.event_shapefs);
			} else {
				tblTurnoView[month][day]
						.setBackgroundResource(R.drawable.event_shape);
			}
		}

		destacaDataAtual(year);
	}

	void removeDestaqueEventos(){
		for (Event event : eventos) {
			day = tblTurno.getPositionDay((GregorianCalendar) event
					.getDtStart());
			month = event.getDtStart().get(Calendar.MONTH);
			if (((day + 1) % 7 == 0 || (day + 1) % 7 == 6)) {
				tblTurnoView[month][day]
						.setBackgroundResource(R.color.weekendBck);
			} else {
				tblTurnoView[month][day]
						.setBackgroundColor(Color.TRANSPARENT);
			}
		}
	}


	void destacaDataAtual(int year){


		//Caso esteja no ano atual destaca o dia atual
		if (year == date.get(Calendar.YEAR)) {
			month = date.get(Calendar.MONTH);
			day = tblTurno.getPositionDay(date);
			tblTurnoView[month][day].setBackgroundResource(R.color.todayBck);
			tblTurnoView[month][day].setTextAppearance(getApplicationContext(),
					R.style.todayCell);
			destacado = true;
		}else{
			if(destacado){
				month = date.get(Calendar.MONTH);
				day = tblTurno.getPositionDay(date);
				if (((day + 1) % 7 == 0 || (day + 1) % 7 == 6)) {
					tblTurnoView[month][day]
							.setBackgroundResource(R.color.weekendBck);
				} else {
					tblTurnoView[month][day]
							.setBackgroundColor(Color.TRANSPARENT);
				}
				destacado = false;
			}
		}

	}

	void atualizaTextoCalendario(){
		int qtLin = tblTurnoView.length;
		int qtCol = tblTurnoView[0].length;
		//Atualiza o texto do calendário
		for (int lin = 0; lin < qtLin; lin++) {
			for (int col = 0; col < qtCol; col++) {
				tblTurnoView[lin][col].setText(tblTurno.getDayFromTable(lin,
						col + 1));
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuItem item = menu.add(0, INFO, 0, R.string.info);
		item.setIcon(R.drawable.ic_info);
		item = menu.add(0, ADDEVENTO, 0, R.string.addEvento);
		menu.add(0, SELECIONACALENDARIOS, 0, R.string.selecionaCalendarios);

		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		super.onMenuItemSelected(featureId, item);
		switch (item.getItemId()) {
		case INFO:
			this.startActivity(new Intent(getApplicationContext(),
					InfoActivity.class));
			return true;
		case ADDEVENTO:
			adicionaEvento();
			return true;
		case SELECIONACALENDARIOS:
			it.setClass(getApplicationContext(), CalendariosActivity.class);
			this.startActivity(it);
			return true;

		}

		return false;
	}

	private void adicionaEvento() {
		Intent l_intent = new Intent(Intent.ACTION_EDIT);
		l_intent.setType("vnd.android.cursor.item/event");
		// l_intent.putExtra("calendar_id", m_selectedCalendarId); //this
		// doesn't work
		// l_intent.putExtra("title", "roman10 calendar tutorial test");
		// l_intent.putExtra("description",
		// "This is a simple test for calendar api");
		// l_intent.putExtra("eventLocation", "@home");
		l_intent.putExtra("beginTime", System.currentTimeMillis());
		l_intent.putExtra("endTime", System.currentTimeMillis() + 1800 * 1000);
		// l_intent.putExtra("allDay", 0);
		// status: 0~ tentative; 1~ confirmed; 2~ canceled
		// l_intent.putExtra("eventStatus", 1);
		// 0~ default; 1~ confidential; 2~ private; 3~ public
		// l_intent.putExtra("visibility", 0);
		// 0~ opaque, no timing conflict is allowed; 1~ transparency, allow
		// overlap of scheduling
		// l_intent.putExtra("transparency", 0);
		// 0~ false; 1~ true
		// l_intent.putExtra("hasAlarm", 1);
		try {
			startActivity(l_intent);
		} catch (Exception e) {
			Toast.makeText(this.getApplicationContext(),
					"Desculpe, nenhum calendário compatível foi encontrado!",
					Toast.LENGTH_LONG).show();
		}
		finally{
			updateTabela(tblTurno.getYear());
		}
	}

	private class Event {
		public String titulo;
		public long dtStart;
		public long dtEnd;
		private Calendar calS = Calendar.getInstance();
		private Calendar calE = Calendar.getInstance();

		public Event(String titulo, long dtStart, long dtEnd) {
			this.titulo = titulo;
			this.dtStart = dtStart;
			this.dtEnd = dtEnd;
			calS.setTimeInMillis(dtStart);
			calE.setTimeInMillis(dtEnd);

		}

		public String getTitulo() {
			return titulo;
		}

		public void setTitulo(String titulo) {
			this.titulo = titulo;
		}

		public Calendar getDtStart() {
			calS.setTimeInMillis(dtStart);
			return calS;
		}

		public Calendar getDtEnd() {
			calE.setTimeInMillis(dtEnd);
			return calE;
		}

		@Override
		public String toString() {
			int dia = calS.get(Calendar.DAY_OF_MONTH);
			int mes = calS.get(Calendar.MONTH) + 1;
			StringBuilder str = new StringBuilder();
			str.append(titulo).append(':').append(dia).append('/').append(mes);

			return str.toString();
		}

	}

	private ArrayList<Event> getEvents(int year) {
		Uri l_eventUri;
		ArrayList<Event> listEvents = new ArrayList<Event>();
		if (Build.VERSION.SDK_INT >= 8) {
			l_eventUri = Uri.parse("content://com.android.calendar/events");
		} else {
			l_eventUri = Uri.parse("content://calendar/events");
		}
		String[] l_projection = new String[] { "title", "dtstart", "dtend" };
		String selection = "((calendar_id=?) AND (" + "dtstart>=?) AND ("
				+ "dtstart<=?))";
		Calendar dtStart = Calendar.getInstance();
		Calendar dtEnd = Calendar.getInstance();
		dtStart.set(year, Calendar.JANUARY, 1);
		dtEnd.set(year + 1, Calendar.JANUARY, 1);

		String calendarioID = "2";

		String[] selectionArgs;
		selectionArgs = new String[] { calendarioID,
				dtStart.getTimeInMillis() + "", dtEnd.getTimeInMillis() + "" };

		Cursor l_managedCursor = this.managedQuery(l_eventUri, l_projection,
				selection, selectionArgs, "dtstart ASC, dtend ASC");
		// Cursor l_managedCursor = this.managedQuery(l_eventUri, l_projection,
		// null, null, null);
		if (l_managedCursor.moveToFirst()) {
			Date dtBegin = new Date();
			Date dtFim = new Date();
			String l_title;
			long l_begin;
			long l_end;

			int l_colTitle = l_managedCursor.getColumnIndex(l_projection[0]);
			int l_colBegin = l_managedCursor.getColumnIndex(l_projection[1]);
			int l_colEnd = l_managedCursor.getColumnIndex(l_projection[2]);

			do {
				l_title = l_managedCursor.getString(l_colTitle);
				l_begin = l_managedCursor.getLong(l_colBegin);
				l_end = l_managedCursor.getLong(l_colEnd);
				dtBegin.setTime(l_begin);
				dtFim.setTime(l_end);
				Log.d("Eventos","Título: "+l_title+
						"\nInício: "+dtBegin.toLocaleString()+
						"\nFim: "+dtFim.toLocaleString()+"\n\n");
				
				listEvents.add(new Event(l_title, l_begin, l_end));

			} while (l_managedCursor.moveToNext());

		}
		return listEvents;
	}
}