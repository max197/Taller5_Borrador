package view;

import model.data_structures.IPriorityQueue;
import model.data_structures.MaxHeapCP;
import model.logic.Comparendo;
import model.logic.Modelo;

public class View 
{
	    /**
	     * Metodo constructor
	     */
	    public View()
	    {
	    	
	    }
	    
		public void printMenu()
		{
			System.out.println("1. Cargar los datos en el MaxColaCP y MaxHeapCP");
			System.out.println("2. Requerimiento Funcional 1");
			System.out.println("3. Requerimiento Funcional 2");
			System.out.println("4. Exit");
			System.out.println("Dar el numero de opcion a resolver, luego oprimir tecla Return: (e.g., 1):");
		}

		public void printMessage(String mensaje) {

			System.out.println(mensaje);
		}		
		public void printPriorityQ (MaxHeapCP<Comparendo> maxHeap)
		{
			for (Comparendo comparendo : maxHeap) {
				System.out.println(comparendo);
			}
		}
		
}
