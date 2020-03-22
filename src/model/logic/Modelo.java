package model.logic;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;


import model.data_structures.ListaEncadenada;
import model.data_structures.MaxHeapCP;

/**
 * Definicion del modelo del mundo
 *
 */
public class Modelo 
{
	
	
	private ListaEncadenada<Comparendo> datos;
	
	private MaxHeapCP<Comparendo> maxHeapCP;
	
	private MaxHeapCP<Comparendo> req2MaxHeapCP;
	
	
	
	/**
	 * Constante con la ruta
	 */
	public final String RUTA_test = "./data/comparendos_dei_2018_small.geojson";
	public final String RUTA = "./data/Comparendos_DEI_2018_Bogot�_D.C.geojson";
	
	/**
	 * Si esTest = true, carga los datos de prueba, si no, carga los normales
	 * @param esTest
	 */
	public Modelo(boolean esTest)
	{
		datos = new ListaEncadenada<Comparendo>();
		if (esTest == false)
			cargarComparendos(RUTA);
		else
			cargarComparendos(RUTA_test);
		//Se inicializa el maxheap diciendole que su criterio es comparar por latitud!!
		maxHeapCP = new MaxHeapCP<Comparendo>(Comparendo.Comparadores.darComparadorLatitud());
		req2MaxHeapCP = null;
	}
	
	/**
	 * Servicio de consulta de numero de elementos presentes en el modelo 
	 * @return numero de elementos presentes en el modelo
	 */
	public int darTamano()
	{
		return datos.size();
	}

	

	/**
	 * Consulta de los datos de la lista enlazada 
	 * @return Datos de la lista enlazada
	 */
	public ListaEncadenada<Comparendo> darListaEncadenadaDatos()
	{
		return datos;
	}
	
