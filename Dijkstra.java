// Anthony Moore
// Dijkstra Algorithm Implementation
// Runs with input file named "input.txt"

import java.io.File;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

public class Dijkstra 
{
    Set<Point> points = new TreeSet<>();
    Point source;

    // creates and returns a Point to be added to the Array List graph
    Point createPoint(int vertex)
    {
        Point p = new Point();
        p.vertex = vertex;
        return p;    
    }

    // sets the source point given the source's vertex
    void setSource(int source)
    {
        for(Point p : points)
            if(p.vertex == source)
                this.source = p;

        this.source.distance = 0;
    }

    // given current point, finds shortest path to source point
    void findShortestPath(Point current)
    {
        int distCurrPoint;
        
        // traverses through all points surrounding current point
        for(Point p : current.surroundingPoints.keySet())
        {
            // sets the distCurrPoint to the adjacent point's cost
            distCurrPoint = current.surroundingPoints.get(p);
            
            // if distance of current node + distance of adjacent node is
            // less than the adjacent node's cost, replace cost and parent
            if(current.distance + distCurrPoint < p.distance)
            {
                p.distance = current.distance + distCurrPoint;
                p.parent = current;
            }
        }
        
        // set current point to visited
        current.visited = true;
    }
    
    // returns unvisited point with the shortest distance to the source point
    Point setCurrent()
    {
        Point current = new Point();
        // if source point hasn't been visited, set current to the source point
        if(source.visited == false)
            return source;
        else
        {
            int dist = Integer.MAX_VALUE;
            
            // traverses through all vertices in the graph to find the shortest
            // distance point that hasn't been visited yet
            for(Point p : points)
            {
                if(p.distance < dist && p.visited == false)
                {
                    dist = p.distance;
                    current = p;
                }
            }
        }

        return current;
    }

    // class for all the vertices
    class Point implements Comparable<Point>
    {
        // Variable Declarations
        int vertex;
        Integer distance = Integer.MAX_VALUE;
        Map<Point, Integer> surroundingPoints = new HashMap<>();
        boolean visited = false;
        Point parent = null;
        
        public String toString()
        {
            return this.vertex + " " +
            ((this.vertex == source.vertex) ?  "-1" : this.distance ) + " " +
            ((this.parent == null) ? "" : this.parent.vertex) +
            (this.vertex == source.vertex ? "-1" : "");
        }

        // creates all the connections to the Point and adds them to the Map for that point 
        void createConnection(int []vertex, int []connection, int[] cost, int size)
        {
            for(int i = 0; i < size; i++){
                if(vertex[i] == this.vertex){
                for(Point p : points){
                    if(p.vertex == connection[i])
                        this.surroundingPoints.put(p,cost[i]);
                }
                }
            }
            for(int i = 0; i < size; i++){
                if(connection[i] == this.vertex){
                for(Point p : points){
                    if(p.vertex == vertex[i])
                        this.surroundingPoints.put(p,cost[i]);
                }
                }
            }            
        }

        // Override to compareTo() so it compares the vertices of the Points
        @Override
        public int compareTo(Point o) 
        {
            if(this.vertex == o.vertex)
                return 0;
            else if(this.vertex > o.vertex)
                return 1;
            else
                return -1;
        }

    }
    public static void main(String [] args)
    {
        // Variable Declarations
        int numVertices, sourceVertex, numEdges;
        Scanner readFile;
        Dijkstra graph = new Dijkstra();
        PrintWriter pw;
        
        // Initializing Scanner and PrintWriter for File IO
        try{
            readFile = new Scanner(new File("input.txt"));
            pw = new PrintWriter("output.txt");
        } catch(Exception e){
            e.printStackTrace();
            return;
        }

        // Begin Read from File
        numVertices = readFile.nextInt();
        sourceVertex = readFile.nextInt();
        numEdges = readFile.nextInt();
        
        // Stores all the values from input file into separate arrays for
        // vertices, connections, and cost
        int vertex[] = new int[numEdges], connected[] = new int[numEdges], cost[] = new int[numEdges], index = 0;

        while(readFile.hasNext())
        {
            vertex[index] = readFile.nextInt();
            connected[index] = readFile.nextInt();
            cost[index++] = readFile.nextInt();
        }

        readFile.close();
        // End Read from File

        // adds all vertices to the graph
        for(int i = 0; i < numEdges; i++)
        {
            graph.points.add(graph.createPoint(vertex[i]));
            graph.points.add(graph.createPoint(connected[i]));
        }

        // creates the connections for all the vertices
        for(Point p : graph.points)
            p.createConnection(vertex, connected, cost, numEdges);   
        
        graph.setSource(sourceVertex);
        
        // loops through all the vertices to find the shortest path to each one
        for(int i = 0; i < numVertices; i++)
            graph.findShortestPath(graph.setCurrent());

        // begin print to file
        pw.printf("%d\n",numVertices);
        
        int printIndex = 0;
        
        for(Point p : graph.points)
        {
            pw.printf("%s", p.toString());
            
            if(printIndex++ == numVertices - 1)
                break;
            else
                pw.printf("\n");
        }

        pw.close();
        // end print to file
    }
}
