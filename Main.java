/**
 *MI verseny feladat
 *@author Szilvácsku Péter
 *@email rtompika@gmail.com
 */


import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.PriorityQueue;
import java.util.Scanner;


public class Main {
	
	
	public static final int DI_COST = 10;
    public static final int V_H_COST = 5;
	
    
    static class Cell{  
        int heuristicCost = 0;
        int finalCost = 0; 
        int i, j;
        Cell parent; 
        
        Cell(int i, int j){
            this.i = i;
            this.j = j; 
        }
        
        
        public int getI() {
			return i;
		}
        
        public int getJ() {
        	return j;
        }


		@Override
        public String toString(){
            return "["+this.i+", "+this.j+"]";
        }
    }
    
    static Cell [][] grid = new Cell[5][5];
    
    static PriorityQueue<Cell> open;
     
    static boolean closed[][];
    static int startI, startJ;
    static int endI, endJ;
            
    public static void setBlocked(int i, int j){
        grid[i][j] = null;
    }
    
    public static void setStartCell(int i, int j){
        startI = i;
        startJ = j;
    }
    
    public static void setEndCell(int i, int j){
        endI = i;
        endJ = j; 
    }
    
    static void checkAndUpdateCost(Cell current, Cell t, int cost){
        if(t == null || closed[t.i][t.j])
        	return;
        
        int t_final_cost = t.heuristicCost+cost;
        
        boolean inOpen = open.contains(t);
        if(!inOpen || t_final_cost<t.finalCost){
            t.finalCost = t_final_cost;
            t.parent = current;
            if(!inOpen)open.add(t);
        }
    }
    
    
    
	public static int rownum, colnum;
	public static int si, sj;
	public static int ei, ej;
	
	
	
	public static void findPath(int map[][], int start_i, int start_j, int end_i, int end_j){
		
		grid = new Cell[rownum][colnum];
        closed = new boolean[rownum][colnum];
        open = new PriorityQueue<>((Object o1, Object o2) -> {
             Cell c1 = (Cell)o1;
             Cell c2 = (Cell)o2;

             return c1.finalCost<c2.finalCost?-1: 
                     c1.finalCost>c2.finalCost?1:0;
         });
        //Set start and end position
        setStartCell(start_i, start_j); 
        setEndCell(end_i, end_j); 
        
        for(int i=0;i<rownum;++i){
            for(int j=0;j<colnum;++j){
                grid[i][j] = new Cell(i, j);
                
                
                if(map[i][j] == 9)
                	grid[i][j].heuristicCost = Math.abs(i-endI)+Math.abs(j-endJ)+map[i][j]+100;
                
                else
                	grid[i][j].heuristicCost = Math.abs(i-endI)+Math.abs(j-endJ)+map[i][j];
                	
                	
                if(map[i][j] == 0)
                    	grid[i][j]=null;
                	
            }
            
         }
		
        run();
		
		
		
	}
	
	public static void drawRoute(int map[][]){
		
		if(closed[endI][endJ]){
            
			
			int mapwithpath[][] = new int[rownum][colnum];
			
			for(int i=0;i<rownum;i++)
            	for(int j=0;j<colnum;j++){
            		mapwithpath[i][j]=map[i][j];
            	}
            		
			
			
			Cell current = grid[endI][endJ];
			List<Cell> path = new ArrayList<Cell>();
			
			while(current.parent!=null)
			{
				path.add(current);
				current=current.parent;
			}
			Collections.reverse(path);
			
			
			int i,j;
			for(i=0;i<rownum;i++)
            	for(j=0;j<colnum;j++)
            		for(Cell cell : path ){
            			if ( i == cell.getI() && j == cell.getJ())
            				mapwithpath[i][j]=-6;
            			}
			

			
			
            for(i=0;i<rownum;i++){
            	for(j=0;j<colnum;j++){
           		
            				if(i == si && j == sj)
            					System.out.print("#");
                			
            				else if(i == ei && j == ej)
            					System.out.print("#");
            				
            				else if(mapwithpath[i][j]== -6){
            					System.out.print("#");
            				}
            				
            				else if(map[i][j] == -9)
            					System.out.print("B");
                			
            				else
            					System.out.print(map[i][j]);
            				
		
            	}
            	System.out.println();
            	
            }
        }
		else
			System.out.println("No possible path!");
            
		
		
		
		
	}

	public static void run(){
		
		
		//add the start location to open list.
        open.add(grid[startI][startJ]);
        
        Cell current;
        
        while(true){ 
            current = open.poll();
            if(current==null)
            	break;
            
            closed[current.i][current.j]=true; 

            if(current.equals(grid[endI][endJ])){
                return; 
            } 

            Cell t;  
            if(current.i-1>=0){
                t = grid[current.i-1][current.j];
                checkAndUpdateCost(current, t, current.finalCost+V_H_COST); 

                if(current.j-1>=0){                      
                    t = grid[current.i][current.j-1];//i-1
                    checkAndUpdateCost(current, t, current.finalCost+DI_COST); 
                }

                if(current.j+1<grid[0].length){
                    t = grid[current.i][current.j+1]; //i-1
                    checkAndUpdateCost(current, t, current.finalCost+DI_COST); 
                }
            } 

            if(current.j-1>=0){
                t = grid[current.i][current.j-1];
                checkAndUpdateCost(current, t, current.finalCost+V_H_COST); 
            }

            if(current.j+1<grid[0].length){
                t = grid[current.i][current.j+1];
                checkAndUpdateCost(current, t, current.finalCost+V_H_COST); 
            }

            if(current.i+1<grid.length){
                t = grid[current.i+1][current.j];
                checkAndUpdateCost(current, t, current.finalCost+V_H_COST); 

                if(current.j-1>=0){
                    t = grid[current.i][current.j-1];//i+1
                    checkAndUpdateCost(current, t, current.finalCost+DI_COST); 
                }
                
                if(current.j+1<grid[0].length){
                   t = grid[current.i][current.j+1];//i+1
                    checkAndUpdateCost(current, t, current.finalCost+DI_COST); 
                }  
            }
        }
		
		
		
		
		
		
		
		
	}
	
