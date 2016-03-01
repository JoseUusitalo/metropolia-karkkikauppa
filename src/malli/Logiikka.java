package malli;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Locale;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import tuki.Dev;
import tuki.MersenneTwisterFast;
import ohjain.Simu;
import nakyma.Kehys;

/**
 * MVC: Malli<br>
 * Kaikki logiikka simulaation pyörittämiseksi ja datan käsittelyä. Kutsut vain
 * ohjaimeen.
 * 
 * @see Simu
 * @see Kehys
 */
public class Logiikka
{
	Simu ohjain;

	static Pankkitili tili = new Pankkitili(0.0); // Ei paras mahdollinen ratkaisu.
	static Kauppa kauppa = new Kauppa(Nimi.luoKayttajanNimi(), Nimi.luoKaupanNimi(), tili, new Pankkikortti(2015, tili));
	static Pankki pankki = new Pankki("Garggibanggi", 100000.0);

	public static ArrayList<Tukku> tukut = new ArrayList<Tukku>();
	private ArrayList<Henkilokunta> palkattavat = new ArrayList<Henkilokunta>();

	public Logiikka(final Simu simu)
	{
		this.ohjain = simu;

		if (Simu.debug)
		{
			System.out.println("\t(DBG) Kauppa luotu, tili yhdistetty.");
			System.out.println("\t(DBG) Pankki luotu.");
		}

		luoTukut();

		if (Simu.debug)
		{
			System.out.println("\t(DBG) Tukkuliikket luotu.");
			Dev.tulostaTukut(tukut);
		}

		luoPalkattavat();

		if (Simu.debug)
		{
			System.out.println("\t(DBG) Palkattava henkilökunta luotu.");
			Dev.tulostaHenkilokunta(palkattavat);
		}

		pankki.lainaa(5000.0);

		if (Simu.debug)
		{
			System.out.println("\t(DBG) Rahaa lainattu.");
		}
	}

	public static ArrayList<Asiakas> getAsiakkaat()
	{
		return kauppa.getAsiakkaat();
	}

	/**
	 * Luodaan kaikki mahdolliset palkattavat henkilöt.
	 */
	private void luoPalkattavat()
	{
		MersenneTwisterFast satunnaisluku = new MersenneTwisterFast();

		// Luodaan [6,20] henkilöä.
		for (int i = 1; i <= satunnaisluku.randInt(10, 20); i++)
		{
			palkattavat.add(Henkilokunta.luoTyontekija());
		}
	}

	/**
	 * Luo kaikki karkki- ja kalustetukkuliikeet.<br>
	 * Tukkuja tulee toistaiseksi olemaan vain yksi.
	 */
	private static void luoTukut()
	{
		MersenneTwisterFast satunnaisluku = new MersenneTwisterFast();

		// Hintamarginaali on [1.1, 1.3] %
		// Valikoima [20,max karkkinimien määrä] kpl
		// Karkkeja [20 000,200 000] grammaa
		double marginaali = (satunnaisluku.randInt(20) + 110) / 100.0;
		tukut.add(new Tukku("Testukku Ab", KarkkiLaatikko.luoKarkkiVarasto(satunnaisluku.randInt(20, Nimi.getKarkkiListaKoko()), 20000, 200000), marginaali));
		
		/*
		// Hintamarginaali on [1.2, 1.4] %
		// Valikoima [20,30]
		// Karkkeja [1 000 000,10 000 000]
		marginaali = (satunnaisluku.randInt(20) + 120) / 100.0;
		tukut.add(new Tukku("MEGARKKI CO.", KarkkiLaatikko.luoKarkkiVarasto(satunnaisluku.randInt(30, Nimi.getKarkkilistaKoko()), satunnaisluku.randInt(1000000, 10000000)), marginaali));

		// Hintamarginaali on [1.15, 1.25] %
		// Valikoima [15,25]
		// Karkkeja [50000,200000]
		marginaali = (satunnaisluku.randInt(15) + 115) / 100.0;
		tukut.add(new Tukku("Suklaamaa Ky", KarkkiLaatikko.luoKarkkiVarasto(satunnaisluku.randInt(15, 25), satunnaisluku.randInt(50000, 200000)), marginaali));

		// Kalusteita [10,100]
		tukut.add(new Tukku("Sohvat & Sängyt Oy", Kaluste.luoKalusteVarasto(satunnaisluku.randInt(70) + 10)));

		// Kalusteita [10,240]
		tukut.add(new Tukku("Kalustemaailma Oyj", Kaluste.luoKalusteVarasto(satunnaisluku.randInt(230) + 10)));
		*/
	}

	/**
	 * @param desimaaleja
	 *            Kuinka monta desimaalia. [0,4]
	 * @param arvo
	 *            Double.
	 * @return arvo tekstinä pyöristettynä annettuun määrään desimaaleja.
	 */
	public static String muotoileDesimaalit(final int desimaaleja, final double arvo)
	{
		// Amerikkalaiset numerot että Javakin ymmärtää. Ilman DecimalFormatSymbolsia lukuun tulee pilkku.

		if (desimaaleja == 1)
			return new DecimalFormat("0.0", DecimalFormatSymbols.getInstance(Locale.US)).format(arvo);
		else if (desimaaleja == 2)
			return new DecimalFormat("0.00", DecimalFormatSymbols.getInstance(Locale.US)).format(arvo);
		else if (desimaaleja == 3)
			return new DecimalFormat("0.000", DecimalFormatSymbols.getInstance(Locale.US)).format(arvo);
		else if (desimaaleja == 4)
			return new DecimalFormat("0.0000", DecimalFormatSymbols.getInstance(Locale.US)).format(arvo);
		else
			return new DecimalFormat("0.#", DecimalFormatSymbols.getInstance(Locale.US)).format(arvo);
	}

