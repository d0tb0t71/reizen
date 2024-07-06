package com.example.reizen;

import com.example.reizen.models.PlaceModel;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class PlaceScrapper {

    public interface PlaceListCallback {
        void onPlaceListRetrieved(ArrayList<PlaceModel> list);
    }

    public void getPlaceList(int currentIndex, PlaceListCallback callback) {
        new Thread(() -> {
            ArrayList<PlaceModel> list = new ArrayList<>();

            try {
                String url = "https://www.trip.com/travel-guide/attraction/sylhet-21399/tourist-attractions/" + currentIndex;
                Document document = Jsoup.connect(url).get();
                Elements places = document.select(".online-poi-item-card");

                for (Element place : places) {
                    String name = place.select(".poi-name h3").text();
                    String location = place.select(".distance .location").text();
                    String imageUrl = place.select(".img-box .bg-img").attr("src");

                    PlaceModel placeModel = new PlaceModel(name, location, imageUrl);

                    list.add(placeModel);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (callback != null) {
                callback.onPlaceListRetrieved(list);
            }
        }).start();
    }
}
