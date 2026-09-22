/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hms.dao;
import java.io.FileWriter;
import java.io.IOException;
import hms.model.Asset;
import hms.model.Assignment;
import java.util.Scanner;
import java.io.File;
import java.util.ArrayList;



/**
 *
 * @author HP
 */
public class AssetDAO {
    public void saveAsset(Asset asset){
        String line = asset.getAssetId() + "," + asset.getAssetType() + "," + asset.getAssetName();
        try{
            FileWriter writer = new FileWriter("Asset.txt", true);
            writer.write(line);
            writer.write("\n");
            writer.close();
        }
    catch (IOException e){
        System.out.println("Error saving Asset: " + e.getMessage());
    }
    
    
    }
    
    public ArrayList<Asset> loadAllAssets() {
    ArrayList<Asset> assetList = new ArrayList<>();
    File file = new File("Asset.txt");

    if (!file.exists()) {
        return assetList;
    }

    try {
        Scanner scanner = new Scanner(file);

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split(",");

            Asset a = new Asset(parts[0], parts[1], parts[2]);
            assetList.add(a);
        }

    } catch (IOException e) {
        System.out.println("Error loading Asset: " + e.getMessage());
    }

    return assetList;
}
    
}
