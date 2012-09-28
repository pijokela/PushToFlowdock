package pj

import java.net.URL
import java.net.HttpURLConnection
import java.net.URLEncoder
import java.io.DataOutputStream
import java.io.InputStream
import java.io.BufferedReader
import java.io.InputStreamReader
import scala.io.Source
import javax.net.ssl.HttpsURLConnection

/*
 * http://docs.oracle.com/javase/1.5.0/docs/api/javax/net/ssl/HttpsURLConnection.html
 * http://www.xyzws.com/Javafaq/how-to-use-httpurlconnection-post-data-to-web-server/139
 * http://stackoverflow.com/questions/4376405/httpsurlconnection-and-post
 * 
 */
class HttpPost(urlStr : String) {
  val url : URL = new URL(urlStr)
  
  def post(params : Seq[(String, String)]) : String = {
    
    val encodedParams = params.map(t=>(t._1, URLEncoder.encode(t._2, "UTF-8")))
    val paramsString = encodedParams.map(t=>t._1 + "=" + t._2).mkString("&")
    
    //Create connection
    val connection = url.openConnection().asInstanceOf[HttpURLConnection]
    connection.setRequestMethod("POST")
    connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
    connection.setRequestProperty("Content-Length", "" + paramsString.getBytes().length);
    connection.setRequestProperty("Content-Language", "fi-FI");  
			
    connection.setUseCaches (false);
    connection.setDoInput(true);
    connection.setDoOutput(true);

    //Send request
    val wr : DataOutputStream = new DataOutputStream(connection.getOutputStream())
    wr.writeBytes(paramsString)
    wr.flush()
    wr.close()

    //Get Response	
    val is : InputStream = connection.getInputStream()
    val source = Source.fromInputStream(is, "UTF-8")
    val result = source.getLines.mkString("\n")
    source.close
    connection.disconnect
    result
  }
}

