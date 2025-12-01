package pl.paynowapp.samples.security.callme.saml.address;


import java.util.ArrayList;

public class Address {
        private String id;
        private ArrayList<String> addressLines;
        private Place place;
        private ArrayList<GeographicArea> geographicAreas;

    public String getId() {
        return id;}
    public void setId(String id) {
        this.id = id;}
    public ArrayList<String> getAddressLines() {
        return addressLines;}
    public void setAddressLines(ArrayList<String> addressLines) {
        this.addressLines = addressLines;}
    public Place getPlace() {
        return place;}
    public void setPlace(Place place) {
        this.place = place;}
    public ArrayList<GeographicArea> getGeographicAreas() {
        return geographicAreas;}
    public void setGeographicAreas(ArrayList<GeographicArea> geographicAreas) {
        this.geographicAreas = geographicAreas;
    }
}
