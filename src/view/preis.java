package view;

import java.awt.BorderLayout;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.awt.event.ActionEvent;
import java.sql.*;
import java.util.ArrayList;
import java.awt.Font;
import javax.swing.JComboBox;

public class preis extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	JLabel artname;
	JLabel paf;
	JLabel r2;
	JLabel pelas;
	JLabel pelas2;
	JLabel month;
	JButton btnVisuelleDarstellung;
	JComboBox comboBox;
	double [][] data;
	double abs;
	double stg1;
	double stg2;
	double stg3;
	JLabel prize ;
	JLabel menge;
	ArrayList<Artikel> artikel= new ArrayList<Artikel>();
	SimpleRegression reg= new SimpleRegression();
	 static int [] p = new int [16];
	    static int [] m = new int [16];
	    ArrayList<Coefficient> coef = new ArrayList<Coefficient>();
	private JTextField textField_1;
	private JTextField textField_2;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					 
				   	preis frame = new preis();
				   	frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public preis() {
		//s
		setTitle("Preisabsatzfunktion + Bachelorprojekt");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 799, 475);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Artikel Suchen:");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel.setBounds(20, 22, 130, 20);
		contentPane.add(lblNewLabel);
		
		textField = new JTextField();
		textField.setBounds(20, 79, 195, 20);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JLabel lblArtikelnamenEingeben = new JLabel("EAN-Nummer eingeben:");
		lblArtikelnamenEingeben.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblArtikelnamenEingeben.setBounds(20, 53, 151, 14);
		contentPane.add(lblArtikelnamenEingeben);
		
		JButton btnNewButton = new JButton("Suchen");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
					artname.setVisible(false);
					paf.setVisible(false);
					prize.setText("0.00");
					menge.setText("0");
					r2.setVisible(false);
					pelas.setVisible(false);
					pelas2.setVisible(false);
					btnVisuelleDarstellung.setEnabled(false);
					month.setVisible(false);
					table(textField.getText(),(String)comboBox.getSelectedItem());
			}
		});
		btnNewButton.setBounds(227, 107, 89, 23);
		contentPane.add(btnNewButton);
		
		JLabel lblArtikel = new JLabel("Artikel:");
		lblArtikel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblArtikel.setBounds(20, 153, 46, 14);
		contentPane.add(lblArtikel);
		
		artname = new JLabel("New label");
		artname.setFont(new Font("Tahoma", Font.PLAIN, 12));
		artname.setBounds(76, 153, 240, 14);
		contentPane.add(artname);
		artname.setVisible(false);
		
		JLabel lblNewLabel_1 = new JLabel("Preisabsatzfunktion:");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblNewLabel_1.setBounds(20, 194, 130, 14);
		contentPane.add(lblNewLabel_1);
		

		paf = new JLabel("New label");
		paf.setFont(new Font("Tahoma", Font.PLAIN, 13));
		paf.setBounds(20, 237, 405, 14);
		contentPane.add(paf);
		paf.setVisible(false);
		
		JLabel lblNewLabel_2 = new JLabel("Preis bestimmen:");
		lblNewLabel_2.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel_2.setBounds(450, 37, 143, 14);
		contentPane.add(lblNewLabel_2);
		
		JLabel lblMengeEingeben = new JLabel("Menge eingeben:");
		lblMengeEingeben.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblMengeEingeben.setBounds(450, 82, 118, 14);
		contentPane.add(lblMengeEingeben);
		
		textField_1 = new JTextField();
		textField_1.setBounds(556, 79, 112, 20);
		contentPane.add(textField_1);
		textField_1.setColumns(10);
		
		JLabel lblPreis = new JLabel("Preis:");
		lblPreis.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblPreis.setBounds(450, 118, 94, 14);
		contentPane.add(lblPreis);
		
		prize= new JLabel("0.00");
		prize.setBounds(556, 119, 46, 14);
		contentPane.add(prize);
		
		JButton btnNewButton_1 = new JButton("Ermitteln");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				prize.setText("0.00");
				pelas2.setVisible(false);
				if(textField_1.getText().length()>0)
				prize.setText(""+getPreis(textField_1.getText())+"");
			}
		});
		btnNewButton_1.setBounds(678, 78, 89, 23);
		contentPane.add(btnNewButton_1);
		

		btnVisuelleDarstellung = new JButton("Visuelle Darstellung");
		btnVisuelleDarstellung.setBounds(122, 361, 151, 23);
		btnVisuelleDarstellung.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			 	
			
				PriceEstimator demo;
				try {
    				demo = new PriceEstimator(coef, artikel,abs,stg1);
					demo.pack();
   				demo.setVisible(true);
					demo.drawRegressionLine();
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
			}
		});
		
		contentPane.add(btnVisuelleDarstellung);
		btnVisuelleDarstellung.setEnabled(false);
		
		JLabel lblAbsatzmengeBestimmen = new JLabel("Absatzmenge bestimmen:");
		lblAbsatzmengeBestimmen.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblAbsatzmengeBestimmen.setBounds(450, 193, 201, 14);
		contentPane.add(lblAbsatzmengeBestimmen);
		
		JLabel lblPreisEingeben = new JLabel("Preis eingeben:");
		lblPreisEingeben.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblPreisEingeben.setBounds(450, 242, 99, 14);
		contentPane.add(lblPreisEingeben);
		
		textField_2 = new JTextField();
		textField_2.setColumns(10);
		textField_2.setBounds(556, 240, 112, 20);
		contentPane.add(textField_2);
		
		JButton button = new JButton("Ermitteln");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				pelas.setVisible(false);
				menge.setText("0");
				if(textField_2.getText().length()!=0)
			 menge.setText(""+getMenge(textField_2.getText())+"");
			}
		});
		button.setBounds(678, 239, 89, 23);
		contentPane.add(button);
		
		JLabel lblMenge = new JLabel("Menge:");
		lblMenge.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblMenge.setBounds(450, 284, 94, 14);
		contentPane.add(lblMenge);
		
		menge = new JLabel("0");
		menge.setFont(new Font("Tahoma", Font.PLAIN, 12));
		menge.setBounds(556, 284, 112, 14);
		contentPane.add(menge);
		
		JLabel lblR = new JLabel("R\u00B2:");
		lblR.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblR.setBounds(20, 284, 46, 14);
		contentPane.add(lblR);
		
		r2= new JLabel("New label");
		r2.setBounds(76, 278, 285, 20);
		contentPane.add(r2);
		r2.setVisible(false);
		
		JLabel lblPreiselastizitt = new JLabel("Preiselastizit\u00E4t:");
		lblPreiselastizitt.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblPreiselastizitt.setBounds(450, 322, 94, 14);
		contentPane.add(lblPreiselastizitt);
		
		pelas = new JLabel("New label");
		pelas.setFont(new Font("Tahoma", Font.PLAIN, 12));
		pelas.setBounds(556, 322, 95, 14);
		contentPane.add(pelas);
		pelas.setVisible(false);
		
		JLabel lblNewLabel_3 = new JLabel("Quartal:");
		lblNewLabel_3.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblNewLabel_3.setBounds(20, 110, 76, 14);
		contentPane.add(lblNewLabel_3);
		
		String [] quart= {"1.Quartal","2.Quartal","3.Quartal","4.Quartal"};
		comboBox = new JComboBox<String>(quart);
		comboBox.setBounds(101, 110, 114, 20);
		contentPane.add(comboBox);
		
		JButton btnSuchen = new JButton("Suchen");
		btnSuchen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				artname.setVisible(false);
				paf.setVisible(false);
				prize.setText("0.00");
				menge.setText("0");
				r2.setVisible(false);
				pelas.setVisible(false);
				pelas2.setVisible(false);
				btnVisuelleDarstellung.setEnabled(false);
				month.setVisible(false);
				table(textField.getText(),"normal");	
				}
			
		});
		btnSuchen.setBounds(227, 78, 89, 23);
		contentPane.add(btnSuchen);
		
		JLabel lblMonat = new JLabel("Monat:");
		lblMonat.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblMonat.setBounds(20, 322, 46, 14);
		contentPane.add(lblMonat);
		
		month = new JLabel("New label");
		month.setBounds(76, 322, 95, 14);
		contentPane.add(month);
		month.setVisible(false);
		
		JLabel label = new JLabel("Preiselastizit\u00E4t:");
		label.setFont(new Font("Tahoma", Font.PLAIN, 12));
		label.setBounds(450, 153, 94, 14);
		contentPane.add(label);
		
		pelas2 = new JLabel("New label");
		pelas2.setFont(new Font("Tahoma", Font.PLAIN, 12));
		pelas2.setBounds(556, 153, 94, 14);
		contentPane.add(pelas2);
		pelas2.setVisible(false);
		
		
		
	}
	


	public void  table(String s,String q){
		artikel.clear();
		if(s.length()!=0&&q.length()!=0){
		boolean a=true;
		long id=0;
		try{
		 id=Long.parseLong(s);
		}
		catch(NumberFormatException e){
			a=false;
		}
		if(a!=false)
		{ 
//			int i=0;
			connection c=new connection();

				  c.getconnection(id,q);
				  artikel = c.artikel;
				   for(int m=0 ; m<c.c1.size();m++){
					   System.out.println("CID: " + c.c1.get(m).coefID + " " + "CN :" + c.c1.get(m).coefname +"\n");
				   }
				   for(int n=0 ; n< c.s1.size();n++){
					   System.out.println("STN: " + c.s1.get(n).StatN + " "+ "StV: " + c.s1.get(n).StatV +"\n");

				   }
				   artname.setText(c.aname);
				   artname.setVisible(true);

			
//			while (rs.next())
//			{   //if(i==0){
//				//artname.setText(rs.getString(5));
//				//artname.setVisible(true);
//				//i++;}
//				Artikel art= new Artikel(rs.getDouble("preis"),rs.getInt("menge"));
//				if(artikel.isEmpty()){
//				artikel.add(art);
//				}
//				else{
//					boolean da=false;
//					for(int j=0; j<artikel.size();j++)
//					{
//						if(artikel.get(j).preis==art.preis)
//						{ 
//							artikel.get(j).menge+=art.menge;
//							da=true;
//							break;
//						}
//					}
//					if(da==false)
//					artikel.add(art);
//					
//				}
//			}
//			conn.close();
//			
//			data= new double [artikel.size()][2];
//				for(int k=0;k<artikel.size();k++)
//				{
//					data[k][0]=artikel.get(k).preis;
//					data[k][1]=artikel.get(k).menge;
//					
//				}
				 if(c.ok==0){
					 if(c.monat!=0)
					 { month.setText(""+c.monat+"");
					    month.setVisible(true);
					 }
				this.regression(c.c1,c.s1);
				btnVisuelleDarstellung.setEnabled(true);
				 }
			
}
		else{
			JOptionPane.showMessageDialog(null,"Bitte geben sie nur ganzahlige Zahlen ein","Fehler",JOptionPane.ERROR_MESSAGE);
		}
		}
	
}
	
	public void regression(ArrayList<Coefficient> cof,ArrayList<Statics> stat){
		  coef = cof;

//		reg.addData(data);
		abs= Math.round(cof.get(0).coefID*100.0)/100.0;
		stg1= Math.round(cof.get(1).coefID*100.0)/100.0;
//		stg2= Math.round(cof.get(2).coefID*100.0)/100.0;
//		stg3= Math.round(cof.get(3).coefID*100.0)/100.0;
		System.out.println(+abs+" "+stg1);
		if(stg1>=0)
		{
			if(stg1>=0)
				paf.setText("q(p)= " + abs + " + " +stg1+ "*p"); 	
			if(stg1<0)
				paf.setText("q(p)= " + abs + " " +stg1+ "*p"); 
//			if(stg1<0)
//				paf.setText("q(p)= " + abs + " " +stg1+ "*p"); 
//			if(stg1>=0&&stg2>=0&&stg3<0)
//				paf.setText("q(p)= " + abs + " + " +stg1+ "*p+ "); 
//			if(stg1>=0&&stg2<0&&stg3>=0)	
//				paf.setText("q(p)= " + abs + " + " +stg1+ "*p"); 
//			if(stg1<0&&stg2>=0&&stg3>=0)
//				paf.setText("q(p)= " + abs + " " +stg1+ "*p"); 
//			if(stg1>=0&&stg2>=0&&stg3>=0)
//				paf.setText("q(p)= " + abs + " + " +stg1+ "*p"); 
		
		}
		else{
		paf.setText("q(p)= " + abs + " " +stg1+ "*p "); 
		}
		
		double best=stat.get(0).StatV;
		r2.setText(" "+best+" ");
		r2.setVisible(true);
		paf.setVisible(true);
		
		
		
	}
	
