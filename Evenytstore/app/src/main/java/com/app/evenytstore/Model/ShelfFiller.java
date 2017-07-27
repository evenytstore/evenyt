package com.app.evenytstore.Model;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import EvenytServer.model.Brand;
import EvenytServer.model.Customer;

/**
 * Created by Enrique on 14/07/2017.
 */

public class ShelfFiller{

    public ShelfFiller(){

    }

    public HashMap<String, Customer> fillCustomers(DatabaseAccess databaseAccess)throws ParseException{
        HashMap<String, Customer> hashCustomers;
        databaseAccess.open();
        hashCustomers = databaseAccess.getAllCustomers();
        databaseAccess.close();
        return hashCustomers;
    }

    public HashMap<String, Brand> fillBrands(DatabaseAccess databaseAccess)throws ParseException{
        HashMap<String, Brand> hashCustomers;
        databaseAccess.open();
        hashCustomers = databaseAccess.getAllBrands();
        databaseAccess.close();
        return hashCustomers;
    }

    public HashMap<String, List<String>> fillCities(){
        HashMap<String, List<String>> hashCities = new HashMap<>();
        List<String> districts = new ArrayList<>();
        districts.add("Ate");
        districts.add("Barranco");
        districts.add("Bellavista");
        districts.add("Breña");
        districts.add("Carmen de la Legua");
        districts.add("Callao");
        districts.add("Cercado de Lima");
        districts.add("Comas");
        districts.add("Chorrillos");
        districts.add("El Agustino");
        districts.add("Independencia");
        districts.add("Jesús María");
        districts.add("La Molina");
        districts.add("La Perla");
        districts.add("La Punta");
        districts.add("La Victoria");
        districts.add("Lince");
        districts.add("Los Olivos");
        districts.add("Magdalena del Mar");
        districts.add("Miraflores");
        districts.add("Pueblo Libre");
        districts.add("Puente Piedra");
        districts.add("Rimac");
        districts.add("San Borja");
        districts.add("San Isidro");
        districts.add("San Juan de Lurigancho");
        districts.add("San Juan de Miraflores");
        districts.add("San Luis");
        districts.add("San Martín de Porres");
        districts.add("San Miguel");
        districts.add("Santa Anita");
        districts.add("Santa Rosa");
        districts.add("Santiago de Surco");
        districts.add("Ventanilla");
        districts.add("Villa El Salvador");
        districts.add("Villa María del Triunfo");
        hashCities.put("Lima", districts);
        return hashCities;
    }
}
