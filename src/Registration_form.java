

	import java.awt.*;import java.awt.event.*;

import org.htmlparser.NodeFilter;
	import org.htmlparser.Parser;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.util.NodeIterator;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

	import java.io.*; import java.util.ArrayList;
import java.util.Stack;

import javax.swing.*;
/**The Graphical User Interface class implemented using Java swing classes.
 * */
	public class Registration_form extends JFrame implements AWTEventListener, ActionListener
	{
	  String curDir; JLabel statusbar;
	  String[] course_details = new String[5];
	  String filelist;
	  JTextField name;
	  JTextField Jno;
	  JTextField course_nos;
	  JLabel l_course_nos;
	  JLabel l_name;
	  static long recent_event=0;
	  String[] create_UI()
	  {
		  
		  new Registration_form();
		  
		  return course_details;
	  }
	  /** Constructor of Registration form, where the GUI container is created with all text fields for teh user to
	  the data.
	  */
	  public Registration_form() // File_picker
	  {
	    super("Registration_form"); setBounds(100,100,500,500);
	    setDefaultCloseOperation(EXIT_ON_CLOSE);
	    JButton openButton = new JButton("Open File");
	    l_course_nos= new JLabel("Enter course nos separated by commas. eg: CSC216, CSC215");
	    l_name= new JLabel("Enter student name as it appears in transcript");
	    JLabel L_Jno= new JLabel("Enter Jnumber");
	    
	    course_nos= new JTextField ("", 30);
	    name =  new JTextField ( "",30);
	    Jno = new JTextField("",15);
	    curDir = System.getProperty("user.dir") + File.separator;
	    statusbar = new JLabel("File location is:");
	    JButton button = new JButton("Submit >>");
	    // Adding the buttons to container
	    Container c = getContentPane();
	    c.setSize(15000,10000);
	    c.setLayout(new FlowLayout(FlowLayout.LEFT,40,40));
	    c.add(l_name);c.add(name);
	    c.add(L_Jno);c.add(Jno);
	    c.add(l_course_nos);c.add(course_nos);
	    c.add(openButton);c.add(button);c.add(statusbar);
	    setVisible(true);
	    openButton.addActionListener(new FileHandler());
	    button.addActionListener(new submitHandler());
	   
	    this.trackSessionTimeout();
	  }
	  
	   
	  private int SESSION_TIMEOUT = 10000; // 10 secs
	  private static Registration_form client = null;
	  private Timer timer;

	  
	    public static Registration_form getSystemTimeoutClient()
	    {
	        if (client == null)
	        {
	            client = new Registration_form();
	        }
	        return client;
	    }

	    public void trackSessionTimeout()
	    {
	        timer = new Timer(SESSION_TIMEOUT, this);
	        timer.setInitialDelay(SESSION_TIMEOUT);
	        timer.setRepeats(true);
	        timer.start();
	        System.out.println("Timer started");
	        trackSystemEvents();
	    }

	    public void stopSessionTracking()
	    {
	        if (timer.isRunning())
	        {
	            timer.stop();
	            System.out.println("Timer stopped");
	        }
	    }

	    private void trackSystemEvents()
	    {
	        Toolkit.getDefaultToolkit().addAWTEventListener(this, AWTEvent.MOUSE_EVENT_MASK + AWTEvent.KEY_EVENT_MASK);
	    }

	    @Override
	    public void eventDispatched(AWTEvent event)
	    {
	        // User action detected. Restart the timer
	        if (timer.isRunning())
	        {
	            timer.restart();
	           
	        }
	    }

	    @Override
	    public void actionPerformed(ActionEvent e)
	    {
	        // Session Timeout occured. Close resources and log out from system. 
	    	this.stopSessionTracking();
	    	System.out.println("Log out");
	    	// mAKE  this one a window
	    	
	    	
	    	JFrame frame;
	    	frame = new JFrame("Show Message Dialog");
	        frame.setSize(400, 400);
	        frame.setVisible(true);
	        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    	JOptionPane.showMessageDialog(frame,"Your session has expired. Please re-login.");
	    	       
	    	// Excellent Code to close a swing application without using system.exit
	    	this.processWindowEvent(
	                new WindowEvent(
	                      this, WindowEvent.WINDOW_CLOSING));

	    }
	  /*This class is instantiated on the click of submit button. It has a handler that handles 
	   * the submit button click event. In the handler a String array object is initailized by 
	   * appending name, jNo, course List and the transcript file location fields. 
	   * */
	  class submitHandler implements ActionListener
	  { public void actionPerformed(ActionEvent ae1)
	    {
		  course_details[1]=name.getText();
		  course_details[2]=Jno.getText();
          if(course_nos.getText().contains(","))
        	  {Check_Pre_requisites_Tree.remove_comma(course_nos.getText());}// more than one courses, delimited by commas}
          else
		     {course_details[3]=course_nos.getText();
		     Check_Pre_requisites_Tree.courses.add( course_details[3]);
		     }
		  
          
          course_details[4]=filelist;
          
		  try {
			this.returnValue();
		} catch (ParserException e) {
			System.out.println("Exception: "+e);
		}
	    }
	  
	  /**Calls create_pre_requisite method to create the tree. Also calls the decide() method in 
	   * create_Pre_requisite_tree class to decide if teh student is elligible to enroll or
	   * not.
	   * @return: String[] containing the student details entered in the Reg. form
	   * */
	  
	  String[] returnValue() throws ParserException
	  {
		   
		  Parse_transcripts IN = new Parse_transcripts();
		  ArrayList al_hashMap= new ArrayList();
		  Parser parser = new Parser ( course_details[4]);
			
		  String[] validateField = IN.validate_form_fields(course_details[1],  course_details[2], course_details[4]);
		  if (validateField[0]!= "NONE" ){
			  JOptionPane.showMessageDialog(null, "Student name "+course_details[1]+": Name must be all letters!", 
						"Pre-Req-Check", 1);
		  }
			boolean result=IN.validate_student_transcript( course_details[1],  course_details[2], course_details[4]); // get name and jno separated by comma. remove comma
			// Fetching all TD tags
			if(result)
				{
				NodeList list = new NodeList ();
				NodeFilter filter = new TagNameFilter ("TD");
			    for (NodeIterator e = parser.elements (); e.hasMoreNodes ();)
			         e.nextNode ().collectInto (list, filter);
			    IN.processTRnodes(list);
			    
			    Check_Pre_requisites_Tree mO= new Check_Pre_requisites_Tree();
				al_hashMap = mO.create_Pre_requisite_tree();
				
				
			    while(!Check_Pre_requisites_Tree.courses.isEmpty())
				{
			    
			     String course_str=(String)Check_Pre_requisites_Tree.courses.get(0);// send pre_req list wherever we want-test CSC323
				//compare with transcript
			    ArrayList pre_req_List = null;
			    pre_req_List = new ArrayList();
			    Stack st= new Stack();
			     mO.Fetch_Pre_reqs(course_str,pre_req_List, st );
			     if(mO.Decide(pre_req_List, IN.hashmap_Sucess_courses ))
					{
					 JOptionPane.showMessageDialog(null, "Student "+course_details[1] +" meets all neccessary course pre-requisites for enrolling into: "+course_str, 
								"Pre-Req-Check", 1);
					}
				else
					{
					 JOptionPane.showMessageDialog(null, "Student "+course_details[1]+" does not meet all neccessary course pre-requisites for enrolling into: "+course_str+" . \n Courses that need to be completed before enrolling are: "+mO.not_completed, 
							"Pre-Req-Check", 1);
					
					}
			     Check_Pre_requisites_Tree.courses.remove(0);
			    			
				}
				
				}
			else
			{
				
				JOptionPane.showMessageDialog(null, "Entered Student name/JNumber doesnot match the name in the transcript.","Pre-Req-Check: Validation Error", 1);
			}
	 return course_details;
		  
	  }
	
	  }
	  
	  /**This class takes care of handling the file browsing activity. It has an handler that is invoked when the user clicks on File button in the 
	   * Registration form.
	   * */
	  class FileHandler implements ActionListener
	  {  
	    public void actionPerformed(ActionEvent ae)
	    {
	      
	     JFileChooser selectFile = new JFileChooser(curDir);
	     selectFile.setMultiSelectionEnabled(true);
	     selectFile.setFileSelectionMode(JFileChooser.FILES_ONLY);
	     
	      String[] html = new String[] {"htm","html"};
	      selectFile.addChoosableFileFilter(new ExtFilter(html,
	              "HTML file (*.htm, *.html)"));
	      int option = selectFile.showOpenDialog(Registration_form.this);
	      if (option == JFileChooser.APPROVE_OPTION)
	      {
	        filelist = "Empty";
	        File[] sf = selectFile.getSelectedFiles();
	        try
	        {
	          if (sf.length > 0) filelist = sf[0].getCanonicalPath();
	          for (int i = 1; i < sf.length; i++)
	          {filelist += ", " + sf[i].getCanonicalPath() + "\n";}
	        }
	        catch (IOException evt) {System.out.println("IO exception: "+evt);}
	        statusbar.setText("Your file choices: \n" + filelist);
	      }
	      else {statusbar.setText("File selection is cancelled");}
	    }
	  }
	  
	  /**This class takes care of handling the directory browsing activity. It has an handler that is invoked when the user clicks on Directory button in the 
	   * Registration form.
	   * */
	  
	
	}

	
	
