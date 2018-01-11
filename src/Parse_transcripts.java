import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.nodes.TagNode;
import org.htmlparser.nodes.TextNode;
import org.htmlparser.util.*;
public class Parse_transcripts{
	// Let me view all my TD nodes as 
	HashMap hashmap_Sucess_courses = new HashMap();
	int count=0 ;
	
	/* Remove_delimiter_in_String, removes all delimiters between Name and Jnumber fields. 
	 * The value of delim is a "space" when the function is invoked from Validate_Student_transcripts().
	 * The value of delim is a "comma" when the function is invoked from make_pre_requisites.main() method.
	 * @parameters: String, String -> the Name&Jnumber , delimiter
	 * @returns: String -> the name and Jnumber are merged together without any delimiters */
	public static String Remove_delimiter_in_String(String str, String delim)
	{
		StringTokenizer token;
		StringBuffer sb= new StringBuffer();
		// The name and Jnumber fields exist in 2 different lines in the transcript and so we need to first 
		// remove the delimiter newline and then remove the comma delimiter.  
		token= new StringTokenizer(str, "\n"); // first parse as per the new line
		token.nextToken();
		str = token.nextToken().trim();// to extract only the secon token and trim and send
		
				
		// Next parse as per the space
		token= new StringTokenizer(str, delim);
		
		while(token.hasMoreTokens())
		{
		  sb.append(token.nextToken().trim());
		  
		}
		
		return sb.toString();
	}
	String[] validate_form_fields( String reg_form_stud_name, String reg_form_Jno, String file_path) throws ParserException
	{
	  System.out.println("Reg form name "+reg_form_stud_name);
		String[] result = {"NONE", "f"};
	  String flag = "f";

	  for (int i = 0; i < reg_form_stud_name.length(); i++) {

          if (Character.isDigit(reg_form_stud_name.charAt(i)))
        	  { flag = "t"; 
        	    result[0] = reg_form_stud_name;
        	    result[1] = flag;
        	    return result ;
        	  }
      }
	  
	  for (int i = 1; i < reg_form_Jno.length(); i++) {

          if (!Character.isDigit(reg_form_Jno.charAt(i)))
        	  { flag = "t"; 
        	    result[0] = reg_form_Jno;
        	    result[1] = flag;
        	    return result ;
        	  }
      }
	  
	  return result;	
	}
	/**Validates the transcript of the student by checking the name and Jnumber fields entered by user with those  that are
	 * in the transcript */
	
  boolean validate_student_transcript(String reg_form_name, String reg_form_Jno, String file_path) throws ParserException
 {
	 TagNode tag1;
	 NodeList list1=new NodeList();
	 
	 
	 NodeFilter filter1 = new TagNameFilter ("DIV");
	 Parser parser = new Parser (file_path);
	 StringBuffer ui_concat_name_Jno=  new StringBuffer();
	 ui_concat_name_Jno.append(reg_form_Jno);
	 ui_concat_name_Jno.append(reg_form_name);
	 
	 String Name_Jno= "";
	
		  for (NodeIterator e = parser.elements (); e.hasMoreNodes ();)
		  { 
			  e.nextNode ().collectInto (list1, filter1);
		  }
		  
		  for (NodeIterator e = list1.elements (); e.hasMoreNodes ();)
		  {
			  
			  tag1 = (TagNode)e.nextNode();
			  
		  	 if(tag1.getAttribute("CLASS").equalsIgnoreCase("staticheaders"))
			   {
		  	     Name_Jno = Remove_delimiter_in_String(tag1.toPlainTextString(), " ");
		  	    	
			   }
		  }
	if(Name_Jno.equals(ui_concat_name_Jno.toString()))  
		
	 return true;
	else 
		return false;
 }
 
/**Extracts all the TR tags which have Td tags with class attribute = "dddefault"
 * these are the tags that have the course names
 *
 * @param lm
 * @throws ParserException
 */
 void processTRnodes (NodeList lm) throws ParserException
 {
	 TagNode td_nodes, td_parent;
	 NodeList td_List = new NodeList();
	 Node n;
	 StringBuffer h_s_c_key;
	 ArrayList h_s_c_value;

	    for(NodeIterator e2=lm.elements(); e2.hasMoreNodes();)

	         {
		    	td_nodes=  (TagNode)e2.nextNode();
		    	if(td_nodes.getAttribute("CLASS").equals("dddefault")  )
		    		if ((td_nodes.toPlainTextString().equals("CSC")) ||(td_nodes.toPlainTextString().equals("CSCL"))|| (td_nodes.toPlainTextString().equals("EN")) || (td_nodes.toPlainTextString().equals("ENL")) || (td_nodes.toPlainTextString().equals("MATH")))
		    		{ // These are our target nodes..put them in a new list -> get its parent and 
		    			// get its Ist, second,5th and 6th child 
		    		  
		    			td_parent= (TagNode)td_nodes.getParent();
		    			NodeFilter filter = new TagNameFilter ("TR"); // parent is a TR node
		    			td_parent.collectInto(td_List, filter);
		    		}
		    				
		    			
		 
	         }
	    
	    
	    for(NodeIterator e3=td_List.elements(); e3.hasMoreNodes();)	
	    {
	    	String str= e3.nextNode().toPlainTextString();
	    	int token_count=0;
	    	
	    	h_s_c_key =new StringBuffer();
	    	h_s_c_value = new ArrayList();
	    	// Apply a tokenizer and get all important elements in a hashmap
	    	StringTokenizer token= new StringTokenizer(str, "\n");
	    	String t; 
	    	while(token.hasMoreTokens()&& token_count < 7)
			{
				if( token_count == 1 || token_count == 2 )
	    		  { t=token.nextToken().trim();
	    		    
	    			h_s_c_key.append(t);// to extract only the second token and trim and send
	    		   }
				else if ( token_count == 5 ||  token_count == 6 )
					{
					t=token.nextToken().trim();
					h_s_c_value.add(t);
					}
				else
					token.nextToken();
			 
			   token_count++;
			}
	    	
	        hashmap_Sucess_courses.put(h_s_c_key.toString(), h_s_c_value);		
	    	
	    }
	    
		
	    Set s= hashmap_Sucess_courses.entrySet();
	    Iterator e= s.iterator();
	    while ( e.hasNext() )
	      System.out.println( e.next() + " ");
	    
	    String s1= new String("CSC539");
	    boolean blnExists = hashmap_Sucess_courses.containsKey(s1);
	    
	    ArrayList val= (ArrayList)hashmap_Sucess_courses.get("CSC118");
	    
	 
 }

}
