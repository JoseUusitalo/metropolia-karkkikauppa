package malli;

import java.util.ArrayList;

import ohjain.Simu;
import tuki.Dev;
import tuki.Mat;
import tuki.MersenneTwisterFast;

public class Tukku
{
	private String nimi;
	private ArrayList<KarkkiLaatikko> karkkiVarasto = new ArrayList<KarkkiLaatikko>();
	//private ArrayList<Kaluste> kalusteVarasto = new ArrayList<Kaluste>();;
	private double hintamarginaali;
	private Pankkitili tili;
	private int valikoima;
	
	public Tukku(final String nimi, final ArrayList<KarkkiLaatikko> karkkiVarasto, final double hintamarginaali)
	{
		this.nimi = nimi;
		this.karkkiVarasto = karkkiVarasto;
		this.hintamarginaali = hintamarginaali;
		valikoima = karkkiVarasto.size();
		tili = Pankkitili.luoPankkitili();
	}
	
	/*
	public Tukku(final String nimi, final ArrayList<Kaluste> varasto)
	{
		this.nimi = nimi;
		this.kalusteVarasto = varasto;
		this.hintamarginaali = 1.0;
	}
	*/
	
	public void tulosta()
	{
		System.out.print(nimi + ", hintamarginaali: " + hintamarginaali + ", valikoima: " + valikoima + " tuotemerkkiä, varasto: ");
		if (!karkkiVarasto.isEmpty())
		{
			System.out.println(Logiikka.muotoileDesimaalit(3, getGrammaaKarkkiaVarastossa() / 1000.0) + " kg karkkia");
			Dev.tulostaKarkkivarasto(karkkiVarasto, this);
		}
		/*else if (!kalusteVarasto.isEmpty())
		{
			System.out.println(kalusteVarasto.size() + " kalustetta");
		}*/
		else
		{
			System.err.println("VIRHE! Varastot tyhjiä!");
		}
	}
	
	/**
	 * @return kuinka paljon varastossa on yhteensä karkkia grammoina.
	 */
	private int getGrammaaKarkkiaVarastossa()
	{
		int koko = 0;
		
		for (KarkkiLaatikko karkki : this.karkkiVarasto)
		{
			koko += karkki.getGrammat();
		}
		
		return koko;
	}
	
	/**
	 * @param karkki
	 *            Karkki.
	 * @return kuinka paljon karkki maksaa tässä tukussa.
	 */
	public double getKarkinKilohinta(final KarkkiLaatikko karkki)
	{
		return karkki.getKilohinta() * hintamarginaali;
	}
	
	/**
	 * Ostat n määrä x karkkia.
	 * 
	 * @param karkki
	 *            Karkki-olio.
	 * @param grammaa
	 *            Grammamäärä.
	 * @return true jos osto onnistui, false jos tuli virhe.
	 */
	public boolean osta(final KarkkiLaatikko karkki, final int grammaa)
	{
		// Onko tukussa tätä karkkia ollenkaan?
		if (karkkiVarasto.contains(karkki))
		{
			double kilot = grammaa / 1000.0;
			
			// Riittääkö rahani karkkeihin?
			if (Logiikka.kauppa.getSaldo() >= getKarkinKilohinta(karkki) * kilot)
			{
				// Onko tukussa tarpeeksi karkkeja?
				if (tavaraaVarastossa(karkki, grammaa))
				{
					// Maksetaan karkeista.
					// Pakotetaan kilohinta järkeväksi. Jokunen sentin tuhannesosa katoaa bittiavaruuteen mutta ei voi mitään.
					Logiikka.kauppa.getTili().siirto(this.tili, Mat.pakotaDesimaalit(2, getKarkinKilohinta(karkki) * kilot));
					
					// Poistetaan karkit.
					this.vahennaTavaraa(karkki, grammaa);
					
					// Ostinko kaikki tukun karkit?
					if (grammaaKarkkiaJaljella(karkki) == 0)
					{
						karkkiVarasto.remove(karkki);
					}
					
					// Lisätään karkit omaan kauppaan.
					Logiikka.kauppa.lisaaKarkkeja(karkki, grammaa);
					
					return true;
				}
				else
				{
					Simu.varoitusLoota("<!>", "Tukussa ei ole noin paljon karkkia!.");
				}
			}
			else
			{
				Simu.varoitusLoota("<!>", "Sinulla ei ole varaa ostaa noin paljon karkkia!");
			}
		}
		else
		{
			Simu.varoitusLoota("VIRHE", "Tuota karkkia ei ole olemassa!");
		}
		
		// Jos osto ei onnistunut ja palauttanut true, kaikissa muissa tapauksissa palautetaan false.
		return false;
	}
	