public void regression2(ArrayList<Coefficient> cof,ArrayList<Statics> stat){
	  coef = cof;
		
//		reg.addData(data);
		abs= Math.round(cof.get(0).coefID*100.0)/100.0;
		stg1= Math.round(cof.get(1).coefID*100.0)/100.0;
		stg2= Math.round(cof.get(2).coefID*100.0)/100.0;
		stg3= Math.round(cof.get(3).coefID*100.0)/100.0;
		System.out.println(+abs+" "+stg1+ " " +stg2+" "+stg3);
		if(stg1>=0||stg2>=0||stg3>=0)
		{
			if(stg1>=0&&stg2<0&&stg3<0)
				paf.setText("q(p)= " + abs + " + " +stg1+ "*p "+ stg2+ "*p� " + stg3+ "*p�"); 	
			if(stg1<0&&stg2>=0&&stg3<0)
				paf.setText("q(p)= " + abs + " " +stg1+ "*p+ "+ stg2+ "*p� " + stg3+ "*p�"); 
			if(stg1<0&&stg2<0&&stg3>=0)
				paf.setText("q(p)= " + abs + " " +stg1+ "*p "+ stg2+ "*p�+ " + stg3+ "*p�"); 
			if(stg1>=0&&stg2>=0&&stg3<0)
				paf.setText("q(p)= " + abs + " + " +stg1+ "*p+ "+ stg2+ "*p� " + stg3+ "*p�"); 
			if(stg1>=0&&stg2<0&&stg3>=0)	
				paf.setText("q(p)= " + abs + " + " +stg1+ "*p "+ stg2+ "*p�+ " + stg3+ "*p�"); 
			if(stg1<0&&stg2>=0&&stg3>=0)
				paf.setText("q(p)= " + abs + " " +stg1+ "*p+ "+ stg2+ "*p�+ " + stg3+ "*p�"); 
			if(stg1>=0&&stg2>=0&&stg3>=0)
				paf.setText("q(p)= " + abs + " + " +stg1+ "*p+ "+ stg2+ "*p�+ " + stg3+ "*p�+"); 
		
		}
		else{
		paf.setText("q(p)= " + abs + " " +stg1+ "*p "+ stg2+ "*p� " + stg3+ "*p�"); 
		}
		paf.setVisible(true);
		
		double best=Math.round(stat.get(0).StatV*100000.0)/100000.0;
		r2.setText(" "+best+" ");
		r2.setVisible(true);
		
	}
	public double getPreis(String menge){
		double p=0.00;
		boolean a=true;
		int m=0;
		try{
		m=Integer.parseInt(menge);
		}catch (NumberFormatException e)
		{
			a=false;
			JOptionPane.showMessageDialog(null,"Bitte geben sie nur ganzahlige Zahlen ein","Fehler",JOptionPane.ERROR_MESSAGE);
			
		}
		if(a!=false){
			if(m>0){
			double i= (m-abs)/stg1; // i �ndern um PAF
			p = Math.round(i*100.0)/100.0;	
			if(p<0)
			{p=Math.round(0.00*100.0)/100.0;
			 JOptionPane.showMessageDialog(null,"Der eingegebene Menge w�rde eine negativen Preis liefern!","Fehler",JOptionPane.ERROR_MESSAGE);
			}
			else
				 pelasticity2(p,m,1);
			}
			else
				JOptionPane.showMessageDialog(null,"Bitte geben sie nur ganzahlige Zahlen ein","Fehler",JOptionPane.ERROR_MESSAGE);
				
		}
		return p;
	}

	public long getMenge(String s){
		boolean a=true;
		long m=0;
		double p=0.00;
		try{
			p= Double.parseDouble(s);
		}catch (NumberFormatException e)
		{
			a=false;
			JOptionPane.showMessageDialog(null,"Bitte geben sie nur Preise mit ('.') ein!","Fehler",JOptionPane.ERROR_MESSAGE);
			
		}
		if(a!=false){
			String k=s.substring(s.indexOf(".")+1);
			if(k.length()>2 && !s.equals(k))
				JOptionPane.showMessageDialog(null,"Bitte geben sie nur Zahlen mit zwei Nachkommastellen ein!","Fehler",JOptionPane.ERROR_MESSAGE);
			else{	
				if(p>0){
					m = (long) (abs+stg1*p+stg2*p*p+stg3*p*p*p);// i �ndern um PAF
					if(m<0){
						m=0;
						JOptionPane.showMessageDialog(null,"Der eingegebene Preis w�rde eine negative Menge liefern!","Fehler",JOptionPane.ERROR_MESSAGE);
					}
					else
						 pelasticity(p,m,2);
				}
				else
					JOptionPane.showMessageDialog(null,"Bitte geben sie keine negativen Preise ein!","Fehler",JOptionPane.ERROR_MESSAGE);
			}
		}
		return m;
	}
	public long getMenge2(String s){
		boolean a=true;
		long m=0;
		double p=0.00;
		try{
			p= Double.parseDouble(s);
		}catch (NumberFormatException e)
		{
			a=false;
			JOptionPane.showMessageDialog(null,"Bitte geben sie nur Preise mit ('.') ein!","Fehler",JOptionPane.ERROR_MESSAGE);
			
		}
		if(a!=false){
			String k=s.substring(s.indexOf(".")+1);
			if(k.length()>2 && !s.equals(k))
				JOptionPane.showMessageDialog(null,"Bitte geben sie nur Zahlen mit zwei Nachkommastellen ein!","Fehler",JOptionPane.ERROR_MESSAGE);
			else{	
				if(p>0){
					m = (long) (abs+stg1*p);// i �ndern um PAF
					if(m<0){
						m=0;
						JOptionPane.showMessageDialog(null,"Der eingegebene Preis w�rde eine negative Menge liefern!","Fehler",JOptionPane.ERROR_MESSAGE);
					}
					else
						 pelasticity2(p,m,2);
				}
				else
					JOptionPane.showMessageDialog(null,"Bitte geben sie keine negativen Preise ein!","Fehler",JOptionPane.ERROR_MESSAGE);
			}
		}
		return m;
	}
	
	public void pelasticity(double p,long m,int w){
	    double abl= stg1+stg2*2*p+stg3*3*p*p;
	    double pez= Math.abs(abl*(p/m));
	    double pe=Math.round(pez*100.0)/100.0;
	    
	    
	    pelas.setText(""+pe+"");
	    pelas.setVisible(true);
	    
	    
		
		
	}
	public void pelasticity2(double p,long m,int w){
	   double abl= stg1;
	    double pez= Math.abs(abl*(p/m));
	    double pe=Math.round(pez*100.0)/100.0;
	   
	    if(w==1){
		    pelas2.setText(""+pe+"");
		    pelas2.setVisible(true);
		   }
		     
	   if(w==2){
	    pelas.setText(""+pe+"");
	    pelas.setVisible(true);
	   }
	    
		
	}
}
