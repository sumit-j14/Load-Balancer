//to remove any working server simply change server name in line 107

import java.util.ArrayList;
import java.util.HashSet;

//main class
public class Load_Balancer {
    
        public static void updateTaskRing(ArrayList<task> tasks,server newserver) {
            tasks.get(newserver.assigned_rings[0]).assigned_server=4;
            tasks.get(newserver.assigned_rings[1]).assigned_server=4;
        }
        
        public static void main(String[] args) {
        //assumed ringsize is 30 you may change accordingly
        int ringsize=30;
        System.out.println("service running");
        
        //occupied ring places
        HashSet<Integer> occupied = new HashSet<>();
        
        //created ring of tasks
        ArrayList<task>tasks = new ArrayList<>(ringsize);
        for(int i = 0 ; i <ringsize;i++)
        tasks.add(i,new task(i,-1));
        
        //creation of servers with virtual services ( 2 hashes)
        ArrayList<server> servers = new ArrayList<>(4);
        for( int i = 0 ; i < 4; i ++)
        {
            server newserver = new server(""+i,ringsize,occupied);
            servers.add(i, newserver );
            tasks.get(newserver.assigned_rings[0]).assigned_server=i;
            tasks.get(newserver.assigned_rings[1]).assigned_server=i;
        }        // servers added 
        
        
        //printing servers 
        System.out.println("initial working server list\n");
        PrintServer(servers);
        System.out.println();
        
        //printing tasks
        System.out.println("initial task list\n");
        PrintTasks(tasks);
        System.out.println();
        
        //calculation of loads
        int initial_loads[]=new int[4];
        for( int i =0 ; i <ringsize;i++)
        {
            int currentLoad=1;
            // System.out.println("current task is "+i);
            // System.out.println("server assigned to this task is "+tasks.get(i).assigned_server);
            while(i<ringsize&&tasks.get(i).assigned_server==-1)
            {
                currentLoad++;
                i++;
            }
            System.out.println(i+" has load "+currentLoad);
            if(i==30)
            {
                int first=0;
                while(tasks.get(first).assigned_server==-1)
                first++;
                initial_loads[tasks.get(first).assigned_server]=initial_loads[tasks.get(first).assigned_server]+currentLoad-1;    
                break;
            }
            initial_loads[tasks.get(i).assigned_server]=initial_loads[tasks.get(i).assigned_server]+currentLoad;
        }
        // System.out.println(" i is "+i);
        // while((tasks.get(i).assigned_server)==-1)
        // {
            //     currentLoad++;
            //     i=(i+1)%ringsize;
            // }
            // System.out.println("current is"+tasks.get(i).assigned_server);
            // initial_loads[tasks.get(i).assigned_server]+=currentLoad;
            
            //printing loads
            System.out.println("\nthese are the initial loads on servers\n");
            PrintLoads(initial_loads);
            System.out.println();
            //initial load balancing done


            //adding a server S-5
            System.out.println("adding a new server named Server-4");
            server newserver= new server("4", ringsize, occupied);
            servers.add(4, newserver);

            
            //printing new serverlist
            System.out.println("\nnew server list is ");
            PrintServer(servers);
            //new places are already alloted to servers
            
            
            //updating task ring
            updateTaskRing(tasks, newserver);
            
            //printing new tasks
            PrintTasks(tasks);

            //removing a server
            // for example we remove a server named "1"
            int remove_server=1;
            server removed= servers.get(remove_server);
            
            // storing previously assigned tasks
            int stopped_1=removed.assigned_rings[0];
            int stopped_2=removed.assigned_rings[1];

            //removing the assigned tasks
            removed.assigned_rings[0]=-1;
            removed.assigned_rings[1]=-1;
            
            tasks.get(stopped_1).assigned_server=-1;
            
            tasks.get(stopped_2).assigned_server=-1;
            int increase_load=0;
            
            System.out.println("\nchanged server scenario\n");
            PrintServer(servers);
            
            
            //balanceing load
            int load_balance_1;
            boolean doubleload=false;
            for( int i = stopped_1;;i--)
            {
                if(i==-1)
                i=29;
                // System.out.println("number task "+i+" has server allotted "+tasks.get(i).assigned_server);
                if(tasks.get(i).assigned_server==1)
                doubleload=true;
                if(tasks.get(i).assigned_server!=-1)
                {
                    load_balance_1=tasks.get(i).assigned_server;
                    break;
                }
                else
                {
                    increase_load++;
                }
            }
             System.out.println("\n"+increase_load+" tasks unit increased on server "+load_balance_1);
            // System.out.println("new resposnsibility on server "+load_balance_1);
            if(!doubleload)
            {
                increase_load=0;
             System.out.println("entered second server allocation\n");
             int load_balance_2;
             for( int i = stopped_2;;i--)
            {
                if(i==-1)
                i=29;
                // System.out.println("number task "+i+" has server allotted "+tasks.get(i).assigned_server);
                if(tasks.get(i).assigned_server!=-1)
                {
                    load_balance_2=tasks.get(i).assigned_server;
                    System.out.println("\nnew resposnsibility on server "+load_balance_2);
                    break;
                }
                else
                {
                    increase_load++;
                }
            }
            System.out.println(increase_load+" tasks unit increased on server "+load_balance_2);
            }

        }
        //utility functions
        public static void PrintServer(ArrayList<server> servers)
        {
            for( server current : servers)
            System.out.println("server-"+current.name+" "+current.assigned_rings[0]+" "+current.assigned_rings[1]);
        }
        
        public static void PrintTasks(ArrayList<task> tasks)
        {
            for( task currentTask: tasks) {
                System.out.println("task"+currentTask.task_id+" "+currentTask.assigned_server);
            }
        }
        
        public static void PrintLoads(int[] initial_loads) {
            int k=0;
            for( int i : initial_loads)
            {
                System.out.println("load on server"+(k++)+" is "+i);
            }
        }
    }
    //server class specification
    class server{
        String name="";
        int assigned_rings[]= new int[2];

        //server constructor
        server(String name,int ringsize,HashSet<Integer> occupied)
        {
            
            //allotment of first place in the task ring
            this.name= name;
            int place_1=this.hashCode()%ringsize;
            while(occupied.contains(place_1))
            place_1=(++place_1)%ringsize;
            
            
            //allotment of second place in the task ring
            this.assigned_rings[0]=place_1;
            
            occupied.add(this.assigned_rings[0]);
            int place_2=(this.hashCode()/3)%ringsize;
            while(occupied.contains(place_2))
            place_2=(++place_2)%ringsize;
            this.assigned_rings[1]=place_2;
            occupied.add(this.assigned_rings[1]);
        }
    }
    //class object specification
    class task{
        int assigned_server;
        int task_id;

        //task constructor
        task(int id,int server)
        {
            this.task_id=id;
            this.assigned_server=server;
        }
    }
