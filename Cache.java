import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

//class for the information stored in the cache
class Data {
    String tag;
    Integer Time;
    
    //constructor for data class
    Data(String tag) {
        this.tag = tag;
        this.Time = 0;
    }

    //comaparing the strings
    public boolean equals(Object o) {
        if (this.tag == ((Data) o).tag)
            return true;
        else
            return false;
    }
}

class Output {
    int numSet;

    //Constructor for class output
    Output(int numSet) {
        this.numSet = numSet;
        init();
    }

    //variables to output
    Integer hits;
    Integer misses;
    int[] setHits;
    int[] setMisses;

    //Initialiser of the variables
    public void init() {
        hits = 0;
        misses = 0;
        setHits = new int[numSet];
        setMisses = new int[numSet];
        for (int i = 0; i < numSet; i++) {
            setHits[i] = 0;
            setMisses[i] = 0;
        }
    }

    //converts the hexadecimal addresses to a binary addresses
    public String convert(String hex) {
        hex = hex.substring(2, hex.length());
        Long num = 0L;
        try {
            num = Long.parseLong(hex, 16);
        } catch (NumberFormatException e) {
            System.out.println("Number format exception");
        }
        String binary = String.format("%32s", Long.toBinaryString(num)).replace(" ", "0");
        return binary;
    }

    //returns the setIndex of the addresses
    public int getSet(String biAdd, Integer numSet) {
        int val = 0;
        int ns = (int) (Math.log(numSet) / Math.log(2));
        String temp = "";
        for (int i = 25 - ns + 1; i <= 25; i++) {
            temp += biAdd.charAt(i);
        }
        try {
            val = Integer.parseInt(temp);
        } catch (StringIndexOutOfBoundsException e) {
            System.out.println("Number format Exception");
        }
        int dec = 0;
        int base = 1;
        int repeat = val;
        while (repeat > 0) {
            int last_digit = repeat % 10;
            repeat = repeat / 10;
            dec += last_digit * base;
            base *= 2;
        }
        return dec;
    }

    //returns the tag in the addresses
    public String getflag(String biAdd, Integer numSet) {
        String temp = "";
        int ns = (int) (Math.log(numSet) / Math.log(2));
        for (int i = 0; i <= 25 - ns; i++) {
            temp += biAdd.charAt(i);
        }
        return temp;
    }
}

//Main class
public class Cache {
    //method for calculating the nunmber of sets in the cache
    public static int values(int size, int ways, int blockSize) {
        int numSet = 0;
        numSet = size * 1024 / blockSize / ways;
        return numSet;
    }

    public static void main(String[] args) throws FileNotFoundException {
        ArrayList<ArrayList<Data>> cache = new ArrayList<>();
        int numSet = values(Integer.parseInt(args[0]), Integer.parseInt(args[1]),Integer.parseInt(args[2]));
        Output output = new Output(numSet);
        
        //creating the cache
        for (int i = 0; i < numSet; i++) {
            ArrayList<Data> set = new ArrayList<>(Integer.parseInt(args[1]));
            cache.add(set);
        }

        //scanning the file
        File ReadAdd = new File(args[3]);
        try {
            Scanner sc = new Scanner(ReadAdd);
            while (sc.hasNext()) {
                String address = sc.next();
                String biAdd = output.convert(address);
                int a = output.getSet(biAdd, numSet);
                String flag = output.getflag(biAdd, numSet);
                boolean isPresent = false;

                Data datax = new Data(flag);

                int index = -1;
                //checking if the data object is alredy present in the cache
                for (int i = 0; i < cache.get(a).size(); i++) {
                    if (cache.get(a).get(i).tag.equals(flag)) {
                        isPresent = true;
                        index = i;
                        break;
                    }
                }

                if (!isPresent && cache.get(a).size() < Integer.parseInt(args[1])) {
                    cache.get(a).add(datax);
                    for (int i = 0; i < cache.get(a).size(); i++) {
                        cache.get(a).get(i).Time++;
                    }
                    output.misses++;
                    output.setMisses[a]++;
                } else if (!isPresent && cache.get(a).size() == Integer.parseInt(args[1])) {
                    int max = 0;
                    for (int i = 0; i < cache.get(a).size(); i++) {
                        if (cache.get(a).get(i).Time > cache.get(a).get(max).Time) {
                            max = i;
                        }
                    }
                    cache.get(a).remove(max);
                    cache.get(a).add(datax);
                    for (int i = 0; i < cache.get(a).size(); i++) {
                        cache.get(a).get(i).Time++;
                    }
                    output.misses++;
                    output.setMisses[a]++;
                } else if (isPresent) {
                    cache.get(a).get(index).Time = 0;
                    for (int i = 0; i < cache.get(a).size(); i++) {
                        cache.get(a).get(i).Time++;
                    }
                    output.hits++;
                    output.setHits[a]++;
                }
            }
            sc.close();

            //printing the results
            for(int i=0;i<numSet;i++){
                System.out.println("The set-wise misses of set "+ i+":" + output.setMisses[i]);
                System.out.println("The set-wise hits of set "+ i+":" + output.setHits[i]);
                System.out.println("");
            }
            System.out.println("The total misses: " + output.misses);
            System.out.println("The total hits: " + output.hits);
        } catch (FileNotFoundException e) {
            System.out.println("File not found!!");
        }
    }
}