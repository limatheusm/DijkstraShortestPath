import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;


class Vertex {
	private int id;
	private int value;
	private Vertex parent;
	private ArrayList<Vertex> listAdj;
	
	public Vertex(int id, int value, Vertex parent){
		this.id = id;
		this.value = value;
		this.parent = parent;
		this.listAdj = new ArrayList<Vertex>();
	}

	public int getId(){return this.id;}
	public int getValue(){return this.value;}
	public Vertex getParent(){return this.parent;}
	public ArrayList<Vertex> getListAdj(){return this.listAdj;}

	public void setId(int id){this.id = id;}
	public void setValue(int value){this.value = value;}
	public void setParent(Vertex parent){this.parent = parent;}
	
}

public class Dijkstra {
	
	private static int INFINITY = Integer.MAX_VALUE;


	public static void dijkstraMST(int[][] matrixAdj, Vertex[] listVertex){
		Vertex currentVertex = null;
		Vertex[] result = new Vertex[matrixAdj.length]; 
		/*Ja recebo a lista inicializada*/
		listVertex[0].setValue(0); //Atribui zero ao vertice inicial
		/*q eh a lista de vertices a serem investigados*/
		ArrayList<Vertex> q = buildMinHeap(listVertex.clone());

		while (q.size() != 0){ //enquanto conter vertex na lista
			currentVertex = q.remove(0); // extrai o vertex de menor valor (no caso, o primeiro do minHeap)

			/*Percorre seus vizinhos atraves de sua lista de adj*/
			for (Vertex v : currentVertex.getListAdj()) {
				/*RELAX*/
				/*iterativeV > currentV + distancia entre currentV e iterativeV(aresta) ? */
				if (q.contains(v) && 
					v.getValue() > currentVertex.getValue() + matrixAdj[currentVertex.getId()][v.getId()]){
					/*faz o relaxamento, atualizando o parentNode and Value of the iterativeNode*/
					v.setValue(currentVertex.getValue() + matrixAdj[currentVertex.getId()][v.getId()]);
					v.setParent(currentVertex);
				}
			}

			q = buildMinHeap(q.toArray(new Vertex[q.size()])); //update heap
			result[matrixAdj.length - (q.size()+1)] = currentVertex; //salva os vertices acessados

		}

		/*Print vertex and parents*/
		System.out.println("Result:");
		for(int i = 0; i < result.length; i++){

			if(result[i].getParent() != null)				
				System.out.println("vertex: "+result[i].getId()+" - parent: "+result[i].getParent().getId());
			else				
				System.out.println("vertex: "+result[i].getId()+" - parent: "+result[i].getParent());
		}

		System.out.println();

		/*Print awnser shortesPath 0 to n-1*/
		System.out.println("shortesPath 0 to "+(result.length-1)+" is: ");
		String awnser = "";
		/*Procura o ultimo elemento*/
		for (Vertex v : result) {
			if (v.getId() == result.length-1){
				currentVertex = v;
				break;
			}
		}

		awnser = currentVertex.getId()+"";
		while (currentVertex.getParent() != null){			
			awnser = currentVertex.getParent().getId()+" -> "+awnser;
			currentVertex = currentVertex.getParent();
		}		
		System.out.println(awnser);
	}

	/*SUPPORT FUNCTIONS*/

	/*HEAP MIN*/

	private static ArrayList<Vertex> buildMinHeap(Vertex[] vet){
		
		for (int i = (vet.length/2)-1; i >= 0; i--)
			minHeapfy(vet,vet.length,i);
		
		ArrayList<Vertex> ret = new ArrayList<Vertex>();
		
		for (int i = 0; i < vet.length; i++)
			ret.add(vet[i]);
		
		return ret;
	}
	
	private static Vertex[] minHeapfy(Vertex[] vet, int n, int index){
		int min = index, left = 2 * index, right = 2 * index + 1;
		
		if ((left <= n - 1) && (vet[left].getValue() < vet[min].getValue()))
			min = left;
		
		if((right <= n-1) && (vet[right].getValue() < vet[min].getValue()))
			min = right;
		
		if(min != index){
			Vertex aux = vet[index];
			vet[index] = vet[min];
			vet[min] = aux;
			
			return minHeapfy(vet,n,min);
		}
		
		return vet;
	}

	private static void showBiMatrix(int[][] m){
		for (int[] rows : m) {
	    	for (int col : rows) {
	        	System.out.format("%5d", col);
	    	}
	    	System.out.println();
		}
		System.out.println();
	}

	/*
		Modelo de entrada:
			n
			mAdj[i,j]
	*/
	public static void main(String[] args) {
		BufferedReader inReader = 
	            new BufferedReader(new InputStreamReader(System.in));

		int n = 0;
		String line;
		try {
		
			n = Integer.parseInt(inReader.readLine().split("[ ,\t]")[0]);
			int matrixAdj[][] = new int[n][n];
			Vertex listVertex[] = new Vertex[n];			

			String nums = "";
			int k = 0;
			
			while (inReader.ready()){
				nums += inReader.readLine()+" ";
			}

			String[] numbers = nums.split("[\td+, d+, \rd+]");

			/*Inicializa all vertex with INFINITE value*/
			for (int i = 0; i < n; i++)
				listVertex[i] = new Vertex(i, INFINITY, null);						

			for (int i = 0; i < n; i++){				
				for (int j = i + 1; j < n; j++){
					if (numbers[k].matches("[-+]?\\d*\\.?\\d+")){
						int value = Integer.parseInt(numbers[k]);

						/*Building matrix of the distances*/
						matrixAdj[i][j] = value;						
						matrixAdj[j][i] = value;

						/*List of adj*/
						if (value != 0){
							listVertex[i].getListAdj().add(listVertex[j]);
							listVertex[j].getListAdj().add(listVertex[i]);							
						}
						k++;
					}
					else{j--;k++;}
				}
			}

			inReader.close();
			showBiMatrix(matrixAdj);
			dijkstraMST(matrixAdj, listVertex);
						
			
		} catch (Exception e){
			e.printStackTrace();
		}
	}//main
}
