package ohjain;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import javax.swing.JComboBox;
import javax.swing.table.TableModel;
import javax.swing.Timer;

import tuki.Mat;
import tuki.MersenneTwisterFast;
import malli.Henkilokunta;
import malli.KarkkiLaatikko;
import malli.Logiikka;
import malli.Pankki;
import nakyma.Kehys;

/**
 * MVC: Ohjain<br>
 * Mallin päivitys ja käyttöliittymän tapahtumiin reagointi.
 * 
 * @see Logiikka
 * @see Kehys
 */
public class Simu
{
	public static boolean simuloi = true;

	// Tulosta konsoliin debuggaustietoja.
	public static final boolean debug = false;

	private static Logiikka malli;
	static Kehys kehys;

	//private static int simu6min = 250; // Millisekuntia.
	private static int simu1tunti = 600; // Millisekuntia.
	public static int paiva = 0;
	public static int tunti = 0;
	public static int minuutti = 0;
	public static Timer timer;
	private static boolean konkurssi = false;

	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					Simu simu = new Simu();
					malli = new Logiikka(simu);
					kehys = new Kehys(simu);

					if (Simu.debug)
						System.out.println("\t(DBG) Kehys luotu, aika käyntiin.");

					ActionListener action = new ActionListener()
					{
						public void actionPerformed(ActionEvent event)
						{
							if (simuloi)
							{
								lisaaTunti();
							}
							else
							{
								timer.stop();
								laskePisteet(paiva, tunti, malli.getLiikevaihto());
							}
						}
					};

					timer = new Timer(simu1tunti, action);
					timer.setInitialDelay(0);
					timer.start();

