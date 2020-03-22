package controller;

import java.util.Scanner;

import model.data_structures.MaxHeapCP;
import model.logic.Comparendo;
import model.logic.Modelo;
import view.View;

public class Controller {

	/* Instancia del Modelo*/
	private Modelo modelo;
	
	/* Instancia de la Vista*/
	private View view;
	
	/**
	 * Crear la vista y el modelo del proyecto
	 * @param capacidad tamaNo inicial del arreglo
	 */
	public Controller ()
	{
		view = new View();
		//es falso porque no es test, esta cargando los datos de verdad.
		modelo = new Modelo(false);
		//recordar que al crear el modelo se estan cargando ya de una los datos EN LA LISTA no en el HEAP
	}
		
	public void run() 
	{
		Scanner lector = new Scanner(System.in);
		boolean fin = false;
		int N = 0;
		String respuesta = "";

		while( !fin ){
			view.printMenu();

			int option = lector.nextInt();
			switch(option)
			{
				case 1:
					view.printMessage("--------- \nInserte el tamanio de la muestra aleatoria que quiere cargar en las colas de prioridad: ");
				    N = lector.nextInt();
				    try
				    {
				    	Long [] tiempos = modelo.cargarComparendosMaxHeapCP(N);
				    	MaxHeapCP<Comparendo> maxHeap = modelo.darMaxHeapCP();
				    	view.printMessage("El numero de comparendos es: " + maxHeap.darNumElementos() + "\n");
				    	view.printMessage("El tiempo que demor� en cargar los datos en el MaxColaCP fue de " + tiempos[1] + " milisegundos\n");
				    	view.printMessage("El tiempo que demor� en cargar los datos en el MaxHeapCP fue de " + tiempos[0] + " milisegundos\n");					
				    }
				    catch (Exception e)
				    {
				    	view.printMessage(e.getMessage());
				    }
				    break;
				case 2:
					// TODO adaptarlo porque el 2 es el de MaxColaCP MAX
					//view.printMessage("--------- Inserte N (Numero de comparendos que quiere ver): ");
					//N = lector.nextInt();
					//view.printMessage("Inserte las clases de vehiculos, separadas por comas e incluyendo tildes");
					//respuesta = lector.next();
					//String [] tiposVehiculos = respuesta.split(",");
					//long startTimeHeap = System.currentTimeMillis();
					//MaxHeapCP<Comparendo> maxHeap = modelo.NComparendosMasAlNorteMaxHeap(N, tiposVehiculos);
					//long endTimeHeap = System.currentTimeMillis();
					//long durationHeap = endTimeHeap - startTimeHeap;
					//view.printPriorityQ(maxHeap);
					//view.printMessage("\nTiempo de ejecucion: " + durationHeap + " milisegundos");					
					break;

				case 3:
					view.printMessage("--------- Inserte N (Numero de comparendos que quiere ver): ");
					try
					{
						N = lector.nextInt();
						view.printMessage("Inserte las clases de vehiculos, separadas por COMAS (sin espacios) e incluyendo tildes");
						respuesta = lector.next();
						try
						{
							String [] tiposVehiculos = respuesta.split(",");
							long startTimeHeap = System.currentTimeMillis();
							MaxHeapCP<Comparendo> maxHeap = modelo.NComparendosMasAlNorteMaxHeap(N, tiposVehiculos);
							long endTimeHeap = System.currentTimeMillis();
							long durationHeap = endTimeHeap - startTimeHeap;
							view.printPriorityQ(maxHeap);
							view.printMessage("\nTiempo de ejecucion: " + durationHeap + " milisegundos");						
						}
						catch (Exception e)
						{
							view.printMessage("Lo inserto en un formato diferente!");
						}
						
						
					}
					catch(Exception e)
					{
						e.printStackTrace();
						view.printMessage(e.getMessage());
					}
					break;
				case 4: 
					view.printMessage("--------- \n Hasta pronto !! \n---------"); 
					lector.close();
					fin = true;
					break;	

				default: 
					view.printMessage("--------- \n Opcion Invalida !! \n---------");
					break;
			}
		}
		
	}	
}
