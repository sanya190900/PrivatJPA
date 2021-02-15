package org.example;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;

public class App {
    static EntityManagerFactory emf;
    static EntityManager em;
    public static void main( String[] args ) throws IOException {
        try{
            emf = Persistence.createEntityManagerFactory("JPATest");
            em = emf.createEntityManager();

            boolean go = true;
            while(go){
                Scanner scanner = new Scanner(System.in);
                DataBaseAPI api = new DataBaseAPI();

                System.out.println("Actions : ");
                System.out.println("1: add saleRate to base");
                System.out.println("2: find average saleRate in base");
                System.out.println("3: find maximum saleRate in base");
                System.out.println("4: find minimum saleRate in base");
                System.out.print("-> ");

                String s = scanner.nextLine();
                switch (s) {
                    case "1":
                        api.getCurrency(em);
                        break;
                    case "2":
                        api.selectAvg(em);
                        break;
                    case "3":
                        api.selectMax(em);
                        break;
                    case "4":
                        api.selectMin(em);
                        break;
                    default:
                        return;
                }
                api.selectAll(em);
                if(!check()) go = false;
            }
        } finally {
            em.close();
            emf.close();
        }
    }

    private static boolean check() {
        boolean go = true;
        Scanner scanner = new Scanner(System.in);
        System.out.println("Do you want to continue? Type \"Yes\" or \"No\" : ");
        String checker = scanner.nextLine();
        if (checker.equals("Yes")) go = true;
        else if (checker.equals("No")) go = false;
        else check();
        return go;
    }

    public static boolean isDateValid(String date) {
        try {
            DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
            df.setLenient(false);
            df.parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }
}
