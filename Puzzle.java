import java.awt.*;  
import javax.swing.*;  
import java.awt.event.*;  
import java.util.*;

public class Puzzle extends JFrame implements ActionListener
{  
	JButton[] b=new JButton[9];  
	JButton shuffle,solve,change;  
	
	ImageIcon[] ii=new ImageIcon[9];
	Random r=new Random();
	int i, heuristic=9;
	static int solvingStep=0;
	static int imageCount=0;
	int[] allHeuristic=new int[]{9,9,9,9};
	java.util.List<Integer> indexList=new ArrayList<Integer>();

	String dir="./TestPic/";
	String ext=".jpg";
	Puzzle()
	{  
		super("Image Puzzle");  

		//Test Images
		for(i=0;i<8;i++)
			ii[i]=new ImageIcon(dir+"TestPic"+(i+1)+".jpg");  
		ii[i]=new ImageIcon(dir+"space"+ext);  

		//Insert In Sequence
		// for(i=0;i<9;i++)
		// 	b[i]=new JButton(ii[i]);  

		//Insert In random Order
		for (i=0;i<9;i++)
			indexList.add(i);
		
		Collections.shuffle(indexList); 
		// for(i=0;i<9;i++)
		// 	b[i]=new JButton(ii[indexList.get(i)]);
		
		b[0]=new JButton(ii[4-1]);  
		b[1]=new JButton(ii[1-1]);  
		b[2]=new JButton(ii[3-1]);  
		b[3]=new JButton(ii[9-1]);  
		b[4]=new JButton(ii[2-1]);  
		b[5]=new JButton(ii[6-1]);  
		b[6]=new JButton(ii[7-1]);  
		b[7]=new JButton(ii[5-1]);  
		b[8]=new JButton(ii[8-1]);
  
		shuffle=new JButton("SHUFFLE");  
		solve=new JButton("SOLVE");   
		change=new JButton("CHANGE");   

		b[0].setBounds(20,10,200,200);  
		b[1].setBounds(220,10,200,200);  
		b[2].setBounds(420,10,200,200);  
		b[3].setBounds(20,210,200,200);  
		b[4].setBounds(220,210,200,200);  
		b[5].setBounds(420,210,200,200);  
		b[6].setBounds(20,410,200,200);  
		b[7].setBounds(220,410,200,200);  
		b[8].setBounds(420,410,200,200);

		shuffle.setBounds(40,625,150,50);  
		solve.setBounds(240,625,150,50);
		change.setBounds(440,625,150,50);

		shuffle.setFocusPainted(false);
		shuffle.setContentAreaFilled(false);

		solve.setFocusPainted(false);
		solve.setContentAreaFilled(false);

		change.setFocusPainted(false);
		change.setContentAreaFilled(false);

		shuffle.setBorder(new RoundedBorder(40)); //40 is the radius
		solve.setBorder(new RoundedBorder(40)); //40 is the radius
		change.setBorder(new RoundedBorder(40)); //40 is the radius

		for(i=0;i<9;i++)
			add(b[i]);  
    
		add(shuffle);add(solve);add(change);   
		
		for(i=0;i<9;i++)
			b[i].addActionListener(this);   

		shuffle.addActionListener(this);  
		solve.addActionListener(this);  
		change.addActionListener(this);  
  
		// shuffle.setBackground(Color.BLACK);  
		shuffle.setForeground(Color.BLACK);
		shuffle.setFont(new Font("Arial", Font.PLAIN, 15)); 

		// solve.setBackground(Color.BLACK);  
		solve.setForeground(Color.BLACK);
		solve.setFont(new Font("Arial", Font.PLAIN, 15)); 

		// change.setBackground(Color.BLACK);  
		change.setForeground(Color.BLACK); 
		change.setFont(new Font("Arial", Font.PLAIN, 15)); 

		setSize(650,730);
		setResizable(false);
		setLayout(null);  
		setVisible(true);  
		setIconImage(Toolkit.getDefaultToolkit().getImage("icon.png"));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
	}//end of constructor 

	//get difference between two strings
	public static int indexOfDifference(String str1, String str2) {
	    if (str1 == str2) {
	        return -1;
	    }
	    if (str1 == null || str2 == null) {
	        return 0;
	    }
	    int i;
	    for (i = 0; i < str1.length() && i < str2.length(); ++i) {
	        if (str1.charAt(i) != str2.charAt(i)) {
	            break;
	        }
	    }
	    if (i < str2.length() || i < str1.length()) {
	        return i;
	    }
	    return -1;
	} 

