package tuki;

import java.util.ArrayList;

import malli.Henkilokunta;
import malli.KarkkiLaatikko;
import malli.Kauppa;
import malli.Tukku;
import ohjain.Simu;

public class Dev
{
	public static void tulostaHenkilokunta(final ArrayList<Henkilokunta> lista)
	{
		if (Simu.debug)
			System.out.println("\t(DBG) Tulostetaan henkilökunta:");
	
		for (Henkilokunta henk : lista)
		{	
			henk.tulosta();
		}
	}
	
	public static void tulostaKarkkivarasto(final ArrayList<KarkkiLaatikko> lista)
	{
		if (Simu.debug)
			System.out.println("\t(DBG) Tulostetaan karkkivarasto:");
	
		for (KarkkiLaatikko kark : lista)
		{	
			kark.tulosta();
		}
	}
	
	public static void tulostaKarkkivarasto(final ArrayList<KarkkiLaatikko> lista, final Tukku tukku)
	{
		if (Simu.debug)
			System.out.println("\t(DBG) Tulostetaan " + tukku.toString() + " karkkivarasto:");
	
		for (KarkkiLaatikko kark : lista)
		{	
			kark.tulosta(tukku);
		}
	}
	
	public static void tulostaTukut(final ArrayList<Tukku> lista)
	{
		if (Simu.debug)
			System.out.println("\t(DBG) Tulostetaan tukut:");
		
		for (int i = 0; i < lista.size(); i++)
		{
			System.out.print("\t(DBG) Tukku " + i + ": ");
			lista.get(i).tulosta();
		}
	}

	public static void tulostaKarkkivarasto(final ArrayList<KarkkiLaatikko> lista, final Kauppa kauppa)
	{
		if (Simu.debug)
			System.out.println("\t(DBG) Tulostetaan kaupan karkkivarasto:");
		
		for (KarkkiLaatikko kark : lista)
		{
			kark.tulosta(kauppa);
		}
	}
	
}
