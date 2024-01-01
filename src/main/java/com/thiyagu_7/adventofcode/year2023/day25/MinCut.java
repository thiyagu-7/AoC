package com.thiyagu_7.adventofcode.year2023.day25;

import com.thiyagu_7.adventofcode.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MinCut {
    static int V,E;
    static int[] parent;
    static int[] rank;

    static Random rand;
    MinCut(int V, int E){
        MinCut.V =V;
        MinCut.E =E;
        parent=new int[V];
        rank=new int[V];
        rand = new Random();

        for(int i=0;i<V;i++)
        {
            parent[i]=i;
            rank[i]=0;
        }
    }

    public List<Pair<Integer,Integer>> minCutKarger(Edge edges[]){
        int vertices=V;

        while(vertices>2)
        {
            // Getting a random integer
            // in the range [0, E-1].
            int i=rand.nextInt(E);

            // Finding leader element to which
            // edges[i].u belongs.
            int set1=find(edges[i].u);
            // Finding leader element to which
            // edges[i].v belongs.
            int set2=find(edges[i].v);

            // If they do not belong
            // to the same set.
            if(set1!=set2)
            {
                //System.out.println("Contracting vertices "+edges[i].u+" and "+edges[i].v);
                // Merging vertices u and v into one.
                union(edges[i].u,edges[i].v);
                // Reducing count of vertices by 1.
                vertices--;
            }
        }

        List<Pair<Integer, Integer>> edgesToBeRemoved = new ArrayList<>();

        for(int i=0;i<E;i++)
        {
            // Finding leader element to which
            // edges[i].u belongs.
            int set1=find(edges[i].u);
            // Finding leader element to which
            // edges[i].v belongs.
            int set2=find(edges[i].v);

            // If they are not in the same set.
            if(set1!=set2)
            {
                edgesToBeRemoved.add(
                        new Pair<>(edges[i].u, edges[i].v));

            }
        }
        return edgesToBeRemoved;
    }
    // Find function
    public static int find(int node){

        // If the node is the parent of
        // itself then it is the leader
        // of the tree.
        if(node==parent[node]) return node;

        //Else, finding parent and also
        // compressing the paths.
        return parent[node]=find(parent[node]);
    }

    // Union function
    static void union(int u,int v){

        // Make u as a leader
        // of its tree.
        u=find(u);

        // Make v as a leader
        // of its tree.
        v=find(v);

        // If u and v are not equal,
        // because if they are equal then
        // it means they are already in
        // same tree and it does not make
        // sense to perform union operation.
        if(u!=v)
        {
            // Checking tree with
            // smaller depth/height.
            if(rank[u]<rank[v])
            {
                int temp=u;
                u=v;
                v=temp;
            }
            // Attaching lower rank tree
            // to the higher one.
            parent[v]=u;

            // If now ranks are equal
            // increasing rank of u.
            if(rank[u]==rank[v])
                rank[u]++;
        }
    }
    // Edge class
    static class Edge{
        // Endpoints u and v
        // of the Edge e.
        int u;
        int v;
        // Constructor for
        // Initializing values.
        Edge(int u,int v){
            this.u=u;
            this.v=v;
        }
    }
}