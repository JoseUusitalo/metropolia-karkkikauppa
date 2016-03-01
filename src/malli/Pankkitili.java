package malli;

import ohjain.Simu;
import tuki.Mat;
import tuki.MersenneTwisterFast;

public class Pankkitili
{
	private double rahamaara;
	
	public Pankkitili(double alkuarvo)
	{
		this.rahamaara = alkuarvo;
	}
	
	public double getRahamaara()
	{
		return rahamaara;
	}
	
	public void setRahamaara(final double uusiRahamaara)
	{
		this.rahamaara = uusiRahamaara;
	}
	
	/**
	 * Lisää rahaa tilille.
	 * @param lisays Eurot.
	 */
	public void talletaTilille(final double lisays)
	{
		this.rahamaara = Mat.pakotaDesimaalit(2, this.rahamaara + lisays);
	}
	
	/**
	 * Poista tililtä rahaa tyhjyyteen.
	 * 
	 * @param poisto
	 *            Rahan määrä.
	 * @return true jos poisto onnistui, false jos tilillä on liian vähän rahaa poistoon.
	 */
	public boolean poistaTililta(final double poisto)
	{
		if (poisto > rahamaara)
		{
			if (Simu.debug)
				System.out.println("\t(DBG)Tilillä ei ole tarpeeksi katetta maksaa " + poisto + ". Rahaa tilillä: " + rahamaara);
			
			rahamaara = 0;
			
			return false;
		}
		else
		{
			/*
			 * Voi elämä näitä pyöristysvirheitä.
			 * 1000.0 - 10.55 = 989.450000000000000001
			 * Pakotetaan sitten.
			 */
			rahamaara = Mat.pakotaDesimaalit(2, rahamaara - poisto);
			
			if (Simu.debug)
				System.out.println("\t(DBG) Tililtä poistettiin " + poisto + " €, tilille jäi " + this.rahamaara + " €");
			return true;
		}
	}
	
	/**
	 * @return Pankkitilin missä on [10,6000] rahaa.
	 */
	public static Pankkitili luoPankkitili()
	{
		MersenneTwisterFast satunnaisluku = new MersenneTwisterFast();
		
		return new Pankkitili(Mat.pakotaDesimaalit(2, 10.0 + (6990.0 * satunnaisluku.nextDouble())));
	}
	
	/**
	 * @param tili
	 *            Pankkitili mille siirretään rahaa.
	 * @param raha
	 *            Rahan määrä.
	 * @return true jos siirto onnistui, false jos rahaa on liian vähän siirrettäväksi.
	 */
	public boolean siirto(final Pankkitili tili, final double raha)
	{
		if (poistaTililta(raha))
		{
			tili.talletaTilille(raha);
			if (Simu.debug)
				System.out.println("\t(DBG) Tilisiirto: " + raha + " €");
			return true;
		}
		else
		{
			return false;
		}
		
	}
}
