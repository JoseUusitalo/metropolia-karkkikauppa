package nakyma;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import java.awt.BorderLayout;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JSlider;
import javax.swing.BoxLayout;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ListSelectionModel;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import ohjain.Simu;
import malli.Henkilokunta;
import malli.KarkkiLaatikko;
import malli.Logiikka;
import tuki.Mat;

import java.awt.FlowLayout;

/**
 * MVC: Näkymä<br>
 * Graafiset elementit. Tyhmä luokka, mahdollisimman vähän logiikkaa. Kaikki kutsut vain ohjaimeen.
 *
 * @see Logiikka
 * @see Simu
 */
public class Kehys extends JFrame
{
	
	private static final long serialVersionUID = 1L; // En tiedä.
	
	private Simu ohjain;
	
	private JFrame ikkunaKarkkikauppa;
	private JTextField tukku0TextEurot;
	private JSlider tukku0Slider;
	private JTextField tukku0TextGrammat;
	private JComboBox<String> tukku0KarkkiLista;
	private JPanel tukku0Paneeli;
	private JPanel tukku0GrammaPaneeli;
	private JPanel tukku0HintaPaneeli;
	private JLabel lblTuntiArvo;
	private JLabel lblPaivaArvo;
	private JTable tableKaupanVarasto;
	private JTextField kauppaVarastoKilohintaTekstiloota;
	private JTable tableAsiakkaat;
	private JTextField textFieldHintamarginaali;
	private JTable tableHenkilokunta;
	private JTable tablePalkattavat;
	private JPanel panelVarastoKilohinta;
	
	private int tukku0KarkkiListaValintaID = 0;
	private KarkkiLaatikko tukku0ValittuKarkki;
	private int tukku0MaxGrammat;
	private int tukku0ValitutGrammat = 0;
	private Henkilokunta valittuHenkilokunta;
	private Henkilokunta valittuPalkattava;
	private KarkkiLaatikko kauppaValittuVarastoKarkki;
	
