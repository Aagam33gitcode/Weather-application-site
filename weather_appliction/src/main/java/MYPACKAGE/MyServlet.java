 package MYPACKAGE;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.Scanner;

import javax.naming.ldap.SortResponseControl;

import org.apache.jasper.tagplugins.jstl.core.Catch;

import com.google.gson.Gson;
import com.google.gson.JsonObject;





/**
 * Servlet implementation class MyServlet
 */
public class MyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MyServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@SuppressWarnings("deprecation")
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		String inputData=request.getParameter("city");
		System.out.println(inputData);
		//API setup
		String apiKey="4c9915288d739112405e83cbf26cafa5";
		//Get the city from the  from input
		String city=request.getParameter("city");
		//create api url
		String apiUrl="https://api.openweathermap.org/data/2.5/weather?q="+city+"&appid="+apiKey;
		try {
		URL url=new URL(apiUrl);
		HttpURLConnection connection=(HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		//To GET and READ the data from NETWORK
		InputStream inputStream=connection.getInputStream();
		InputStreamReader reader=new InputStreamReader(inputStream); 
		//STORE IN STRING
		StringBuilder responseContent=new StringBuilder();
		//Input lene ke liye from reader, will create scanner object
		Scanner scanner=new Scanner(reader);
		while (scanner.hasNext()) {
			responseContent.append(scanner.nextLine());
			
			
		}
		scanner.close();
		System.out.println(responseContent);
//		Type casting= parsing the data into json
	Gson gson=new Gson();
	JsonObject jsonObject=gson.fromJson(responseContent.toString(),JsonObject.class);
	System.out.println(jsonObject);
		
		//Date and Time
		long dateTimestamp=jsonObject.get("dt").getAsLong()*1000;
		String date=new Date(dateTimestamp).toString();
		
		//Temperature
		double temperatureKelvin=jsonObject.getAsJsonObject("main").get("temp").getAsDouble();
		int temperatureCelsius=(int) (temperatureKelvin-273.15);
		
		//Humidity
		int humidity=jsonObject.getAsJsonObject("main").get("humidity").getAsInt();
		
		//Wind speed
		double windSpeed=jsonObject.getAsJsonObject("wind").get("speed").getAsDouble();
		
		//Weather condition
//		String weatherCondition=jsonObject.getAsJsonObject("weather").get("0").getAsJsonObject().get("main").getAsString();
		
	
		
			// TODO: handle exception
		//set the data as request attributes
		request.setAttribute("date", date);
		request.setAttribute("city", city);
    request.setAttribute("temperature", temperatureCelsius);
//		request.setAttribute("weatherCondition",weatherCondition);
		request.setAttribute("humidity", humidity);
		request.setAttribute("windSpeed", windSpeed);
		request.setAttribute("weatherData",responseContent.toString());
		
		
		connection.disconnect();
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();		}
		
		//Forward the request to the weather.jsp page rendering
		request.getRequestDispatcher("index.jsp").forward(request, response);	
	}

}