	/**
	 * Servicio de consulta de numero de elementos presentes en el modelo 
	 * @return numero de elementos presentes en el modelo
	 */
	public MaxHeapCP<Comparendo> darMaxHeapCP()
	{
		return maxHeapCP;
	}
	
	
	/**
	 * Requerimiento Funcional 2.
	 * Muestra los N comparendos que ocurrieron mas al norte (basado en latitud, es decir, mayor latitud) y
	 * estan en una ListaEncadenada de Strings 
	 * En consola debe mostrarse el OBJECT ID, la fecha ,la clase vehiculo, la latitud y longitud.
	 * @return Retorno un MaxHeap que tiene esos N comparendos 
	 */
	public MaxHeapCP<Comparendo> NComparendosMasAlNorteMaxHeap(int N, String[] tiposVehiculos) 
	throws Exception
	{
		/*
		 * Estrategia:
		 * 0. Se asume que los datos de las colas de prioridad ya se cargaron, con el numero N de comparendos
		 * 1. Ya los tengos de mayor a menor en mi MaxHeapCP -> Voy preguntandole a mi MaxHeap el elemento
		 * maximo y lo voy eliminando, para que suba el siguiente maximo. Los que voy eliminando, los voy
		 * metiendo en otro MaxHeap que contenga los N que yo quiero. 
		 * OJO: Tener en cuenta los tiposVehiculos.
		 */
		
		//Para no repetir esto muchas veces: si ya lo hice, lo retorno. Si no lo he hecho, lo hago
		
		//Si nunca he hecho el proceso. Lo hago o si ya lo hice pero el N era diferente, tambien lo hago. 
		
		if (req2MaxHeapCP == null || ( req2MaxHeapCP != null && req2MaxHeapCP.darNumElementos() != N ) )
		{
			req2MaxHeapCP = new MaxHeapCP<Comparendo>(N, Comparendo.Comparadores.darComparadorLatitud());
			
			if (N > maxHeapCP.darNumElementos())
				throw new Exception ("No se pueden dar los " + N + " elementos porque solo hay " + maxHeapCP.darNumElementos());
			
			int i = N;
			
			while (!maxHeapCP.esVacia() && i > 0)
			{
				Comparendo comparendo = maxHeapCP.sacarMax();
				boolean coincideConTiposVehiculos = false;
				
				for (String string : tiposVehiculos) 
				{
					if (comparendo.darClaseVehiculo().equalsIgnoreCase(string))
					{
						coincideConTiposVehiculos = true;
						break;
					}
				}
				if (coincideConTiposVehiculos)
				{
					req2MaxHeapCP.agregar(comparendo);
					i--;
				}
			}
			return req2MaxHeapCP;
		}
		//Si ya hice el proceso y era el mismo N, no lo vuelvo a hacer! 
		else 
		{
			return req2MaxHeapCP;
		}
	}
	
	
	
	
	//Cargar los datos en la cola de prioridad
	/**
	 * Carga los datos de la lista a la cola de prioridad
	 * Pre: Este metodo asume que la lista de comparendos ya se cargaron. Es decir, los datos ya tiene
	 * los elementos cargados!
	 * @param pRuta. Archivo de donde va a cargar los datos
	 * @param N Numero de datos que el usuario quiere cargar!
	 * @return Un arreglo. La posicion 0 trae el tiempo que tarde en almacenar para el MaxHeapCP,
	 * 		La posicion 1 es el tiempo que demoro en el maxColaCP.
	 */
	public Long[] cargarComparendosMaxHeapCP (int N) throws Exception
	{
		//TODO en este metodo tiene que cargar los datos de MaxHeapCP y el otro max heap. Ambos se cargan simultaneamente
		
		//IMPORTANTE, asume que se cargan los comparendos en la lista -> eso ahora lo hace el constructor
		
		//Despues de cargar los comparendos, la informacion ya se encuentra en la lista encadenada 
		//llamada "datos"
		
		//Ahora se inserta en el MaxHeapCP
		
		Long []arreglo = new Long[2]; //En la posicion uno va a estar la duraccion en el max heap
		
		if (N <= datos.size())
		{
			ListaEncadenada<Integer> muestraRandom = muestraAleatoria(N, datos.size() -1);
			long startTimeHeap = System.currentTimeMillis();
			for (Integer index : muestraRandom) 
			{
				Comparendo comparendo = datos.get(index);
				maxHeapCP.agregar(comparendo);
			}
			long endTimeHeap = System.currentTimeMillis();
			long durationHeap = endTimeHeap - startTimeHeap;
			arreglo[0] = durationHeap;
			
			//TODO ESTO SE HACE para el maxColaCP MAX 
			long startTimeCola = System.currentTimeMillis();
			for (Integer index : muestraRandom) 
			{
				Comparendo comparendo = datos.get(index);
				//AGREGAR AQUI A LA OTRA MaxCOLACP
			}
			long endTimeCola = System.currentTimeMillis();
			long durationCola = endTimeCola - startTimeCola;
			arreglo[1] = durationCola;
			
			return arreglo;
		}
		else
			throw new Exception ("El numero de datos que quiere almacenar en las colas de prioridad es mayor al numero de comparendos");
		
	}

