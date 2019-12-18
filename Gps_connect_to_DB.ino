#include <TinyGPS++.h>
#include<Arduino.h>  //library for using arduino syntax and default functions in C++
#include<ESP8266WiFi.h>  //library for Wi-Fi connection
#include<SoftwareSerial.h>  //library for software serial communication
#include<ESP8266HTTPClient.h>  //library for handling client-side http requests

// Choose two Arduino pins to use for software serial
int TXPin = D1;
int RXPin = D2;

int GPSBaud = 9600;
// Create a TinyGPS++ object
TinyGPSPlus gps;
// Create a software serial port called "gpsSerial"
SoftwareSerial gpsSerial( TXPin, RXPin);

const char* ssid = "JulkerNienAkib";
const char* password = "akib108064";

float latitude , longitude;
String lat_str , lng_str;



void setup()
{
  Serial.begin(9600);  //initializing serial communication with PC 
  gpsSerial.begin(GPSBaud); // Start the software serial port at the GPS's default baud
  
  WiFi.mode(WIFI_STA);  //declaring NodeMCU to be a client to the WLAN, can also be declared to be an access point
  pinMode(D4, OUTPUT);  //onboard blue LED is connected to pin D4, we want this LED to be an indicator of successful transmission in TCP/IP protocol
  
  WiFi.begin(ssid, password);  //initializing connection to the Wi-Fi router
  WiFi.hostname("Akibs-NodeMCU");  //declaring hostname for DHCP on the Wi-Fi router
  Serial.println("Starting NodeMCU v1.0 (ESP-12E)..");  //welcome message
  Serial.print("Waiting for Wi-Fi connection");  //Wi-Fi connection initializing message for the serial monitor on PC

 

  while(WiFi.status() != WL_CONNECTED)  //while the NodeMCU is trying to connect to the router show a dot on the serial monitor every 0.5 second
  {
    delay(500);
    Serial.print(".");
  }

  Serial.println("");
  Serial.print("Connected to Wi-Fi network: ");
  Serial.println(ssid);  //SSID of the router which the NodeMCU is connected to
  Serial.print("Assigned IP address: ");
  Serial.println(WiFi.localIP());  //assigned IP address of the NodeMCU
  Serial.print("Hostname: ");
  Serial.println(WiFi.hostname());  //DHCP hostname of the NodeMCU
  Serial.println("");
}

void loop()
{       
        if (gpsSerial.available() > 0){
                  HTTPClient http;  //httpclient object declaration
                  //digitalWrite(D4, LOW);  //keep the onboard blue LED off at the beginning

                  while (gpsSerial.available() > 0)
                        if (gps.encode(gpsSerial.read())){
                               if (gps.location.isValid()) {
                                  lat_str = String(gps.location.lat(), 8);
                                  Serial.print("Latitude: " + lat_str);
                                  Serial.println();
                                  lng_str = String(gps.location.lng(), 8);
                                  Serial.println("Longitude: " + lng_str);
                                  Serial.println();
                                  String link =  String("http://192.168.0.107/add.php?&lat=") + lat_str + "&long=" + lng_str;
                                  //String link =  String("http://uiulivebus.wasdpa-bd.org/apiforhw/index.php?id=1&lat=") + lat_str + "&long=" + lng_str;
                                  http.begin(link);  //beginning http request
                                  int httpCode = http.GET();  //obtaining http request response code from server
                                     if(httpCode == 200)  //200 is the successful transmission code in http
                                     {
                                        Serial.println("Successfully uploaded data to the web server..");  //printing successful transmission message over serial communication with PC
                                     }
                                    else{
                                        Serial.println("Error uploading data to the web server!!");  //printing unsuccessful transmission message over serial communication with PC        
                                    }
                                    delay(1000);
                                  
                                }
                        }    
                 http.end();  //ending http session
        }                      
}
