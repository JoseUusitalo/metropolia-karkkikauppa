package malli;

import java.util.ArrayList;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import ohjain.Simu;
import tuki.Dev;
import tuki.Mat;
import tuki.MersenneTwisterFast;

/**
 * Pelaajan kauppa.
 */
public class Kauppa
{
	private Nimi omistaja;
	private String nimi;
	private Pankkitili tili;
	private ArrayList<KarkkiLaatikko> karkkiVarasto;
	private double hintamarginaali;
	private int valikoima;
	private Pankkikortti kortti;
	private ArrayList<Henkilokunta> henkilokunta = new ArrayList<Henkilokunta>();
	private static ArrayList<Asiakas> kaupanAsiakkaat = new ArrayList<Asiakas>();
	private double liikevaihto;

	//private ArrayList<KarkkiLaatikko> karkkiHyllyt;
	//private ArrayList<Kaluste> kalusteVarasto;

	public Kauppa(final Nimi nimiOmistaja, final String nimi, final Pankkitili tili, final Pankkikortti kortti)
	{
		this.omistaja = nimiOmistaja;
		this.nimi = nimi;
		this.tili = tili;
		this.kortti = kortti;
		karkkiVarasto = new ArrayList<KarkkiLaatikko>();
		valikoima = karkkiVarasto.size();
		hintamarginaali = 1.0;
		//kalusteVarasto = new ArrayList<Kaluste>();
		//karkkiHyllyt = new ArrayList<KarkkiLaatikko>();
	}