	//Metodo que genera una ListaEncadenada de numeros Aleatorios sin repeticion. La idea es 
	//que esta lista lo haga entre 0 y datos.size()-1, incluyendo el datos.size()-1
	/**
	 * Genera una lista que me dara los indices de mi muestra aleatoria.
	 * @param N. Numero de datos que se van a sacar. Sera el tama�o de la lista que se retorna
	 * @param rangoMaximo. Sera el datos.size() - 1
	 * @return Lista con los indices de mi muestra aleatoria!
	 */
	private ListaEncadenada<Integer> muestraAleatoria (int N, int rangoMaximo)
	{
		ListaEncadenada<Integer> lista = new ListaEncadenada<Integer>();
		Random gen = new Random();
		
		int numeroAleatorio;
		int i = 0;
		while (i < N)
		{
			numeroAleatorio = gen.nextInt(rangoMaximo + 1);
			if (!lista.contains(numeroAleatorio))
			{
				lista.add(numeroAleatorio);
				i++;
			}
		}
		return lista;
	}
	
	
	//Cargar datos en la lista encadenada
	public ListaEncadenada<Comparendo> cargarComparendos(String pRuta)
	{
		try 
		{
			datos = readJsonStream(new DataInputStream(new FileInputStream(pRuta)));
			return datos;
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		return datos;
	}
	
	
	//Metodos privados para cargar los comparendos

	private ListaEncadenada<Comparendo> readJsonStream(InputStream in) throws IOException
	{
		JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
		try {
			return readData(reader);
		} finally {
			reader.close();
		}
	}

	private  ListaEncadenada<Comparendo> readData(JsonReader reader) throws IOException 
	{

		ListaEncadenada<Comparendo> comparendos = new ListaEncadenada<Comparendo>();

		reader.beginObject();
		while (reader.hasNext()) {
			String name = reader.nextName();
			if (name.equals("features") && reader.peek() != JsonToken.NULL) {
				comparendos = readFeaturesArray(reader);
			} else {
				reader.skipValue();
			}
		}
		reader.endObject();
		return comparendos;
	}

	private ListaEncadenada<Comparendo> readFeaturesArray(JsonReader reader) throws IOException {
		ListaEncadenada<Comparendo> comparendos = new ListaEncadenada<Comparendo>();

		reader.beginArray();
		while (reader.hasNext()) {
			comparendos.add(readFeature(reader));
		}
		reader.endArray();
		return comparendos;
	}



	private Comparendo readFeature(JsonReader reader) throws IOException 
	{


		int OBJECTID = -1;
		String FECHA_HORA = null;
		String MEDIO_DETE = null;
		String CLASE_VEHI = null;
		String TIPO_SERVI = null;
		String INFRACCION = null;
		String DES_INFRAC = null;
		String LOCALIDAD = null;
		Double[] coordinates = null;


		reader.beginObject();

		while (reader.hasNext()) 
		{
			String name = reader.nextName();

			if (name.equals("properties"))
			{
				reader.beginObject();
				while (reader.hasNext()) {
					String nombre = reader.nextName();
					if (nombre.equals("OBJECTID")) {
						OBJECTID = reader.nextInt();
					} else if (nombre.equals("FECHA_HORA")) {
						FECHA_HORA = reader.nextString();
					} else if (nombre.equals("MEDIO_DETECCION")) {
						MEDIO_DETE = reader.nextString();
					} else if (nombre.equals("CLASE_VEHICULO")) {
						CLASE_VEHI = reader.nextString();
					} else if (nombre.equals("TIPO_SERVICIO")) {
						TIPO_SERVI = reader.nextString();
					} else if (nombre.equals("INFRACCION")) {
						INFRACCION = reader.nextString();
					} else if (nombre.equals("DES_INFRACCION")) {
						DES_INFRAC = reader.nextString();
					} else if (nombre.equals("LOCALIDAD")) {
						LOCALIDAD = reader.nextString();
					} else {
						reader.skipValue();
					}
				}
				reader.endObject();

			}
			else if (name.equals("geometry"))
			{
				reader.beginObject();
				while (reader.hasNext())
				{
					String n = reader.nextName();
					if (n.equals("coordinates") && reader.peek() != JsonToken.NULL)
					{
						coordinates = readDoublesArray(reader);

					} else {
						reader.skipValue();
					}
				}
				reader.endObject();
			}
			else 
				reader.skipValue();

		}

		//Desglosar la fecha!
		String [] fechas = FECHA_HORA.split("T");
		fechas[1] = fechas[1].substring(0, 8); 
		//System.out.println(fechas[0]);
		//System.out.println(fechas[1]);
		FECHA_HORA = fechas[0] + " " +fechas[1];
		//System.out.println(FECHA_HORA);
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		Date fecha;
		try 
		{
			reader.endObject();
			fecha = formatter.parse(FECHA_HORA);
			Comparendo comparendo = new Comparendo(OBJECTID, fecha, MEDIO_DETE, CLASE_VEHI, TIPO_SERVI, INFRACCION, DES_INFRAC, LOCALIDAD, coordinates[0], coordinates[1]);
			return comparendo;
		} 
		catch (ParseException e) 
		{
			System.out.println("No se pudo pasar de string a fecha.");
			System.out.println("El metodo readFeature(JsonReader) esta retornando nulo. Ojo!");
		}
		//Si retorna null es porque no se pudo pasar el formato

		return null;
	}


	private Double[] readDoublesArray(JsonReader reader) throws IOException
	{
		Double doubles[] = new Double[3];

		reader.beginArray();
		while (reader.hasNext()) {
			doubles[0] = reader.nextDouble();
			doubles[1] = reader.nextDouble();
			doubles [2] = reader.nextDouble();	
		}
		reader.endArray();
		return doubles;
	}





}
