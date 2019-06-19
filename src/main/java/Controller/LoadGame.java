package Controller;

import Model.Item;
import Model.Location;
import Model.User;
import Model.World;
import Utils.ReadJSON;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class LoadGame {
    private String userSave;
    private String worldSave;

    public LoadGame(String userSave, String worldSave) {
        this.userSave = userSave;
        this.worldSave = worldSave;
    }

    public void loadWorld() {
        String locationSaveString = ReadJSON.getJSON(getClass().getResource("/" + worldSave).getFile());
        JsonArray locationArray = new JsonParser().parse(locationSaveString).getAsJsonArray();

        for(int i = 0; i < locationArray.size(); i++) {
            String locationID = String.valueOf(locationArray.get(i).getAsJsonObject().get("id"));

            if(World.getWorld().getLocationByID(locationID) == null) {
                Location location = new Location();
                location = this.buildLocation(location, locationArray.get(i).getAsJsonObject());
                World.getWorld().addLocation(location);
            }
            else {
                this.buildLocation(World.getWorld().getLocationByID(locationID), locationArray.get(i).getAsJsonObject());
            }
        }
    }

    private Location  buildLocation(Location location, JsonObject locData) {
        location.setLocationID(String.valueOf(locData.get("id")));
        location.setLocationName(String.valueOf(locData.get("filePath")));
        for (int j = 0; j < locData.getAsJsonArray("items").size(); j++) {
            Item item = new Item();
            item.setItemID(String.valueOf(locData.getAsJsonArray("items").get(j).getAsJsonObject().get("id")));
            item.setItemName(String.valueOf(locData.getAsJsonArray("items").get(j).getAsJsonObject().get("filePath")));
            double x = Double.parseDouble(String.valueOf(locData.getAsJsonArray("items").get(j).getAsJsonObject().get("positionX")));
            double y = Double.parseDouble(String.valueOf(locData.getAsJsonArray("items").get(j).getAsJsonObject().get("positionY")));
            item.setPosition(x, y);
            location.addItem(item);
        }
        JsonArray neighborArray = locData.getAsJsonArray("neighbors");
        for(int i = 0; i < neighborArray.size(); i++) {
            if(World.getWorld().getLocationByID(String.valueOf(neighborArray.get(i).getAsJsonObject().get("id"))) != null) {
                location.addNeighbor(World.getWorld().getLocationByID(String.valueOf(neighborArray.get(i).getAsJsonObject().get("id"))), Integer.parseInt(String.valueOf(neighborArray.get(i).getAsJsonObject().get("angle"))));
            }
            else {
                Location newNeighbor = new Location();
                newNeighbor.setLocationID(String.valueOf(neighborArray.get(i).getAsJsonObject().get("id")));
                World.getWorld().addLocation(newNeighbor);
                location.addNeighbor(newNeighbor, Integer.parseInt(String.valueOf(neighborArray.get(i).getAsJsonObject().get("angle"))));
            }
        }
        return location;
    }

    public void loadUser() {
        String userSaveString = ReadJSON.getJSON(getClass().getResource("/" + userSave).getFile());
        JsonArray userArray = new JsonParser().parse(userSaveString).getAsJsonArray();
        for(int i = 0; i < userArray.size(); i++) {
            User user = new User();
            JsonObject userData = userArray.get(i).getAsJsonObject();
            user.setUserID(String.valueOf(userData.get("id")));
            user.setCurrentLocation(World.getWorld().getLocationByID(String.valueOf(userData.get("currentLocation"))));
            World.getWorld().addUser(user);
        }
    }
}