	public void tulosta()
	{
		System.out.print(nimi + ", hintamarginaali: " + hintamarginaali + ", valikoima: " + valikoima + " tuotemerkkiä, varasto: ");
		if (!karkkiVarasto.isEmpty())
		{
			System.out.println(Logiikka.laskeKarkkiGrammatVarastossa(karkkiVarasto) + " g karkkia");
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
	 * Lisää kauppaan karkkeja.
	 * 
	 * @param karkki
	 *            Karkkilaatikko.
	 * @param grammaa
	 *            Grammaa.
	 */
	public void lisaaKarkkeja(final KarkkiLaatikko karkki, final int grammaa)
	{
		boolean uusiTuote = true;
		KarkkiLaatikko varastossa = null;

		for (final KarkkiLaatikko varastoKarkki : karkkiVarasto)
		{
			if (karkki.toString().equals(varastoKarkki.toString()))
			{
				uusiTuote = false;
				varastossa = varastoKarkki;
				break; // Hyi hyi, mutta en keksi nyt parempaakaan.
			}
		}

		if (!uusiTuote)
		{
			if (Simu.debug)
				System.out.println("\t(DBG) " + karkki.toString() + " karkkia on jo varastossa, lisätään " + grammaa + "g.");

			karkkiVarasto.get(karkkiVarasto.indexOf(varastossa)).lisaaGrammoja(grammaa);
		}
		else
		{
			if (Simu.debug)
				System.out.println("\t(DBG) Uusi tuote! Lisätään " + karkki.toString() + ".");

			karkkiVarasto.add(karkki.kopioiUudellaMaaralla(grammaa));
		}
	}

	/**
	 * Aseta kaupan hintamarginaali.
	 * 
	 * @param hintamarginaali
	 */
	public void setHintamarginaali(final double hintamarginaali)
	{
		this.hintamarginaali = hintamarginaali;
	}

	public ArrayList<KarkkiLaatikko> getKarkkiVarasto()
	{
		return karkkiVarasto;
	}

	public double getSaldo()
	{
		return tili.getRahamaara();
	}

	public Pankkitili getTili()
	{
		return tili;
	}

	public String toString()
	{
		return nimi;
	}

	/**
	 * Otetaan asiakkaalta rahaa omalle tilille.
	 * 
	 * @param asiakas
	 *            Asiakas.
	 * @param rahat
	 *            Euroja.
	 */
	public void asiakkaaltaRahaa(Asiakas asiakas, final double rahat)
	{
		asiakas.getKortti().maksa(rahat, tili);
	}

	public ArrayList<Henkilokunta> getHenkilokunta()
	{
		return henkilokunta;
	}

	/**
	 * @param karkki
	 *            Karkki.
	 * @param grammaa
	 *            Grammat.
	 * @return true jos karkkia on varastossa >= grammat.
	 */
	public boolean tarpeeksiKarkkia(final KarkkiLaatikko karkki, final int grammaa)
	{
		return (karkki.getGrammat() >= grammaa);
	}

	/**
	 * @return Satunnaisen karkin varastosta, null jos varasto on tyhjä.
	 */
	public KarkkiLaatikko getRandomKarkki()
	{
		if (!onkoVarastoTyhja())
		{
			MersenneTwisterFast satunnaisluku = new MersenneTwisterFast();
			return karkkiVarasto.get(satunnaisluku.randInt(karkkiVarasto.size() - 1));
		}
		else
			return null;
	}

	/**
	 * @param karkki
	 *            Karkki.
	 * @return karkin hinta tässä kaupassa.
	 */
	public double getKarkinKilohinta(final KarkkiLaatikko karkki)
	{
		return karkki.getKaupanKilohinta() * hintamarginaali;
	}

	/**
	 * @return Taulukon karkkien nimistä, määristä ja hinnoista.
	 */
	public TableModel karkkiVarastoTauluun()
	{
		if (Simu.debug)
			System.out.println("\t- KAUPAN VARASTON PÄIVITYS -");

		
		
		if (!onkoVarastoTyhja())
		{
			Object[][] lista = new Object[karkkiVarasto.size()][];

			// Karkkilaatikko-olio , kilot, hinta
			for (int i = 0; i < karkkiVarasto.size(); i++)
			{
				lista[i] = new Object[]
				{ karkkiVarasto.get(i), karkkiVarasto.get(i).getKiloina() + " kg", Logiikka.muotoileDesimaalit(2, getKarkinKilohinta(karkkiVarasto.get(i))) + " €/kg" };
			}

			@SuppressWarnings("serial") DefaultTableModel taulukko = new DefaultTableModel(lista, new String[]
			{ "Karkkilaatikko", "Kiloa", "Kilohinta" })
			{
				// Estetään taulukon muokkaus.
				public boolean isCellEditable(int row, int column)
				{
					return false;
				}
			};

			return taulukko;
		}
		else
		{
			if (Simu.debug)
				System.out.println("\t(DBG) Varasto tyhjä! Palautetaan null lista.");
			
			@SuppressWarnings("serial") DefaultTableModel taulukko = new DefaultTableModel(null, new String[]
			{ "Karkkilaatikko", "Kiloa", "Kilohinta" })
			{
				// Estetään taulukon muokkaus.
				public boolean isCellEditable(int row, int column)
				{
					return false;
				}
			};

			return taulukko;

		}
	}

	/**
	 * Poista tavaraa varastosta.
	 * 
	 * @param karkki
	 *            Karkki.
	 * @param grammaa
	 *            Paljonko poistetaan.
	 */
	private void vahennaTavaraa(final KarkkiLaatikko karkki, final int grammaa)
	{
		if (!karkkiVarasto.get(karkkiVarasto.indexOf(karkki)).vahenna(grammaa)) // Kaikki karkit ostettu!
		{
			if (Simu.debug)
				System.out.println("\t(DBG) Otetaan varastosta pois loputkin " + karkki.toString() + ".");

			karkkiVarasto.remove(karkki);
		}
		else
		{
			if (Simu.debug)
				System.out.println("\t(DBG) Otetaan varastosta pois " + karkki.toString() + " grammoja: " + grammaa + " varastossa: " + this.grammaaKarkkiaJaljella(karkki));
		}
	}

	/**
	 * @param karkki
	 *            Karkki.
	 * @return kuinka monta grammaa karkkia on jäljellä.
	 */
	public int grammaaKarkkiaJaljella(final KarkkiLaatikko karkki)
	{
		return karkkiVarasto.get(karkkiVarasto.indexOf(karkki)).getGrammat();
	}

	/**
	 * Muuta karkin kilohintaa kaupassa.
	 * 
	 * @param karkki
	 *            Karkki.
	 * @param uusiHinta
	 *            Uusi kilohinta.
	 */
	public void muutaMyyntihinta(KarkkiLaatikko karkki, double uusiHinta)
	{
		try
		{
			karkkiVarasto.get(karkkiVarasto.indexOf(karkki)).muutaMyyntihinta(uusiHinta);
		}
		catch (IndexOutOfBoundsException e)
		{
			// Ei tehdä mitään, käyttäjä yritti asettaa kilohintaa ilman valittua karkkia.
		}
	}

	/**
	 * Lasketaan kuinka monta grammaa karkkia saa n eurolla.
	 * 
	 * @param karkki
	 *            Karkki mitä halutaan.
	 * @param euroa
	 *            Kuinka monta euroa halutaan maksaa.
	 * @return Grammat.
	 */
	public int grammaaKarkkiaEurolla(final KarkkiLaatikko karkki, final double euroa)
	{
		return (int) Math.ceil(1000.0 * (euroa / getKarkinKilohinta(karkki)));
	}

	/**
	 * Luo kauppaan uudet asiakkaat.
	 */
	public void uudetAsiakkaat()
	{
		if (Simu.debug)
			System.out.println("\t(DBG) Luodaan uudet asiakkaat.");

		Logiikka.getAsiakkaat().clear();

		MersenneTwisterFast satunnaisluku = new MersenneTwisterFast();

		// Luodaan [1,20] henkilöä.
		for (int i = 1; i <= satunnaisluku.randInt(1, 20); i++)
		{
			getAsiakkaat().add(Asiakas.luoAsiakas());
		}
	}

	/**
	 * Tarkistaa kaupan maksukortin PIN-koodin.
	 * 
	 * @param pincode
	 *            Pin.
	 * @return true jos pin-koodi on oikein.
	 */
	public boolean tarkistaPIN(int pincode)
	{
		return kortti.tarkistaPIN(pincode);
	}

	/**
	 * Asiakas ostaa karkkia.
	 * 
	 * @param asiakas
	 *            Asiakas ketä ostaa.
	 * @return true jos tavaraa riittää, false jos tavaraa on liian vähän
	 *         jäljellä, karkit liian kalliita tai asiakas ei jostain muusta
	 *         syystä halua ostaa enää karkkia.
	 */
	public boolean asiakasOsto(Asiakas asiakas)
	{
		KarkkiLaatikko karkki = getRandomKarkki(); // Karkki mitä ostetaan.
		int minimiGramma = grammaaKarkkiaEurolla(karkki, 1.0); // Ostetaan minimissään eurolla.
		int maksimiGramma = grammaaKarkkiaEurolla(karkki, 80.0); // Ostetaan maksimissaan 80 eurolla.
		int grammaa;
		boolean okMaara = false;
		double hinta = 0;

		if (minimiGramma > 10000) // Mutta ei kuitenkaan yli kymmentä kiloa.
			minimiGramma = 10000;

		if (!(getKarkinKilohinta(karkki) > 30.0)) // Karkit liian kalliita.
		{
			if (tarpeeksiKarkkia(karkki, minimiGramma)) // Riittääkö karkit minimiostoon?
			{
				if (asiakas.tarpeeksiRahaa(1.0)) // Tarpeeksi rahaa minimiostoon?
				{
					MersenneTwisterFast satunnaisluku = new MersenneTwisterFast();

					do
					{
						if (minimiGramma < 10000)
							grammaa = satunnaisluku.randInt(minimiGramma, maksimiGramma); // Ostetaan vähintään eurolla karkkia, tai enemmän.
						else
							grammaa = minimiGramma;

						if (Simu.debug)
							System.out.println("\t(DBG) Asiakas päättää ostaa: " + grammaa + "g");

						if (tarpeeksiKarkkia(karkki, grammaa)) // Karkkia riittää.
						{
							hinta = Mat.pakotaDesimaalit(2, getKarkinKilohinta(karkki) * (grammaa / 1000.0));

							if (asiakas.tarpeeksiRahaa(hinta)) // Asiakkaalla on tarpeeksi rahaa.
								okMaara = true;
							else
								okMaara = false;
						}

					} while (!okMaara);

					if (Simu.debug)
						System.out.println("\t(DBG) Asiakas yrittää ostaa: " + hinta + "€, " + grammaa + "g");

					// Asiakkalla on tarpeeksi rahaa, karkkia on tarpeeksi. Ostetaan.

					vahennaTavaraa(karkki, grammaa);
					asiakkaaltaRahaa(asiakas, hinta);
					lisaaLiikevaihtoon(hinta);

					if (Simu.debug)
						System.out.println("\t(DBG) Kaupalla nyt rahaa: " + tili.getRahamaara());

					return true;
				}
				else
				{
					// Asiakkaalla ei ollut rahaa lopetetaan ostaminen.
					return false;
				}
			}
			else
			{
				// Ei ollut tarpeeksi tavaraa minimiostoon, ostetaan loputkin karkit pois.

				hinta = Mat.pakotaDesimaalit(2, getKarkinKilohinta(karkki) * karkki.getDoubleKiloina());

				if (asiakas.tarpeeksiRahaa(hinta)) // Onko varaa?
				{
					// Asiakkalla on tarpeeksi rahaa, karkkia on tarpeeksi. Ostetaan.

					if (Simu.debug)
						System.out.println("\t(DBG) Ostetaan loputkin karkit hinnalla: " + hinta);

					vahennaTavaraa(karkki, grammaaKarkkiaJaljella(karkki));
					asiakkaaltaRahaa(asiakas, hinta);
					lisaaLiikevaihtoon(hinta);
					return true;
				}
				else
				{
					// Asiakkaalla ei ollut rahaa lopetetaan ostaminen.
					return false;
				}
			}
		}
		else
			return false; // Liian kallista.
	}

	public boolean onkoVarastoTyhja()
	{
		return (karkkiVarasto.size() == 0);
	}

	/**
	 * Poistaa asiakkaat kaupasta.
	 */
	public void poistaAsiakkaat()
	{
		Logiikka.getAsiakkaat().clear();
	}

	public double getHintamarginaali()
	{
		return hintamarginaali;
	}

	public Nimi getOmistaja()
	{
		return omistaja;
	}

	/**
	 * Maksaa työntekijöiden palkat koko kuukaudelta. 9 tunnin työpäivät.
	 * 
	 * @return maksettu euromäärä.
	 */
	public double maksaPalkat()
	{
		double palkka;
		double summa = 0;

		if (Simu.debug)
			System.out.println("\t(DBG) Maksetaan palkat!");

		for (final Henkilokunta henkilo : henkilokunta)
		{
			palkka = Mat.pakotaDesimaalit(2, henkilo.getPalkka() * 9.0 * 30.0);
			if (!tili.poistaTililta(palkka)) // Jos ei pysty maksamaan palkkoja 9 h * 30 pv.
				Simu.konkurssi();
			summa += palkka;
		}

		return summa;
	}

	/**
	 * @return true jos kaupassa on edes yksi myyjä, false muuten.
	 */
	public boolean onkoMyyjia()
	{
		for (final Henkilokunta henkilo : henkilokunta)
		{
			if (henkilo.josMyyja())
				return true;
		}

		return false;
	}

	/**
	 * @return true jos kaupassa on edes yksi myyjä, false muuten.
	 */
	public boolean onkoVartijoita()
	{
		for (final Henkilokunta henkilo : henkilokunta)
		{
			if (henkilo.josVartija())
				return true;
		}

		return false;
	}

	public void lisaaLiikevaihtoon(double euroja)
	{
		liikevaihto += euroja;
	}

	public double getLiikevaihto()
	{
		return liikevaihto;
	}

	public void maksaVuokra()
	{
		if (Simu.debug)
			System.out.println("\t(DBG) Maksetaan vuokra!");

		if (!tili.poistaTililta(5000.0)) // Jos ei pysty maksamaan vuokraa.
			Simu.konkurssi();
	}

	/**
	 * @return true jos kaupassa on yhtään henkilökuntaa, false muuten.
	 */
	public boolean josHenkilokuntaa()
	{
		for (final Henkilokunta henkilo : henkilokunta)
		{
			if (henkilo.josMyyja() || henkilo.josVartija())
				return true;
		}

		return false;
	}

	/**
	 * Varastetaan kaupan karkkeja ja rahaa.
	 */
	public void rosvoa()
	{
		Asiakas rosvo = new Asiakas(true); // Luodaan uusi rosvo.

		if (!onkoVartijoita()) // Jos ei ole vartijoita.
		{
			MersenneTwisterFast satunnaisluku = new MersenneTwisterFast();

			kaupanAsiakkaat.add(rosvo);
			KarkkiLaatikko kallein = null;
			int koko;
			double raha = 0.0;

			Simu.paivitaAsiakkaat();

			if (!onkoVarastoTyhja()) // Tyhjästä varastosta on huono varastaa mitään.
			{
				if (karkkiVarasto.size() > 3)
				{
					kallein = karkkiVarasto.get(0); // Aloitetaan ensimmäisestä.
					ArrayList<KarkkiLaatikko> varastetut = new ArrayList<KarkkiLaatikko>();

					// Viedään kolme kalleinta.
					koko = 3;

					for (int i = 0; i < koko; i++)
					{
						if (Simu.debug)
							Dev.tulostaKarkkivarasto(varastetut);

						for (final KarkkiLaatikko karkki : karkkiVarasto)
						{
							if (kallein.getPohjahinta() <= karkki.getPohjahinta() && !varastetut.contains(karkki))
								kallein = karkki;
						}

						if (Simu.debug)
							System.out.println("\t(DBG) Varastetaan: " + kallein.toString());

						rosvo.varasta(kallein);
						varastetut.add(kallein);
						kallein = karkkiVarasto.get(0); // Aloitetaan taas ensimmäisestä.
					}

					if (Simu.debug)
						Dev.tulostaKarkkivarasto(varastetut);

					// Tähän on kyllä parempikin tapa, mutta tehdään nyt näin.
					for (KarkkiLaatikko karkki : varastetut)
					{
						karkkiVarasto.remove(karkki);
					}
				}
				else
				{
					// Viedään kaikki.
					for (int i = 0; i < karkkiVarasto.size(); i++)
					{
						if (Simu.debug)
							System.out.println("\t(DBG) Varastetaan: " + karkkiVarasto.get(i).toString());

						rosvo.varasta(karkkiVarasto.get(i));
					}
					karkkiVarasto.clear();
				}

				Simu.paivitaKaupanVarasto();

				// Lopuksi viedään 10% rahoista 50% mahdollisuudella.
				if (satunnaisluku.nextBoolean())
				{
					raha = Mat.pakotaDesimaalit(2, getSaldo() * 0.1);
					rosvo.getTili().siirto(tili, raha);

					Simu.varoitusLoota("ROSVO!", "Rosvo varasti karkkeja varastostasi ja " + raha + " euroa!");
				}
				else
				{
					Simu.varoitusLoota("ROSVO!", "Rosvo varasti karkkeja varastostasi!");
				}
			}
		}
		else
		{
			// Vartijoita paikalla, rosvo käy silti kääntymässä.
			kaupanAsiakkaat.add(rosvo);
			Simu.paivitaAsiakkaat();
		}

	}

	public ArrayList<Asiakas> getAsiakkaat()
	{
		return kaupanAsiakkaat;
	}
}