	public static String difference(String str1, String str2) {
	    if (str1 == null) {
	        return str2;
	    }
	    if (str2 == null) {
	        return str1;
	    }
	    int at = indexOfDifference(str1, str2);
	    if (at == -1) {
	        return "";
	    }
	 	return str2.substring(at);
	}

	public int getHeuristic(JButton[] state){
		int heuristic=0,i;

		for(i=0;i<9;i++){
			if(!(state[i].getIcon().equals(ii[i]))){
				heuristic++;
			}
		}
		return heuristic;
	}

	public int getMinimumHeuristic(int[] allHeuristic){
		int minHeuristicIndex=0;
		for(i=0;i<4;i++){
			if(allHeuristic[minHeuristicIndex]>allHeuristic[i]){
				minHeuristicIndex=i;
			}
		}
		return minHeuristicIndex;
	}

	public boolean changeState(JButton[] originalState, int heuristic){

		JButton[] currentState=new JButton[9]; 
		Icon[] finalState=new Icon[9]; 
		int space=0;
		int heuristicCount=0;
		boolean flag=false;

		for(i=0;i<9;i++)
			finalState[i]=ii[i];

		for(i=0;i<9;i++){
			if(difference(dir,(originalState[i].getIcon()+"").replace(ext,"")).equalsIgnoreCase("space")){
				space=i;
				break;
			}	
		}

		while(heuristicCount<4){
			for(i=0;i<9;i++){
				if(originalState[i].getIcon().equals(finalState[i])){
					flag=true;
				}
				else{
					flag=false;
					break;
				}	
				if(flag==true && i==8){
					JOptionPane.showMessageDialog(Puzzle.this,"!!! SOLVED USING HEURISTIC !!!");
					solvingStep=0;
					return true;
				}
			}

			for(i=0;i<9;i++)
				currentState[i]=originalState[i];

			//HEURISTIC CHECK UP
			if(heuristicCount==0){
				if((space-3)>=0){
					Icon temp=currentState[space].getIcon();
					currentState[space].setIcon(currentState[space-3].getIcon());
					currentState[space-3].setIcon(temp);

					allHeuristic[0]=getHeuristic(currentState);
					// JOptionPane.showMessageDialog(Puzzle.this,"!!! heuristicCount = "+heuristicCount+"!!!");

					temp=currentState[space].getIcon();
					currentState[space].setIcon(currentState[space-3].getIcon());
					currentState[space-3].setIcon(temp);
				}
			}
			//HEURISTIC CHECK DOWN
			else if(heuristicCount==1){
				if((space+3)<9){
					Icon temp=currentState[space].getIcon();
					currentState[space].setIcon(currentState[space+3].getIcon());
					currentState[space+3].setIcon(temp);
					
					allHeuristic[1]=getHeuristic(currentState);
					// JOptionPane.showMessageDialog(Puzzle.this,"!!! heuristicCount = "+heuristicCount+"!!!");

					temp=currentState[space].getIcon();
					currentState[space].setIcon(currentState[space+3].getIcon());
					currentState[space+3].setIcon(temp);
				}
			}
			//HEURISTIC CHECK RIGHT
			else if(heuristicCount==2){
				if(((space+1)<9) && (space!=8) && (space!=5) && (space!=2)){
					Icon temp=currentState[space].getIcon();
					currentState[space].setIcon(currentState[space+1].getIcon());
					currentState[space+1].setIcon(temp);
					
					allHeuristic[2]=getHeuristic(currentState);
					// JOptionPane.showMessageDialog(Puzzle.this,"!!! heuristicCount = "+heuristicCount+"!!!");

					temp=currentState[space].getIcon();
					currentState[space].setIcon(currentState[space+1].getIcon());
					currentState[space+1].setIcon(temp);
				}
			}
			//HEURISTIC CHECK LEFT
			else if(heuristicCount==3){
				if(((space-1)>=0) && (space!=0) && (space!=3) && (space!=6)){
					Icon temp=currentState[space].getIcon();
					currentState[space].setIcon(currentState[space-1].getIcon());
					currentState[space-1].setIcon(temp);

					allHeuristic[3]=getHeuristic(currentState);
					// JOptionPane.showMessageDialog(Puzzle.this,"!!! heuristicCount = "+heuristicCount+"!!!");

					temp=currentState[space].getIcon();
					currentState[space].setIcon(currentState[space-1].getIcon());
					currentState[space-1].setIcon(temp);
				}
			}

			heuristicCount-=-1;
		}

		if(getMinimumHeuristic(allHeuristic)<heuristic){

			//SET HEURISTIC STATE UP
			if(getMinimumHeuristic(allHeuristic)==0){
				if((space-3)>=0){
					Icon temp=currentState[space].getIcon();
					currentState[space].setIcon(currentState[space-3].getIcon());
					currentState[space-3].setIcon(temp);
				}
			}
			//SET HEURISTIC STATE DOWN
			else if(getMinimumHeuristic(allHeuristic)==1){
				if((space+3)<9){
					Icon temp=currentState[space].getIcon();
					currentState[space].setIcon(currentState[space+3].getIcon());
					currentState[space+3].setIcon(temp);
				}
			}
			//SET HEURISTIC STATE RIGHT
			else if(getMinimumHeuristic(allHeuristic)==2){
				if(((space+1)<9) && (space!=8) && (space!=5) && (space!=2)){
					Icon temp=currentState[space].getIcon();
					currentState[space].setIcon(currentState[space+1].getIcon());
					currentState[space+1].setIcon(temp);
				}
			}
			//SET HEURISTIC STATE LEFT
			else if(getMinimumHeuristic(allHeuristic)==3){
				if(((space-1)>=0) && (space!=0) && (space!=3) && (space!=6)){
					Icon temp=currentState[space].getIcon();
					currentState[space].setIcon(currentState[space-1].getIcon());
					currentState[space-1].setIcon(temp);
				}
			}
		}
		JOptionPane.showMessageDialog(Puzzle.this,"!!! SOLVING STEP !!!"+(++solvingStep));
		
		return changeState(currentState,heuristic);
	}