	/**
	 * @param karkkiVarasto
	 *            Varasto.
	 * @return Varaston koko grammoina.
	 */
	public static int laskeKarkkiGrammatVarastossa(final ArrayList<KarkkiLaatikko> karkkiVarasto)
	{
		int koko = 0;

		for (KarkkiLaatikko karkki : karkkiVarasto)
		{
			koko += karkki.getGrammat();
		}

		return koko;
	}

	public Tukku getTukku(final int tukkuID)
	{
		return tukut.get(tukkuID);
	}

	public Kauppa getKauppa()
	{
		return kauppa;
	}

	/**
	 * @return Listan asiakkaiden nimistä ja rahoista.
	 */
	public TableModel getAsiakasTaulu()
	{
		Object[][] lista = new Object[kauppa.getAsiakkaat().size()][];

		// Asiakas nimi, rahaa
		for (int i = 0; i < kauppa.getAsiakkaat().size(); i++)
		{
			lista[i] = new Object[]
			{ kauppa.getAsiakkaat().get(i), kauppa.getAsiakkaat().get(i).getRaha() };
		}

		@SuppressWarnings("serial") DefaultTableModel taulukko = new DefaultTableModel(lista, new String[]
		{ "Asiakas", "Rahaa" })
		{
			// Estetään taulukon muokkaus.
			public boolean isCellEditable(int row, int column)
			{
				return false;
			}
		};

		return taulukko;
	}

	/**
	 * @return Listan henkilokunnan nimistä ja palkoista.
	 */
	public TableModel getHKuntaTaulu()
	{
		if (Simu.debug)
			System.out.println("\t- HENKILÖKUNNAN PÄIVITYS -");

		Object[][] lista = new Object[getKauppa().getHenkilokunta().size()][];

		// Henkilokunta nimi, palkka
		for (int i = 0; i < getKauppa().getHenkilokunta().size(); i++)
		{
			lista[i] = new Object[]
			{ getKauppa().getHenkilokunta().get(i), getKauppa().getHenkilokunta().get(i).getTyoNimi(), muotoileDesimaalit(2, getKauppa().getHenkilokunta().get(i).getPalkka()) + " €/t" };
		}
		
		@SuppressWarnings("serial") DefaultTableModel taulukko = new DefaultTableModel(lista, new String[]
		{ "Henkilökunta", "Työ", "Tuntipalkka" })
		{
			// Estetään taulukon muokkaus.
			public boolean isCellEditable(int row, int column)
			{
				return false;
			}
		};

		return taulukko;

	}

	/**
	 * Asiakkaat ostaa karkkeja.
	 */
	public void asiakkaatOstaa()
	{
		MersenneTwisterFast satunnaisluku = new MersenneTwisterFast();

		int kuinkaMoniOstaa = satunnaisluku.randInt(getAsiakkaat().size());

		if (kuinkaMoniOstaa == 0)
			kuinkaMoniOstaa = 1;

		if (Simu.debug)
			System.out.println("\t(DBG) Kaupassa asiakkaita: " + getAsiakkaat().size() + " joista " + kuinkaMoniOstaa + " ostaa tällä minuutilla.");

		for (int i = 0; i < kuinkaMoniOstaa; i++)
		{
			if (!kauppa.onkoVarastoTyhja())
			{
				if (!getAsiakkaat().get(i).osta())
					break; // Kama loppu kesken.
			}
			else
				break;
		}
	}

	/**
	 * @return taulun palkattavista henkilöistä ja palkkatoiveista.
	 */
	public TableModel getPalkattavatTaulu()
	{
		if (Simu.debug)
			System.out.println("\t- PALKATTAVIEN PÄIVITYS -");

		Object[][] lista = new Object[palkattavat.size()][];

		// Henkilokunta nimi, palkka
		for (int i = 0; i < palkattavat.size(); i++)
		{
			lista[i] = new Object[]
			{ palkattavat.get(i), palkattavat.get(i).getTyoNimi(), muotoileDesimaalit(2, palkattavat.get(i).getPalkka()) + " €/t" };
		}

		@SuppressWarnings("serial") DefaultTableModel taulukko = new DefaultTableModel(lista, new String[]
		{ "Hakija", "Työ", "Tuntipalkka" })
		{
			// Estetään taulukon muokkaus.
			public boolean isCellEditable(int row, int column)
			{
				return false;
			}
		};

		return taulukko;
	}

	public ArrayList<Henkilokunta> getHenkilokunta()
	{
		return getKauppa().getHenkilokunta();
	}

	public ArrayList<Henkilokunta> getPalkattavat()
	{
		return palkattavat;
	}

	/**
	 * Maksaa kaikkien työntekijöiden palkat.
	 * 
	 * @return maksettu euromäärä.
	 */
	public double maksaPalkat()
	{
		return getKauppa().maksaPalkat();
	}

	public double getLiikevaihto()
	{
		return getKauppa().getLiikevaihto();
	}

	public boolean josAsiakkaita()
	{
		return !kauppa.getAsiakkaat().isEmpty();
	}

	public void maksaVuokra()
	{
		getKauppa().maksaVuokra();
	}

	/**
	 * Luo uuden karkkivaraston tukuille.
	 */
	public void uudetTukut()
	{
		getTukku(0).uusiVarasto();
	}
}
