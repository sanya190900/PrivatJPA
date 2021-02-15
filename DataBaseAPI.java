package org.example;

import com.google.gson.Gson;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.*;
import java.util.List;
import java.util.Scanner;

import static org.example.App.isDateValid;

public class DataBaseAPI{

    public void add(ObjectToDataBase object, EntityManager em){
        em.getTransaction().begin();
        try{
            em.persist(object);
            em.getTransaction().commit();
        }catch (Exception ex){
            em.getTransaction().rollback();
        }
    }

    public void getCurrency(EntityManager em) throws IOException{
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter date in format dd.mm.yyyy : ");
        String date = scanner.nextLine();
        if (!isDateValid(date)){
            System.out.println("Invalid date, try again.");
            getCurrency(em);
        }
        String url = "https://api.privatbank.ua/p24api/exchange_rates?json&date="+date;
        URL obj = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null){
            response.append(inputLine);
        }
        in.close();
        Base base = new Gson().fromJson(response.toString(), Base.class);
        Base.Exchange usd = base.findUSD();

        ObjectToDataBase object = new ObjectToDataBase(base.getDate(), usd.getSaleRateNB());
        add(object, em);
    }

    public void delete(EntityManager em){
        Scanner scanner = new Scanner(System.in);
        Long id = null;
        System.out.print("Enter id to delete : ");
        try{
            id = Long.parseLong(scanner.nextLine());
            ObjectToDataBase object = em.getReference(ObjectToDataBase.class, id);
            if(object == null){
                System.out.println("Exchange rate was not found");
                return;
            }
            em.getTransaction().begin();
            try{
                em.remove(object);
                em.getTransaction().commit();
            }catch (Exception ex){
                em.getTransaction().rollback();
            }
        }catch (Exception e){
            System.out.println("Can't recognize id. Try enter again...");
            delete(em);
        }
    }

    public void selectAvg(EntityManager em){
        try {
            Query query = em.createQuery(
                    "SELECT avg(saleRateUSD) FROM ObjectToDataBase", Double.class);
            Double avg = (Double) query.getSingleResult();
            System.out.println(String.format("Average currency is %.4f", avg));
        } catch (NoResultException ex) {
            System.out.println("No results!");
        }
    }

    public void selectMax(EntityManager em){
        try {
            Query query = em.createQuery(
                    "SELECT max(saleRateUSD) FROM ObjectToDataBase c", Double.class);
            Double max = (Double) query.getSingleResult();
            System.out.println(String.format("Maximum currency is %.4f", max));
        } catch (NoResultException ex) {
            System.out.println("No results!");
        }
    }

    public void selectMin(EntityManager em){
        try {
            Query query = em.createQuery(
                    "SELECT min(saleRateUSD) FROM ObjectToDataBase c", Double.class);
            Double min = (Double) query.getSingleResult();
            System.out.println(String.format("Minimum currency is %.4f", min));
        } catch (NoResultException ex) {
            System.out.println("No results!");
        }
    }

    public void selectAll(EntityManager em){
        Query query = em.createQuery(
                "SELECT c FROM ObjectToDataBase c", ObjectToDataBase.class);
        List<ObjectToDataBase> list = (List<ObjectToDataBase>) query.getResultList();
        System.out.println("ID\t\t\tDATE\t\t\t USD\n----------------------------------");
        for (ObjectToDataBase c : list)
            System.out.println(c);
    }
}

