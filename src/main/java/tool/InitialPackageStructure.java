package tool;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class InitialPackageStructure {
    public static void main(String[] args) {

        List<String> classNames = new ArrayList<String>();
        Set<String> packages_names = new HashSet<>();
        ZipInputStream zip = null;
        try {
            zip = new ZipInputStream(new FileInputStream("src\\main\\resources\\jars\\manta-1.9.jar"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            for (ZipEntry entry = zip.getNextEntry(); entry != null; entry = zip.getNextEntry()) {
                if (!entry.isDirectory() && entry.getName().endsWith(".class")) {
                    String className = entry.getName().replace('/', '.');
                    classNames.add(className);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (String s : classNames) {
            //System.out.println("DDD: " + s.split("\\.([^\\.]+)\\.class").length);
            packages_names.add(s.split("\\.([^\\.]+)\\.class")[0]);
        }

        //each package will contain a set with all the classes it contains
        HashMap<String, HashSet<String>> package_classes = new HashMap<>();

        //put all the packages names as keys in the map
        for (String s : packages_names) {
            package_classes.put(s, new HashSet<String>());
        }

        //
        for (String className : classNames) {
            for (String packageName : packages_names) {

                //className is composed by package name + class name
                //must fin the exact package name in a full class name
                //in order to add that class to list of classes contained by a package
                Pattern p = Pattern.compile(packageName + "\\.([^\\.]+)\\.class");

                //if it is a match, that full class name will be added to list of classes for that package
                if ((p.matcher(className).matches())) {
                    package_classes.get(packageName).add(className);
                    System.out.println("Package: " + packageName + " -- class: " + className);
                }
            }
        }
        for (Map.Entry<String, HashSet<String>> e : package_classes.entrySet()) {
            String key = e.getKey();
            HashSet<String> value = e.getValue();
            System.out.println("Key: " + key);
            System.out.println("Val: " + value);
        }
    }
}

