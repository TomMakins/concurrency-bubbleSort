import java.util.ArrayList;
import java.util.Arrays;

class SortingAgent extends Thread
{
    private ArrayList <Integer> storage;
    private ArrayList <Integer> outputStorage;
    int startPoint = 0;
    int endPoint = 0;

    public SortingAgent(ArrayList <Integer> arr, int start, int end)
    {
        storage = arr;
        startPoint = start;
        endPoint = end;

        outputStorage = new ArrayList<Integer>();
    }

    public void bubbleSort()
    {
        for(int i = startPoint; i < endPoint; i++)
        {
            for(int j  = startPoint + 1; j < endPoint; j++)
            {
                if(storage.get(j) < storage.get(j -1))
                {
                    int temp = storage.get(j);
                    storage.set(j , storage.get(j -1));
                    storage.set(j - 1, temp);
                }
            }
        }

        for(int i = startPoint; i < endPoint; i++)
        {
            outputStorage.add(storage.get(i));
        }
    }

    public ArrayList<Integer> getSortedArray() {return outputStorage;}

    public void setSortedArray()
    {
        outputStorage.remove(0);
    }

    @Override  
    public void run()
    {
        bubbleSort();
       // display();
    }  
}

class ConcurrentSorting
{  
    public static void main(String args[])
    {   
        ArrayList <Integer> arr = new ArrayList <Integer>( Arrays.asList(9,8,7,6,5,4,3,2,1,0));

        int numThreads = 1;
        for(int i = 1; i < arr.size(); i++)
        {
            if(arr.size() % i == 0)
                numThreads = i;
        }

        // This for loops separates the array into smaller pieces. Then a thread is created to sort each piece.
        SortingAgent threads[] = new SortingAgent[numThreads];
        int counter = 0;
        for(int i = 0; i < arr.size(); i = i + arr.size() / numThreads)
        {
            SortingAgent agent = new SortingAgent(arr, i, i + arr.size() / numThreads);
            agent.start();
            threads[counter] = agent;
            counter++;
        }

        // This loop joins the threads. And will organise all of the work that has been done.
        for(int i = 0; i < threads.length; i++)
        {
            try {
                threads[i].join();
            } 
            catch (Exception e) 
            {}
        } 

        // This for loop joins all of the smaller sorted arrays back into one array.
        for(int i = 0; i < arr.size(); i++)
        {
            int index = 0;
            int min = 100000;

            for(int j = 0; j < numThreads; j++)
            {
                ArrayList <Integer> temp = threads[j].getSortedArray();
                if(temp.size() != 0)
                {
                    if(temp.get(0) < min)
                    {
                        index = j;
                        min = temp.get(0);
                    }
                }
            }
            threads[index].setSortedArray();
            arr.set(i, min);
        }

        // This loop displays the final solution.
        for(int i = 0; i < arr.size(); i++)
        {
            System.out.println(arr.get(i));
        }
    }  
}  