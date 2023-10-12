package com.anas.theride.services.ats;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.stereotype.Service;

import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.LatLng;
import com.google.maps.model.TravelMode;

@Service
public class DistanceCalculatorService {
    private final String GOOGLE_MAPS_API_KEY = "AIzaSyBQwsnUKPXUEwcEJ_a35Wteizh1vReQ5E4";

    public double calculateRoadDistance(double startLat, double startLon, double endLat, double endLon) {
        GeoApiContext context = new GeoApiContext.Builder().apiKey(GOOGLE_MAPS_API_KEY).build();

        DirectionsApiRequest request = DirectionsApi.newRequest(context)
												.origin(new LatLng(startLat, startLon))
												.destination(new LatLng(endLat, endLon))
												.mode(TravelMode.DRIVING);
		//System.out.println( "From: " + startLat + ", " + startLon );
		//System.out.println( "To: " + endLat + ", " + endLon );
		Map<DirectionsRoute, Double> routeDistance = new HashMap<>();
		LinkedHashMap<DirectionsRoute, Double> sortedRouteDistance = new LinkedHashMap<>();
		ArrayList<Double> list = new ArrayList<>();
        try {
            DirectionsResult result = request.await();
            if (result.routes.length > 0) {
                // Assuming the first route is the best route
				for (DirectionsRoute r : result.routes) {
					double totalDistance = 0.0;
					for( DirectionsLeg l: r.legs ){
						totalDistance = totalDistance + l.distance.inMeters;
					}
					routeDistance.put(r, totalDistance);
					list.add( totalDistance );
				}
                //return result.routes[0].legs[0].distance.inMeters;  /// 1000.0// Convert to kilometers
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Collections.sort(list, new Comparator<Double>() {
            public int compare(Double str, Double str1) {
                return (str).compareTo(str1);
            }
        });

		for (Double str : list) {
            for (Entry<DirectionsRoute, Double> entry : routeDistance.entrySet()) {
                if (entry.getValue().equals(str)) {
                    sortedRouteDistance.put(entry.getKey(), str);
                }
            }
        }

		if( sortedRouteDistance.size() > 0 ){
			return (double) sortedRouteDistance.values().toArray()[0];
		}

        return Double.MAX_VALUE; // Indicate an error or no route found
    }
}
