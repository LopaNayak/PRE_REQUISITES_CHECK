
//RunDB
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
public class Login extends JFrame{
    Connection connection;
   // Statement statement;
    Registration_form rf;
    JTextField name;
    JTextField pass;
    JTextField maiden;
    JLabel l_name;
    JLabel l_pass;
    JLabel l_maiden;
    public Login(){
        super("Login");
        setBounds(100,100,400,300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        l_name = new JLabel ("User ID :");
        l_pass = new JLabel ("Password :");
        l_maiden = new JLabel("Mother's Maiden Name :");
        name =  new JTextField ( "",20);
        pass = new JTextField("",15);
        maiden = new JTextField("", 15);
        JButton button = new JButton("Submit ");
        // Adding the buttons to container
        Container c = getContentPane();
        c.setSize(10000,10000);
        c.setLayout(new FlowLayout(FlowLayout.LEFT,40,40));
        c.add(l_name);
        c.add(name);
        c.add(l_pass);
        c.add(pass);
        c.add(l_maiden);
        c.add(maiden);
        c.add(button);
        setVisible(true);
        button.addActionListener(new submitHandler());
    }
    class submitHandler implements ActionListener
    {
        public void actionPerformed(ActionEvent ae1)
        {
            
            	 String user = validateInput(name.getText());
            	 String password = pass.getText();
            	 String mother_name = maiden.getText();
            	             	 
            	 String r_motherName = QueryDB("select mother_name from Mother_maiden_Login where Username like  ?", user);
            	 
              
                 if(r_motherName!="") {
                                                          
                      if(r_motherName.equals(mother_name))
                    	  {
                    	  String r_pass = QueryDB("select Password from Login where Username like ?", user);
	      	                                       
	      	                      if(r_pass.equals(password))
	      	                    	  rf= new Registration_form();
                    	  
                    	  }
                      else
                      
                    	  JOptionPane.showMessageDialog(null, "Incorrect credentials","Pre-Req-Check: Validation Error", 1);
                    }
                else
                    
              	  JOptionPane.showMessageDialog(null, "Incorrect credentials","Pre-Req-Check: Validation Error", 1);
        }
           // Pass in the query prep st. and the parameter to query and this will work  
            String QueryDB(String query, String parameter){
        	String text = "";
        	
         	    try {
         	    	PreparedStatement statement = connection.prepareStatement(query);
					statement.setString(1, parameter);
					ResultSet set1 = statement.executeQuery();
					if(set1!=null) {
  	                	
		                  while(set1.next())
		                     text =set1.getString(1);
		                  
		                  
		              }
				} catch (SQLException e)
	            {
	                System.out.println("Exception: "+e);
	            }
              
              
              return text; 
        }
        
        /*Replace all single quotes by double quotes.  */
        String validateInput(String input)
        {
        	input = input.replaceAll("\n", "\\\\n");
        	input = input.replaceAll("'", "''");
        	//System.out.println("Formatted string"+input);
        	return input;
        }
    }
        public void loadDriver() throws ClassNotFoundException{
            Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
        }
        // Make the Connection
        public void makeConnection() throws SQLException {
            connection=DriverManager.getConnection("jdbc:odbc:User_Login");
        }
        public void buildStatement() throws SQLException {
        	
        }
        // Display the Results
        void displayResults(ResultSet rs) throws SQLException {
            ResultSetMetaData metaData = rs.getMetaData();
       }
}
