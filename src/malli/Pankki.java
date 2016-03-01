package malli;

import ohjain.Simu;

public class Pankki
{
	private String nimi;
	private double raha;
	// private double lainaKorko;
	private static boolean annettuLaina = false;
	private static double lainanMaara;
	
	public Pankki(final String nimi, final double raha)
	{
		this.nimi = nimi;
		this.raha = raha;
	}
	
	public String getNimi()
	{
		return nimi;
	}
	
	public void lainaa(final double maara)
	{
		if (maara <= raha)
		{
			raha -= maara;
			Logiikka.kauppa.getTili().talletaTilille(maara);
			annettuLaina = true;
			lainanMaara = maara;
		}
		else
		{
			System.out.println("Pankilla ei ole varaa lainata noin paljoa.");
			annettuLaina = false;
		}
	}
	
	/**
	 * @return <tt>true</tt> jos laina on erääntynyt, <tt>false</tt> jos laina on maksettu.
	 */
	public static boolean tarkistaLaina()
	{
		if (Simu.paiva > 30 && annettuLaina == true)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	/**
	 * @return true jos on laina, false jos ei ole.
	 */
	public static boolean josLaina()
	{
		return annettuLaina;
	}
	
	/**
	 * Maksa laina.
	 * @return true jos laina maksettu, false jos ei ole tarpeeksi rahaa maksaa laina.
	 */
	public static boolean maksaLaina()
	{
		if (Logiikka.kauppa.getTili().getRahamaara() >= lainanMaara)
		{
			Logiikka.kauppa.getTili().poistaTililta(lainanMaara);
			annettuLaina = false;
			return true;
		}
		else
		{
			System.out.println("Tililläsi ei ole tarpeeksi katetta.");
			annettuLaina = true;
			return false;
		}
	}
}
