package br.diemes.main;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class TabelaTurno {
	private static byte[] CICLO = { -1, 8, 8, 8, -1, 0, 0, 0, -1, -1, 16, 16,
					16, 16, -1, 0, 0, -1, -1, -1, -1, 16, 16, 
					16, -1,  8, 8, 8, 8, -1,-1, 0, 0, -1, -1, };
	private static String[] MESES = { "JAN", "FEV", "MAR", "ABR", "MAI", "JUN",
			"JUL", "AGO", "SET", "OUT", "NOV", "DEZ" };
	private static char[] DIA_DA_SEMANA = { 'D', 'S', 'T', 'Q', 'Q', 'S', 'S' };

	private static byte COLUNA_DATA_BASE = 27;

	private String[][] tabela = new String[12][37];

	final private GregorianCalendar dataBase = new GregorianCalendar(2012,
			Calendar.JANUARY, 1);
	final private GregorianCalendar dataTabela = new GregorianCalendar();
	
	public TabelaTurno() {
		tabela = this.getTabela(dataTabela.get(Calendar.YEAR));
	}

	public void setYear(int year) {
		dataTabela.set(Calendar.YEAR, year);
		tabela = this.getTabela(year);
	}
	
	public int getYear() {
		return dataTabela.get(Calendar.YEAR);
	}
	
	public String getDayFromTable(int lin, int col) {
		return tabela[lin][col];
	}

	public String getCiclo(int grupo, int posicao) {
		byte[] desloc = { 0, 28, 21, 14, 7 };

		posicao = (posicao + desloc[grupo - 1]) % 35;

		if (posicao < 0)
			posicao += 35;

		switch (CICLO[posicao]) {
		case -1:
			return " F";
		case 8:
			return " 8";
		case 16:
			return "16";
		default:
			return " 0";
		}
	}

	/*
	 * public String getHorario(int grupo, GregorianCalendar data) { int posicao
	 * = data.getPeriodoEmDias(dataBase) + COLUNA_DATA_BASE; return
	 * getCiclo(grupo, posicao); }
	 * 
	 * public String getMes(GregorianCalendar data) { return
	 * MESES[data.get(Calendar.MONTH)]; }
	 * 
	 * public char getDiaDaSemana(GregorianCalendar data) { return
	 * DIA_DA_SEMANA[data.getDiaDaSemana()]; }
	 */
	
	public int getPositionDay(GregorianCalendar data){
	
		int position = ((getPeriodoEmDias(data.getTime()) + COLUNA_DATA_BASE) % 35);

		if (position < 0)
			position += 35;
		
		
		return position;
	}
	
	public String[][] getTabela(int ano) {
		int i = 0;
		String[][] tabela = new String[12][37];

		for (i = 0; i < 12; i++) {
			tabela[i] = getMesLinha(i + 1, ano);
		}

		return tabela;
	}

	public void imprimeTabela() {
		int lin = 0, col = 0;
		System.out.print(String.format(" %65s","TABELA DE TURNO REPLAN  "));
		System.out.println(getYear());
		System.out.print(" MES ");
		for (lin = 1; lin <= CICLO.length; lin++) {
			System.out.print(String.format(" %-2s", DIA_DA_SEMANA[lin % 7]));
		}
		System.out.println("MES");

		for (lin = 0; lin < this.tabela.length; lin++) {
			for (col = 0; col < this.tabela[lin].length; col++) {

				System.out.print(String.format(" %-2s",
						this.getDayFromTable(lin, col)));

			}
			System.out.println("");
		}

	}

	public String[] getMesLinha(int mes, int ano) {
		String[] mesLinha = new String[37];
		GregorianCalendar data = new GregorianCalendar(ano, mes - 1, 01);

		int posicao = ((getPeriodoEmDias(data.getTime()) + COLUNA_DATA_BASE) % 35);

		if (posicao < 0)
			posicao += 35;

		int i;
		mesLinha[0] = MESES[mes - 1];

		mesLinha[0] = MESES[mes - 1];
		int MAX = data.getActualMaximum(Calendar.DAY_OF_MONTH);
		for (i = 0; i < 35; i++) {
			
			if (i+1 == MAX){
				mesLinha[((posicao + i) % 35) + 1] = String.valueOf(i+1);
				data.set(Calendar.MONTH, mes);
				data.set(Calendar.DAY_OF_MONTH, 1);
			}else if(i+1 < MAX){
				mesLinha[((posicao + i) % 35) + 1] = String.valueOf(i+1);
				data.set(Calendar.DAY_OF_MONTH, i+1);
			}else{
				mesLinha[((posicao + i) % 35) + 1] = "";
			}
			
		}

		mesLinha[36] = MESES[mes - 1];

		return mesLinha;
	}

	public int getPeriodoEmDias(Date date) {
		GregorianCalendar dataInicio = new GregorianCalendar();
		dataInicio.setTime(date);
		GregorianCalendar dataFim = new GregorianCalendar();
		/*
		 * System.out.println(dataBase.get(Calendar.DAY_OF_MONTH)+"/"+
		 * dataBase.get(Calendar.MONTH)+"/"+ dataBase.get(Calendar.YEAR));
		 */
		byte sinal = 1;
		int qtDias = 0;

		if (dataBase.after(dataInicio)) {
			dataFim.setTime(dataBase.getTime());
			sinal = -1; // vai setar o retorno como negativo
		} else if (dataBase.before(dataInicio)) {// Caso data recebida seja
													// mais
			// recente que objeto atual.
			dataFim.setTime(dataInicio.getTime());
			dataInicio.setTime(dataBase.getTime());

		} else {
			// Caso a data recebida seja igual a do objeto atual
			return qtDias;// Se a data passa for igual a data do objeto get
			// Periodo retorna 0
		}

		if (dataFim.get(Calendar.YEAR) == dataInicio.get(Calendar.YEAR)) {
			if (dataFim.get(Calendar.MONTH) == dataInicio.get(Calendar.MONTH)) {
				qtDias = dataFim.get(Calendar.DAY_OF_MONTH)
						- dataInicio.get(Calendar.DAY_OF_MONTH);
				return qtDias * sinal;// Caso as datas estejam no mesmo ano e
				// mesmo mes, mas em dias diferentes
			} else {
				qtDias += (dataInicio.getActualMaximum(Calendar.DAY_OF_MONTH) - dataInicio
						.get(Calendar.DAY_OF_MONTH));
				dataInicio.set(Calendar.DAY_OF_MONTH, 1);
				dataInicio.add(Calendar.MONTH, 1);

				while (dataInicio.get(Calendar.MONTH) < dataFim
						.get(Calendar.MONTH)) {
					qtDias += dataInicio
							.getActualMaximum(Calendar.DAY_OF_MONTH);
					dataInicio.add(Calendar.MONTH, 1);
				}

				qtDias += dataFim.get(Calendar.DAY_OF_MONTH);
				return qtDias * sinal;
			}
		} else {
			int anoAtual = dataInicio.get(Calendar.YEAR);
			qtDias += (dataInicio.getActualMaximum(Calendar.DAY_OF_MONTH) - dataInicio
					.get(Calendar.DAY_OF_MONTH));
			dataInicio.set(Calendar.DAY_OF_MONTH, 1);
			dataInicio.add(Calendar.MONTH, 1);

			while (dataInicio.get(Calendar.MONTH) <= 12
					&& (dataInicio.get(Calendar.YEAR) == anoAtual)) {
				qtDias += dataInicio.getActualMaximum(Calendar.DAY_OF_MONTH);
				dataInicio.add(Calendar.MONTH, 1);
			}

			while (dataInicio.get(Calendar.YEAR) < dataFim.get(Calendar.YEAR)) {
				if (dataInicio.isLeapYear(dataInicio.get(Calendar.YEAR)))
					qtDias += 366;

				else
					qtDias += 365;

				dataInicio.add(Calendar.YEAR, 1);
			}

			while (dataInicio.get(Calendar.MONTH) < dataFim.get(Calendar.MONTH)) {
				qtDias += dataInicio.getActualMaximum(Calendar.DAY_OF_MONTH);
				dataInicio.add(Calendar.MONTH, 1);
			}

			qtDias += dataFim.get(Calendar.DAY_OF_MONTH);
			return qtDias * sinal;
		}
		


	}

}

class Holiday{
	public final static byte HOLIDAY = 1;
	
	private byte day;
	private byte month;
	
	byte tipo;
	String description = null;
	
	public Holiday(byte day, byte month, byte tipo){
		setDay(day);
		setMonth(month);
		setTipo(tipo);	
	}
	
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the holiday
	 */
	public static byte getHoliday() {
		return HOLIDAY;
	}

	public byte getTipo() {
		return tipo;
	}

	public void setTipo(byte tipo) {
		this.tipo = tipo;
	}

	public byte getDay() {
		return day;
	}
	public void setDay(byte day) {
		this.day = day;
	}
	public byte getMonth() {
		return month;
	}
	public void setMonth(byte month) {
		this.month = month;
	}

	
}
