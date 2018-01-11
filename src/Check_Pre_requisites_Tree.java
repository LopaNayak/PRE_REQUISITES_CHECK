import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Stack;
import java.util.StringTokenizer;

import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.util.NodeIterator;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;


public class Check_Pre_requisites_Tree {
	ArrayList al;
	static ArrayList al_hashMap= new ArrayList();
	
	ArrayList not_completed;
	static ArrayList  courses = new ArrayList();

	/**Each course in the pre_Requisite_List is searched in the courses in the transcript to evaluate 
	 *if the student has completed all pre_requisites or not.
	 *@param: ArrayList, HashMap
	 *@return: boolean
	 **/
	
	boolean Decide(ArrayList pre_reqs, HashMap transcript_courses)
	{
		not_completed = new ArrayList();
		Iterator e1= pre_reqs.iterator();
		while ( e1.hasNext() )
	    { String s= (String)e1.next();
	      System.out.println("S is"+s);
	     if(!transcript_courses.containsKey(s))
	    	 {not_completed.add(s);
	    	  }
	    	  	    
	    }
		if(not_completed.size()!= 0)
			{return false;}
		else
		{return true;}
	}
	/**Initializing course names as string constants*/
	
	final String  k_450= "CSC450";
	final String  k_4XX= "CSC4XX";
	final String  k_475= "CSC475";
	final String  k_441= "CSC441";
	final String  k_435= "CSC435";
	final String  k_323= "CSC323";
	final String  k_330= "CSC330";
	final String  k_325= "CSC325";
	final String  k_350= "CSC350";
	final String  k_216= "CSC216";
	final String  k_l_216= "CSCL216";
	final String  k_228= "CSC228";
	final String  k_l_228= "CSCL228";
	final String  k_212= "EN212";
	final String  k_l_212= "ENL212";
	final String  k_111= "MATH111";
	final String  k_218= "CSC218";
	final String  k_211= "CSC211";
	final String  k_209= "CSC209";
	final String  k_214= "CSC214";
	final String  k_119= "CSC119";
	final String  k_l_119= "CSCL119";
	final String  k_225= "CSC225";
	final String  k_118= "CSC118";
	final String  k_l_118= "CSCL118";
	
	
	/**This method fetches the pre_requisites for the course name supplied 
	 * to it as input. 
	 * ALL courses beginning with CSC strictly follow the rule
	 **/
	public void Fetch_Pre_reqs(String course_name, ArrayList pre_req_List, Stack st)
	{
	
		
		String pop_element;
		ArrayList al_value=new ArrayList();
		ArrayList Pre_req_list = new ArrayList();
		HashMap hs1= new HashMap();
		al = new ArrayList(); 

           if(!course_name.substring(0, 2).equals("EN")&& !course_name.substring(0, 3).equals("ENL")&& !course_name.equals("CSC118")&&!course_name.equals("CSCl118"))  // exception to rules
           {	
        	 if((course_name.equals("CSC225")))
             {
            	hs1=(HashMap)al_hashMap.get(3); 
             } 
        	 else if(course_name.substring(0, 3).equals("CSC"))
        	 {
            	hs1 = Get_Hashmap(course_name.substring(3, 4));// get the course number
            	
        	 }
            	al_value= (ArrayList)hs1.get(course_name);
        		
        		// All the parents in the Parent list are fetched and put in the Pre_req_list AL.
        		Iterator e= al_value.iterator();
        	    if(!course_name.equals("CSC118"))
	        	{ while ( e.hasNext() )
	        	    { String s= (String)e.next();
	        	      // Optimization. We can exclude those courses that are already completed by the student from being added. 
   	        	      // But then we may not show the tree search algorithm
	        	      pre_req_List.add(s); // ALL PARENTS are IN pre-RE_LIST
	        	      st.push(s);
	        	    }
	        	}
        	    
        	    
        	    //pop top of stack and 
        	    pop_element= (String)st.pop();
        	    
        	    if (pop_element.substring(3,4).equals("L")) // If a  lab course, keep popping
       	        { 
        	    	pop_element= (String)st.pop();
        	    	 
       	        }      	    
        	     if (!pop_element.substring(3,4).equals("L")) // If not a lab course, keep popping
        	     {   
        	    	    	    
	        	    	// Each parent in the Parent list may have a parent too. So those parents are fetched. 
	        	    	// So this function calls itself recursively. 
	        	      Fetch_Pre_reqs(pop_element,pre_req_List,st);
        	     }
        	     
        	}
           else // if course names=EN or ENL, check if stack is empty
           {
        	   if(st.empty())
        	   {
        	    System.out.println("The PRe -req list is "+pre_req_List);
        	   
        	   }
        	   else // NOTE: STACK MAY BE EMPTY, BUT we need to process the call before checking for emptyness
        	   {   System.out.println("The stack has : "+st);
        	   if(st.empty())
        	   { 	 System.out.println("Final List is "+pre_req_List);}
        	   else
        	   { pop_element= (String)st.pop();
         	    
      	         while(pop_element.substring(3,4).equals("L")&& !st.empty()) // If a  lab course, keep popping
      	          {System.out.println("Current poped element"+pop_element);
      	    	   pop_element= (String)st.pop();
      	    	   System.out.println("Now poped element"+pop_element);
      	    	   }
      	          Fetch_Pre_reqs(pop_element,pre_req_List,st);
        		     	         
        	   }  
             	      
        	   }
           }
        
	}
	/** Method to remove commas in the course list received as input.
	 * @param: String
	 * @return: ArrayList containing all course names
	 * */
	public static ArrayList remove_comma(String str)
	  {
		  
		  StringTokenizer token= new StringTokenizer(str, ","); // first parse as per the new line
		  		int i=0;	
		  while(token.hasMoreTokens())
			{
			  courses.add(token.nextToken().trim());
			  System.out.println("courses are "+courses.get(i));
			  i++;
			}
				  
		  return courses;
	  }