	/**
	 * Ottaa tukusta pois karkkia.
	 * 
	 * @param karkki
	 *            Karkki.
	 * @param grammaa
	 *            Kuinka paljon.
	 */
	private void vahennaTavaraa(final KarkkiLaatikko karkki, final int grammaa)
	{
		if (Simu.debug)
			System.out.println("\t(DBG) Otetaan varastosta pois " + karkki.toString() + " grammoja: " + grammaa + " varastossa: " + this.grammaaKarkkiaJaljella(karkki));
		karkkiVarasto.get(karkkiVarasto.indexOf(karkki)).vahenna(grammaa);
	}
	
	/**
	 * @param karkki
	 *            Karkki
	 * @param grammaa
	 *            Gramma
	 * @return true jos varastossa on karkkia vähintään gramma verran, muuten false.
	 */
	private boolean tavaraaVarastossa(final KarkkiLaatikko karkki, final int grammaa)
	{
		return karkkiVarasto.get(karkkiVarasto.indexOf(karkki)).getGrammat() >= grammaa;
	}
	
	/**
	 * @param karkki
	 *            Karkki
	 * @return kuinka paljon karkkia on varastossa jäljellä grammoina.
	 */
	public int grammaaKarkkiaJaljella(final KarkkiLaatikko karkki)
	{
		return karkkiVarasto.get(karkkiVarasto.indexOf(karkki)).getGrammat();
	}
	
	/*
	public void osta(final Kaluste kaluste, final ArrayList<Kaluste> varastoon)
	{
		if (kalusteVarasto.contains(kaluste))
		{
			kalusteVarasto.remove(kaluste);
			varastoon.add(kaluste);
		}
		else
		{
			System.out.println("VIRHE! Kalustetta ei ole olemassa.");
		}
	}
	*/
	
	/**
	 * @return Listan karkkien nimistä, määristä ja hinnoista kivana tekstilistana comboboksia varten.
	 */
	public String[] listaaKarkkiVarasto()
	{
		if (Simu.debug)
			System.out.println("\t- TUKKUJEN KARKKILISTOJEN PÄIVITYS -");
		
		String[] lista = new String[karkkiVarasto.size()];
		
		for (int i = 0; i < karkkiVarasto.size(); i++)
		{
			lista[i] = karkkiVarasto.get(i).toString() + " [" + karkkiVarasto.get(i).getKiloina() + " kg | " + Logiikka.muotoileDesimaalit(2, getKarkinKilohinta(karkkiVarasto.get(i))) + " €/kg]";
		}
		
		return lista;
	}
	
	/**
	 * Huomaa että jos karkki poistuu varastosta, se poistetaan myös näkymän karkkilistauksesta. Joten String[] karkki listan ID = karkkiVaraston elementin ID.
	 * 
	 * @return Karkki-olion sen varasto ID:n avulla.
	 */
	public KarkkiLaatikko getKarkkiByID(final int ID)
	{
		return karkkiVarasto.get(ID);
	}
	
	public String toString()
	{
		return nimi;
	}
	
	public int getValikoimanKoko()
	{
		return karkkiVarasto.size();
	}
	
	/**
	 * Joka viikko tukut saavat uuden varaston.
	 */
	public void uusiVarasto()
	{
		MersenneTwisterFast satunnaisluku = new MersenneTwisterFast();
		karkkiVarasto.clear();
		Nimi.getKaytetytKarkit().clear();
		
		if (Simu.debug)
			System.out.println("\t(DBG) Luodaan uusi varasto tukulle.");
		
		karkkiVarasto = KarkkiLaatikko.luoKarkkiVarasto(satunnaisluku.randInt(20, Nimi.getKarkkiListaKoko()), 20000, 200000);
		
		if (Simu.debug)
		{
			System.out.println("\t(DBG) Uudessa tukun varastossa on karkkia: ");
			tulosta();
		}
	}
}
