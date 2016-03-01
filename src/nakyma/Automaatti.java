package nakyma;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import ohjain.Simu;
import java.awt.FlowLayout;
import javax.swing.JLabel;

/**
 * Maksuautomaatti.
 */
public class Automaatti
{
	private static Simu ohjain;
	private static JTextField automaatinnaytto;
	private static String PINCODE = "";
	private static JLabel lblAutomaatti;
	public static JPanel automaattipaneeli = new JPanel();
	private static JButton btnMaksaLaina;
	
	/**
	 * Initialize the contents of the frame.
	 * 
	 * @param ohjain
	 * @wbp.parser.entryPoint
	 */
	public static void init(Simu simu)
	{
		ohjain = simu;
		automaattipaneeli.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JPanel panelSisa = new JPanel();
		automaattipaneeli.add(panelSisa);
		panelSisa.setLayout(new BoxLayout(panelSisa, BoxLayout.Y_AXIS));
		
		JPanel naytto = new JPanel();
		panelSisa.add(naytto);
		
		lblAutomaatti = new JLabel("PIN:");
		naytto.add(lblAutomaatti);
		
		automaatinnaytto = new JTextField();
		naytto.add(automaatinnaytto);
		automaatinnaytto.setEditable(false);
		automaatinnaytto.setColumns(12);
		
		JPanel panel_3 = new JPanel();
		panelSisa.add(panel_3);
		
		JButton b7 = new JButton("7");
		b7.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				
				PINCODE = PINCODE.concat("7");
				automaatinnaytto.setText(PINCODE);
				lblAutomaatti.setText("PIN:");
				
			}
		});
		panel_3.add(b7);
		
		JButton b8 = new JButton("8");
		b8.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				
				PINCODE = PINCODE.concat("8");
				automaatinnaytto.setText(PINCODE);
				lblAutomaatti.setText("PIN:");
				
			}
		});
		panel_3.add(b8);
		
		JButton b9 = new JButton("9");
		b9.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				
				PINCODE = PINCODE.concat("9");
				automaatinnaytto.setText(PINCODE);
				lblAutomaatti.setText("PIN:");
				
			}
		});
		panel_3.add(b9);
		
		JPanel panel_2 = new JPanel();
		panelSisa.add(panel_2);
		
		JButton b4 = new JButton("4");
		b4.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				
				PINCODE = PINCODE.concat("4");
				automaatinnaytto.setText(PINCODE);
				lblAutomaatti.setText("PIN:");
				
			}
		});
		panel_2.add(b4);
		
		JButton b5 = new JButton("5");
		b5.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				
				PINCODE = PINCODE.concat("5");
				automaatinnaytto.setText(PINCODE);
				lblAutomaatti.setText("PIN:");
				
			}
		});
		panel_2.add(b5);
		
		JButton b6 = new JButton("6");
		b6.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				
				PINCODE = PINCODE.concat("6");
				automaatinnaytto.setText(PINCODE);
				lblAutomaatti.setText("PIN:");
				
			}
		});
		panel_2.add(b6);
		
		JPanel panel_1 = new JPanel();
		panelSisa.add(panel_1);
		
		JButton b1 = new JButton("1");
		b1.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				
				PINCODE = PINCODE.concat("1");
				automaatinnaytto.setText(PINCODE);
				lblAutomaatti.setText("PIN:");
				
			}
		});
		panel_1.add(b1);
		
		JButton b2 = new JButton("2");
		b2.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				
				PINCODE = PINCODE.concat("2");
				automaatinnaytto.setText(PINCODE);
				lblAutomaatti.setText("PIN:");
				
			}
		});
		panel_1.add(b2);
		
		JButton b3 = new JButton("3");
		b3.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				
				PINCODE = PINCODE.concat("3");
				automaatinnaytto.setText(PINCODE);
				lblAutomaatti.setText("PIN:");
				
			}
		});
		panel_1.add(b3);
		
		JPanel panel_5 = new JPanel();
		panelSisa.add(panel_5);
		
		JButton b0 = new JButton("0");
		b0.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				
				PINCODE = PINCODE.concat("0");
				automaatinnaytto.setText(PINCODE);
				lblAutomaatti.setText("PIN:");
				
			}
		});
		panel_5.add(b0);
		
		JPanel panel_4 = new JPanel();
		panelSisa.add(panel_4);
		
		JButton bpyyhi = new JButton("<-PYYHI");
		bpyyhi.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				PINCODE = "";
				automaatinnaytto.setText(PINCODE);
				lblAutomaatti.setText("PIN:");
				btnMaksaLaina.setVisible(false);
				
			}
		});
		panel_4.add(bpyyhi);
		
		JButton bok = new JButton("OK");
		bok.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				int pincode = 0;
				boolean oikeakoodi = false;
				String talteen = automaatinnaytto.getText();
				
				if (talteen.isEmpty() == true || talteen.equals("Error") == true)
				{
					automaatinnaytto.setText("Error");
					btnMaksaLaina.setVisible(false);
				}
				else
				{
					try
					{
						pincode = Integer.parseInt(talteen);
					} catch (NumberFormatException ex)
					{
						automaatinnaytto.setText("Error");
						btnMaksaLaina.setVisible(false);
					}
					
					Simu.tarkistaPIN(pincode);
					oikeakoodi = Simu.tarkistaPIN(pincode);
					PINCODE = "";
					automaatinnaytto.setText(PINCODE);
				}
				if (oikeakoodi == true)
				{
					lblAutomaatti.setText("Pankkitili:");
					automaatinnaytto.setText(Simu.getKaupanRaha() + "€");
					
					if (ohjain.josLaina())
						btnMaksaLaina.setVisible(true);
				}
				else
				{
					automaatinnaytto.setText("Error");
					btnMaksaLaina.setVisible(false);
				}
				
			}
		});
		panel_4.add(bok);
		
		btnMaksaLaina = new JButton("Maksa laina");
		btnMaksaLaina.setVisible(false);
		
		btnMaksaLaina.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				
				if (ohjain.maksaLaina())
					PINCODE = "Maksettu";
				else
					PINCODE = "Ei maksettu";
				
				automaatinnaytto.setText(PINCODE);
				btnMaksaLaina.setVisible(false);
				lblAutomaatti.setText("PIN:");
			}
		});
		
		panel_4.add(btnMaksaLaina);
	}
	
}
