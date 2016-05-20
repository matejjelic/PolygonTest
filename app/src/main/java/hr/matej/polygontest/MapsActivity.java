package hr.matej.polygontest;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener {

    private GoogleMap mMap;
    private PolygonOptions polygonOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapClickListener(this);

        getHomeLocation();
        drawPolygon();
    }

    public void getHomeLocation() {
        // Add a marker in Zagreb (my home) and move the camera
        LatLng home = new LatLng(45.804614, 16.022390);
        mMap.addMarker(new MarkerOptions().position(home).title("Doma kod mene - tu dođite na pivo! :D"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(home));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(home, 16.5f));
    }

    public void drawPolygon() {
        // Instantiates a new Polygon object and adds points to define a rectangle
        polygonOptions = new PolygonOptions().add(
                new LatLng(45.804669, 16.023835),
                new LatLng(45.804078, 16.020214),
                new LatLng(45.805821, 16.020359),
                new LatLng(45.805997, 16.023170)
        );
        polygonOptions.fillColor(Color.CYAN);

        // Get back the mutable Polygon
        Polygon polygon = mMap.addPolygon(polygonOptions);
    }

    @Override
    public void onMapClick(LatLng latLng) {
        PolygonOptions po = getPolygonOptions();
        boolean result = false;

        if (po != null) {
            // SOURCE: http://stackoverflow.com/questions/8721406/how-to-determine-if-a-point-is-inside-a-2d-convex-polygon
            int i, j;
            List<LatLng> points = po.getPoints();
            for (i = 0, j = points.size() - 1; i < points.size(); j = i++) {
                if ((points.get(i).longitude > latLng.longitude) != (points.get(j).longitude > latLng.longitude) &&
                        (latLng.latitude < (points.get(j).latitude - points.get(i).latitude) * (latLng.longitude - points.get(i).longitude) / (points.get(j).longitude - points.get(i).longitude) + points.get(i).latitude)) {
                    result = !result;
                }
            }
        }

        if (result) {
            Toast.makeText(this, "Clicked point \nlat: " + latLng.latitude + ", \nlong: " + latLng.longitude + "\nis within the polygon!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Point - lat: " + latLng.latitude + ", \nlong: " + latLng.longitude + "\nis NOT within the polygon!\nbla", Toast.LENGTH_SHORT).show();
        }
    }

    public PolygonOptions getPolygonOptions() {
        return polygonOptions;
    }

    public void setPolygonOptions(PolygonOptions polygonOptions) {
        this.polygonOptions = polygonOptions;
    }
}