	public void solvePuzzle(){
		JButton[] originalState=new JButton[9]; 

		for(i=0;i<9;i++)
			originalState[i]=b[i]; 
			// originalState[i].setIcon(b[i].getIcon());
		
		changeState(originalState,heuristic);

	}
  
	public void actionPerformed(ActionEvent e)
	{  
		Icon mySpace=ii[8];
		for (i=0;i<9;i++){
			if(difference(dir,(b[i].getIcon()+"").replace(ext,"")).equalsIgnoreCase("space")){
				mySpace=b[i].getIcon();
				break;
			}
		}

		//Shuffle the Order
		if(e.getSource()==shuffle){  

			solvingStep=0;
			Collections.shuffle(indexList); 
			for(i=0;i<9;i++)
				b[i].setIcon(ii[indexList.get(i)]);

		}//end of if

		//Change Puzzle Image
		if(e.getSource()==change){
			dir="./images/";
			imageCount++;

			//Change Image
			if(imageCount==1){
				dir=dir+""+1+"/";
				for(i=0;i<9;i++){
					if(i!=2)
						ii[i]=new ImageIcon(dir+"profile"+(i+1)+ext);   
				}
				ii[2]=new ImageIcon(dir+"space"+ext);
			}
			
			else if(imageCount==2){
				dir=dir+""+2+"/";
				for(i=0;i<9;i++)
					if(i!=0)
						ii[i]=new ImageIcon(dir+"profile"+(i+1)+ext);   
				ii[0]=new ImageIcon(dir+"space"+ext); 
			}

			else if(imageCount==3){
				dir=dir+""+3+"/";
				for(i=0;i<9;i++)
					if(i!=0)
						ii[i]=new ImageIcon(dir+"profile"+(i+1)+ext);   
				ii[0]=new ImageIcon(dir+"space"+ext); 
			}

			else if(imageCount==4){
				dir="./TestPic/";

				for(i=0;i<8;i++)
					ii[i]=new ImageIcon(dir+"TestPic"+(i+1)+".jpg");  
				ii[i]=new ImageIcon(dir+"space"+ext);

				imageCount=0;
			}

			for(i=0;i<9;i++)
				b[i].setIcon(ii[i]);

			solvingStep=0;
			Collections.shuffle(indexList); 
			for(i=0;i<9;i++)
				b[i].setIcon(ii[indexList.get(i)]);

			JOptionPane.showMessageDialog(Puzzle.this,"!!! IMAGE CHANGED !!!");
		}//end of if  

		if(e.getSource()==solve){  
			solvePuzzle();

			return;
		}//end of else if  

		else if(e.getSource()==b[0])
		{  
			Icon myii=b[0].getIcon();  
			if(b[1].getIcon().equals(mySpace))
			{
				b[1].setIcon(myii); b[0].setIcon(mySpace);
			}  
			else if(b[3].getIcon().equals(mySpace))
			{
				b[3].setIcon(myii); b[0].setIcon(mySpace);
			}  
		}//end of else if  
		
		else if(e.getSource()==b[1])
		{  
			Icon myii=b[1].getIcon();  
			if(b[0].getIcon().equals(mySpace))
			{
				b[0].setIcon(myii); b[1].setIcon(mySpace);
			}  
			else if(b[2].getIcon().equals(mySpace))
			{ 
				b[2].setIcon(myii); b[1].setIcon(mySpace);
			}  
			else if(b[4].getIcon().equals(mySpace))
			{
				b[4].setIcon(myii); b[1].setIcon(mySpace);
			}  
		}//end of else if  
	  
		else if(e.getSource()==b[2])
		{  
			Icon myii=b[2].getIcon();  
			if(b[1].getIcon().equals(mySpace))
			{
				b[1].setIcon(myii); b[2].setIcon(mySpace);
			}  
			else if(b[5].getIcon().equals(mySpace))
			{
				b[5].setIcon(myii); b[2].setIcon(mySpace);
			}  
		}//end of else if  
	  
		else if(e.getSource()==b[3])
		{  
			Icon myii=b[3].getIcon();  
			if(b[0].getIcon().equals(mySpace))
			{
				b[0].setIcon(myii); b[3].setIcon(mySpace);
			}  
			else if(b[6].getIcon().equals(mySpace))
			{
				b[6].setIcon(myii); b[3].setIcon(mySpace);
			}  
			else if(b[4].getIcon().equals(mySpace))
			{
				b[4].setIcon(myii); b[3].setIcon(mySpace);
			}  
		}//end of else if  
	  
		else if(e.getSource()==b[4])
		{  
			Icon myii=b[4].getIcon();  
			if(b[1].getIcon().equals(mySpace))
			{
				b[1].setIcon(myii); b[4].setIcon(mySpace);
			}  
			else if(b[3].getIcon().equals(mySpace))
			{
				b[3].setIcon(myii); b[4].setIcon(mySpace);
			}  
			else if(b[5].getIcon().equals(mySpace))
			{
				b[5].setIcon(myii); b[4].setIcon(mySpace);
			}  
			else if(b[7].getIcon().equals(mySpace))
			{
				b[7].setIcon(myii); b[4].setIcon(mySpace);
			}  
		}//end of else if  
	  
		else if(e.getSource()==b[5])
		{  
	  
			Icon myii=b[5].getIcon();  
			if(b[8].getIcon().equals(mySpace))
			{
				b[8].setIcon(myii); b[5].setIcon(mySpace);
			}  
			else if(b[2].getIcon().equals(mySpace))
			{
				b[2].setIcon(myii); b[5].setIcon(mySpace);
			}  
			else if(b[4].getIcon().equals(mySpace))
			{
				b[4].setIcon(myii); b[5].setIcon(mySpace);
			}  
	  
		}//end of else if  
	  
		else if(e.getSource()==b[6])
		{  
			Icon myii=b[6].getIcon();  
			if(b[3].getIcon().equals(mySpace))
			{
				b[3].setIcon(myii); b[6].setIcon(mySpace);
			}  
			else if(b[7].getIcon().equals(mySpace))
			{
				b[7].setIcon(myii); b[6].setIcon(mySpace);
			}  
		}//end of else if  
	  
		else if(e.getSource()==b[7])
		{  
			Icon myii=b[7].getIcon();  
			if(b[6].getIcon().equals(mySpace))
			{
				b[6].setIcon(myii); b[7].setIcon(mySpace);
			}  
			else if(b[8].getIcon().equals(mySpace))
			{ 
				b[8].setIcon(myii); b[7].setIcon(mySpace);
			}  
			else if(b[4].getIcon().equals(mySpace))
			{
				b[4].setIcon(myii); b[7].setIcon(mySpace);
			}  
	  
		}//end of else if  
	  
		else if(e.getSource()==b[8])
		{  
			Icon myii=b[8].getIcon();  
			if(b[5].getIcon().equals(mySpace))
			{
				b[5].setIcon(myii); b[8].setIcon(mySpace);
			}  
			else if(b[7].getIcon().equals(mySpace))
			{
				b[7].setIcon(myii); b[8].setIcon(mySpace);
			}  
	  
		}//end of else if  
		
		if(b[0].getIcon().equals(ii[0])&&b[1].getIcon().equals(ii[1])&&b[2].getIcon()  
			.equals(ii[2])&&b[3].getIcon().equals(ii[3])&&b[4].getIcon().equals(ii[4])  
			&&b[5].getIcon().equals(ii[5])&&b[6].getIcon().equals(ii[6])&&b[7].getIcon()  
			.equals(ii[7])&&b[8].getIcon().equals(ii[8]))
			{   
			JOptionPane.showMessageDialog(Puzzle.this,"!!! You Won : You Solved It By Yourself!!!");  
		}  
	}//end of actionPerformed  
   
	public static void main(String[] args)
	{  
		new Puzzle();  
	}//end of main  
  
}//end of class
