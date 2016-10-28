
import edu.duke.*;

import java.io.File;

//import java.io.*;
import org.apache.commons.csv.*;

public class BabyBirths {

	public static int getTotalBirthsRankedHigher(int year, String name, String gender){
		DirectoryResource dr = new DirectoryResource();
		int births = 0;
		for (File file : dr.selectedFiles()){
			if(year != parseYearFromFileName(file.getName()))
				continue;
			FileResource input_file = new FileResource(file);
			for(CSVRecord rec : input_file.getCSVParser(false)){
				if(rec.get(1).equals(gender) && !rec.get(0).equals(name))
					births = births + Integer.parseInt(rec.get(2));
				if(rec.get(0).equals(name))
					break;
			}
		}
		return births;
	}
	public static double getAverageRank(String name, String gender){
		int name_pos_sum = 0;
		int name_occurrences = 0;
		DirectoryResource dr = new DirectoryResource();
		for (File file : dr.selectedFiles()){
			FileResource input_file = new FileResource(file);
			int pos = 0;
			for(CSVRecord rec : input_file.getCSVParser(false)){
				if(rec.get(1).equals(gender)){
					pos++;
					if(rec.get(0).equals(name)){
						name_occurrences++;
						name_pos_sum += pos;
						break;
					}
				}
			}
		}
		if(name_occurrences > 0)
			return (double) name_pos_sum / name_occurrences;
		else
			return 0.0;
	}
	public static int yearOfHighestRank(String name, String gender){
		int highest_rank = 99999999;
		int highest_rank_year = -1;
		DirectoryResource dr = new DirectoryResource();
		for (File file : dr.selectedFiles()){
			int pos = 0;
			int current_file_year = parseYearFromFileName(file.getName());
			FileResource input_file = new FileResource(file);			
			for(CSVRecord rec : input_file.getCSVParser(false)){
				pos++;
				if(rec.get(0).equals(name) && rec.get(1).equals(gender)){
					if(pos < highest_rank){
						highest_rank_year = current_file_year;
						highest_rank = pos;
					}
					break;
				}
			}
		}
		if(highest_rank_year != -1)
			return highest_rank_year;
		else
			return -1;
	}
	public static int parseYearFromFileName(String file_name){
		// yob2012short.csv
		return Integer.parseInt(file_name.substring(3,7));
	}
	public static String whatIsNameInYear(String name, int orig_year, int new_year, String gender){
		String name_new_year = null;		
		int name_rank_orig_year = getRank(orig_year, name, gender);
		name_new_year = getName(new_year, name_rank_orig_year, gender);
		return name_new_year;
	}
	public static String buildFilePathFromYear(int year){
		// yob2012short.csv
		String path = "us_babynames/us_babynames_test/";
		String file_name_start = "yob";
		String file_name_end = "short.csv";
		return path + file_name_start + year + file_name_end;	
	}
	public static String getName(int year, int rank, String gender){
		String name = null;
		String file_full_path = buildFilePathFromYear(year);		
		FileResource file = new FileResource(file_full_path);
		int pos = 0;
		for(CSVRecord rec : file.getCSVParser(false)){
			if(rec.get(1).equals(gender))
				pos++;
				if(pos == rank)
					return rec.get(0);
		}
		return name;
	} 
	public static int getRank(int year, String name, String gender){
		String file_full_path = buildFilePathFromYear(year);		
		FileResource file = new FileResource(file_full_path);
		int name_rank = 0;
		for(CSVRecord rec : file.getCSVParser(false)){
			if(rec.get(1).equals(gender))
				name_rank++;
				if(rec.get(0).equals(name))
					return name_rank;
		}
		return -1;
	}
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
	public static void testGetRank(){
		int year = 2012;
		String name = "Mason";
		String gender = "M";
		int rank = getRank(year, name, gender);
		if(rank != -1)
			System.out.println("Name: " + name + " ranks " + rank + " in year " + year + ".");
		else
			System.out.println("Name: " + name + " was not found in year " + year + ".");
	}
	public static void testGetName(){
		int year = 2012;
		int rank = 5;
		String gender = "F";
		String name = getName(year, rank, gender);
		if(name == null)
			System.out.println("No name found at rank: " + rank + " for gender: " + gender + ".");
		else
			System.out.println("Name: " + name + " is ranked: " + rank + " for gender: " + gender + ".");
	}
	public static void testWhatIsNameInYear(){
		String name = "Isabella";
		int orig_year = 2012;
		int new_year = 2014;
		String gender = "F";
		String new_name = whatIsNameInYear(name, orig_year, new_year, gender);
		System.out.println(name + " born in " + orig_year + " would be " + new_name + " if s/he was born in " + new_year + ".");
	}
	public static void testYearOfHighestRank(){
		String name  = "Mason";
		String genre = "M";
		int top_rank_year = yearOfHighestRank(name, genre);
		if(top_rank_year != -1)
			System.out.println("Best ranking year for: " + name + " was: " + top_rank_year + ".");
		else
			System.out.println("Name: " + name + " not found.");
	}
	public static void testGetAverageRank(){
		String name = "Mason";
		String gender = "M";
		double ave_rank = getAverageRank(name, gender);
		System.out.println("The average rank for " + name + " is: " + String.format( "%.2f", ave_rank));
	}
	public static void testGetTotalBirthsRankedHigher(){
		int year = 2012;
		String name = "Ethan";
		String gender = "M";
		int births = getTotalBirthsRankedHigher(year, name, gender);
		System.out.println("There are " + births + " births ranked higher than " + name + ".");
	}
	public static void main(String[] args) {
		//printNames();
		//testTotalBirths();
		//testGetRank();
		//testGetName();
		//testWhatIsNameInYear();
		//testYearOfHighestRank();
		//testGetAverageRank();
		testGetTotalBirthsRankedHigher();
		
	}

}
