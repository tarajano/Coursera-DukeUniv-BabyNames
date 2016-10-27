
import edu.duke.*;
//import java.io.*;
import org.apache.commons.csv.*;

public class BabyBirths {

	public static void printNames() {
		FileResource input_file = new FileResource("us_babynames/us_babynames_test/example-small.csv");
		int num_born_limit = 100;
		for(CSVRecord rec : input_file.getCSVParser(false)){
			if(num_born_limit >= Integer.parseInt(rec.get(2)))
				System.out.println(rec.get(0) + " -- " + rec.get(1) + " -- " + rec.get(2));
		}
	}
	public static void totalBirths(FileResource input_file) {
		int boys, girls, boys_names, girls_names;
		boys = girls = boys_names = girls_names = 0;
		for(CSVRecord rec : input_file.getCSVParser(false)){
			if(rec.get(1).equals("M")){
				boys += Integer.parseInt(rec.get(2));
				boys_names += 1;
			}else if(rec.get(1).equals("F")){
				girls += Integer.parseInt(rec.get(2));
				girls_names += 1;
			}
		}
		System.out.println("TotalBirths:" + (boys + girls) +  " Girls:" + girls + " Boys:" + boys);
		System.out.println("TotalNames:" + (boys_names + girls_names) +  " GirlsNames:" + girls_names + " BoysNames:" + boys_names);
	}
	public static void testTotalBirths(){
		totalBirths(new FileResource("us_babynames/us_babynames_test/example-small.csv"));
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//printNames();
		testTotalBirths();
	}

}