	/**
	 * Create the application.
	 */
	public Kehys(Simu simu)
	{
		this.ohjain = simu;
		
		tukku0KarkkiListaValintaID = 0;
		tukku0ValittuKarkki = ohjain.getTukunKarkkiByID(0, tukku0KarkkiListaValintaID);
		tukku0MaxGrammat = (int) Math.round(tukku0ValittuKarkki.getGrammat());
		tukku0ValitutGrammat = 0;
		
		initialize();
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	@SuppressWarnings("unchecked")
	private void initialize()
	{
		ikkunaKarkkikauppa = new JFrame();
		ikkunaKarkkikauppa.setResizable(true);
		ikkunaKarkkikauppa.setTitle(ohjain.getKehyksenOtsikko());
		ikkunaKarkkikauppa.setSize(new Dimension(700, 500));
		ikkunaKarkkikauppa.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JTabbedPane valilehtiPaneeli = new JTabbedPane(JTabbedPane.TOP);
		ikkunaKarkkikauppa.getContentPane().add(valilehtiPaneeli, BorderLayout.CENTER);
		
		JPanel paneeliKauppa = new JPanel();
		valilehtiPaneeli.addTab("Kauppa", null, paneeliKauppa, null);
		paneeliKauppa.setLayout(new BorderLayout(0, 0));
		
		JPanel paneeliKauppaOminaisuudet = new JPanel();
		
		JPanel kaupanToiminnot = new JPanel();
		
		paneeliKauppa.add(paneeliKauppaOminaisuudet, BorderLayout.WEST);
		paneeliKauppaOminaisuudet.setLayout(new BorderLayout(0, 0));
		paneeliKauppaOminaisuudet.add(kaupanToiminnot, BorderLayout.NORTH);
		
		JPanel panelHintamarginaali = new JPanel();
		
		JLabel lblHintamarginaali = new JLabel("Hintamarginaali:");
		panelHintamarginaali.add(lblHintamarginaali);
		
		textFieldHintamarginaali = new JTextField();
		textFieldHintamarginaali.setText(ohjain.getKauppaMarginaali());
		panelHintamarginaali.add(textFieldHintamarginaali);
		textFieldHintamarginaali.setColumns(10);
		kaupanToiminnot.setLayout(new BoxLayout(kaupanToiminnot, BoxLayout.Y_AXIS));
		
		// Hintamarginaalia muutettu.
		textFieldHintamarginaali.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				ohjain.asetaKaupanMarginaali(getKirjoitettuMarginaali());
				textFieldHintamarginaali.setText(ohjain.getKauppaMarginaali());
				paivitaKaupanVarasto();
			}
		});
		
		JPanel panelVuokra = new JPanel();
		kaupanToiminnot.add(panelVuokra);
		
		JLabel labelVuokra = new JLabel("Vuokra: 5000 €/30pv");
		panelVuokra.add(labelVuokra);
		kaupanToiminnot.add(panelHintamarginaali);
		
		JPanel panelLopetus = new JPanel();
		paneeliKauppaOminaisuudet.add(panelLopetus, BorderLayout.SOUTH);
		
		JButton btnLopeta = new JButton("Lopeta");
		
		btnLopeta.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				ohjain.lopetaSimu();
			}
		});
		
		panelLopetus.add(btnLopeta);
		
		JPanel panelAsiakasLista = new JPanel();
		panelAsiakasLista.setAlignmentY(Component.BOTTOM_ALIGNMENT);
		panelAsiakasLista.setAlignmentX(Component.LEFT_ALIGNMENT);
		paneeliKauppa.add(panelAsiakasLista, BorderLayout.EAST);
		
		JScrollPane scrollPaneAsiakkaat = new JScrollPane();
		
		scrollPaneAsiakkaat.setAlignmentY(Component.TOP_ALIGNMENT);
		scrollPaneAsiakkaat.setAlignmentX(Component.RIGHT_ALIGNMENT);
		scrollPaneAsiakkaat.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPaneAsiakkaat.setAlignmentY(1.0f);
		scrollPaneAsiakkaat.setAlignmentX(1.0f);
		
		tableAsiakkaat = new JTable();
		tableAsiakkaat.setAlignmentY(Component.BOTTOM_ALIGNMENT);
		tableAsiakkaat.setAlignmentX(Component.RIGHT_ALIGNMENT);
		tableAsiakkaat.setShowVerticalLines(false);
		tableAsiakkaat.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tableAsiakkaat.setRowSelectionAllowed(true);
		
		// Asetetaan varaston taulukkoon tiedot.
		tableAsiakkaat.setModel(ohjain.getAsiakasTaulu());
		
		tableAsiakkaat.getTableHeader().setReorderingAllowed(false);
		tableAsiakkaat.getTableHeader().setResizingAllowed(false);
		panelAsiakasLista.setLayout(new BorderLayout(0, 0));
		tableAsiakkaat.setShowVerticalLines(false);
		
		scrollPaneAsiakkaat.setViewportView(tableAsiakkaat);
		panelAsiakasLista.add(scrollPaneAsiakkaat);
		
		JPanel paneeliHenkilokunta = new JPanel();
		valilehtiPaneeli.addTab("Henkilökunta", null, paneeliHenkilokunta, null);
		paneeliHenkilokunta.setLayout(new BoxLayout(paneeliHenkilokunta, BoxLayout.Y_AXIS));
		
		JPanel panelHkunta = new JPanel();
		panelHkunta.setAlignmentY(Component.TOP_ALIGNMENT);
		paneeliHenkilokunta.add(panelHkunta);
		panelHkunta.setLayout(new BorderLayout(0, 0));
		
		JPanel panelHkuntaValikko = new JPanel();
		panelHkunta.add(panelHkuntaValikko, BorderLayout.NORTH);
		
		JButton btnIrtisano = new JButton("Irtisano valittu");
		
		// Palkkausnappia painettu.
		btnIrtisano.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				ohjain.irtisanoHenkilo(valittuHenkilokunta);
				
				paivitaHenkilokunta();
				paivitaPalkattavat();
			}
		});
		
		panelHkuntaValikko.add(btnIrtisano);
		
		JScrollPane scrollPaneHKunta = new JScrollPane();
		scrollPaneHKunta.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		panelHkunta.add(scrollPaneHKunta);
		
		tableHenkilokunta = new JTable();
		
		tableHenkilokunta.setAlignmentY(Component.BOTTOM_ALIGNMENT);
		tableHenkilokunta.setAlignmentX(Component.RIGHT_ALIGNMENT);
		tableHenkilokunta.setShowVerticalLines(false);
		tableHenkilokunta.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tableHenkilokunta.setRowSelectionAllowed(true);
		
		// Henkilökuntataulukon valintaa muutettu.
		tableHenkilokunta.getSelectionModel().addListSelectionListener(new ListSelectionListener()
		{
			public void valueChanged(ListSelectionEvent event)
			{
				if (!tableHenkilokunta.getSelectionModel().getValueIsAdjusting())
				{
					if (tableHenkilokunta.getSelectedRow() > -1)
					{
						valittuHenkilokunta = (Henkilokunta) tableHenkilokunta.getValueAt(tableHenkilokunta.getSelectedRow(), 0);
					}
				}
			}
		});
		
		// Asetetaan henkilökunta taulukkoon tiedot.
		tableHenkilokunta.setModel(ohjain.getHKuntaTaulu());
		
		tableHenkilokunta.getTableHeader().setReorderingAllowed(false);
		tableHenkilokunta.getTableHeader().setResizingAllowed(false);
		tableHenkilokunta.setShowVerticalLines(false);
		
		scrollPaneHKunta.setViewportView(tableHenkilokunta);
		
		JPanel panelPalkattava = new JPanel();
		panelPalkattava.setAlignmentY(Component.BOTTOM_ALIGNMENT);
		paneeliHenkilokunta.add(panelPalkattava);
		panelPalkattava.setLayout(new BorderLayout(0, 0));
		
		JPanel panelPalkkaValikko = new JPanel();
		panelPalkattava.add(panelPalkkaValikko, BorderLayout.NORTH);
		panelPalkkaValikko.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JButton btnPalkkaa = new JButton("Palkkaa valittu");
		btnPalkkaa.setAlignmentY(Component.BOTTOM_ALIGNMENT);
		btnPalkkaa.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		// Palkkausnappia painettu.
		btnPalkkaa.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				ohjain.palkkaaHenkilo(valittuPalkattava);
				
				paivitaPalkattavat();
				paivitaHenkilokunta();
			}
		});
		
		panelPalkkaValikko.add(btnPalkkaa);
		
		JScrollPane scrollPanePalkattavat = new JScrollPane();
		scrollPanePalkattavat.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		panelPalkattava.add(scrollPanePalkattavat);
		
		tablePalkattavat = new JTable();
		
		tablePalkattavat.setAlignmentY(Component.BOTTOM_ALIGNMENT);
		tablePalkattavat.setAlignmentX(Component.RIGHT_ALIGNMENT);
		tablePalkattavat.setShowVerticalLines(false);
		tablePalkattavat.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tablePalkattavat.setRowSelectionAllowed(true);
		
		// Palkattavat taulukon valintaa muutettu.
		tablePalkattavat.getSelectionModel().addListSelectionListener(new ListSelectionListener()
		{
			public void valueChanged(ListSelectionEvent event)
			{
				if (!tablePalkattavat.getSelectionModel().getValueIsAdjusting())
				{
					if (tablePalkattavat.getSelectedRow() > -1)
					{
						valittuPalkattava = (Henkilokunta) tablePalkattavat.getValueAt(tablePalkattavat.getSelectedRow(), 0);
					}
				}
			}
		});
		
		// Asetetaan palkattavat taulukkoon tiedot.
		tablePalkattavat.setModel(ohjain.getPalkattavatTaulu());
		
		tablePalkattavat.getTableHeader().setReorderingAllowed(false);
		tablePalkattavat.getTableHeader().setResizingAllowed(false);
		tablePalkattavat.setShowVerticalLines(false);
		
		scrollPanePalkattavat.setViewportView(tablePalkattavat);
		
		JPanel paneeliVarasto = new JPanel();
		valilehtiPaneeli.addTab("Varasto", null, paneeliVarasto, null);
		paneeliVarasto.setLayout(new BorderLayout(0, 0));
		
		JPanel kauppaVarastoKarkit = new JPanel();
		kauppaVarastoKarkit.setAlignmentY(Component.TOP_ALIGNMENT);
		kauppaVarastoKarkit.setAlignmentX(Component.LEFT_ALIGNMENT);
		paneeliVarasto.add(kauppaVarastoKarkit);
		
		JScrollPane scrollPaneKaupanVarastoKarkit = new JScrollPane();
		scrollPaneKaupanVarastoKarkit.setAlignmentY(Component.BOTTOM_ALIGNMENT);
		scrollPaneKaupanVarastoKarkit.setAlignmentX(Component.RIGHT_ALIGNMENT);
		scrollPaneKaupanVarastoKarkit.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		panelVarastoKilohinta = new JPanel();
		panelVarastoKilohinta.setAlignmentX(Component.RIGHT_ALIGNMENT);
		panelVarastoKilohinta.setAlignmentY(Component.TOP_ALIGNMENT);
		panelVarastoKilohinta.setVisible(false);
		kauppaVarastoKarkit.setLayout(new BorderLayout(0, 0));
		kauppaVarastoKarkit.add(panelVarastoKilohinta, BorderLayout.NORTH);
		
		JLabel lblKauppaKarkkiKilohinta = new JLabel("Kilohinta:");
		panelVarastoKilohinta.add(lblKauppaKarkkiKilohinta);
		
		kauppaVarastoKilohintaTekstiloota = new JTextField();
		panelVarastoKilohinta.add(kauppaVarastoKilohintaTekstiloota);
		kauppaVarastoKilohintaTekstiloota.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("€/kg");
		panelVarastoKilohinta.add(lblNewLabel);
		
		JButton btnAsetaKilohinta = new JButton("Aseta");
		panelVarastoKilohinta.add(btnAsetaKilohinta);
		
		// Kilohinta nappia painettu.
		btnAsetaKilohinta.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				ohjain.muutaKaupanKilohinta(kauppaValittuVarastoKarkki, getKirjoitettuKilohinta());
				kauppaVarastoKilohintaTekstiloota.setText("");
				paivitaKaupanVarasto();
			}
		});
		
		tableKaupanVarasto = new JTable();
		tableKaupanVarasto.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tableKaupanVarasto.setRowSelectionAllowed(true);
		
		// Varastotaulukon valintaa muutettu.
		tableKaupanVarasto.getSelectionModel().addListSelectionListener(new ListSelectionListener()
		{
			public void valueChanged(ListSelectionEvent event)
			{
				if (!tableKaupanVarasto.getSelectionModel().getValueIsAdjusting())
				{
					if (tableKaupanVarasto.getSelectedRow() > -1)
					{
						kauppaValittuVarastoKarkki = (KarkkiLaatikko) tableKaupanVarasto.getValueAt(tableKaupanVarasto.getSelectedRow(), 0);
						
						kauppaVarastoKilohintaTekstiloota.setText(ohjain.getKauppaKilohinta(kauppaValittuVarastoKarkki));
					}
				}
			}
		});
		
		// Asetetaan varaston taulukkoon tiedot.
		tableKaupanVarasto.setModel(ohjain.getKauppaKarkkiVarastoTaulu());
		
		tableKaupanVarasto.getTableHeader().setReorderingAllowed(false);
		tableKaupanVarasto.getTableHeader().setResizingAllowed(false);
		tableKaupanVarasto.setShowVerticalLines(false);
		
		// Pitää olla toi ihme setViewportView että ne otsikot näkyy.
		scrollPaneKaupanVarastoKarkit.setViewportView(tableKaupanVarasto);
		
		kauppaVarastoKarkit.add(scrollPaneKaupanVarastoKarkit);
		
		JPanel paneeliTukut = new JPanel();
		valilehtiPaneeli.addTab("Tukut", null, paneeliTukut, null);
		
		// Slideria muutettu.
		class SliderListener implements ChangeListener
		{
			public void stateChanged(ChangeEvent e)
			{
				JSlider slideri = (JSlider) e.getSource();
				
				if (slideri.getValueIsAdjusting())
				{
					tukku0ValitutGrammat = slideri.getValue();
					
					tukku0TextEurot.setText(ohjain.karkkiGrammatHinnaksi(0, tukku0ValittuKarkki, tukku0ValitutGrammat));
					
					tukku0TextGrammat.setText(String.valueOf(tukku0ValitutGrammat));
				}
			}
		}
		paneeliTukut.setLayout(new BorderLayout(0, 0));
		
		JPanel panelKarkkiTukut = new JPanel();
		paneeliTukut.add(panelKarkkiTukut, BorderLayout.NORTH);
		
		tukku0Paneeli = new JPanel();
		panelKarkkiTukut.add(tukku0Paneeli);
		tukku0Paneeli.setAlignmentY(Component.TOP_ALIGNMENT);
		tukku0Paneeli.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		JPanel tukku0NimiPaneeli = new JPanel();
		
		// Hae tukun nimi.
		JLabel tukku0LabelNimi = new JLabel(ohjain.getTukunNimi(0));
		
		tukku0NimiPaneeli.add(tukku0LabelNimi);
		tukku0LabelNimi.setAlignmentY(Component.TOP_ALIGNMENT);
		
		tukku0KarkkiLista = new JComboBox<String>();
		tukku0NimiPaneeli.add(tukku0KarkkiLista);
		tukku0KarkkiLista.setAlignmentX(Component.RIGHT_ALIGNMENT);
		tukku0KarkkiLista.setAlignmentY(Component.TOP_ALIGNMENT);
		
		// Listaa tukun karkit.
		tukku0KarkkiLista.setModel(new DefaultComboBoxModel<String>(ohjain.getTukkuKarkkiLista(0)));
		
		tukku0GrammaPaneeli = new JPanel();
		
		tukku0Slider = new JSlider();
		tukku0GrammaPaneeli.add(tukku0Slider);
		tukku0Slider.setPaintTicks(true);
		tukku0Slider.setPaintLabels(true);
		tukku0Slider.setMaximum(tukku0MaxGrammat);
		tukku0Slider.setValue(0);
		
		tukku0Slider.setLabelTable(null);
		tukku0Slider.setMinorTickSpacing(tukku0MaxGrammat / 4);
		tukku0Slider.setMajorTickSpacing(tukku0MaxGrammat / 2);
		
		tukku0Slider.addChangeListener(new SliderListener());
		
		tukku0TextGrammat = new JTextField("0");
		tukku0GrammaPaneeli.add(tukku0TextGrammat);
		tukku0TextGrammat.setColumns(10);
		
		// Grammoja muutettu.
		
		tukku0TextGrammat.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				if (ohjain.tarkistaGrammat(getKirjoitetutGramma(0), tukku0MaxGrammat))
				{
					tukku0Slider.setValue(getKirjoitetutGramma(0));
					
					tukku0TextEurot.setText(ohjain.karkkiGrammatHinnaksi(0, tukku0ValittuKarkki, getKirjoitetutGramma(0)));
				}
				else
				{
					tukku0Slider.setValue(tukku0MaxGrammat);
					tukku0TextGrammat.setText(String.valueOf(tukku0MaxGrammat));
					tukku0TextEurot.setText(ohjain.karkkiGrammatHinnaksi(0, tukku0ValittuKarkki, tukku0MaxGrammat));
					
				}
			}
		});
		
		JLabel tukku0LabelG = new JLabel("g");
		tukku0GrammaPaneeli.add(tukku0LabelG);
		
		tukku0HintaPaneeli = new JPanel();
		
		tukku0TextEurot = new JTextField();
		tukku0HintaPaneeli.add(tukku0TextEurot);
		tukku0TextEurot.setText("0.00");
		tukku0TextEurot.setColumns(6);
		
		// Euroja muutettu.
		// TODO: On mahdollista yrittää ostaa miljoonalla karkkia vaikka tukussa
		// ei riitä tavara.
		tukku0TextEurot.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				if (ohjain.tarkistaEuromaara(getKirjoitetutEurot(0), tukku0ValittuKarkki))
				{
					double euro = getKirjoitetutEurot(0);
					tukku0TextGrammat.setText(ohjain.eurotGrammoiksi(0, tukku0ValittuKarkki, euro));
					
					tukku0Slider.setValue(getKirjoitetutGramma(0));
				}
			}
		});
		
		JLabel tukku0LabelE = new JLabel("€");
		tukku0HintaPaneeli.add(tukku0LabelE);
		
		JButton tukku0NappiOsta = new JButton("Osta");
		
		// Osta nappia painettu.
		tukku0NappiOsta.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				// TODO: Muuta niin että karkkeja ostetaan euromäärän mukaan, ei
				// grammojen. Raha on tärkeämpää kuin se että saa karkkeja
				// gramman tai kaksi vähemmän kuin mitä laatikossa lukee.
				
				ohjain.ostaKarkki(0, tukku0ValittuKarkki, getKirjoitetutGramma(0));
				
				paivitaTukkuKarkit();
				paivitaKaupanVarasto();
			}
		});
		
		tukku0HintaPaneeli.add(tukku0NappiOsta);
		tukku0Paneeli.setLayout(new BoxLayout(tukku0Paneeli, BoxLayout.Y_AXIS));
		tukku0Paneeli.add(tukku0NimiPaneeli);
		tukku0Paneeli.add(tukku0GrammaPaneeli);
		tukku0Paneeli.add(tukku0HintaPaneeli);
		
		// Listan valintaa muutettu.
		tukku0KarkkiLista.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				tukku0KarkkiListaValintaID = ohjain.valitunKarkinListaID((JComboBox<String>) e.getSource(), 0);
				
				paivitaTukkuLuvut();
			}
		});
		
		JPanel paneeliAutomaatti = new JPanel();
		valilehtiPaneeli.addTab("Automaatti", null, paneeliAutomaatti, null);
		paneeliAutomaatti.setLayout(new BorderLayout());
		
		Automaatti.init(ohjain);
		paneeliAutomaatti.add(Automaatti.automaattipaneeli, BorderLayout.CENTER);
		
		JPanel panelAika = new JPanel();
		ikkunaKarkkikauppa.getContentPane().add(panelAika, BorderLayout.SOUTH);
		
		JLabel lblPaiva = new JLabel("Päivä:");
		
		lblPaivaArvo = new JLabel("0");
		
		JLabel lblTunti = new JLabel("Tunti:");
		
		lblTuntiArvo = new JLabel("0");
		panelAika.add(lblPaiva);
		panelAika.add(lblPaivaArvo);
		panelAika.add(lblTunti);
		panelAika.add(lblTuntiArvo);
		
		// Keskitys.
		ikkunaKarkkikauppa.setLocationRelativeTo(null);
		
		ikkunaKarkkikauppa.setVisible(true);
	}
	
	private double getKirjoitettuMarginaali()
	{
		double luku;
		try
		{
			luku = Double.valueOf(textFieldHintamarginaali.getText());
		} catch (NumberFormatException e)
		{
			return 1.0;
		}
		return Mat.pakotaDesimaalit(2, luku);
	}
	
	private double getKirjoitetutEurot(int tukkuID)
	{
		if (tukkuID == 0)
		{
			double eurot;
			try
			{
				eurot = Double.valueOf(tukku0TextEurot.getText());
			} catch (NumberFormatException e)
			{
				return 0.0;
			}
			
			return Mat.pakotaDesimaalit(2, eurot);
		}
		else
		{
			return 0.0;
		}
	}
	
	/**
	 * Päivittää tukku paneelin kaikkien tukkujen tiedot.
	 *
	 * @see Logiikka
	 */
	public void paivitaTukkuLuvut()
	{
		// TODO: Ottaa huomioon vain tukku 0.
		if (ohjain.getTukunValikoimanKoko(0) != 0) // Kaikki karkit ostettu!
		{
			if (Simu.debug)
				System.out.println("\t- TUKKUJEN LUKUJEN PÄIVITYS -");
			
			// Comboboksissa valittu karkkilaatikko.
			tukku0ValittuKarkki = ohjain.getTukunKarkkiByID(0, tukku0KarkkiListaValintaID);
			
			// Valitun karkkilaatikon koko.
			tukku0MaxGrammat = ohjain.getKarkkilaatikonKoko(0, tukku0ValittuKarkki);
			
			// Sliderit nollaan.
			tukku0Slider.setValue(0);
			
			// Sliderin maksimit karkkilaatikon kokoon.
			tukku0Slider.setMaximum(tukku0MaxGrammat);
			
			// Sliderin labelit pois.
			tukku0Slider.setLabelTable(null);
			
			// Ja uudet labelit takas.
			tukku0Slider.setMinorTickSpacing(tukku0MaxGrammat / 4);
			tukku0Slider.setMajorTickSpacing(tukku0MaxGrammat / 2);
			
			// Aluksi valitaan 0 grammaa.
			tukku0ValitutGrammat = 0;
			tukku0TextEurot.setText("0");
			tukku0TextGrammat.setText("0");
			
			// Listataan kaupan karkkivarasto.
			// kauppaVarastoListaKarkit.setModel(ohjain.getKauppaKarkkiLista());
		}
		else
		{
			poistaTukku(0);
		}
	}
	
	/**
	 * Päivittää tukku paneelin kaikkien tukkujen karkkilistat.
	 *
	 * @see Logiikka
	 */
	public void paivitaTukkuKarkit()
	{
		if (ohjain.getTukunValikoimanKoko(0) != 0) // Kaikki karkit ostettu!
		{
			// Karkkilistat comboboksi.
			tukku0KarkkiLista.setModel(new DefaultComboBoxModel<String>(ohjain.getTukkuKarkkiLista(0)));
			
			paivitaTukkuLuvut();
		}
		else
		{
			poistaTukku(0);
		}
	}
	
	public void paivitaKaupanVarasto()
	{		
		tableKaupanVarasto.setModel(ohjain.getKauppaKarkkiVarastoTaulu());
		
		if (tableKaupanVarasto.getModel().getRowCount() > 0)
			panelVarastoKilohinta.setVisible(true);
		else
			panelVarastoKilohinta.setVisible(false);
	}
	
	private double getKirjoitettuKilohinta()
	{
		double luku;
		
		try
		{
			luku = Double.valueOf(kauppaVarastoKilohintaTekstiloota.getText());
		} catch (NumberFormatException e)
		{
			return 0.0;
		}
		return Mat.pakotaDesimaalit(2, luku);
	}
	
	private void poistaTukku(final int tukkuID)
	{
		if (tukkuID == 0)
		{
			tukku0KarkkiLista.setModel(new DefaultComboBoxModel<String>(ohjain.getTukkuKarkkiLista(tukkuID)));
			tukku0Paneeli.remove(tukku0GrammaPaneeli);
			tukku0Paneeli.remove(tukku0HintaPaneeli);
		}
	}
	
	private int getKirjoitetutGramma(final int tukkuID)
	{
		// System.out.println("Dodiih eli grammat lootassa lukee " +
		// tukku0TextGrammat.getText() + " mikä on Integerin mukaan " +
		// Integer.valueOf(tukku0TextGrammat.getText()));
		if (tukkuID == 0)
		{
			int luku;
			
			try
			{
				luku = Integer.valueOf(tukku0TextGrammat.getText());
			} catch (NumberFormatException e)
			{
				return 0;
			}
			return luku;
		}
		else
		{
			return 0;
		}
	}
	
	/**
	 * Asetetaan karkkilistan valinnan ID ohjaimesta käsin.
	 *
	 * @param tukkuID
	 *            0-2
	 * @param valintaID
	 *            Valitun karkin comboboksin valinnan ID.
	 */
	public void asetaKarkkiListanValinta(final int tukkuID, final int valintaID)
	{
		if (tukkuID == 0)
		{
			tukku0KarkkiListaValintaID = valintaID;
		}
	}
	
	public void lisaaTunti()
	{
		lblTuntiArvo.setText(String.valueOf(Simu.tunti));
	}
	
	public void lisaaPaiva()
	{
		lblTuntiArvo.setText(String.valueOf(Simu.tunti));
		lblPaivaArvo.setText(String.valueOf(Simu.paiva));
	}
	
	public void paivitaAsiakkaat()
	{		
		tableAsiakkaat.setModel(ohjain.getAsiakasTaulu());
	}
	
	public void paivitaHenkilokunta()
	{		
		tableHenkilokunta.setModel(ohjain.getHKuntaTaulu());
	}
	
	public void paivitaPalkattavat()
	{
		tablePalkattavat.setModel(ohjain.getPalkattavatTaulu());
	}
	
	public void lisaaMinuutti()
	{
		paivitaAsiakkaat();
	}
	
	public void infoLoota(final String otsikko, final String viesti)
	{
		JOptionPane.showMessageDialog(null, viesti, otsikko, JOptionPane.INFORMATION_MESSAGE);
	}
	
	public void varoitusLoota(final String otsikko, final String viesti)
	{
		JOptionPane.showMessageDialog(null, viesti, otsikko, JOptionPane.WARNING_MESSAGE);
	}
}
