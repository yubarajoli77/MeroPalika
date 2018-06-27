package com.softechfoundation.municipal.Pojos;

public class LocalLevelResponsePojo {
    private String ruralMunicipality;
    private String municipality;
    private String metropolitan;
    private String subMetropolitan;
    private String wardNo;

    public LocalLevelResponsePojo(String ruralMunicipality, String municipality, String metropolitan, String subMetropolitan, String wardNo) {
        this.ruralMunicipality = ruralMunicipality;
        this.municipality = municipality;
        this.metropolitan = metropolitan;
        this.subMetropolitan = subMetropolitan;
        this.wardNo = wardNo;
    }

    public String getRuralMunicipality() {
        return ruralMunicipality;
    }

    public String getMunicipality() {
        return municipality;
    }

    public String getMetropolitan() {
        return metropolitan;
    }

    public String getSubMetropolitan() {
        return subMetropolitan;
    }

    public String getWardNo() {
        return wardNo;
    }

    public void setRuralMunicipality(String ruralMunicipality) {
        this.ruralMunicipality = ruralMunicipality;
    }

    public void setMunicipality(String municipality) {
        this.municipality = municipality;
    }

    public void setMetropolitan(String metropolitan) {
        this.metropolitan = metropolitan;
    }

    public void setSubMetropolitan(String subMetropolitan) {
        this.subMetropolitan = subMetropolitan;
    }

    public void setWardNo(String wardNo) {
        this.wardNo = wardNo;
    }
}