	/**Based on the course name starting number, the corresponding hashmap is returned. E.g: CSC325 starts
	 * with a 3, implying an Junior year course. All Junior year courses are stored in the hs_Junior. And this hashmap
	 * is stored as the second element in the ArrayList (al_hashmap) which contains all the four hashmaps.
	 * @param: String - starting number of the course name 
	 * @return: HashMap - containing the requested course name 
	 * */
	private HashMap Get_Hashmap(String course_name_start_char) 
	{ 
	  System.out.println("Char reading is "+course_name_start_char+"ArrayList size is "+al_hashMap.size());
	  if(course_name_start_char.equals("3"))
		  return ((HashMap)al_hashMap.get(1));
	  else if(course_name_start_char.equals("4"))
		  return ((HashMap)al_hashMap.get(0));
	  else if(course_name_start_char.equals("2"))
		   return ((HashMap)al_hashMap.get(2));
		 
	  else // course name starts with 1
		  return ((HashMap)al_hashMap.get(3));
	  	
	}
	/**There are four types of hashmaps based on the courses offered in different years on the under-graduate study;
	 * Each hashMap holds the courses offered in one particular year viz - hs_FreshMan stores courses offered in freshman year.
	 * Every hashmap's key = course name and value = Arraylist of parent course names. Thus every course name points to a list
	 * of parent course names, which in turn have parents too. This is how the tree structure is created. Thus, one course's
	 * prerequisites are all the courses that are parents/grandparents of this course; can be fetched by going up the hierarchy
	 * tree till we reach the root - i.e. CSC118. Here, we are creating that tree structure.
	 * @return: A global array list of hashmaps called al_hashmap
	 **/
	public ArrayList create_Pre_requisite_tree()
	{
		ArrayList al = new ArrayList();
		String key="";
		HashMap hs_FreshMan = new HashMap(); // This hashmap stores Freshman courses: viz - EN212, CSC118, CSC119, CSC225
		HashMap hs_Sophomore = new HashMap(); // This hashmap stores Sophomore courses: viz - CSC216, CSC228, CSC209, CSC214, CSC211
		HashMap hs_Junior = new HashMap();// This hashmap stores Junior year courses: viz - CSC312, CSC350, CSC312 etc
		HashMap hs_Senior= new HashMap();//// This hashmap stores Senior year courses: viz - CSC435, CSC450, CSC441
		/* Values set in Senior hashmap*/
		key= k_450;
		al.add(k_325);
		hs_Senior.put(key, al);
		
		al=new ArrayList();
		key= k_4XX;
		al.add(k_323);
		al.add(k_325);
		hs_Senior.put(key, al); // al = 323, 325
		
		al=new ArrayList();
		key= k_475;
		al.add(k_325);
		hs_Senior.put(key, al); // al = 325
		
		al=new ArrayList();
		key= k_435;
		al.add(k_325);
		al.add(k_323);
		hs_Senior.put(key, al); // al =  325
		
		al=new ArrayList();
		key=k_441;
		al.add(k_330);
		al.add(k_325);
		hs_Senior.put(key, al); // al = 325, 330
		
	
	    Set s0= hs_Senior.entrySet();
	    Iterator e0= s0.iterator();
	    while ( e0.hasNext() )
	      System.out.println( e0.next() + " ");
		
	    /*Putting Values in Junior hashmap*/
		
	    al=new ArrayList();
		key= k_323;
		al.add(k_228);
		al.add(k_l_228);
		hs_Junior.put(key, al);
		
		al=new ArrayList();
		key=k_325;
		al.add(k_228);
		al.add(k_l_228);
		al.add(k_216);
		al.add(k_l_216);
		hs_Junior.put(key, al);
		
		al=new ArrayList();
		key=k_350;
		al.add(k_228);
		al.add(k_l_228);
		al.add(k_216);
		al.add(k_l_216);
		hs_Junior.put(key, al);
		
		al=new ArrayList();
		key=k_330;
		al.add(k_228);
		al.add(k_l_228);
		al.add(k_323);
		hs_Junior.put(key, al);
		

	    Set s= hs_Junior.entrySet();
	    Iterator e= s.iterator();
	    while ( e.hasNext() )
	      System.out.println( e.next() + " ");
	    
		/*Putting Values in sophomore hashmap*/
	    al=new ArrayList();
		key=k_216;
		al.add(k_119);
		al.add(k_l_119);
		al.add(k_225);
		al.add(k_212);
		al.add(k_l_212);
		hs_Sophomore.put(key, al);
	   		
		al=new ArrayList();
		key=k_228;
		al.add(k_119);
		al.add(k_l_119);
		al.add(k_225);
		hs_Sophomore.put(key, al);
		
		al=new ArrayList();
		key=k_212;
		al.add(k_111);
		hs_Sophomore.put(key, al);
		
		al=new ArrayList();
		key=k_218;
		al.add(k_119);
		al.add(k_l_119);
		hs_Sophomore.put(key, al);
		
		al=new ArrayList();
		key=k_211;
		al.add(k_119);
		al.add(k_l_119);
		hs_Sophomore.put(key, al);
		
		al=new ArrayList();
		key=k_209;
		al.add(k_119);
		al.add(k_l_119);
		hs_Sophomore.put(key, al);
		
		al=new ArrayList();
		key=k_214;
		al.add(k_119);
		al.add(k_l_119);
		hs_Sophomore.put(key, al);
		
		
	    Set s1= hs_Sophomore.entrySet();
	    Iterator e1= s1.iterator();
	    while ( e1.hasNext() )
	      System.out.println( e1.next() + " ");
		
	    /*Putting into freshman*/
	    
	    al=new ArrayList();
		key=k_119;
		al.add(k_118);
		al.add(k_l_118);
		hs_FreshMan.put(key, al);
		
		key=k_225;
		hs_FreshMan.put(key, al);
		
		
	    Set s2= hs_FreshMan.entrySet();
	    Iterator e2= s2.iterator();
	    while ( e2.hasNext() )
	      System.out.println( e2.next() + " ");
		
	   
	    al_hashMap.add(hs_Senior);
	    al_hashMap.add(hs_Junior);
	    al_hashMap.add(hs_Sophomore);
	    al_hashMap.add(hs_FreshMan);
	   
		
		return al_hashMap;
	}
}

