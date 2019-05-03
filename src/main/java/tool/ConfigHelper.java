package tool;

import java.io.*;
import java.util.ArrayList;
import java.util.Properties;

public class ConfigHelper {

    String result = "";
    int res = 0;
    InputStream inputStream;

    public static ArrayList<Integer> getPropertiesValues() {
        FileInputStream inputStream;

        ArrayList<Object> properties_arr;
        ArrayList<Integer> prop_values_arr = new ArrayList<>();

        try {
            Properties prop = new Properties();
            String propFileName = "D:\\LICENTA\\LicentaPrjPractice\\src\\main\\resources\\config.properties";

            inputStream = new FileInputStream(propFileName);

            if (inputStream != null) {
                prop.load(inputStream);
            } else {
                throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
            }
            properties_arr = new ArrayList<Object>(prop.values());

            for (Object i : properties_arr) {
                prop_values_arr.add(Integer.parseInt((String) i));
            }

        } catch (Exception e) {
            System.out.println("Exception: " + e);
        }
        return prop_values_arr;
    }

    public void readCSV() {
        String line = "";
        String cvsSplitBy = ",";

        ArrayList<Integer> weight = new ArrayList<>();
        weight = getPropertiesValues();


        //the first line in the csv file has to be skipped
        int count_line = 0;

        int wgt = 0;

        //try with resources
        String csvFile = "D:\\LICENTA\\JBUGGER_outputs\\asd.jar_edges.csv";
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {

            //reading the file, line by line
            while ((line = br.readLine()) != null) {

                if (count_line == 0) {
                    count_line++;
                    continue;
                }

                // splitting the line after comma, eg: ana, bia, maria => edg[0] = ana; edg[1] = bia; edg[2] = maria
                String[] edges = line.split(cvsSplitBy);
                int ind = 6;
                int o = 0;
                while (ind != 13) {
                    wgt += weight.get(o) * Integer.parseInt(edges[ind]);
                    ind = ind+1;
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public String getPropValues() throws IOException {

        try {
            Properties prop = new Properties();
            String propFileName = "config.properties";

            inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);

            if (inputStream != null) {
                prop.load(inputStream);
            } else {
                throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
            }

            // get the property value and print it out
            String hasReturn = prop.getProperty("hasReturn");
            String hasParameter = prop.getProperty("hasParameter");
            String hasBinding = prop.getProperty("hasBinding");


            result = "Test: " + Integer.parseInt(hasReturn) + "\t" + hasParameter + "\t" + hasBinding;
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        } finally {
            inputStream.close();
        }
        return result;
    }

    public int getPropValuesInt() throws IOException {

        try {
            Properties prop = new Properties();
            String propFileName = "config.properties";

            inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);

            if (inputStream != null) {
                prop.load(inputStream);
            } else {
                throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
            }


            // get the property value and print it out
            String hasReturn = prop.getProperty("hasReturn");
            String hasParameter = prop.getProperty("hasParameter");
            String hasBinding = prop.getProperty("hasBinding");


            res = Integer.parseInt(hasReturn) + Integer.parseInt(hasParameter) + Integer.parseInt(hasBinding);
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        } finally {
            inputStream.close();
        }
        return res;
    }
}