					aloitus();
				} catch (Exception e)
				{
					System.err.println("\t !!! Kaikki hajoaa !!!");
					e.printStackTrace();
				}
			}
		});
	}

	/*
	static void lisaa6Min()
	{
		if (minuutti < 60)
		{
			minuutti += 6;
			uusiMinuutti();
			kehys.lisaaMinuutti();
		}
		else
		{
			minuutti = 0;
			lisaaTunti();
		}
	}
	
	private static void uusiMinuutti()
	{
		if (debug)
			System.out.println("\t(DBG) Minuutti!");
		
		if (!malli.getKauppa().onkoVarastoTyhja()) // Tyhjästä kaupasta on huono ostaa.
		{
			MersenneTwisterFast satunnaisluku = new MersenneTwisterFast();
			
			if (satunnaisluku.randBoolean(0.2f) && malli.getKauppa().onkoMyyjia() && malli.josAsiakkaita()) //20% mahdollisuus ostaa karkkia mutta vain jos on myyjiä ja asiakkaita.
			{
				malli.asiakkaatOstaa();
				kehys.paivitaKaupanVarasto();
			}
		}
	}
	*/

	static void lisaaTunti()
	{
		uusiTunti();

		if (tunti < 24)
		{
			kehys.lisaaTunti();
		}
		else
		{
			tunti = 0;
			lisaaPaiva();
		}

		tunti++;
	}

	private static void lisaaPaiva()
	{
		paiva++;
		uusiPaiva();
		kehys.lisaaPaiva();

		if (paiva % 7 == 0 && paiva != 0)
			uusiViikko();

		if (paiva % 30 == 0 && paiva != 0)
			uusiKuukausi();
	}

	/**
	 * Kaikki mita tapahtuu uuden tunnin alkaessa.
	 */
	private static void uusiTunti()
	{
		/*if (debug)
			System.out.println("Tunti!");*/

		if (!malli.getKauppa().onkoVarastoTyhja() && malli.getKauppa().onkoMyyjia() && tunti >= 9 && tunti <= 18) // Jos on karkkia, myyjiä ja kauppa auki, uudet asiakkaat.
		{
			if (Simu.debug)
				System.out.println("\t- ASIAKKAIDEN PÄIVITYS -");

			malli.getKauppa().uudetAsiakkaat();
			kehys.paivitaAsiakkaat();
		}
		else
		{
			// Muuten kaikki lähtee pois.
			malli.getKauppa().poistaAsiakkaat();
			kehys.paivitaAsiakkaat();
		}

		if (malli.getKauppa().onkoMyyjia() && malli.josAsiakkaita()) //Jos on myyjiä ja asiakkaita.
		{
			if (!malli.getKauppa().onkoVarastoTyhja()) // Tyhjästä kaupasta on huono ostaa.
			{
				malli.asiakkaatOstaa();
				kehys.paivitaKaupanVarasto();
				kehys.paivitaAsiakkaat();
			}
		}
	}

	/**
	 * Kaikki mitä tapahtuu uuden päivän alkaessa.
	 */
	private static void uusiPaiva()
	{
		if (debug)
			System.out.println("Päivä!");

		if (Pankki.tarkistaLaina())
			kehys.varoitusLoota("MAKSA LAINASI!", "Pankkiiri kolkuttaa ovellasi vihaisen näköisenä. \"Maksa lainasi!\"");
	}

	/**
	 * Kaikki mitä tapahtuu uuden viikon alkaessa.
	 */
	private static void uusiViikko()
	{
		MersenneTwisterFast satunnaisluku = new MersenneTwisterFast();

		if (debug)
			System.out.println("Viikko!");

		if (satunnaisluku.randBoolean(1.0))
		{
			if (debug)
				System.out.println("\t(DBG) Rosvo!");

			malli.getKauppa().rosvoa();
		}

		malli.uudetTukut();
		kehys.paivitaTukkuKarkit();
		kehys.paivitaTukkuLuvut();
	}

	/**
	 * Kaikki mitä tapahtuu uuden kuukauden alkaessa.
	 */
	private static void uusiKuukausi()
	{
		if (debug)
			System.out.println("Kuukausi!");

		if (malli.getKauppa().josHenkilokuntaa())
		{
			double eurot = malli.maksaPalkat();
			kehys.infoLoota("Palkat maksettu", "Henkilökunnan palkat maksettu, tililtä poistettu: " + Logiikka.muotoileDesimaalit(2, eurot) + " €");
		}

		malli.maksaVuokra();
		kehys.infoLoota("Vuokra maksettu", "Vuokra maksettu, tililtä poistettu 5000 €");
	}

	/**
	 * @param comboLoota
	 *            ComboBox
	 * @param tukkuID
	 *            Tukun ID.
	 * @return ComboBoxissa valitun kohdan ID.
	 */
	public int valitunKarkinListaID(final JComboBox<String> comboLoota, final int tukkuID)
	{
		/* Homma hajoaa jos on kaksi eri karkki-ohjektia millä on sama nimi, kilohinta ja määrä.
		 * Mutta kun karkit luodaan, kahdella eri karkki-objektilla ei voi olla samaa nimeä.
		 */

		return Arrays.asList(getTukkuKarkkiLista(tukkuID)).indexOf((String) comboLoota.getSelectedItem());
	}

	/**
	 * @param tukkuID
	 *            Tukku.
	 * @param karkki
	 *            Karkki.
	 * @param eurot
	 *            Kuinka monta euroa.
	 * @return kuinka monta grammaa karkkia tukusta saa euromäärällä, tekstinä.
	 */
	public String eurotGrammoiksi(final int tukkuID, KarkkiLaatikko karkki, final double eurot)
	{
		return String.valueOf((int) Math.floor((eurot / malli.getTukku(tukkuID).getKarkinKilohinta(karkki)) * 1000.0));
	}

	/**
	 * @param tukkuID
	 *            Tukku.
	 * @param karkki
	 *            Karkki.
	 * @param grammat
	 *            Karkin määrä grammoina.
	 * @return kuinka monta euroa tämä grammamäärä karkkia maksaa tukussa,
	 *         tekstinä.
	 */
	public String karkkiGrammatHinnaksi(final int tukkuID, final KarkkiLaatikko karkki, final int grammat)
	{
		return Logiikka.muotoileDesimaalit(2, malli.getTukku(tukkuID).getKarkinKilohinta(karkki) * (grammat / 1000.0));
	}

	/**
	 * @param tukkuID
	 *            0-2
	 * @return Tukun karkkivarasto kivana tekstilistana.
	 */
	public String[] getTukkuKarkkiLista(final int tukkuID)
	{
		return malli.getTukku(tukkuID).listaaKarkkiVarasto();
	}

	/**
	 * Osta tukusta karkkia.
	 * 
	 * @param tukkuID
	 *            Tukku ID.
	 * @param karkki
	 *            Karkki
	 * @param grammaa
	 *            Kuinka paljon
	 * @return true jos osto onnistui, false jos ei.
	 */
	public boolean ostaKarkki(final int tukkuID, final KarkkiLaatikko karkki, final int grammaa)
	{
		int varastoMaara = malli.getTukku(tukkuID).getValikoimanKoko();

		if (malli.getTukku(tukkuID).osta(karkki, grammaa))
		{
			if (varastoMaara != malli.getTukku(tukkuID).getValikoimanKoko()) // Varaston määrä muuttunut, valitaan ensimmäinen listasta.
				kehys.asetaKarkkiListanValinta(0, 0);
			return true;
		}
		else
			return false;
	}

	/**
	 * @param tukkuID
	 *            Tukun ID.
	 * @param karkkiListanValintaID
	 *            Valitun karkin comboboksi ID.
	 * @return Karkki-olio combolaatikon IDn mukaan.
	 */
	public KarkkiLaatikko getTukunKarkkiByID(final int tukkuID, final int karkkiListanValintaID)
	{
		return malli.getTukku(tukkuID).getKarkkiByID(karkkiListanValintaID);
	}

	/**
	 * @return Pelaajan kaupan nimen.
	 */
	public String getKehyksenOtsikko()
	{
		return malli.getKauppa().getOmistaja() + "n " + malli.getKauppa().toString();
	}

	public void paivitaNakymaTukutLuvut()
	{
		kehys.paivitaTukkuLuvut();
	}

	public static void paivitaKaupanVarasto()
	{
		kehys.paivitaKaupanVarasto();
	}

	public static void paivitaAsiakkaat()
	{
		kehys.paivitaAsiakkaat();
	}

	public void paivitaNakymaTukutListat()
	{
		kehys.paivitaTukkuKarkit();
	}

	public void paivitaPalkattavat()
	{
		kehys.paivitaPalkattavat();
	}

	/**
	 * @param tukkuID
	 *            Tukku ID.
	 * @param tukku0ValittuKarkki
	 *            Karkki-olio.
	 * @return kuinka monta grammaa karkkia on tukussa jäljellä.
	 */
	public int getKarkkilaatikonKoko(final int tukkuID, final KarkkiLaatikko tukku0ValittuKarkki)
	{
		return malli.getTukku(tukkuID).grammaaKarkkiaJaljella(tukku0ValittuKarkki);
	}

	public String getTukunNimi(final int tukkuID)
	{
		return malli.getTukku(tukkuID).toString();
	}

	public int getTukunValikoimanKoko(final int tukkuID)
	{
		return malli.getTukku(tukkuID).getValikoimanKoko();
	}

	/**
	 * @return taulukko kaupan karkkivarastosta.
	 */
	public TableModel getKauppaKarkkiVarastoTaulu()
	{
		return malli.getKauppa().karkkiVarastoTauluun();
	}

	/**
	 * @param karkki
	 *            Karkki.
	 * @return kuinka paljon karkki maksaa kaupassa.
	 */
	public String getKauppaKilohinta(final KarkkiLaatikko karkki)
	{
		return Logiikka.muotoileDesimaalit(2, malli.getKauppa().getKarkinKilohinta(karkki));
	}

	/**
	 * Muuttaa karkin myyntihintaa kaupassa. Ottaa huomioon hintamarginaalin.
	 * 
	 * @param karkki
	 *            Karkki.
	 * @param uusiHinta
	 *            Haluttu lopullinen myyntihinta.
	 */
	public void muutaKaupanKilohinta(KarkkiLaatikko karkki, double uusiHinta)
	{
		malli.getKauppa().muutaMyyntihinta(karkki, Mat.pakotaDesimaalit(2, uusiHinta / malli.getKauppa().getHintamarginaali()));
	}

	/**
	 * @return taulukko kaupan asiakkaista.
	 */
	public TableModel getAsiakasTaulu()
	{
		return malli.getAsiakasTaulu();
	}

	/**
	 * @param pincode
	 *            Koodi.
	 * @return true jos annettu pinkoodi on oikein, false muuten.
	 */
	public static boolean tarkistaPIN(int pincode)
	{
		return malli.getKauppa().tarkistaPIN(pincode);
	}

	/**
	 * @return kaupan pankkitilillä olevan rahamäärän.
	 */
	public static String getKaupanRaha()
	{
		return String.valueOf(malli.getKauppa().getSaldo());
	}

	public void asetaKaupanMarginaali(double uusiMarginaali)
	{
		malli.getKauppa().setHintamarginaali(uusiMarginaali);
	}

	/**
	 * @return kaupan hintamarginaalin.
	 */
	public String getKauppaMarginaali()
	{
		return String.valueOf(malli.getKauppa().getHintamarginaali());
	}

	/**
	 * @param halututGrammat
	 * @param maxGrammat
	 * @return true jos: 0 < halutut grammat <= max grammat
	 */
	public boolean tarkistaGrammat(int halututGrammat, int maxGrammat)
	{
		if (halututGrammat > maxGrammat || halututGrammat <= 0)
		{
			return false;
		}
		else
		{
			return true;
		}
	}

	/**
	 * @param halututEurot
	 * @param tukku0ValittuKarkki
	 * @return true jos: 0 < halutut eurot <= tukun karkin hinta koko laatikolle
	 */
	public boolean tarkistaEuromaara(double halututEurot, KarkkiLaatikko tukku0ValittuKarkki)
	{
		double karkinmaxhinta = malli.getTukku(0).getKarkinKilohinta(tukku0ValittuKarkki) * (tukku0ValittuKarkki.getGrammat() / 1000.0);
		if (halututEurot > karkinmaxhinta || halututEurot <= 0)
		{
			return false;
		}
		else
		{
			return true;
		}
	}

	public boolean maksaLaina()
	{
		return Pankki.maksaLaina();
	}

	/**
	 * @return taulukko kaupan henkilökunnasta.
	 */
	public TableModel getHKuntaTaulu()
	{
		return malli.getHKuntaTaulu();
	}

	/**
	 * @return taulukko palkattavista henkilöistä.
	 */
	public TableModel getPalkattavatTaulu()
	{
		return malli.getPalkattavatTaulu();
	}

	public void palkkaaHenkilo(Henkilokunta valittuPalkattava)
	{
		malli.getPalkattavat().remove(valittuPalkattava);
		malli.getHenkilokunta().add(valittuPalkattava);
	}

	public void irtisanoHenkilo(Henkilokunta valittuHenkilokunta)
	{
		malli.getHenkilokunta().remove(valittuHenkilokunta);
	}

	/**
	 * Simulaatio loppuu: konkurssiin.
	 */
	public static void konkurssi()
	{
		simuloi = false;
		konkurssi = true;

		if (debug)
			System.out.println("Menit konkurssiin!");
	}

	/**
	 * Simulaatio loppuu: lopetukseen.
	 */
	public void lopetaSimu()
	{
		simuloi = false;
		konkurssi = false;

		if (debug)
			System.out.println("Pelaaja lopetti!");
	}

	/**
	 * Lasketaan pelaajan pisteet.
	 * 
	 * @param paiva
	 *            Nykyinen päivä.
	 * @param tunti
	 *            Nykyinen tunti.
	 * @param liikevaihto
	 *            Kaupan liikevaihto.
	 */
	private static void laskePisteet(int paiva, int tunti, double liikevaihto)
	{
		// Sata pojoo = vuosi, 1mil = 100p
		int pisteet = (int) Math.round(((paiva * 24.0 + tunti) / (365.0 * 24.0) * 100) + (liikevaihto / 1000000) * 100);

		String viesti = "Selvisit " + paiva + " päivää ja " + tunti + " tuntia. Liikevaihtosi oli: " + Logiikka.muotoileDesimaalit(2, liikevaihto) + " €  ja lopulliset pisteesi ovat: " + pisteet;

		if (konkurssi)
			kehys.infoLoota("Menit konkurssiin!", viesti);
		else
			kehys.infoLoota("Lopetit simulaation!", viesti);
	}

	/**
	 * @return true jos lainaa ei ole maksettu, false jos on maksettu.
	 */
	public boolean josLaina()
	{
		return Pankki.josLaina();
	}

	public static void aloitus()
	{
		kehys.infoLoota("Tervetuloa " + malli.getKauppa().getOmistaja().toString() + "!", "Olet juuri perustanut " + malli.getKauppa().toString() + " yrityksen lainaamalla pankista 5000 €. Pankkikorttisi pinkoodi on 2015. Katsotaan kuinka kauan selviät...");
	}

	/**
	 * Näyttää infoikkunan.
	 * @param otsikko
	 *            Laatikon otsikko.
	 * @param viesti
	 *            Laatikon viesti.
	 */
	public static void infoLoota(final String otsikko, final String viesti)
	{
		kehys.infoLoota(otsikko, viesti);
	}

	/**
	 * Näyttää varoitusikkunan.
	 * @param otsikko
	 *            Laatikon otsikko.
	 * @param viesti
	 *            Laatikon viesti.
	 */
	public static void varoitusLoota(final String otsikko, final String viesti)
	{
		kehys.varoitusLoota(otsikko, viesti);
	}
}