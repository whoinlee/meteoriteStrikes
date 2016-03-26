import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import processing.pdf.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class meteorSketch extends PApplet {



// LIBRARIES



// GLOBAL VARIABLES
PShape baseMap;
String csv[];
String myData[][];
PFont fMedium, fLightSm, fHeavyLg;


// SETUP
public void setup() {
  size(1800, 900);
  noLoop();
  fMedium = createFont("Avenir-Medium", 12);
  fLightSm = createFont("Avenir-Light", 9);
  fHeavyLg = createFont("Avenir-Heavy", 25);
  baseMap = loadShape("WorldMap.svg");
  csv = loadStrings("MeteorStrikes.csv");
  myData = new String[csv.length][6];
  for(int i=0; i<csv.length; i++) {
    myData[i] = csv[i].split(",");
  }
}


// DRAW
public void draw() {
  beginRecord(PDF, "meteorStrikes.pdf");
  shape(baseMap, 0, 0, width, height);
  noStroke();
  
  for(int i=(myData.length-1); i>=0; i--){
    float graphLong = map(PApplet.parseFloat(myData[i][3]), -180, 180, 0, width);
    float graphLat = map(PApplet.parseFloat(myData[i][4]), 90, -90, 0, height);
    float markerSize = 0.05f*sqrt(PApplet.parseFloat(myData[i][2]))/PI;
    float markerRadius = markerSize/2;
    float lineDistance = markerRadius + 20;

    fill(255, 51, 102, 75); //-- pink
    textMode(MODEL);
    noStroke();
    ellipse(graphLong, graphLat, markerSize, markerSize);
    
    float x1 = graphLong + markerRadius*cos(PI/10);
    float y1 = graphLat + markerRadius*sin(PI/10);
    float x2 = graphLong + lineDistance*cos(PI/10);
    float y2 = graphLat + lineDistance*sin(PI/10);
    float x3;
    float numW;
    if(i<10){
      if (i == 9) {
        //special case, as it's overlapped w. a previous
        x1 = graphLong - markerRadius*cos(PI/10);
        x2 = graphLong - lineDistance*cos(PI/10);
      }

      stroke(0, 150);
      line(x1, y1, x2, y2);

      //-- place
      fill(0);
      textFont(fMedium);
      float textW = textWidth(myData[i][0]);
      if (i < 9) {
        x3 = x2+3;
      } else {
        //special case
        x3 = x2 - (textW + 3);
      }
      text(myData[i][0], x3, y2 + 5); 
      
      //-- desc data
      textFont(fLightSm);
      float tons = PApplet.parseFloat(myData[i][2])/1000000;
      String dataS = tons + " t" + " " + myData[i][5] + ", " + myData[i][1] + " ";
      text(dataS, x3, y2 + 17);           //-- mass + fell/found + year
      //
      float descW = textWidth(dataS);
      if (descW > textW) textW = descW;
      stroke(0, 100);
      line(x3, y2 + 8, x3+textW, y2 + 8); //-- line

      //-- ranking 
      textFont(fHeavyLg);
      numW = textWidth(i+1+"");
      text(i+1, graphLong-round(numW/2), graphLat+9);
    }
  }
  endRecord();
  println("PDF Saved!");
}
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "meteorSketch" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
