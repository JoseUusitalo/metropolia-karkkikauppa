package malli;

import ohjain.Simu;

/**
 * Ei käytetty.
 */
public class Kassa
{
	private double raha;
	private Kauppa kauppa;
	
	public Kassa(final Kauppa kauppa)
	{
		this.kauppa = kauppa;
	}
	
	public double getRahamaara()
	{
		return raha;
	}
	
	public Kauppa getKauppa()
	{
		return kauppa;
	}
	
	public void lisaaRahaa(final double lisaa)
	{
		this.raha += lisaa;
	}
	
	public boolean nostaKassasta(final double poisto)
	{
		if (poisto > raha)
		{
			if (Simu.debug)
				System.out.println("Kassassa ei ole tarpeeksi rahaa. Rahaa kassassa: " + raha);
			return false;
		}
		else
		{
			raha = raha - poisto;
			if (Simu.debug)
				System.out.println("Kassasta otettiin " + poisto + "€, kassaan jäi " + raha + "€");
			return true;
		}
	}
}
