package com.example.reizen.scrappers;

import android.util.Log;

import com.example.reizen.models.HotelModel;
import com.example.reizen.models.PlaceModel;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class HotelScrapper {

    public interface HotelListCallback {
        void onHotelListRetrieved(ArrayList<HotelModel> list);
    }

    public void getHotelList(int currentIndex, HotelListCallback callback) {
        new Thread(() -> {
            ArrayList<HotelModel> list = new ArrayList<>();

            try {
                String url = "https://sylhettouristplaces.com/hotel-sylhet/";
                Document document = Jsoup.connect(url).get();
                Elements hotelElements = document.select(".wpb_column"); // Adjust the selector based on your HTML structure

                Log.d("--->>> Hotels - ", String.valueOf(hotelElements.size()));

                for (Element hotel : hotelElements) {
                    String name = hotel.select("h2.vc_custom_heading").text();
                    String description = hotel.select("h4.vc_custom_heading").text();
                    // Attempt to get the address from a different element or next sibling
                    String address = hotel.select("h4.vc_custom_heading").next().text();
                    String imageUrl = hotel.select("img.vc_single_image-img").attr("src");
                    String bookingLink = hotel.select("a[href]").attr("href");

                    Log.d("--->>> Hotel Name: " , name);
                    Log.d("--->>> Description: " , description);
                    Log.d("--->>> Address: " , address);
                    Log.d("--->>> Image URL: " , imageUrl);
                    Log.d("--->>> Booking Link: " , bookingLink);

                    if(!name.equals("")){
                        list.add(new HotelModel(name, address, description, imageUrl, bookingLink));
                    }

                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            if (callback != null) {
                callback.onHotelListRetrieved(list);
            }
        }).start();
    }
}
