import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class App {

    public static void main(String[] args) {
        Scanner scan=new Scanner(System.in);

        int length;

	    Runnable r1 = new Runnable(){
            @Override
            public void run() {
                System.out.println();
            }
        };

        Runnable r2 = new Runnable() {
            public void run() {
                System.out.println();
            }
        };

        CyclicBarrier barrier1 = new CyclicBarrier(1, r1);
        CyclicBarrier barrier2 = new CyclicBarrier(1, r2);


        System.out.print("Input Length: ");
        length=scan.nextInt();

        MyCyclicBarrier myCyclicBarrier1 = new MyCyclicBarrier(length,barrier1, barrier2);

        new Thread(myCyclicBarrier1).start();
    }
}

class MyCyclicBarrier implements Runnable {
    CyclicBarrier barrier1, barrier2;
    ArrayList<String> list = new ArrayList<String>();
    private int count;
    int length;
    int total=0;

    public MyCyclicBarrier(int length, CyclicBarrier barrier1, CyclicBarrier barrier2) {
        this.length = length;
        this.barrier1 = barrier1;
        this.barrier2 = barrier2;
    }

    public void run() {
        try {
            DecimalFormat df = new DecimalFormat("0.000");
            double startTime= System.nanoTime();
            countingword();
            this.barrier1.await();

            countingcharacter();
            this.barrier2.await();
            double endTime = System.nanoTime();
            double executionTime = (endTime-startTime)/1000000000;
            System.out.println("Execution time: "+df.format(executionTime)+ " seconds"+"\n");


        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void countingword() throws IOException{

        File file=new File("src/RossBeresford.txt");
        String [] words;

        FileReader fileReader=new FileReader(file);
        BufferedReader bufferedReader=new BufferedReader(fileReader);
        String content;
        while((content=bufferedReader.readLine())!=null){
            words=content.split(" ");
            for(String word : words){
                word=word.replace(",","");
                word=word.replace(".","");
                word=word.replace("/'","");
                if(word.length()==length){
                    if(!list.contains(word)){
                        list.add(word);
                        count++;
                    }
                }
            }
        }
        fileReader.close();
        for(String finalword:list){
            System.out.println(finalword);
        }
        System.out.println("Total words: "+count);

    }

    public void countingcharacter() throws IOException{
        int charcount = 0;
        ArrayList<Character> characterList = new ArrayList<Character>();
        ArrayList<Character> finalcharacterList = new ArrayList<Character>();
        for(String finalword:list){
            for(int i=0; i<finalword.length();i++){
                characterList.add(finalword.charAt(i));
            }
        }
        for(char singlecharacter:characterList){
            if(!finalcharacterList.contains(singlecharacter)){
                finalcharacterList.add(singlecharacter);
                charcount++;
            }
        }
        for(char finalchar:finalcharacterList){
            System.out.print(finalchar+", ");
        }
        System.out.println();
        System.out.println("Total characters: "+charcount);
    }
}