	public static void printHeuristicMap(){
		
		
		for(int i=0;i<rownum;++i){
            for(int j=0;j<colnum;++j){
                if(grid[i][j]!=null)
                	System.out.printf("%-3d ", grid[i][j].finalCost);
                
                else 
                	System.out.print("BL  ");
            }
            System.out.println();
        }
        System.out.println();
		
	}
	
	public static void printPath(){
		
		if(closed[endI][endJ]){
             System.out.println("Path: ");
             Cell current = grid[endI][endJ];

             System.out.print(current);
             
             while(current.parent!=null){
                 System.out.print(" -> "+current.parent);
                 current = current.parent;
             } 
             System.out.println();
        }else System.out.println("No possible path");
		
		
	}
	
	public static void targetSearch(List<List<Integer>> target, int map[][]){
		
		double d = 0;
		double min_d = Double.MAX_VALUE;
		
		for(int i=0 ; i<target.size(); i++){
				d = Math.sqrt( (Math.pow(Math.abs(si-target.get(i).get(0)),2)) + Math.pow(Math.abs(sj-target.get(i).get(1)),2)) ;
				
			
				if(d < min_d)
				{
					
					ei=target.get(i).get(0);
					ej=target.get(i).get(1);
					
					min_d=d;

					
				}

		}	
		
				
	}

	public static int sumPath(int map[][]){
		
		int sum = 0;
		
		if(closed[endI][endJ]){
            		
			sum = 9;
			Cell current = grid[endI][endJ];
			List<Cell> path = new ArrayList<Cell>();
			
			while(current.parent!=null)
			{
				path.add(current);
				current=current.parent;
			}
			Collections.reverse(path);
					
			
			
			
			int i,j;
			for(i=0;i<rownum;i++)
            	for(j=0;j<colnum;j++)
            		for(Cell cell : path ){
            			if ( i == cell.getI() && j == cell.getJ())
            				sum+=map[i][j];
            			}
			}
			
		
		return sum;
		
	}
	
	public static int[][] create2DIntMatrixFromFile(String filename) throws Exception, NumberFormatException {
		int[][] matrix = {{1}, {2}};

		File inFile = new File(filename);
		Scanner in = new Scanner(inFile);
		
		
		int rows = 0;
		while(in.hasNextLine())
		{
		    rows++;
		    in.nextLine();
		}
		
		in = new Scanner(inFile);
		
		//int intLength = 0;
		String[] length = in.nextLine().split("");
		
		
		in.close();
		rownum = rows;
		colnum = length.length;
		
		matrix = new int[rownum][colnum];
		in = new Scanner(inFile);

		
		
		
		int lineCount = 0;
			
		List<List<Integer>> targets = new ArrayList<List<Integer>>();
		
		while (in.hasNextLine()) {
		  String[] currentLine = in.nextLine().split("");
		     for (int i = 0; i < currentLine.length; i++) {
		    	 try{
		    		 
		    		 matrix[lineCount][i] = Integer.parseInt(currentLine[i]);
		    		 
		    	 	}
		    	 catch(NumberFormatException e){
		    		 
		    		 if((e.getMessage().lastIndexOf("A")) !=-1){
		    			 	matrix[lineCount][i] = -1; //Start
		    		 		si=lineCount;
		    		 		sj=i;		    			 	
		    		 	}
		    		 
		    		 else{
		    			 	matrix[lineCount][i] = -9; //Target
		    			 				 	
		    			 	List<Integer> oneTarget = new ArrayList<Integer>();
		    			 	oneTarget.add(lineCount);
		    			 	oneTarget.add(i);
		    			 	
		    			 	targets.add(oneTarget);

		    			 	
		    		 	}

	    			 	
		    	 }		    		 
		       }
		  lineCount++;
		 }                
		
		targetSearch(targets, matrix);
		
		 return matrix;
		}

	
	
	public static void main(String[] args) throws Exception {
	
		int map[][] = null;
		int sumAll = 0; 
		
		if(args.length == 1){
			try {
				
				File mapsFile = new File(args[0]);
				Scanner in = new Scanner(mapsFile);
				
								
				while(in.hasNextLine()){
					map = create2DIntMatrixFromFile(in.nextLine());

					findPath(map, si, sj, ei, ej);
					sumAll+=sumPath(map);
					
					System.out.println(sumPath(map));
					
				}
				System.out.println("Osszes hossz: "+ sumAll);
				
				
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
				
		}
		else if(args.length == 2){
			try {
				
				File mapsFile = new File(args[1]);
				Scanner in = new Scanner(mapsFile);
				
				
				while(in.hasNextLine()){
					map = create2DIntMatrixFromFile(in.nextLine());
					
					findPath(map, si, sj, ei, ej);
					drawRoute(map);
					
					System.out.println();
				}				
				
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
			
			
		}
		else
			System.out.println("Hibas parameter!");

	}

}
