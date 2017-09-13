package view;
import com.sap.db.jdbc.Driver;
import java.sql.*;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import java.sql.CallableStatement;

public class connection {
String url="jdbc:sap://132.252.53.6:39015/?autocommit=false";
String user="BPSS1703";
String password="Han56%1!";
Connection conn;
int ok=0;
//String driverName = "jdbc:sap://132.252.53.6:39015";
ArrayList<Coefficient> c1 = new ArrayList<Coefficient>();
ArrayList<Statics> s1 = new ArrayList<Statics>();
ArrayList<Artikel> artikel= new ArrayList<Artikel>();

String aname="";

public Connection getconnection(long ean,String quartal) 
{

	try {
	    conn = (Connection)DriverManager.getConnection(url,user,password);
			Statement s= conn.createStatement();
			ResultSet r=s.executeQuery("SELECT count(artikelbezeichnung) as anzahl,artikelbezeichnung from EDEKA1.BONS where EAN='"+ean+"' group by artikelbezeichnung");
			int count=0;
			while(r.next())
			{	
				if(count<r.getInt("anzahl")){
				aname=r.getString("artikelbezeichnung");
				count=r.getInt("anzahl");
				}
			}
			s.execute("SET SCHEMA BPSS1703");
			
			//s.execute("DROP TABLE #PAL_PARAMETER_TBL");
			String sql = "CREATE LOCAL TEMPORARY COLUMN TABLE #PAL_PARAMETER_TBL (PARAM_NAME VARCHAR(256), INT_VALUE INTEGER, DOUBLE_VALUE DOUBLE, STRING_VALUE VARCHAR(1000))"; 
             s.executeUpdate(sql);
			
            PreparedStatement p = conn.prepareStatement("INSERT INTO #PAL_PARAMETER_TBL VALUES ('POLYNOMIAL_NUM',1,NULL,NULL)");
            p.execute();
            System.out.println("Insert 1");
            PreparedStatement p1 = conn.prepareStatement("INSERT INTO #PAL_PARAMETER_TBL VALUES ('PMML_EXPORT',2,NULL,NULL)");
            p1.execute();
            System.out.println("Insert 2");
            s.execute("DROP TABLE PAL_PR_DATA_TBL");
        	String sql1 = "CREATE COLUMN TABLE PAL_PR_DATA_TBL ( ID INT GENERATED BY DEFAULT AS IDENTITY,Y DOUBLE,X1 DOUBLE)";
            s.executeUpdate(sql1);
            PreparedStatement p3=null;
            if(quartal.equals("1.Quartal"))
            p3 = conn.prepareStatement("INSERT INTO BPSS1703.PAL_PR_DATA_TBL (Y,X1) select sum(menge),t1.epreis from (select ean,menge,preis,preis/menge as epreis  from EDEKA1.BONS  WHERE month(timestamp)=1 or month(timestamp)=2 or month(timestamp)=3) as t1 where ean='"+ean+"' group by t1.epreis");
            if(quartal.equals("2.Quartal"))
            p3 = conn.prepareStatement("INSERT INTO BPSS1703.PAL_PR_DATA_TBL (Y,X1) select sum(menge),t1.epreis from (select ean,menge,preis,preis/menge as epreis  from EDEKA1.BONS  WHERE month(timestamp)=4 or month(timestamp)=5 or month(timestamp)=6) as t1 where ean='"+ean+"' group by t1.epreis");	
            if(quartal.equals("3.Quartal"))
            p3 = conn.prepareStatement("INSERT INTO BPSS1703.PAL_PR_DATA_TBL (Y,X1) select sum(menge),t1.epreis from (select ean,menge,preis,preis/menge as epreis  from EDEKA1.BONS  WHERE month(timestamp)=7 or month(timestamp)=8 or month(timestamp)=9) as t1 where ean='"+ean+"' group by t1.epreis");	
            if(quartal.equals("4.Quartal"))
            p3 = conn.prepareStatement("INSERT INTO BPSS1703.PAL_PR_DATA_TBL (Y,X1) select sum(menge),t1.epreis from (select ean,menge,preis,preis/menge as epreis  from EDEKA1.BONS  WHERE month(timestamp)=10 or month(timestamp)=11 or month(timestamp)=12) as t1 where ean='"+ean+"' group by t1.epreis");
            p3.execute();
            System.out.println("Insert3");
            ResultSet r3 = s.executeQuery("Select * from BPSS1703.PAL_PR_DATA_TBL;");
            int row=0;
            while (r3.next()){
            	row++;
            }
            if(row>=2){
            CallableStatement s1 = (CallableStatement) conn.prepareCall("CALL _SYS_AFL.PAL_POLYNOMIAL_REGRESSION(PAL_PR_DATA_TBL, '#PAL_PARAMETER_TBL', ?, ?, ?, ?,?)");
            boolean cst =s1.execute();
            int index =0;
            while (cst) {
            	index++;
                ResultSet rs = s1.getResultSet();
    			this.bearbeiteRes(rs,index);

                // process result set

                cst = s1.getMoreResults();
            }
            }
            else{
            	JOptionPane.showMessageDialog(null,"Preisabsatzfunktion kann nicht bestimmt werden!","Fehler",JOptionPane.ERROR_MESSAGE);
            ok=1;
            }
			System.out.println("PrepareCall");
			Statement s2 = conn.createStatement();
            ResultSet d2 = s2.executeQuery("Select * from BPSS1703.PAL_PR_DATA_TBL;");
            this.bearbeiteRes(d2,0);
            while (d2.next()){
            	System.out.println(d2.getDouble("y"));
            }

			conn.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null,"Die EAN-Nr. existiert nicht!","Fehler",JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
			try {
				conn.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
	
        System.out.println("Done");
        
        
        

	return conn;
}
	public void bearbeiteRes(ResultSet r1, int ind) throws SQLException{
		int anzahl = 0;
		while(r1.next()){
	      if (ind ==1){
	    	  Coefficient co1 = new Coefficient();
	    	  co1.coefID = r1.getDouble("COEFFICIENT_VALUE");
	    	  co1.coefname = r1.getString("VARIABLE_NAME");
	    	  c1.add(co1);
				anzahl ++;

	      }
	      
	      else if(ind ==4){
	    	    Statics st1  =new Statics();
	    	    st1.StatN = r1.getString("STAT_NAME");
	    	    st1.StatV = r1.getDouble("STAT_VALUE");
	    	    

 	    	    s1.add(st1);
				anzahl ++;

	      }
	      else if (ind ==0){
	    	   
	    	  while(r1.next()){
	    		  double x =r1.getDouble("X1");
	    		  double y =r1.getDouble("Y");
	    		  Artikel lA  = new Artikel(x,y);
	    		  artikel.add(lA);
	    		  System.out.println("X: "+ x + "Y : "+y);
	    		  
	    		  anzahl++;
	    		  


	    	  }
	      }
	      else{
	    		anzahl++;  
	      }
		}
			System.out.println("Inputable :"+ anzahl);
//	
		// Diskutieren �ber linear und polynom
		// Diskutieren dass bei geringerem preise weniger verkauft wird
	
	}
}



	

