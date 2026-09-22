/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hms.model;




public class Asset {
    private String assetId;
    private String assetType;
    private String assetName;

    public Asset(String assetId, String assetType, String assetName) {
        this.assetId = assetId;
        this.assetType = assetType;
        this.assetName = assetName;
    }

    public String getAssetId() {
        return assetId;
    }

    public String getAssetType() {
        return assetType;
    }

    public String getAssetName() {
        return assetName;
    }
}