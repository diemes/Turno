package br.diemes.main;

import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
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
	public static final int LEGENDAS = 1;

	int lin = 0, col = 0, id = 0, month = 0, day = 0, aux = 0, bckColor = 0,
			colorText = 0;
	ColorStateList textColor = null;

	TabelaTurno tblTurno = new TabelaTurno();
	GregorianCalendar date = new GregorianCalendar();
	int tableYear = date.get(Calendar.YEAR), currentYear = tableYear;

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

	}

	public void updateTabela(int year) {
		tblTurno.setYear(year);
		tvYear.setText(String.valueOf(year));

		for (lin = 0; lin < tblTurnoView.length; lin++) {
			for (col = 0; col < tblTurnoView[lin].length; col++) {
				tblTurnoView[lin][col].setText(tblTurno.getDayFromTable(lin,
						col + 1));
				// formatCell(tblTurnoView[lin][col]);

			}
		}

		if (year == currentYear) {
			month = date.get(Calendar.MONTH);
			day = tblTurno.getPositionDay(date);
			tblTurnoView[month][day].setBackgroundResource(R.color.todayBck);
			tblTurnoView[month][day].setTextAppearance(getApplicationContext(),
					R.style.todayCell);
		} else if (currentYear == year - 1 || currentYear == year + 1) {

			if (((day + 1) % 7 == 0 || (day + 1) % 7 == 6)) {
				tblTurnoView[month][day]
						.setBackgroundResource(R.color.weekendBck);
			} else if (month % 2 == 0) {
				tblTurnoView[month][day].setBackgroundColor(Color.CYAN);
			} else {
				tblTurnoView[month][day].setBackgroundColor(Color.WHITE);
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuItem item = menu.add(0, INFO, 0, R.string.info);
		item.setIcon(R.drawable.ic_info);
		item = menu.add(0, LEGENDAS, 0, R.string.legendas);

		return true;
	}


	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		super.onMenuItemSelected(featureId, item);
		switch (item.getItemId()) {
		case INFO:
			Intent it = new Intent(getApplicationContext(), InfoActivity.class);
			this.startActivity(it);
			return true;
		case LEGENDAS:
			Toast.makeText(this.getApplicationContext(), "Legendas",
					Toast.LENGTH_LONG).show();
			return true;
		}

		return false;
	}

	/*
	 * private void formatCell(TextView cell) {
	 * 
	 * if (date.get(Calendar.MONTH) == lin) {
	 * 
	 * if (aux == col) cell.setTextAppearance(this.getApplicationContext(),
	 * R.style.todayCell); }
	 * 
	 * }
	 */
}